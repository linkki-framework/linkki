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

import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

class HtmlContentBuilderTest {

    @Test
    void testAppendHtmlContent() {
        var builder = new HtmlContentBuilder();

        var innerBuilder = new HtmlContentBuilder();
        var innerHtml = innerBuilder.tag("div", Map.of("id", "inner"), "innerText").build();
        var html = builder.tag("div", Map.of("id", "outer"), innerHtml).build();

        assertThat(html.toString())
                .isEqualToIgnoringWhitespace("""
                        <div id="outer">
                            <div id="inner">innerText</div>
                        </div>
                        """);
    }

    @Test
    void testCreateStyledTag() {
        var html = HtmlContent.styledTag("div", "color: red;", "hello");

        assertThat(html.toString()).isEqualToIgnoringWhitespace("<div style=\"color: red;\">hello</div>");
    }

    @Test
    void appendHtml() {
        var html = new HtmlContentBuilder().tag("div", Map.of("style", "color: red;"), "hello").build();
        assertThat(html.toString()).isEqualToIgnoringWhitespace("<div style=\"color: red;\">hello</div>");
    }

    @Test
    void appendHtml_attributesContainingLessOrGreaterThenSigns() {
        // attributes containing < or > should not be appended
        var html = new HtmlContentBuilder().tag("div", Map.of("style<", "color: red;"), "hello").build();
        assertThat(html.toString()).isEqualToIgnoringWhitespace("<div>hello</div>");
    }

    @Test
    void testAppendHtml_withPropertyMap() {
        var attributes = new TreeMap<String, String>();
        attributes.put("style", "color: red;");
        attributes.put("class", "redDiv");

        var html = new HtmlContentBuilder()
                .tag("div", attributes).build();
        assertThat(html.toString())
                .isEqualToIgnoringWhitespace("""
                        <div class="redDiv" style="color: red;"></div>
                        """);
    }

    @Test
    void testAppendHtml_withTag() {
        var html = new HtmlContentBuilder().tag("div").build();
        assertThat(html.toString()).isEqualToIgnoringWhitespace("<div></div>");
    }

    @Test
    void testAppendHtml_invalidTag_shouldNotBeAppended() {
        var html = new HtmlContentBuilder().tag("<vaadin-horizontal-layout>").tag("div").build();
        assertThat(html.toString()).isEqualToIgnoringWhitespace("&lt;&lt;vaadin-horizontal-layout&gt;&gt;<div></div>");
    }
}
