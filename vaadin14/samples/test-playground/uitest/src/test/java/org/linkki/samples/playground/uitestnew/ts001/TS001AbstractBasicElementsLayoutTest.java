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

package org.linkki.samples.playground.uitestnew.ts001;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts001.BasicElementsLayoutBehaviorModelObject;
import org.linkki.samples.playground.ts001.BasicElementsLayoutBehaviorModelObject.SampleEnum;
import org.linkki.samples.playground.uitestnew.BaseUITest;
import org.linkki.samples.playground.uitestnew.pageobjects.TestCaseSectionElement;

import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.html.testbench.AnchorElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.radiobutton.testbench.RadioButtonGroupElement;
import com.vaadin.flow.component.textfield.testbench.TextAreaElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

abstract class TS001AbstractBasicElementsLayoutTest extends BaseUITest {

    private TestCaseSectionElement testCaseSection;

    @BeforeEach
    void setupEach() {
        testCaseSection = gotoTestCaseSection("TS001", getTestCaseId());
    }

    protected abstract String getTestCaseId();

    @Test
    void testLabel() {
        DivElement label = testCaseSection.getContentWrapper()//
                .$(DivElement.class).id("textLabel");
        assertThat(label.getText()).isEqualTo("I am a text");
    }

    @Test
    void testTextField() {
        TextFieldElement textField = testCaseSection.getContentWrapper()//
                .$(TextFieldElement.class).id("text");

        // precondition
        assertThat(textField.getValue()).isEqualTo("I am a text");

        // action
        textField.sendKeys(" that was changed!");

        // postcondition
        assertThat(textField.getValue()).isEqualTo("I am a text that was changed!");
    }

    @Test
    void testTextArea() {
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
    void testCheckBox() {
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
    void testRadioButton() {
        RadioButtonGroupElement radioButtons = testCaseSection.getContentWrapper()//
                .$(RadioButtonGroupElement.class)
                .id("enumValueRadioButton");

        // precondition
        assertThat(radioButtons.getSelectedText()).isNull();

        // action
        radioButtons.selectByText(SampleEnum.VALUE3.name());

        // postcondition
        assertThat(radioButtons.getSelectedText()).isEqualTo(SampleEnum.VALUE3.name());
    }

    @Test
    void testLink() {
        AnchorElement link = testCaseSection.getContentWrapper()//
                .$(AnchorElement.class)
                .id("link");

        // conditions
        assertThat(link.getText()).isEqualTo("I am a Link to #");
        assertThat(link.getAttribute("href")).endsWith("/#");
    }

    // TODO LIN-2343 more tests for all other elements in this section?

}
