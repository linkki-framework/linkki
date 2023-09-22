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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.timepicker.testbench.TimePickerElement;

class TC016UITimeFieldTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS005, TestScenarioView.TC016);
    }

    @Test
    void testTimeField_EnterTimeManually() {
        typeInTextBox("textFieldOne", "text" + " " + Keys.TAB);
        var timeInputField = getActiveElement();
        assertThat(getElementId(timeInputField)).contains("input-vaadin-time-picker");

        // Change time
        var time = "15:01";
        timeInputField.sendKeys(time);
        // No popup should open
        var tagNameTimeOverlay = By.tagName("vaadin-time-picker-overlay");
        assertThatThrownBy(() -> timeInputField.findElement(tagNameTimeOverlay))
                .isInstanceOf(NoSuchElementException.class);

        // No popup should open on clicking the element
        timeInputField.click();
        assertThatThrownBy(() -> timeInputField.findElement(tagNameTimeOverlay))
                .isInstanceOf(NoSuchElementException.class);

        timeInputField.sendKeys(Keys.TAB);
        // Check time picker value
        var timePickerValue = getTimePickerElement().getValue();
        // Can enter specific time which is not in the step interval
        assertThat(timePickerValue).isEqualTo(time);

        var textFieldTwo = getActiveElement();
        assertThat(getElementId(textFieldTwo)).contains("vaadin-text-field");
        assertThat(getParentId(textFieldTwo)).isEqualTo("textFieldTwo");
    }

    @Test
    void testTimeField_SelectTimeFromDropDown() {
        var timePickerElement = getTimePickerElement();
        timePickerElement.openDropDown();
        var dropDownElementsInView = $("vaadin-time-picker-item").all();

        // Assert steps
        assertThat(dropDownElementsInView.get(0).getText()).isEqualTo("00:00");
        assertThat(dropDownElementsInView.get(1).getText()).isEqualTo("00:30");
        assertThat(dropDownElementsInView.get(2).getText()).isEqualTo("01:00");
        // Select last dropdown element
        timePickerElement.sendKeys(Keys.UP, Keys.ENTER);
        assertThat(timePickerElement.getSelectedText()).isEqualTo("23:30");
    }

    private TimePickerElement getTimePickerElement() {
        return $(TimePickerElement.class).id("time");
    }


    private WebElement getActiveElement() {
        return getDriver().switchTo().activeElement();
    }

    private String getParentId(WebElement child) {
        return getElementId(child.findElement(By.xpath("./..")));
    }

    private String getElementId(WebElement element) {
        return element.getAttribute("id");
    }
}
