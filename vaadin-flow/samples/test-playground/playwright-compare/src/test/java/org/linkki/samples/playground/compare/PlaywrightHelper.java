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

import static java.nio.file.Files.write;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.linkki.samples.playground.compare.TestCaseResult.Difference;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.ScreenshotAnimations;
import com.microsoft.playwright.options.ScreenshotType;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Low-level Playwright operations used by the comparison engine.
 */
public class PlaywrightHelper {

    private static final int NAVIGATION_TIMEOUT_MS = 30_000;
    private static final int SCREENSHOT_TIMEOUT_MS = 120_000;
    /**
     * Per-channel color tolerance for pixel comparison — filters sub-pixel font rendering noise.
     */
    private static final int PIXEL_TOLERANCE = 20;
    private static final Page.ScreenshotOptions BASE_SCREENSHOT_OPS = new Page.ScreenshotOptions()
            .setAnimations(ScreenshotAnimations.DISABLED)
            .setType(ScreenshotType.PNG)
            .setTimeout(SCREENSHOT_TIMEOUT_MS);

    public static final String TEST_LABEL = "test";
    public static final String REFERENCE_LABEL = "reference";

    private static final java.time.format.DateTimeFormatter TIME_FMT = java.time.format.DateTimeFormatter
            .ofPattern("HH:mm:ss");

    private PlaywrightHelper() {
        // utility
    }

    /**
     * Waits until Vaadin's UI is fully idle: document complete and no active Flow clients. More
     * reliable than waiting for a specific element or network idle.
     */
    private static final String VAADIN_IDLE_SCRIPT = "document.readyState === 'complete'"
            + " && !!window.Vaadin?.Flow?.clients"
            + " && Object.keys(window.Vaadin.Flow.clients).length > 0"
            + " && Object.values(window.Vaadin.Flow.clients).every(c => !c.isActive())";

    /** Polls until Vaadin's Flow clients exist and are all idle. */
    public static void waitForVaadin(Page page) {
        var start = System.currentTimeMillis();
        var deadline = start + NAVIGATION_TIMEOUT_MS;
        while (System.currentTimeMillis() < deadline) {
            var ready = Boolean.TRUE.equals(page.evaluate(VAADIN_IDLE_SCRIPT));
            if (ready) {
                log("    Vaadin idle after %d ms%n", System.currentTimeMillis() - start);
                return;
            }
            page.waitForTimeout(100);
        }
        throw new RuntimeException("Timeout waiting for Vaadin to become idle on " + page.url());
    }

    /**
     * Navigates to {@code url} and waits for Vaadin to become idle.
     */
    public static void navigateSafely(Page page, String url) {
        page.navigate(url, new Page.NavigateOptions()
                .setWaitUntil(WaitUntilState.LOAD)
                .setTimeout(NAVIGATION_TIMEOUT_MS));
        waitForVaadin(page);
        page.evaluate("document.querySelector('copilot-main')?.style.setProperty('display','none')");
    }

    /**
     * Captures screenshots of both pages, compares pixels, writes files only if they differ.
     * Returns a {@link Difference} with the given label, or {@code null} if identical.
     */
    @CheckForNull
    public static Difference checkDiffByScreenshot(Page testPage,
            Page referencePage,
            Path outputDir,
            String label,
            int contentOffsetX,
            int contentOffsetY) {
        var vp = testPage.viewportSize();
        var opts = BASE_SCREENSHOT_OPS
                .setClip(contentOffsetX, contentOffsetY,
                         vp.width - contentOffsetX, vp.height - contentOffsetY);
        // Move mouse off content to avoid hover state differences; sync scroll position to test page
        testPage.mouse().move(0, 0);
        referencePage.mouse().move(0, 0);
        syncScroll(testPage, referencePage);
        var testBytes = testPage.screenshot(opts);
        var refBytes = referencePage.screenshot(opts);
        var differentPixel = hasDifferentPixel(testBytes, refBytes, 0, 0);
        if (differentPixel != null) {
            writeToFile(testBytes, outputDir, label, TEST_LABEL);
            writeToFile(refBytes, outputDir, label, REFERENCE_LABEL);
            return new Difference(label, "Visual difference: " + differentPixel);
        }
        return null;
    }

    @CheckForNull
    public static Difference checkDiffByScreenshotFullPage(Page testPage,
            Page referencePage,
            Path outputDir,
            String label,
            int contentOffsetX,
            int contentOffsetY) {
        var ops = BASE_SCREENSHOT_OPS.setFullPage(true);
        testPage.mouse().move(0, 0);
        referencePage.mouse().move(0, 0);
        injectExpandStyle(testPage);
        injectExpandStyle(referencePage);
        var testBytes = testPage.screenshot(ops);
        var refBytes = referencePage.screenshot(ops);
        removeExpandStyle(testPage);
        removeExpandStyle(referencePage);
        var differentPixel = hasDifferentPixel(testBytes, refBytes, contentOffsetX, contentOffsetY);
        if (differentPixel != null) {
            writeToFile(testBytes, outputDir, label, "expanded", TEST_LABEL);
            writeToFile(refBytes, outputDir, label, "expanded", REFERENCE_LABEL);
            return new Difference(label, "Visual difference (full page): " + differentPixel);
        }
        return null;
    }

    @CheckForNull
    static String hasDifferentPixel(byte[] testBytes, byte[] refBytes, int startX, int startY) {
        if (Arrays.equals(testBytes, refBytes))
            return null;
        var test = decode(testBytes);
        var reference = decode(refBytes);
        if (test.getWidth() != reference.getWidth() || test.getHeight() != reference.getHeight())
            return "Dimensions are different (test %s x %s, reference %s x %s)"
                    .formatted(test.getWidth(), test.getHeight(), reference.getWidth(), reference.getHeight());
        var w = test.getWidth() - startX;
        var h = test.getHeight() - startY;
        var pixelsA = test.getRGB(startX, startY, w, h, null, 0, w);
        var pixelsB = reference.getRGB(startX, startY, w, h, null, 0, w);
        for (var i = 0; i < pixelsA.length; i++)
            if (exceedsTolerance(pixelsA[i], pixelsB[i])) {
                var c1 = new Color(pixelsA[i]);
                var c2 = new Color(pixelsB[i]);
                return "Pixel at (%d,%d) differs: test=#%02x%02x%02x ref=#%02x%02x%02x"
                        .formatted(startX + i % w, startY + i / w,
                                c1.getRed(), c1.getGreen(), c1.getBlue(),
                                c2.getRed(), c2.getGreen(), c2.getBlue());
            }
        return null;
    }

    private static boolean exceedsTolerance(int rgb1, int rgb2) {
        var c1 = new Color(rgb1);
        var c2 = new Color(rgb2);
        return Math.abs(c1.getRed() - c2.getRed()) > PIXEL_TOLERANCE
                || Math.abs(c1.getGreen() - c2.getGreen()) > PIXEL_TOLERANCE
                || Math.abs(c1.getBlue() - c2.getBlue()) > PIXEL_TOLERANCE;
    }

    private static BufferedImage decode(byte[] bytes) {
        BufferedImage image;
        try {
            image = ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (image == null) {
            throw new RuntimeException("Converting bytes to image failed");
        }
        return image;
    }

    private static void writeToFile(byte[] bytes, Path outputDir, String... labels) {
        var name = Arrays.stream(labels).map(PlaywrightHelper::sanitize)
                .collect(java.util.stream.Collectors.joining("-"));
        var path = outputDir.resolve(name + ".png");
        try {
            write(path, bytes);
        } catch (Exception e) {
            System.err.printf("Write failed for %s: %s%n", name, e.getMessage());
        }
    }

    static void scrollToTop(Page page) {
        setContentScroll(page, 0);
    }

    private static void syncScroll(Page source, Page target) {
        var scrollTop = ((Number)source.evaluate(
                "document.getElementById('content-wrapper')?.scrollTop ?? 0")).intValue();
        setContentScroll(target, scrollTop);
    }

    private static void setContentScroll(Page page, int scrollTop) {
        page.evaluate("var e=document.getElementById('content-wrapper');if(e)e.scrollTop=%d;"
                .formatted(scrollTop));
    }

    private static void injectExpandStyle(Page page) {
        page.evaluate("document.head.insertAdjacentHTML('beforeend',"
                + "'<style id=\"pw-exp\">* { overflow: visible !important; max-height: none !important; }</style>')");
    }

    private static void removeExpandStyle(Page page) {
        page.evaluate("document.getElementById('pw-exp')?.remove()");
    }

    /**
     * Returns differences for items present on one side but missing on the other.
     */
    public static List<Difference> getDifferences(List<String> testItems,
            List<String> referenceItems,
            String itemLabel) {
        var diffs = new ArrayList<Difference>();
        var onlyTest = new HashSet<>(testItems);
        referenceItems.forEach(onlyTest::remove);
        var onlyReference = new HashSet<>(referenceItems);
        testItems.forEach(onlyReference::remove);
        onlyTest.forEach(i -> diffs.add(new Difference(itemLabel,
                "'%s' present on %s but missing on %s".formatted(i, TEST_LABEL, REFERENCE_LABEL))));
        onlyReference.forEach(i -> diffs.add(new Difference(itemLabel,
                "'%s' present on %s but missing on %s".formatted(i, REFERENCE_LABEL, TEST_LABEL))));
        return diffs;
    }

    /**
     * Compares the {@code innerText} of the TC content locator on both pages line by line.
     */
    public static List<Difference> checkDiffByInnerText(Locator testLocator, Locator refLocator, String label) {
        var testInnerText = lines(testLocator.innerText());
        var refInnerText = lines(refLocator.innerText());
        return getDifferences(testInnerText, refInnerText, label);
    }

    private static List<String> lines(@CheckForNull String text) {
        if (text == null)
            return List.of();
        return Arrays.stream(text.split("\\r?\\n"))
                .map(String::trim)
                .filter(l -> !l.isEmpty())
                .toList();
    }

    /**
     * Returns non-blank text content of all elements matching {@code selector} in the given root.
     */
    public static List<String> getTextContent(Locator root, String selector) {
        var texts = new ArrayList<String>();
        try {
            for (var el : root.locator(selector).all()) {
                var text = el.textContent();
                if (text != null && !text.isBlank())
                    texts.add(text.trim());
            }
        } catch (Exception e) {
            // ignore
        }
        return texts;
    }

    public static boolean clickTab(Page page, Function<Page, Locator> rootLocator, String text) {
        try {
            var tab = rootLocator.apply(page).locator("vaadin-tab")
                    .filter(new Locator.FilterOptions().setHasText(text)).first();
            if (tab.isVisible()) {
                tab.click();
                waitForVaadin(page);
                return true;
            }
        } catch (Exception e) {
            // not present
        }
        return false;
    }

    public static boolean clickCheckboxByLabel(Page page, Function<Page, Locator> rootLocator, String labelText) {
        try {
            var checkbox = rootLocator.apply(page)
                    .locator("vaadin-checkbox")
                    .filter(new Locator.FilterOptions().setHasText(labelText)).first();
            if (!checkbox.isVisible())
                return false;
            checkbox.click();
            waitForVaadin(page);
            return true;
        } catch (Exception e) {
            // not present or timeout
        }
        return false;
    }

    public static List<String> visibleButtonTexts(Locator root) {
        return getTextContent(root, "vaadin-button, button");
    }

    public static boolean clickButton(Locator root, String text) {
        try {
            var btn = root.locator("vaadin-button, button")
                    .filter(new Locator.FilterOptions().setHasText(text)).first();
            if (btn.isVisible() && btn.isEnabled()) {
                btn.click();
                waitForVaadin(btn.page());
                return true;
            }
        } catch (Exception e) {
            // ignore
        }
        return false;
    }

    public static void closeAnyDialog(Page page) {
        var overlays = page.locator("vaadin-dialog-overlay").all();
        for (var overlay : overlays) {
            if (!overlay.isVisible())
                continue;
            for (var id : List.of("okButton", "cancelButton")) {
                var btn = overlay.locator("vaadin-button#" + id);
                if (btn.isVisible()) {
                    btn.click();
                    overlay.waitFor(new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.HIDDEN)
                            .setTimeout(5000));
                    break;
                }
            }
        }
    }

    public static String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_").replaceAll("_+", "_");
    }

    public static void log(String format, Object... args) {
        System.out.printf("[%s] %s", LocalTime.now().format(TIME_FMT), String.format(format, args));
    }
}
