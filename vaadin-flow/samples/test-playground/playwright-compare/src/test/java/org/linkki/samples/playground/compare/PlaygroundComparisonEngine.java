/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.samples.playground.compare;

import static org.linkki.samples.playground.compare.PlaywrightHelper.REFERENCE_LABEL;
import static org.linkki.samples.playground.compare.PlaywrightHelper.TEST_LABEL;
import static org.linkki.samples.playground.compare.PlaywrightHelper.checkDiffByInnerText;
import static org.linkki.samples.playground.compare.PlaywrightHelper.checkDiffByScreenshot;
import static org.linkki.samples.playground.compare.PlaywrightHelper.checkDiffByScreenshotFullPage;
import static org.linkki.samples.playground.compare.PlaywrightHelper.clickButton;
import static org.linkki.samples.playground.compare.PlaywrightHelper.clickCheckboxByLabel;
import static org.linkki.samples.playground.compare.PlaywrightHelper.clickTab;
import static org.linkki.samples.playground.compare.PlaywrightHelper.closeAnyDialog;
import static org.linkki.samples.playground.compare.PlaywrightHelper.getDifferences;
import static org.linkki.samples.playground.compare.PlaywrightHelper.getTextContent;
import static org.linkki.samples.playground.compare.PlaywrightHelper.log;
import static org.linkki.samples.playground.compare.PlaywrightHelper.navigateSafely;
import static org.linkki.samples.playground.compare.PlaywrightHelper.sanitize;
import static org.linkki.samples.playground.compare.PlaywrightHelper.visibleButtonTexts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.linkki.samples.playground.compare.TestCaseResult.Difference;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.CLI;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

/**
 * Orchestrates the comparison of two playground deployments across all TS/TC tabs and theme
 * combinations.
 * <p>
 * Thread-safe for concurrent {@link #compareTestCases} calls: each call creates its own
 * {@link Playwright} and {@link Browser} instance via a {@link ThreadLocal}.
 */
public class PlaygroundComparisonEngine implements AutoCloseable {

    /**
     * CSS selector for the active TC content area. Scoping all queries to this element prevents
     * interactions with the TS/TC navigation tabs outside the test case component.
     */
    public static final String TC_CONTENT = "test-case-component[id]";

    private final String testDeploymentUrl;
    private final String referenceDeploymentUrl;
    private final Path outputDir;

    private final ThreadLocal<Playwright> threadPlaywright = ThreadLocal.withInitial(
            () -> Playwright.create(new Playwright.CreateOptions()
                    .setEnv(Map.of("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD", "1"))));
    private final ThreadLocal<Browser> threadBrowser = ThreadLocal.withInitial(
            () -> threadPlaywright.get().chromium().launch(new LaunchOptions().setHeadless(true)));
    private final CopyOnWriteArrayList<Playwright> playwrights = new CopyOnWriteArrayList<>();

    public PlaygroundComparisonEngine(String testDeploymentUrl, String referenceDeploymentUrl, Path outputDir) {
        this.testDeploymentUrl = testDeploymentUrl;
        this.referenceDeploymentUrl = referenceDeploymentUrl;
        this.outputDir = outputDir;
    }

    public void start() {
        try {
            if (Files.exists(outputDir)) {
                try (var entries = Files.walk(outputDir)) {
                    for (var p : entries.sorted(Comparator.reverseOrder()).toList()) {
                        Files.delete(p);
                    }
                }
            }
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            throw new RuntimeException("Cannot prepare output dir: " + outputDir, e);
        }
        // Install browser if needed (single-threaded, before parallel tests start)
        try (var pw = Playwright.create(new Playwright.CreateOptions()
                .setEnv(Map.of("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD", "1")))) {
            var executablePath = pw.chromium().executablePath();
            if (executablePath == null || !Files.exists(Path.of(executablePath))) {
                try {
                    CLI.main(new String[] { "install", "chromium" });
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException("Failed to install Chromium", e);
                }
            }
        }
        checkDeploymentReachable(testDeploymentUrl, TEST_LABEL);
        checkDeploymentReachable(referenceDeploymentUrl, REFERENCE_LABEL);
    }

    private Browser browser() {
        var browser = threadBrowser.get();
        // track playwright instances for cleanup
        playwrights.addIfAbsent(threadPlaywright.get());
        return browser;
    }

    private NewContextOptions contextOptions() {
        return new NewContextOptions().setViewportSize(1920, 1080).setLocale("de-DE");
    }

    private void checkDeploymentReachable(String url, String label) {
        try (var context = browser().newContext(contextOptions());
                var page = context.newPage()) {
            var response = page.navigate(url);
            if (response == null || response.status() != 200) {
                var status = response == null ? "no response" : String.valueOf(response.status());
                throw new IllegalStateException(
                        "Deployment '" + label + "' not reachable at " + url + " (HTTP " + status + ")");
            }
            PlaywrightHelper.waitForVaadin(page);
        }
    }

    @Override
    public void close() {
        playwrights.forEach(Playwright::close);
    }

    public Path getOutputDir() {
        return outputDir;
    }

    public String getTestDeploymentUrl() {
        return testDeploymentUrl;
    }

    public String getReferenceDeploymentUrl() {
        return referenceDeploymentUrl;
    }

    public List<String> discoverTsTabs() {
        try (var context = browser().newContext(contextOptions());
                var page = context.newPage()) {
            navigateSafely(page, testDeploymentUrl);
            return discoverTsTabs(page);
        }
    }

    public List<TestCaseResult> compareTestCases(String tsId, EnumSet<Theme> themes) {
        try (var testContext = browser().newContext(contextOptions());
                var referenceContext = browser().newContext(contextOptions());
                var testPage = testContext.newPage();
                var referencePage = referenceContext.newPage()) {
            return compareTs(testPage, referencePage, tsId, themes);
        }
    }

    private static List<String> discoverTsTabs(Page page) {
        var ids = new ArrayList<String>();
        for (var tab : page.querySelectorAll("vaadin-tab[id^='TS']")) {
            var id = tab.getAttribute("id");
            if (id != null && id.matches("TS\\d+"))
                ids.add(id);
        }
        if (ids.isEmpty()) {
            for (var link : page.querySelectorAll("a[href*='playground/TS']")) {
                var href = link.getAttribute("href");
                if (href != null)
                    extractIds(href, "TS\\d+", ids);
            }
        }
        return ids;
    }

    private static List<String> discoverTcTabs(Page page, String tsId) {
        var ids = new ArrayList<String>();
        for (var link : page.querySelectorAll("a[href*='" + tsId + "/TC']")) {
            var href = link.getAttribute("href");
            if (href != null)
                extractIds(href, "TC\\d+", ids);
        }
        return ids;
    }

    private static void extractIds(String text, String pattern, List<String> result) {
        for (var part : text.split("/")) {
            if (part.matches(pattern) && !result.contains(part))
                result.add(part);
        }
    }

    private List<TestCaseResult> compareTs(Page testPage, Page referencePage, String tsId, EnumSet<Theme> themes) {
        log("  %s [%s]%n", tsId, themes);
        var results = new ArrayList<TestCaseResult>();

        // Navigate directly to first TC candidate to discover the TC tab list.
        // TC tabs are visible on any TC page, so no separate TS-level navigation needed.
        navigateAndActivateThemes(testPage, testDeploymentUrl + "/" + tsId, themes);
        navigateAndActivateThemes(referencePage, referenceDeploymentUrl + "/" + tsId, themes);

        // Query content offset after first successful navigation — same for all TCs in this TS
        var contentOffsetX = ((Number)testPage.evaluate(
                "() => document.querySelector('test-case-component[id]')?.getBoundingClientRect().left ?? 0"))
                .intValue();
        var contentOffsetY = ((Number)testPage.evaluate(
                "() => Math.round(document.querySelector('.linkki-application-header')?.getBoundingClientRect().bottom ?? 0)"))
                .intValue();
        log("    contentOffsetX=%d contentOffsetY=%d%n", contentOffsetX, contentOffsetY);

        var tcTabsTest = discoverTcTabs(testPage, tsId);
        var tcTabsReference = discoverTcTabs(referencePage, tsId);
        var allTcTabs = union(tcTabsTest, tcTabsReference);

        if (allTcTabs.isEmpty()) {
            // No TC sub-tabs — compare the TS page directly (already loaded)
            results.add(comparePage(testPage, referencePage, tsId, tsId, themes, contentOffsetX, contentOffsetY));
            return results;
        }

        // TC001 already loaded — compare it directly, navigate for the rest
        for (var tcId : allTcTabs.stream().sorted().toList()) {
            var inTest = tcTabsTest.contains(tcId);
            var inReference = tcTabsReference.contains(tcId);

            if (!inTest || !inReference) {
                results.add(new TestCaseResult(tsId, tcId, themes,
                        List.of(new Difference("", "Tab %s missing on %s"
                                .formatted(tcId, !inTest ? TEST_LABEL : REFERENCE_LABEL)))));
                continue;
            }

            if (!"TC001".equals(tcId)) {
                navigateAndActivateThemes(testPage, testDeploymentUrl + "/" + tsId + "/" + tcId, themes);
                navigateAndActivateThemes(referencePage, referenceDeploymentUrl + "/" + tsId + "/" + tcId, themes);
            }
            results.add(comparePage(testPage, referencePage, tsId, tcId, themes, contentOffsetX, contentOffsetY));
        }

        return results;
    }

    private TestCaseResult comparePage(Page testPage,
            Page referencePage,
            String tsId,
            String tcId,
            EnumSet<Theme> themes,
            int contentOffsetX, int contentOffsetY) {
        log("    %s/%s%n", tsId, tcId);
        var prefix = tsId + "-" + tcId + "-" + sanitize(themes.toString());

        var differences = new ArrayList<Difference>(
                checkDiffByInnerText(getTestComponent(testPage), getTestComponent(referencePage), prefix));

        PlaywrightHelper.scrollToTop(testPage);
        PlaywrightHelper.scrollToTop(referencePage);
        var viewportDiff = checkDiffByScreenshot(testPage, referencePage, outputDir, prefix + "--initial",
                                                  contentOffsetX, contentOffsetY);
        Optional.ofNullable(viewportDiff).ifPresent(differences::add);
        if (viewportDiff == null) {
            Optional.ofNullable(checkDiffByScreenshotFullPage(testPage, referencePage, outputDir, prefix + "--initial",
                                                              contentOffsetX, contentOffsetY)).ifPresent(differences::add);
        }

        differences.addAll(interactTabs(testPage, referencePage, prefix, contentOffsetX, contentOffsetY));
        differences.addAll(interactCheckboxes(testPage, referencePage, prefix, contentOffsetX, contentOffsetY));
        differences.addAll(interactButtons(testPage, referencePage, prefix, contentOffsetX, contentOffsetY));

        return new TestCaseResult(tsId, tcId, themes, differences);
    }

    private List<Difference> interactTabs(Page testPage,
            Page referencePage,
            String prefix,
            int contentOffsetX, int contentOffsetY) {
        var testTextContent = getTextContent(getTestComponent(testPage), "vaadin-tab");
        var referenceTextContent = getTextContent(getTestComponent(referencePage), "vaadin-tab");
        var differences = new ArrayList<Difference>(getDifferences(testTextContent, referenceTextContent, "Tab"));

        for (var tabText : union(testTextContent, referenceTextContent)) {
            clickTab(testPage, this::getTestComponent, tabText);
            clickTab(referencePage, this::getTestComponent, tabText);

            var tabLabel = prefix + "-tab-" + sanitize(tabText);
            differences.addAll(checkDiffByInnerText(getTestComponent(testPage), getTestComponent(referencePage), tabLabel));
            Optional.ofNullable(checkDiffByScreenshot(testPage, referencePage, outputDir,
                                                       tabLabel, contentOffsetX, contentOffsetY))
                    .ifPresent(differences::add);
        }
        return differences;
    }

    private List<Difference> interactCheckboxes(Page testPage,
            Page referencePage,
            String prefix,
            int contentOffsetX, int contentOffsetY) {
        var testRoot = testPage.locator(TC_CONTENT).last();
        var referenceRoot = referencePage.locator(TC_CONTENT).last();

        var differences = new ArrayList<Difference>();
        var testTextContent = getTextContent(testRoot, "vaadin-checkbox");
        var referenceTextContent = getTextContent(referenceRoot, "vaadin-checkbox");
        differences.addAll(getDifferences(testTextContent, referenceTextContent, "Checkbox"));

        var common = intersection(testTextContent, referenceTextContent);
        for (var label : common) {
            clickCheckboxByLabel(testPage, this::getTestComponent, label);
            clickCheckboxByLabel(referencePage, this::getTestComponent, label);
            Optional.ofNullable(checkDiffByScreenshot(testPage, referencePage, outputDir,
                                                       prefix + "-chk-" + sanitize(label),
                                                       contentOffsetX, contentOffsetY))
                    .ifPresent(differences::add);

            // restore
            clickCheckboxByLabel(testPage, this::getTestComponent, label);
            clickCheckboxByLabel(referencePage, this::getTestComponent, label);
        }

        return differences;
    }

    /**
     * Interact with buttons and return the differences.
     */
    private List<Difference> interactButtons(Page testPage,
            Page referencePage,
            String prefix,
            int contentOffsetX, int contentOffsetY) {
        var differences = new ArrayList<Difference>();

        var buttonsTest = visibleButtonTexts(getTestComponent(testPage));
        var buttonsRef = visibleButtonTexts(getTestComponent(referencePage));
        differences.addAll(getDifferences(buttonsTest, buttonsRef, "Button"));

        for (var buttonText : intersection(buttonsTest, buttonsRef)) {
            var urlBefore = testPage.url();
            var refUrlBefore = referencePage.url();
            if (clickButton(testPage.locator(TC_CONTENT).last(), buttonText)
                    && clickButton(referencePage.locator(TC_CONTENT).last(), buttonText)) {
                // Skip screenshot if either side navigated away — restore both and continue
                if (!testPage.url().equals(urlBefore) || !referencePage.url().equals(refUrlBefore)) {
                    navigateSafely(testPage, urlBefore);
                    navigateSafely(referencePage, refUrlBefore);
                    continue;
                }
                Optional.ofNullable(checkDiffByScreenshot(testPage, referencePage, outputDir,
                                                           prefix + "-btn-" + sanitize(buttonText),
                                                           contentOffsetX, contentOffsetY))
                        .ifPresent(differences::add);
                closeAnyDialog(testPage);
                closeAnyDialog(referencePage);
            }
        }
        return differences;
    }

    /** Navigates to {@code url}, waits for Vaadin, then activates each theme variant in the set. */
    private void navigateAndActivateThemes(Page page, String url, EnumSet<Theme> themes) {
        navigateSafely(page, url);
        themes.forEach(theme -> theme.activate(page));
    }

    private Locator getTestComponent(Page page) {
        return page.locator(TC_CONTENT).last();
    }

    private static Set<String> union(List<String> a, List<String> b) {
        var set = new HashSet<>(a);
        set.addAll(b);
        return set;
    }

    private static Set<String> intersection(List<String> a, List<String> b) {
        var set = new HashSet<>(a);
        set.retainAll(b);
        return set;
    }
}
