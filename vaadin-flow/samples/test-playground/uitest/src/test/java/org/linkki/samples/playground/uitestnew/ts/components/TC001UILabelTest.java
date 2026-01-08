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

package org.linkki.samples.playground.uitestnew.ts.components;

import com.vaadin.flow.component.combobox.testbench.MultiSelectComboBoxElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.ui.aspects.types.IconPosition;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiTextElement;
import org.openqa.selenium.By;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.samples.playground.uitestnew.ts.components.util.IconTestUtil.verifyIconPosition;

class TC001UILabelTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS005, TestScenarioView.TC001);
    }

    @Test
    void testLabel_HtmlContent() {
        LinkkiTextElement label =
                $("div").id("htmlContentPropertyLabelPmo").$(LinkkiTextElement.class).id("htmlContentLabel");

        assertThat(label.getLabel()).isEqualTo("Label with HTML Content");

        assertThat(label.getText()).isEqualTo("HTML Content");
        assertThat(label.findElements(By.tagName("i"))).hasSize(1);
        assertThat(label.findElements(By.tagName("b"))).hasSize(1);
        assertThat(label.getHTMLContent())
                .isEqualTo("<i style=\"color: red;\">HTML</i> <b>Content</b>");
    }

    @Test
    void testLabel_SanitizeHtmlContent() {
        LinkkiTextElement label =
                $("div").id("htmlContentPropertyLabelPmo").$(LinkkiTextElement.class).id("sanitizedHtmlContentLabel");
        assertThat(label.getLabel()).isEqualTo("Label with sanitized HTML content");
        assertThat(label.findElements(By.tagName("iframe"))).isEmpty();
        assertThat(label.findElements(By.tagName("b"))).hasSize(1);
        assertThat(label.getText())
                .isEqualTo("");
        assertThat(label.getHTMLContent())
                .isEqualTo("<b></b>");
    }

    @Test
    void testLabel_SanitizedHtmlContentWithIcon() {
        LinkkiTextElement label = $("div").id("htmlContentPropertyLabelPmo").$(LinkkiTextElement.class)
                .id("sanitizedHtmlContentWithIconLabel");
        assertThat(label.getLabel()).isEqualTo("Sanitized HTML content containing a Vaadin icon");
        assertThat(label.findElements(By.tagName("vaadin-icon"))).hasSize(1);
        assertThat(label.getHTMLContent()).startsWith("This text should end with a red plus icon " +
                "<vaadin-icon icon=\"vaadin:plus\" style=\"fill:red\">");
    }

    @Test
    void testLabel_NotHtmlContent() {
        LinkkiTextElement label =
                $("div").id("htmlContentPropertyLabelPmo").$(LinkkiTextElement.class).id("notHtmlContentLabel");

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
    void testLabel_CustomStyle_RightAligned() {
        var label = $(LinkkiTextElement.class).withId("rightAlignedLabel").first();

        assertThat(label.getCssValue("text-align")).isEqualTo("right");
        assertThat(label.getCssValue("color")).isEqualTo("rgba(128, 0, 128, 1)");
        assertThat(label.$("span").first().getCssValue("color")).isEqualTo("rgba(128, 0, 128, 1)");
    }

    @Test
    void testLabel_WithTextColor_Info() {
        LinkkiTextElement linkkiTextElement = $(LinkkiTextElement.class).id("styledLabelAndIcon");

        selectStyles("text-info");

        assertThat(linkkiTextElement.$("span").first().getCssValue("color")).isEqualTo("rgba(26, 117, 230, 1)");
        assertThat(linkkiTextElement.$("vaadin-icon").get(1).getCssValue("color")).isEqualTo("rgba(24, 39, 57, 0.94)");
    }

    @Test
    void testLabel_WithTextColor_Warning() {
        LinkkiTextElement linkkiTextElement = $(LinkkiTextElement.class).id("styledLabelAndIcon");

        selectStyles("text-warning");

        assertThat(linkkiTextElement.$("span").first().getCssValue("color")).isEqualTo("rgba(189, 164, 0, 1)");
        assertThat(linkkiTextElement.$("vaadin-icon").get(1).getCssValue("color")).isEqualTo("rgba(24, 39, 57, 0.94)");
    }

    @Test
    void testLabel_WithTextColor_Error() {
        LinkkiTextElement linkkiTextElement = $(LinkkiTextElement.class).id("styledLabelAndIcon");

        selectStyles("text-error");

        assertThat(linkkiTextElement.$("span").first().getCssValue("color")).isEqualTo("rgba(202, 21, 12, 1)");
        assertThat(linkkiTextElement.$("vaadin-icon").get(1).getCssValue("color")).isEqualTo("rgba(24, 39, 57, 0.94)");
    }

    @Test
    void testLabel_WithIconColor_Info() {
        LinkkiTextElement linkkiTextElement = $(LinkkiTextElement.class).id("styledLabelAndIcon");

        selectStyles("icon-info");

        assertThat(linkkiTextElement.$("span").first().getCssValue("color")).isEqualTo("rgba(24, 39, 57, 0.94)");
        assertThat(linkkiTextElement.$("vaadin-icon").get(1).getCssValue("color"))
                .isEqualTo("rgba(26, 117, 230, 1)");
    }

    @Test
    void testLabel_WithIconColor_Warning() {
        LinkkiTextElement linkkiTextElement = $(LinkkiTextElement.class).id("styledLabelAndIcon");

        selectStyles("icon-warning");

        assertThat(linkkiTextElement.$("span").first().getCssValue("color")).isEqualTo("rgba(24, 39, 57, 0.94)");
        assertThat(linkkiTextElement.$("vaadin-icon").get(1).getCssValue("color")).isEqualTo("rgba(255, 204, 0, 1)");
    }

    @Test
    void testLabel_WithIconColor_Error() {
        LinkkiTextElement linkkiTextElement = $(LinkkiTextElement.class).id("styledLabelAndIcon");

        selectStyles("icon-error");

        assertThat(linkkiTextElement.$("span").first().getCssValue("color")).isEqualTo("rgba(24, 39, 57, 0.94)");
        assertThat(linkkiTextElement.$("vaadin-icon").get(1).getCssValue("color")).isEqualTo("rgba(226, 29, 18, 1)");
    }

    @Test
    void testLabel_WithTextAndIconColor_Info() {
        LinkkiTextElement linkkiTextElement = $(LinkkiTextElement.class).id("styledLabelAndIcon");

        selectStyles("text-info", "icon-info");

        assertThat(linkkiTextElement.$("span").first().getCssValue("color")).isEqualTo("rgba(26, 117, 230, 1)");
        assertThat(linkkiTextElement.$("vaadin-icon").get(1).getCssValue("color")).isEqualTo("rgba(26, 117, 230, 1)");
    }

    @Test
    void testLabel_WithTextAndIconColor_Warning() {
        LinkkiTextElement linkkiTextElement = $(LinkkiTextElement.class).id("styledLabelAndIcon");

        selectStyles("text-warning", "icon-warning");

        assertThat(linkkiTextElement.$("span").first().getCssValue("color")).isEqualTo("rgba(189, 164, 0, 1)");
        assertThat(linkkiTextElement.$("vaadin-icon").get(1).getCssValue("color")).isEqualTo("rgba(255, 204, 0, 1)");
    }

    @Test
    void testLabel_WithTextAndIconColor_Error() {
        LinkkiTextElement linkkiTextElement = $(LinkkiTextElement.class).id("styledLabelAndIcon");

        selectStyles("text-error", "icon-error");

        assertThat(linkkiTextElement.$("span").first().getCssValue("color")).isEqualTo("rgba(202, 21, 12, 1)");
        assertThat(linkkiTextElement.$("vaadin-icon").get(1).getCssValue("color")).isEqualTo("rgba(226, 29, 18, 1)");
    }

    @Test
    void testLabel_WithDifferentIconAndTextColor() {
        LinkkiTextElement linkkiTextElement = $(LinkkiTextElement.class).id("styledLabelAndIcon");

        selectStyles("text-success", "icon-info");

        assertThat(linkkiTextElement.$("span").first().getCssValue("color")).isEqualTo("rgba(10, 118, 55, 1)");
        assertThat(linkkiTextElement.$("vaadin-icon").get(1).getCssValue("color")).isEqualTo("rgba(26, 117, 230, 1)");
    }

    @Test
    void testLabel_IconTextColorNotAppliedToHtmlTextIcon() {
        LinkkiTextElement linkkiTextElement = $(LinkkiTextElement.class).id("styledLabelAndIcon");

        selectStyles("text-success", "icon-info");

        assertThat(linkkiTextElement.$("span").first().getCssValue("color")).isEqualTo("rgba(10, 118, 55, 1)");
        // linkki text inner icon has text color but not icon-text-color applied to the additional
        // icon
        assertThat(linkkiTextElement.$("vaadin-icon").first().getCssValue("color")).isEqualTo("rgba(10, 118, 55, 1)");
    }

    private void selectStyles(String... elementsToSelect) {
        MultiSelectComboBoxElement multiSelectComboBoxElement = $(MultiSelectComboBoxElement.class)
                .id("styledLabelAndIconStyleNames");
        multiSelectComboBoxElement.deselectAll();
        multiSelectComboBoxElement.openPopup();
        Stream.of(elementsToSelect).forEach(multiSelectComboBoxElement::selectByText);
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
