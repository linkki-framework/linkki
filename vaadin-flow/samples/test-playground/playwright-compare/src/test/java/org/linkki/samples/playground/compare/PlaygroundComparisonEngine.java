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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.CLI;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Orchestrates the comparison of two playground deployments across all TS/TC tabs and themes.
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

    private Playwright playwright;
    private Browser browser;
    private BrowserContext testDeploymentContext;
    private BrowserContext referenceDeploymentContext;

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
        playwright = Playwright.create(new Playwright.CreateOptions()
                .setEnv(Map.of("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD", "1")));
        var executablePath = playwright.chromium().executablePath();
        if (executablePath == null || !Files.exists(Path.of(executablePath))) {
            try {
                CLI.main(new String[] { "install", "chromium" });
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Failed to install Chromium", e);
            }
        }
        browser = playwright.chromium().launch(new LaunchOptions().setHeadless(true));
        var contextOptions = new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
                .setLocale("de-DE");
        testDeploymentContext = browser.newContext(contextOptions);
        referenceDeploymentContext = browser.newContext(contextOptions);
        checkDeploymentReachable(testDeploymentUrl, TEST_LABEL);
        checkDeploymentReachable(referenceDeploymentUrl, REFERENCE_LABEL);
    }

    private void checkDeploymentReachable(String url, String label) {
        try (var page = browser.newPage()) {
            var response = page.navigate(url);
            if (response == null || response.status() != 200) {
                var status = response == null ? "no response" : String.valueOf(response.status());
                throw new IllegalStateException(
                        "Deployment '" + label + "' not reachable at " + url + " (HTTP " + status + ")");
            }
        }
    }

    @Override
    public void close() {
        if (testDeploymentContext != null)
            testDeploymentContext.close();
        if (referenceDeploymentContext != null)
            referenceDeploymentContext.close();
        if (browser != null)
            browser.close();
        if (playwright != null)
            playwright.close();
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
        try (var page = testDeploymentContext.newPage()) {
            navigateSafely(page, testDeploymentUrl);
            return discoverTsTabs(page);
        }
    }

    public List<TestCaseResult> compareTestCases(String tsId, Theme theme) {
        try (var testPage = testDeploymentContext.newPage();
                var referencePage = referenceDeploymentContext.newPage()) {
            return compareTs(testPage, referencePage, tsId, theme);
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

    private List<TestCaseResult> compareTs(Page testPage, Page referencePage, String tsId, Theme theme) {
        log("  %s [%s]%n", tsId, theme.label());
        var results = new ArrayList<TestCaseResult>();

        // Navigate directly to first TC candidate to discover the TC tab list.
        // TC tabs are visible on any TC page, so no separate TS-level navigation needed.
        navigateAndActivateTheme(testPage, testDeploymentUrl + "/" + tsId, theme);
        navigateAndActivateTheme(referencePage, referenceDeploymentUrl + "/" + tsId, theme);

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
            results.add(comparePage(testPage, referencePage, tsId, tsId, theme, contentOffsetX, contentOffsetY));
            return results;
        }

        // TC001 already loaded — compare it directly, navigate for the rest
        for (var tcId : allTcTabs.stream().sorted().toList()) {
            var inTest = tcTabsTest.contains(tcId);
            var inReference = tcTabsReference.contains(tcId);

            if (!inTest || !inReference) {
                var diff = new TestCaseResult(tsId, tcId, theme, List.of("Tab %s missing on %s", tcId,
                                                                         !inTest ? TEST_LABEL : REFERENCE_LABEL));
                results.add(diff);
                continue;
            }

            if (!"TC001".equals(tcId)) {
                navigateAndActivateTheme(testPage, testDeploymentUrl + "/" + tsId + "/" + tcId, theme);
                navigateAndActivateTheme(referencePage, referenceDeploymentUrl + "/" + tsId + "/" + tcId, theme);
            }
            results.add(comparePage(testPage, referencePage, tsId, tcId, theme, contentOffsetX, contentOffsetY));
        }

        return results;
    }

    private TestCaseResult comparePage(Page testPage,
            Page referencePage,
            String tsId,
            String tcId,
            Theme theme,
            int contentOffsetX, int contentOffsetY) {
        log("    %s/%s%n", tsId, tcId);
        var prefix = tsId + "-" + tcId + "-" + theme.label();

        var differences = new ArrayList<>(checkDiffByInnerText(getTestComponent(testPage), getTestComponent(referencePage), prefix));

        @CheckForNull
        var screenShotDiff = checkDiffByScreenshot(testPage, referencePage, outputDir, prefix + "--initial",
                                                   contentOffsetX, contentOffsetY);
        Optional.ofNullable(screenShotDiff).ifPresent(differences::add);
        @CheckForNull
        var screenShotFullPageDiff = checkDiffByScreenshotFullPage(testPage, referencePage, outputDir,
                                                                   prefix + "--initial",
                                                                   contentOffsetX, contentOffsetY);
        Optional.ofNullable(screenShotFullPageDiff).ifPresent(differences::add);

        differences.addAll(interactTabs(testPage, referencePage, prefix, contentOffsetX, contentOffsetY));
        differences.addAll(interactCheckboxes(testPage, referencePage, prefix, contentOffsetX, contentOffsetY));
        differences.addAll(interactButtons(testPage, referencePage, prefix, contentOffsetX, contentOffsetY));

        return new TestCaseResult(tsId, tcId, theme, differences);
    }

    private List<String> interactTabs(Page testPage,
            Page referencePage,
            String prefix,
            int contentOffsetX, int contentOffsetY) {
        var testTextContent = getTextContent(getTestComponent(testPage), "vaadin-tab");
        var referenceTextContent = getTextContent(getTestComponent(referencePage), "vaadin-tab");
        var differences = new ArrayList<>(getDifferences(testTextContent, referenceTextContent, "Tab"));

        for (var tabText : union(testTextContent, referenceTextContent)) {
            clickTab(testPage, this::getTestComponent, tabText);
            clickTab(referencePage, this::getTestComponent, tabText);

            var tabLabel = prefix + "-tab-" + sanitize(tabText);
            differences.addAll(checkDiffByInnerText(getTestComponent(testPage), getTestComponent(referencePage), tabLabel));

            var visualDifference = checkDiffByScreenshot(testPage, referencePage, outputDir,
                                                         tabLabel, contentOffsetX, contentOffsetY);
            if (visualDifference != null) {
                differences.add(visualDifference);
            }
        }
        return differences;
    }

    private List<String> interactCheckboxes(Page testPage,
            Page referencePage,
            String prefix,
            int contentOffsetX, int contentOffsetY) {
        var testRoot = testPage.locator(TC_CONTENT).last();
        var referenceRoot = referencePage.locator(TC_CONTENT).last();

        var differences = new ArrayList<String>();
        var testTextContent = getTextContent(testRoot, "vaadin-checkbox");
        var referenceTextContent = getTextContent(referenceRoot, "vaadin-checkbox");
        differences.addAll(getDifferences(testTextContent, referenceTextContent, "Checkbox"));

        var common = intersection(testTextContent, referenceTextContent);
        for (var label : common) {
            clickCheckboxByLabel(testPage, this::getTestComponent, label);
            clickCheckboxByLabel(referencePage, this::getTestComponent, label);
            var vdChk = checkDiffByScreenshot(testPage, referencePage, outputDir,
                                              prefix + "-chk-" + sanitize(label), contentOffsetX, contentOffsetY);
            if (vdChk != null) {
                differences.add(vdChk);
            }

            // restore
            clickCheckboxByLabel(testPage, this::getTestComponent, label);
            clickCheckboxByLabel(referencePage, this::getTestComponent, label);
        }

        return differences;
    }

    /**
     * Interact with buttons and return the differences.
     */
    private List<String> interactButtons(Page testPage,
            Page referencePage,
            String prefix,
            int contentOffsetX, int contentOffsetY) {
        var differences = new ArrayList<String>();

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
                var vdBtn = checkDiffByScreenshot(testPage, referencePage, outputDir,
                                                  prefix + "-btn-" + sanitize(buttonText), contentOffsetX, contentOffsetY);
                if (vdBtn != null) {
                    differences.add(vdBtn);
                }
                closeAnyDialog(testPage);
                closeAnyDialog(referencePage);
            }
        }
        return differences;
    }

    /** Navigate, wait for playground, then activate theme. Returns false if navigation failed. */
    private void navigateAndActivateTheme(Page page, String url, Theme theme) {
        navigateSafely(page, url);
        theme.activate(page);
    }

    private Locator getTestComponent(Page page) {
        return page.locator(TC_CONTENT).last();
    }

    private Set<String> union(List<String> a, List<String> b) {
        var set = new HashSet<>(a);
        set.addAll(b);
        return set;
    }

    private Set<String> intersection(List<String> a, List<String> b) {
        var set = new HashSet<>(a);
        set.retainAll(b);
        return set;
    }
}
