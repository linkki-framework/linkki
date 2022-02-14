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

package org.linkki.samples.playground.uitestnew.ts.layouts;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.LinkkiTextElement;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorModelObject;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorModelObject.SampleEnum;
import org.linkki.samples.playground.ui.PlaygroundApplicationView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.Keys;

import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.html.testbench.AnchorElement;
import com.vaadin.flow.component.radiobutton.testbench.RadioButtonGroupElement;
import com.vaadin.flow.component.textfield.testbench.TextAreaElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

abstract class TS001AbstractBasicElementsLayoutTest extends PlaygroundUiTest {

    private TestCaseComponentElement testCaseSection;

    @BeforeEach
    void setup() {
        super.setUp();
        testCaseSection = goToTestCase(PlaygroundApplicationView.TS001, getTestCaseId());
    }

    protected abstract String getTestCaseId();

    protected TestCaseComponentElement getTestCaseSection() {
        return testCaseSection;
    }

    @Order(10)
    @Test
    void testLabel_HasText() {
        LinkkiTextElement label = testCaseSection.getContentWrapper()//
                .$(LinkkiTextElement.class).id("textLabel");
        assertThat(label.getText()).isEqualTo("I am a text");
    }

    @Order(20)
    @Test
    void testTextField_SetValue() {
        TextFieldElement textField = testCaseSection.getContentWrapper()//
                .$(TextFieldElement.class).id("text");

        // precondition
        assertThat(textField.getValue()).isEqualTo("I am a text");

        // action
        textField.sendKeys(Keys.END + " that was changed!");

        // postcondition
        assertThat(textField.getValue()).isEqualTo("I am a text that was changed!");
    }

    @Order(30)
    @Test
    void testTextArea_SetValue() {
        TextAreaElement textArea = testCaseSection.getContentWrapper()//
                .$(TextAreaElement.class)
                .id(BasicElementsLayoutBehaviorModelObject.PROPERTY_LONGTEXT);

        // precondition
        assertThat(textArea.getValue()).startsWith("Lorem ipsum");

        // action
        textArea.setValue("");
        textArea.sendKeys("bla bla");

        // postcondition
        assertThat(textArea.getValue()).isEqualTo("bla bla");
    }

    @Order(40)
    @Test
    void testCheckBox_IsCheckable() {
        CheckboxElement checkBox = testCaseSection.getContentWrapper()//
                .$(CheckboxElement.class)
                .id(BasicElementsLayoutBehaviorModelObject.PROPERTY_BOOLEANVALUE);

        // precondition
        assertThat(checkBox.isChecked()).isTrue();

        // action
        checkBox.click();

        // postcondition
        assertThat(checkBox.isChecked()).isFalse();
    }

    @Order(50)
    @Test
    void testRadioButtons_IsSelectable() {
        RadioButtonGroupElement radioButtons = testCaseSection.getContentWrapper()//
                .$(RadioButtonGroupElement.class)
                .id("enumValueRadioButton");

        // precondition
        assertThat(radioButtons.getSelectedText()).isNull();

        // action
        radioButtons.selectByText(SampleEnum.VALUE3.getName());

        // postcondition
        assertThat(radioButtons.getSelectedText()).isEqualTo(SampleEnum.VALUE3.getName());
    }

    @Order(60)
    @Test
    void testLink_HasTextAndHref() {
        AnchorElement link = testCaseSection.getContentWrapper()//
                .$(AnchorElement.class)
                .id("link");

        // conditions
        assertThat(link.getText()).isEqualTo("I am a Link to #");
        assertThat(link.getAttribute("href")).endsWith("/#");
    }

    @Order(70)
    @Test
    void testTextField_ReadOnly() {
        testCaseSection.getContentWrapper().$(CheckboxElement.class).id("allElementsReadOnly").click();

        assertThat(testCaseSection.getContentWrapper().$(TextFieldElement.class).first().hasAttribute("readonly"))
                .isTrue();
    }

    @Order(80)
    @Test
    void testTextField_DynamicRequired() {
        TextFieldElement textFieldElement = testCaseSection.getContentWrapper().$(TextFieldElement.class).id("text");

        // precondition
        assertThat(textFieldElement.hasAttribute("required")).isFalse();

        // action
        testCaseSection.getContentWrapper().$(CheckboxElement.class).id("allElementsRequired").click();

        // postcondition
        assertThat(textFieldElement.hasAttribute("required")).isTrue();
    }

    @Order(90)
    @Test
    void testTextField_Visible() {
        TextFieldElement textFieldElement = testCaseSection.getContentWrapper().$(TextFieldElement.class).id("text");

        // precondition
        assertThat(textFieldElement.hasAttribute("hidden")).isFalse();

        // action
        testCaseSection.getContentWrapper().$(CheckboxElement.class).id("allElementsVisible").click();

        // postcondition
        assertThat(textFieldElement.getAttribute("hidden")).isEqualTo("true");
    }

    @Order(100)
    @Test
    void testTextField_Required_Empty() {
        TextFieldElement textFieldElement = testCaseSection.getContentWrapper().$(TextFieldElement.class).id("text");

        // actions
        testCaseSection.getContentWrapper().$(CheckboxElement.class).id("allElementsVisible").click();
        testCaseSection.getContentWrapper().$(CheckboxElement.class).id("allElementsReadOnly").click();
        textFieldElement.setValue("");
        textFieldElement.sendKeys("\t");

        // postcondition
        assertThat(textFieldElement.hasAttribute("invalid")).isTrue();
    }

    // TODO LIN-2343 more tests for all other elements in this section?

}
