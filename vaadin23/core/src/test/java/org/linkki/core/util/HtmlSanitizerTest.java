/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link HtmlSanitizer}.
 */
class HtmlSanitizerTest {

    @Test
    void testSanitize_TextIsNull() {
        assertThat(HtmlSanitizer.sanitizeText(null)).isNull();
    }

    @Test
    void testSanitize_StylingTags() {
        assertThat(HtmlSanitizer.sanitizeText("<p>Test</p>")).isEqualTo("<p>Test</p>");
        assertThat(HtmlSanitizer.sanitizeText("<span>Test</span>")).isEqualTo("<span>Test</span>");
        assertThat(HtmlSanitizer.sanitizeText("<b>Test</b>")).isEqualTo("<b>Test</b>");
        assertThat(HtmlSanitizer.sanitizeText("<strong>Test</strong>")).isEqualTo("<strong>Test</strong>");
        assertThat(HtmlSanitizer.sanitizeText("<i>Test</i>")).isEqualTo("<i>Test</i>");
        assertThat(HtmlSanitizer.sanitizeText("<em>Test</em>")).isEqualTo("<em>Test</em>");
        assertThat(HtmlSanitizer.sanitizeText("<u>Test</u>")).isEqualTo("<u>Test</u>");
        assertThat(HtmlSanitizer.sanitizeText("<br>")).isEqualTo("<br>");
    }

    @Test
    public void testSanitize_DivStylingAttributes() {
        assertThat(HtmlSanitizer.sanitizeText("<div style=\"Test\"></div>")).isEqualTo("<div style=\"Test\"></div>");
        assertThat(HtmlSanitizer.sanitizeText("<div id=\"Test\"></div>")).isEqualTo("<div id=\"Test\"></div>");
        assertThat(HtmlSanitizer.sanitizeText("<div class=\"Test\"></div>")).isEqualTo("<div class=\"Test\"></div>");
    }

    @Test
    public void testSanitize_StyleAttribute() {
        assertThat(HtmlSanitizer.sanitizeText("<u style=\"Test\"></u>")).isEqualTo("<u style=\"Test\"></u>");
        assertThat(HtmlSanitizer.sanitizeText("<b style=\"Test\"></b>")).isEqualTo("<b style=\"Test\"></b>");
        assertThat(HtmlSanitizer.sanitizeText("<em style=\"Test\"></em>")).isEqualTo("<em style=\"Test\"></em>");
        assertThat(HtmlSanitizer.sanitizeText("<i style=\"Test\"></i>")).isEqualTo("<i style=\"Test\"></i>");
        assertThat(HtmlSanitizer.sanitizeText("<strong style=\"Test\"></strong>"))
                .isEqualTo("<strong style=\"Test\"></strong>");
    }

    @Test
    void testSanitize_ImageTag_AbsoluteUrl() {
        assertThat(HtmlSanitizer.sanitizeText("<img src=\"http://example-test.png\">"))
                .isEqualTo("<img src=\"http://example-test.png\">");
    }

    @Test
    void testSanitize_ImageTag_RelativeUrl() {
        assertThat(HtmlSanitizer.sanitizeText("<img src=\"example-test.png\">"))
                .isEqualTo("<img src=\"example-test.png\">");
    }

    @Test
    void testSanitize_ForbiddenTags() {
        assertThat(HtmlSanitizer.sanitizeText("<script>")).isEmpty();
        assertThat(HtmlSanitizer.sanitizeText("</script>")).isEmpty();
    }

    @Test
    void testSanitize_ForbiddenAttributes() {
        assertThat(HtmlSanitizer.sanitizeText("<img src=\"http://example-test.png\" onload=\"Test\">"))
                .isEqualTo("<img src=\"http://example-test.png\">");
    }

    @Test
    void testSanitize_IncompleteTags() {
        // incomplete tag
        assertThat(HtmlSanitizer.sanitizeText("<")).isEqualTo("&lt;");
        assertThat(HtmlSanitizer.sanitizeText(">")).isEqualTo("&gt;");
        assertThat(HtmlSanitizer.sanitizeText("<div")).isEmpty();
        assertThat(HtmlSanitizer.sanitizeText("div>")).isEqualTo("div&gt;");
    }
}
