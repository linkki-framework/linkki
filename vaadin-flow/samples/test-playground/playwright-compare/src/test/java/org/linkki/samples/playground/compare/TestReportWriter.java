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

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Writes a minimal HTML comparison report with collapsible sections and side-by-side screenshots.
 */
public class TestReportWriter {

    private static final String TEST_LABEL = "test";
    private static final String REFERENCE_LABEL = "reference";

    static final String RESULTS_FILE = "results.txt";

    private TestReportWriter() {
        // utility
    }

    public static void write(Path outputDir, List<TestCaseResult> diffs, String testUrl, String referenceUrl) {
        writeToText(outputDir, diffs, testUrl, referenceUrl);
        writeToHtml(outputDir, diffs, testUrl, referenceUrl);
    }

    private static void writeToText(Path outputDir, List<TestCaseResult> diffs, String testUrl, String referenceUrl) {
        var lines = new java.util.ArrayList<String>();
        lines.add(testUrl + "|" + referenceUrl);
        diffs.forEach(d -> lines.add(d.toStringLine()));
        try {
            Files.write(outputDir.resolve(RESULTS_FILE), lines);
        } catch (IOException e) {
            System.err.printf("Failed to write results.txt: %s%n", e.getMessage());
        }
    }

    public static void writeToHtml(Path outputDir) throws IOException {
        var lines = Files.readAllLines(outputDir.resolve(RESULTS_FILE));
        var meta = lines.get(0).split("\\|", 2);
        var diffs = lines.subList(1, lines.size()).stream()
                .map(TestCaseResult::fromStringLine)
                .toList();
        writeToHtml(outputDir, diffs, meta[0], meta[1]);
    }

    private static void writeToHtml(Path outputDir, List<TestCaseResult> diffs, String testUrl, String referenceUrl) {
        var reportPath = outputDir.resolve("report.html");
        try (var w = new PrintWriter(Files.newBufferedWriter(reportPath))) {
            writeReport(w, diffs, outputDir, testUrl, referenceUrl);
            System.out.printf("Report written: %s%n", reportPath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to write report: " + e.getMessage());
        }
    }

    private static void writeReport(PrintWriter w, List<TestCaseResult> diffs, Path outputDir,
            String testUrl, String referenceUrl) {
        var totalWithDiffs = diffs.stream().filter(TestCaseResult::hasDifferences).count();
        var timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        w.print("""
                <!DOCTYPE html><html lang="en"><head><meta charset="UTF-8">
                <title>Playground Comparison</title>
                <style>
                body{font-family:sans-serif;margin:16px;color:#222}
                summary{cursor:pointer;font-weight:bold;padding:4px 0}
                details{border:1px solid #ccc;border-radius:4px;padding:8px;margin:6px 0}
                details[open]{background:#fafafa}
                .ok{color:green} .diff{color:#c00}
                ul{margin:4px 0 4px 20px}
                .shots{display:flex;gap:12px;flex-wrap:wrap;margin-top:8px}
                .shots>div{flex:1;min-width:280px}
                .shots img{max-width:100%;border:1px solid #ccc}
                .lbl{font-size:.8em;color:#666;margin-bottom:2px}
                .img-toggle{position:relative;cursor:pointer;display:inline-block}
                .img-toggle .ref-img{display:none;position:absolute;top:0;left:0;max-width:100%}
                .img-toggle.show-ref .ref-img{display:block}
                .img-toggle.show-ref .test-img{visibility:hidden}
                .img-toggle::before{content:'test — click to show reference';font-size:.75em;color:#fff;background:rgba(0,0,0,.5);padding:2px 6px;border-radius:3px;position:absolute;top:4px;left:4px;z-index:1;pointer-events:none}
                .img-toggle.show-ref::before{content:'reference — click to show test'}
                </style></head><body>
                """);

        w.printf("<h2>Playground Comparison — %s</h2>%n", timestamp);
        w.printf("<p><strong>test:</strong> <a href='%s'>%s</a></p>%n", testUrl, testUrl);
        w.printf("<p><strong>reference:</strong> <a href='%s'>%s</a></p>%n", referenceUrl, referenceUrl);
        w.printf("<p>%d tabs checked, <strong>%d differ</strong></p>%n",
                diffs.size(), totalWithDiffs);

        for (var theme : Theme.values()) {
            var themeDiffs = diffs.stream().filter(d -> theme == d.theme()).toList();
            var themeWithDiffs = themeDiffs.stream().filter(TestCaseResult::hasDifferences).count();

            w.printf("<details><summary>Theme: %s &nbsp;<span class='%s'>(%d/%d differ)</span></summary>%n",
                    capitalize(theme.label()), themeWithDiffs > 0 ? "diff" : "ok",
                    themeWithDiffs, themeDiffs.size());

            for (var d : themeDiffs) {
                var label = d.tsId() + (d.tcId() != null ? "/" + d.tcId() : "");
                var hasDiff = d.hasDifferences();

                w.printf("<details><summary>%s &nbsp;<span class='%s'>%s</span></summary>%n",
                        label, hasDiff ? "diff" : "ok", hasDiff ? "differs" : "OK");

                if (hasDiff) {
                    w.println("<ul class='diff'>");
                    for (var diff : d.differences()) w.printf("<li>%s</li>%n", escapeHtml(diff));
                    w.println("</ul>");
                    writeScreenshots(w, d, outputDir, theme.label());
                }

                w.println("</details>");
            }

            w.println("</details>"); // theme
        }

        w.println("</body></html>");
    }

    private static void writeScreenshots(PrintWriter w, TestCaseResult d, Path outputDir, String themeSuffix) {
        var tsId = d.tsId();
        var tcId = d.tcId();
        var prefix = tsId + "-" + (tcId != null ? tcId : tsId) + "-" + themeSuffix + "-";

        // All screenshots (initial, interaction, expanded) — pair test and reference side by side
        try {
            var byInteraction = new TreeMap<String, Map<String, Path>>();
            Files.list(outputDir)
                    .filter(p -> {
                        var name = p.getFileName().toString();
                        return name.startsWith(prefix) && name.endsWith(".png");
                    })
                    .forEach(p -> {
                        var name = p.getFileName().toString().replace(".png", "");
                        // Name format: <prefix>-<interaction>-<side>  (e.g. TS001-TC001-default-tab-FORM-test)
                        var side = name.endsWith("-" + TEST_LABEL) ? TEST_LABEL : REFERENCE_LABEL;
                        var suffix = name.substring(prefix.length(), name.length() - side.length() - 1);
                        byInteraction.computeIfAbsent(suffix, k -> new LinkedHashMap<>()).put(side, p);
                    });

            for (var entry : byInteraction.entrySet()) {
                var sides = entry.getValue();
                var testPath = sides.get(TEST_LABEL);
                var refPath = sides.get(REFERENCE_LABEL);
                w.printf("<p class='lbl'>%s</p>%n", entry.getKey());
                if (testPath != null && refPath != null
                        && Files.exists(testPath) && Files.exists(refPath)) {
                    var testRel = outputDir.relativize(testPath).toString().replace('\\', '/');
                    var refRel = outputDir.relativize(refPath).toString().replace('\\', '/');
                    w.printf("<div class='img-toggle' onclick=\"this.classList.toggle('show-ref')\">%n");
                    w.printf("<img class='test-img' src='%s'>%n", testRel);
                    w.printf("<img class='ref-img' src='%s'>%n", refRel);
                    w.println("</div>");
                } else {
                    w.println("<div class='shots'>");
                    writeImg(w, testPath, TEST_LABEL, outputDir);
                    writeImg(w, refPath, REFERENCE_LABEL, outputDir);
                    w.println("</div>");
                }
            }
        } catch (IOException e) {
            // ignore missing dir
        }
    }

    private static void writeImg(PrintWriter w, Path path, String label, Path outputDir) {
        w.printf("<div><div class='lbl'>%s</div>%n", label);
        if (path != null && Files.exists(path)) {
            var rel = outputDir.relativize(path).toString().replace('\\', '/');
            w.printf("<img src='%s'></div>%n", rel);
        } else {
            w.println("<em>n/a</em></div>");
        }
    }

    private static String capitalize(String s) {
        return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
