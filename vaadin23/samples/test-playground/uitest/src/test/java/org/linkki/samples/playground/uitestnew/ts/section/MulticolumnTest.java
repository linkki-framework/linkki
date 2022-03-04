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

package org.linkki.samples.playground.uitestnew.ts.section;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.LinkkiSectionElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.section.UiFormSectionMultiColumnComponentsPmo;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

class MulticolumnTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS002, TestScenarioView.TC005);
    }

    @Test
    void testTwoColumnLayout() {
        List<TextFieldElement> components = getSection(UiFormSectionMultiColumnComponentsPmo.class).getContent()
                .$(TextFieldElement.class).all();

        assertThat(components.get(0).getLocation().getY()).isEqualTo(components.get(1).getLocation().getY());
    }

    @Test
    void testTabOrder() {
        LinkkiSectionElement section = getSection(UiFormSectionMultiColumnComponentsPmo.class);
        TextFieldElement firstField = section.getContent()
                .$(TextFieldElement.class).id("defaultWidthText");

        firstField.focus();
        new Actions(getDriver()).sendKeys(Keys.TAB).build().perform();
        WebElement activeElement = getDriver().switchTo().activeElement();

        assertThat(activeElement.getAttribute("id")).isEqualTo(section.$(TextFieldElement.class).id("fixedWidthText")
                .findElement(By.tagName("input")).getAttribute("id"));
    }
}
