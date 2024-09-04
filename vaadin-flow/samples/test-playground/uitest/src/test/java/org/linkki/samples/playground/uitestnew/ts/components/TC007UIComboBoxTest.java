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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.Keys;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.html.testbench.InputTextElement;

class TC007UIComboBoxTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS005, TestScenarioView.TC007);
    }

    @Test
    void testClearButton_ComboBoxWithNull() {
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id("directionWithNull");

        assertThat(hasClearButton(comboBoxElement)).isTrue();
    }

    @Test
    void testClearButton_ComboBoxWithoutNull() {
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id("directionWithoutNull");

        assertThat(hasClearButton(comboBoxElement)).isFalse();
    }

    private boolean hasClearButton(ComboBoxElement element) {
        return element.hasAttribute("clear-button-visible");
    }

    @Test
    void testRemoveContent_ComboBoxWithNull() {
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id("directionWithNull");
        comboBoxElement.clear();

        assertThat(comboBoxElement.getInputElementValue()).isNullOrEmpty();
    }

    @Test
    void testRemoveContent_ComboBoxWithoutNull() {
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id("directionWithoutNull");
        comboBoxElement.clear();

        assertThat(comboBoxElement.getInputElementValue()).isNotNull();
    }

    @Test
    void testLeftAligned() {
        ComboBoxElement comboBox = $(ComboBoxElement.class).id("leftAligned");

        comboBox.openPopup();

        // should be 'left' but Vaadin set 'start' instead
        assertThat(comboBox.$(InputTextElement.class).first().getCssValue("text-align")).isEqualTo("start");
        $("vaadin-combo-box-item").all().forEach(i -> assertThat(i.$(DivElement.class).last()
                .getCssValue("text-align")).isEqualTo("left"));

        comboBox.closePopup();
    }

    @Test
    void testCenterAligned() {
        ComboBoxElement comboBox = $(ComboBoxElement.class).id("centerAligned");

        comboBox.openPopup();

        assertThat(comboBox.$(InputTextElement.class).first().getCssValue("text-align")).isEqualTo("center");
        $("vaadin-combo-box-item").all().forEach(i -> assertThat(i.$(DivElement.class).last()
                .getCssValue("text-align")).isEqualTo("center"));

        comboBox.closePopup();
    }

    @Test
    void testRightAligned() {
        ComboBoxElement comboBox = $(ComboBoxElement.class).id("rightAligned");

        comboBox.openPopup();

        // should be 'right' but Vaadin set 'end' instead
        assertThat(comboBox.$(InputTextElement.class).first().getCssValue("text-align")).isEqualTo("end");
        $("vaadin-combo-box-item").all().forEach(i -> assertThat(i.$(DivElement.class).last()
                .getCssValue("text-align")).isEqualTo("right"));

        comboBox.closePopup();
    }

    @Test
    void testFillInitiallyNonNullButRequiredString_shouldNotBeInvalid() {
        ComboBoxElement comboBox = $(ComboBoxElement.class).id("requiredStringValue");
        assertThat(comboBox.hasAttribute("invalid")).isFalse();
        comboBox.selectByText("1");
        assertThat(comboBox.hasAttribute("invalid")).isFalse();
    }

    @Test
    void testFillInitiallyNonNullButRequiredDecimal_shouldNotBeInvalid() {
        ComboBoxElement comboBox = $(ComboBoxElement.class).id("requiredDecimalValue");
        assertThat(comboBox.hasAttribute("invalid")).isFalse();
        comboBox.selectByText("1,00");
        assertThat(comboBox.hasAttribute("invalid")).isFalse();
    }

    @Test
    void testFillInitiallyNonNullAndNotRequiredDecimal_shouldNotBeInvalid() {
        ComboBoxElement comboBox = $(ComboBoxElement.class).id("notRequiredDecimalValue");
        assertThat(comboBox.hasAttribute("invalid")).isFalse();
        comboBox.selectByText("1,00");
        assertThat(comboBox.hasAttribute("invalid")).isFalse();
        comboBox.selectByText("");
        assertThat(comboBox.hasAttribute("invalid")).isFalse();
        comboBox.selectByText("1,00");
        assertThat(comboBox.hasAttribute("invalid")).isFalse();
    }

    @Test
    void testFocusBehavior_OneMatchingElement() {
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id("directionWithNull");
        comboBoxElement.selectByText("DOWN");

        comboBoxElement.sendKeys(Keys.TAB);
        getDriver().switchTo().activeElement().sendKeys(Keys.SHIFT, Keys.TAB);
        comboBoxElement.sendKeys("U");
        comboBoxElement.sendKeys(Keys.TAB);

        assertThat(comboBoxElement.getInputElementValue())
                .isEqualTo("UP");
    }

    @Test
    void testFocusBehavior_MultipleMatchingElement() {
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id("directionWithNull");
        comboBoxElement.selectByText("DOWN");

        comboBoxElement.sendKeys(Keys.TAB);
        getDriver().switchTo().activeElement().sendKeys(Keys.SHIFT, Keys.TAB);
        comboBoxElement.sendKeys("t");
        comboBoxElement.sendKeys(Keys.TAB);

        assertThat(comboBoxElement.getInputElementValue())
                .isEqualTo("DOWN");
    }

    @Test
    void testFocusBehavior_NonExisting() {
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id("directionWithNull");
        comboBoxElement.selectByText("UP");

        comboBoxElement.sendKeys(Keys.TAB);
        getDriver().switchTo().activeElement().sendKeys(Keys.SHIFT, Keys.TAB);
        comboBoxElement.sendKeys("DOWN");
        comboBoxElement.sendKeys("Non existing");
        comboBoxElement.sendKeys(Keys.TAB);

        assertThat(comboBoxElement.getInputElementValue())
                .as("Inputting non existing option should focus no option")
                .isEqualTo("UP");
    }

    @Test
    void testFocusBehavior_OpenPopupWithoutInput() {
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id("directionWithNull");
        comboBoxElement.clear();

        // in reality, this can be achieved by clicking on the arrow button
        comboBoxElement.openPopup();
        comboBoxElement.sendKeys(Keys.TAB);

        assertThat(comboBoxElement.getInputElementValue())
                .as("Opening the popup should not result in any values being selected")
                .isEqualTo("");
    }

    @Test
    void testFocusBehavior_InputSameLengthAsValue() {
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id("directionWithNull");
        comboBoxElement.selectByText("UP");

        comboBoxElement.sendKeys(Keys.TAB);
        getDriver().switchTo().activeElement().sendKeys(Keys.SHIFT, Keys.TAB);
        // input has the same length as the value, used to be a bug in the script
        comboBoxElement.sendKeys("ri");
        comboBoxElement.sendKeys(Keys.TAB);

        assertThat(comboBoxElement.getInputElementValue())
                .as("If the input has the same length as the value, the matching input should still be focused. " +
                        "This used to be a bug in the javascript.")
                .isEqualTo("RIGHT");
    }

    @Test
    void testFocusBehavior_Delete_WithNull() {
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id("directionWithNull");
        comboBoxElement.selectByText("RIGHT");

        comboBoxElement.sendKeys(Keys.TAB);
        getDriver().switchTo().activeElement().sendKeys(Keys.SHIFT, Keys.TAB);
        comboBoxElement.sendKeys(Keys.ESCAPE);
        comboBoxElement.sendKeys(Keys.TAB);

        assertThat(comboBoxElement.getInputElementValue())
                .as("Escape should remove the value if null is allowed " +
                        "(Vaadin default behavior should not be broken)")
                .isEqualTo("");
    }

    @Test
    void testFocusBehavior_Delete_WithoutNull() {
        ComboBoxElement comboBoxWithoutNull = $(ComboBoxElement.class).id("directionWithoutNull");
        comboBoxWithoutNull.selectByText("DOWN");

        comboBoxWithoutNull.sendKeys(Keys.TAB);
        getDriver().switchTo().activeElement().sendKeys(Keys.SHIFT, Keys.TAB);
        comboBoxWithoutNull.sendKeys(Keys.DELETE);
        comboBoxWithoutNull.sendKeys(Keys.TAB);

        assertThat(comboBoxWithoutNull.getInputElementValue())
                .as("Delete should not change the value if null is not allowed")
                .isEqualTo("DOWN");
    }

}
