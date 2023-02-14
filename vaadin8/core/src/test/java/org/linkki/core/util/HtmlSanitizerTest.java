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

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link HtmlSanitizer}.
 */
class HtmlSanitizerTest {
    @Test
    void testSanitize_TextIsNull() {
        assertThat(HtmlSanitizer.sanitizeText(null), nullValue());
    }

    @Test
    void testSanitize_StylingTags() {
        assertThat(HtmlSanitizer.sanitizeText("<p>Test</p>"), is("<p>Test</p>"));
        assertThat(HtmlSanitizer.sanitizeText("<span>Test</span>"), is("<span>Test</span>"));
        assertThat(HtmlSanitizer.sanitizeText("<b>Test</b>"), is("<b>Test</b>"));
        assertThat(HtmlSanitizer.sanitizeText("<strong>Test</strong>"), is("<strong>Test</strong>"));
        assertThat(HtmlSanitizer.sanitizeText("<i>Test</i>"), is("<i>Test</i>"));
        assertThat(HtmlSanitizer.sanitizeText("<em>Test</em>"), is("<em>Test</em>"));
        assertThat(HtmlSanitizer.sanitizeText("<u>Test</u>"), is("<u>Test</u>"));
        assertThat(HtmlSanitizer.sanitizeText("<br>"), is("<br>"));
    }

    @Test
    public void testSanitize_DivStylingAttributes() {
        assertThat(HtmlSanitizer.sanitizeText("<div style=\"Test\"></div>"), is("<div style=\"Test\"></div>"));
        assertThat(HtmlSanitizer.sanitizeText("<div id=\"Test\"></div>"), is("<div id=\"Test\"></div>"));
        assertThat(HtmlSanitizer.sanitizeText("<div class=\"Test\"></div>"), is("<div class=\"Test\"></div>"));
    }

    @Test
    public void testSanitize_StyleAttribute() {
        assertThat(HtmlSanitizer.sanitizeText("<u style=\"Test\"></u>"), is("<u style=\"Test\"></u>"));
        assertThat(HtmlSanitizer.sanitizeText("<b style=\"Test\"></b>"), is("<b style=\"Test\"></b>"));
        assertThat(HtmlSanitizer.sanitizeText("<em style=\"Test\"></em>"), is("<em style=\"Test\"></em>"));
        assertThat(HtmlSanitizer.sanitizeText("<i style=\"Test\"></i>"), is("<i style=\"Test\"></i>"));
        assertThat(HtmlSanitizer.sanitizeText("<strong style=\"Test\"></strong>"),
                   is("<strong style=\"Test\"></strong>"));
    }

    @Test
    void testSanitize_ImageTag_AbsoluteUrl() {
        assertThat(HtmlSanitizer.sanitizeText("<img src=\"http://example-test.png\">"),
                   is("<img src=\"http://example-test.png\">"));
    }

    @Test
    void testSanitize_ImageTag_RelativeUrl() {
        assertThat(HtmlSanitizer.sanitizeText("<img src=\"example-test.png\">"), is("<img src=\"example-test.png\">"));
    }

    /**
     * In Vaadin 8, icons are defined by the code {@code <span class="..." style="...">Icon-ID</span>}.
     */
    @Test
    void testSanitize_VaadinIconWithSpanTag() {
        String iconHtml = "<span class=\"Test\" style=\"Test\">Icon</span>";
        assertThat(HtmlSanitizer.sanitizeText(iconHtml), is(iconHtml));
    }

    @Test
    void testSanitize_ForbiddenTags() {
        assertThat(HtmlSanitizer.sanitizeText("<script>"), is(emptyString()));
        assertThat(HtmlSanitizer.sanitizeText("</script>"), is(emptyString()));
    }

    @Test
    void testSanitize_ForbiddenAttributes() {
        assertThat(HtmlSanitizer.sanitizeText("<img src=\"http://example-test.png\" onload=\"Test\">"),
                   is("<img src=\"http://example-test.png\">"));
    }

    @Test
    void testSanitize_IncompleteTags() {
        // incomplete tag
        assertThat(HtmlSanitizer.sanitizeText("<"), is("&lt;"));
        assertThat(HtmlSanitizer.sanitizeText(">"), is("&gt;"));
        assertThat(HtmlSanitizer.sanitizeText("<div"), is(emptyString()));
        assertThat(HtmlSanitizer.sanitizeText("div>"), is("div&gt;"));
    }

    @Test
    void testEscapeHtml() {
        assertThat(HtmlSanitizer.escapeText("<div>"), is("&lt;div&gt;"));
        assertThat(HtmlSanitizer.escapeText("<div style:\"\">"), is("&lt;div style:&quot;&quot;&gt;"));
        assertThat(HtmlSanitizer.escapeText("<div style:\"\"/>"), is("&lt;div style:&quot;&quot;/&gt;"));
        assertThat(HtmlSanitizer.escapeText("</div>"), is("&lt;/div&gt;"));
        assertThat(HtmlSanitizer.escapeText("Test&"), is("Test&amp;"));
        assertThat(HtmlSanitizer.escapeText("Test'"), is("Test&#39;"));
    }
}
