package org.linkki.samples.playground.compare;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class RewriteHtmlReport {

    @Test
    void rewriteHtmlReport() throws IOException {
        TestReportWriter.writeToHtml(Paths.get(PlaygroundComparisonTest.OUTPUT_DIR));
    }
}
