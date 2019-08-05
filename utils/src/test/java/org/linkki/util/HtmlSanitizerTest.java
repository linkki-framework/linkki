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

package org.linkki.util;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class HtmlSanitizerTest {

    @Test
    public void testSanitize() {
        // allowed tags
        assertThat(HtmlSanitizer.sanitize("<p>"), is("<p>"));
        assertThat(HtmlSanitizer.sanitize("</p>"), is("</p>"));
        assertThat(HtmlSanitizer.sanitize("<div>"), is("<div>"));
        assertThat(HtmlSanitizer.sanitize("</div>"), is("</div>"));
        assertThat(HtmlSanitizer.sanitize("<span>"), is("<span>"));
        assertThat(HtmlSanitizer.sanitize("</span>"), is("</span>"));
        assertThat(HtmlSanitizer.sanitize("<br>"), is("<br>"));
        assertThat(HtmlSanitizer.sanitize("</br>"), is("</br>"));
        assertThat(HtmlSanitizer.sanitize("<b>"), is("<b>"));
        assertThat(HtmlSanitizer.sanitize("</b>"), is("</b>"));
        assertThat(HtmlSanitizer.sanitize("<strong>"), is("<strong>"));
        assertThat(HtmlSanitizer.sanitize("</strong>"), is("</strong>"));
        assertThat(HtmlSanitizer.sanitize("<i>"), is("<i>"));
        assertThat(HtmlSanitizer.sanitize("</i>"), is("</i>"));
        assertThat(HtmlSanitizer.sanitize("<em>"), is("<em>"));
        assertThat(HtmlSanitizer.sanitize("</em>"), is("</em>"));
        assertThat(HtmlSanitizer.sanitize("<u>"), is("<u>"));
        assertThat(HtmlSanitizer.sanitize("</u>"), is("</u>"));

        // forbidden tags
        assertThat(HtmlSanitizer.sanitize("<script>"), is("&lt;script&gt;"));
        assertThat(HtmlSanitizer.sanitize("</script>"), is("&lt;/script&gt;"));
        assertThat(HtmlSanitizer.sanitize("<style>"), is("&lt;style&gt;"));
        assertThat(HtmlSanitizer.sanitize("</style>"), is("&lt;/style&gt;"));

        // incomplete tag
        assertThat(HtmlSanitizer.sanitize("<"), is("&lt;"));
        assertThat(HtmlSanitizer.sanitize(">"), is("&gt;"));
        assertThat(HtmlSanitizer.sanitize("div"), is("div"));
        assertThat(HtmlSanitizer.sanitize("<div"), is("&lt;div"));
        assertThat(HtmlSanitizer.sanitize("div>"), is("div&gt;"));
    }

}
