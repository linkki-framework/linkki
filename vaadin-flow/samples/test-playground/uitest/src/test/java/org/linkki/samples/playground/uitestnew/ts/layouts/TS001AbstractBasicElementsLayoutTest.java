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
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorModelObject;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorModelObject.SampleEnum;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiTextElement;
import org.openqa.selenium.Keys;

import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.radiobutton.testbench.RadioButtonGroupElement;
import com.vaadin.flow.component.textfield.testbench.TextAreaElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

abstract class TS001AbstractBasicElementsLayoutTest extends PlaygroundUiTest {

    private static final String ID_VISIBLE = "allElementsVisible";
    private static final String ID_REQUIRED = "allElementsRequired";
    private static final String ID_READ_ONLY = "allElementsReadOnly";
    private TestCaseComponentElement testCaseSection;

    @BeforeEach
    protected void goToTestCase() {
        testCaseSection = goToTestCase(TestScenarioView.TS001, getTestCaseId());
        testCaseSection.getContentWrapper().$(CheckboxElement.class).id(ID_REQUIRED).setChecked(false);
        testCaseSection.getContentWrapper().$(CheckboxElement.class).id(ID_VISIBLE).setChecked(true);
        testCaseSection.getContentWrapper().$(CheckboxElement.class).id(ID_READ_ONLY).setChecked(false);
    }

    protected abstract String getTestCaseId();

    protected TestCaseComponentElement getTestCaseSection() {
        return testCaseSection;
    }

    @Test
    void testLabel_HasText() {
        LinkkiTextElement label = testCaseSection.getContentWrapper()//
                .$(LinkkiTextElement.class).id("textLabel");
        assertThat(label.getText()).isEqualTo("I am a text");
    }

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

    @Test
    void testLink_HasTextAndHref() {
        var link = testCaseSection.getContentWrapper()//
                .$(LinkkiTextElement.class)
                .id("link");

        // conditions
        assertThat(link.getText()).isEqualTo("I am a Link to #");
        assertThat(link.getContent().getAttribute("href")).endsWith("/#");
    }

    @Test
    void testTextField_ReadOnly() {
        testCaseSection.getContentWrapper().$(CheckboxElement.class).id(ID_READ_ONLY).setChecked(true);

        assertThat(testCaseSection.getContentWrapper().$(TextFieldElement.class).first().hasAttribute("readonly"))
                .isTrue();
    }

    @Test
    void testTextField_DynamicRequired() {
        TextFieldElement textFieldElement = testCaseSection.getContentWrapper().$(TextFieldElement.class).id("text");

        // precondition
        assertThat(textFieldElement.hasAttribute("required")).isFalse();

        // action
        testCaseSection.getContentWrapper().$(CheckboxElement.class).id(ID_REQUIRED).setChecked(true);

        // postcondition
        assertThat(textFieldElement.hasAttribute("required")).isTrue();
    }

    @Test
    void testTextField_Visible() {
        TextFieldElement textFieldElement = testCaseSection.getContentWrapper().$(TextFieldElement.class).id("text");

        // precondition
        assertThat(textFieldElement.hasAttribute("hidden")).isFalse();

        // action
        testCaseSection.getContentWrapper().$(CheckboxElement.class).id(ID_VISIBLE).click();

        // postcondition
        assertThat(textFieldElement.getAttribute("hidden")).isEqualTo("true");
    }

    @Test
    void testTextField_Required_Empty() {
        TextFieldElement textFieldElement = testCaseSection.getContentWrapper().$(TextFieldElement.class).id("text");

        // actions
        testCaseSection.getContentWrapper().$(CheckboxElement.class).id(ID_REQUIRED).setChecked(true);
        textFieldElement.setValue("");
        textFieldElement.sendKeys("\t");

        // postcondition
        assertThat(textFieldElement.hasAttribute("invalid")).as("The presence of the attribute invalid").isTrue();
    }

    // TODO LIN-2343 more tests for all other elements in this section?

}
