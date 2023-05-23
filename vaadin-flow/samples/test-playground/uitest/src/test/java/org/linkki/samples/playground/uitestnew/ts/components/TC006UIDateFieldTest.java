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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

class TC006UIDateFieldTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS005, TestScenarioView.TC006);
    }

    @Test
    void testDateFieldBehaviour() {
        $(TextFieldElement.class).id("textFieldOne").sendKeys("text" + " " + Keys.TAB);

        // active element is the input field of the date field!
        WebElement dateInputField = getActiveElement();

        assertThat(getElemendId(dateInputField)).contains("vaadin-date-picker");
        assertThat(getParentId(dateInputField)).isEqualTo("date");

        // change date
        dateInputField.sendKeys("01.01.2031");
        // no popup
        By tagName = By.tagName("vaadin-date-picker-overlay");
        assertThrows(NoSuchElementException.class, () -> dateInputField.findElement(tagName));

        dateInputField.sendKeys(Keys.TAB);

        WebElement textFieldTwo = getActiveElement();

        assertThat(getElemendId(textFieldTwo)).contains("vaadin-text-field");
        assertThat(getParentId(textFieldTwo)).isEqualTo("textFieldTwo");

        textFieldTwo.sendKeys("some text input");

        assertThat($(TextFieldElement.class).id("textFieldTwo").getValue()).isEqualTo("some text input");
    }

    private WebElement getActiveElement() {
        return getDriver().switchTo().activeElement();
    }

    private String getParentId(WebElement child) {
        return getElemendId(child.findElement(By.xpath("./..")));
    }

    private String getElemendId(WebElement element) {
        return element.getAttribute("id");
    }
}
