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

package org.linkki.samples.playground.uitestnew.ts.components;

import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.samples.playground.uitestnew.ts.components.util.IconTestUtil.verifyIconPosition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.ui.aspects.types.IconPosition;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiTextElement;
import org.openqa.selenium.By;

class TC001UILabelTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS005, TestScenarioView.TC001);
    }

    @Test
    void testLabel_HtmlContent() {
        LinkkiTextElement label = $(LinkkiTextElement.class).id("htmlContentLabel");

        assertThat(label.getLabel()).isEqualTo("Label with HTML Content");

        assertThat(label.getText()).isEqualTo("HTML Content");
        assertThat(label.findElements(By.tagName("i"))).hasSize(1);
        assertThat(label.findElements(By.tagName("b"))).hasSize(1);
        assertThat(label.getHTMLContent())
                .isEqualTo("<i style=\"color: red;\">HTML</i> <b>Content</b>");
    }

    @Test
    void testLabel_SanitizeHtmlContent() {
        LinkkiTextElement label = $(LinkkiTextElement.class).id("sanitizedHtmlContentLabel");
        assertThat(label.getLabel()).isEqualTo("Label with sanitized HTML content");
        assertThat(label.findElements(By.tagName("iframe"))).isEmpty();
        assertThat(label.findElements(By.tagName("b"))).hasSize(1);
        assertThat(label.getText())
                .isEqualTo("This should be bold text without showing the stripped tag 'iframe'");
        assertThat(label.getHTMLContent())
                .isEqualTo("<b>This should be bold text without showing the stripped tag 'iframe'</b>");
    }
    
    @Test
    void testLabel_SanitizedHtmlContentWithIcon() {
        LinkkiTextElement label = $(LinkkiTextElement.class).id("sanitizedHtmlContentWithIconLabel");
        assertThat(label.getLabel()).isEqualTo("Sanitized HTML content containing a Vaadin icon");
        assertThat(label.findElements(By.tagName("vaadin-icon"))).hasSize(1);
        assertThat(label.getHTMLContent()).startsWith("This text should end with a red plus icon " +
                "<vaadin-icon icon=\"vaadin:plus\" style=\"fill:red\">");
    }

    @Test
    void testLabel_NotHtmlContent() {
        LinkkiTextElement label = $(LinkkiTextElement.class).id("notHtmlContentLabel");

        assertThat(label.getText()).isEqualTo("<b>NOT</b> HTML Content");
        assertThat(label.findElements(By.tagName("b"))).isEmpty();
        assertThat(label.getHTMLContent())
                .isEqualTo("&lt;b&gt;NOT&lt;/b&gt; HTML Content");
    }

    @Test
    void testLabel_WithConverterForBigDecimalValues() {
        LinkkiTextElement label = $(LinkkiTextElement.class).id("bigDecimalLabel");

        assertThat(label.getText()).isEqualTo("12.345,679");
    }

    @Test
    void testLabel_WithCustomStyle() {
        LinkkiTextElement label = $(LinkkiTextElement.class).id("styledLabel");

        assertThat(label.getCssValue("color")).isEqualTo("rgba(0, 128, 0, 1)");
    }

    @Test
    void testLabel_WithIconLeft() {
        LinkkiTextElement label = $(LinkkiTextElement.class).id("iconLeftLabel");

        verifyIconPosition(label, IconPosition.LEFT);
    }

    @Test
    void testLabel_WithIconRight() {
        LinkkiTextElement label = $(LinkkiTextElement.class).id("iconRightLabel");

        verifyIconPosition(label, IconPosition.RIGHT);
    }
}
