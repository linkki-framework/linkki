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

import org.junit.jupiter.api.Test;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.layout.annotation.UICssLayout;
import org.linkki.core.ui.layout.annotation.UIFormLayout;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.core.vaadin.component.base.LinkkiAnchor;
import org.linkki.core.vaadin.component.base.LinkkiText;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorModelObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.datepicker.testbench.DatePickerElement;
import com.vaadin.flow.component.html.testbench.AnchorElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.radiobutton.testbench.RadioButtonGroupElement;
import com.vaadin.flow.component.textfield.testbench.PasswordFieldElement;
import com.vaadin.flow.component.textfield.testbench.TextAreaElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

/**
 * Tests, that {@link LinkkiText labels}, {@link LinkkiAnchor links}, {@link UICheckBox checkboxes} and
 * {@link Button buttons} do <b>not</b> have labels in {@link UIHorizontalLayout},
 * {@link UIVerticalLayout}, {@link UIFormLayout} and {@link UICssLayout}
 */
abstract class TC001AbstractLayoutTest extends TS001AbstractBasicElementsLayoutTest {

    @Test
    void testLabel_HasNoLabel() {
        DivElement divElement = getTestCaseSection().$(DivElement.class).id("textLabel");

        assertThat(hasLabel(divElement)).isFalse();
    }

    @Test
    void testTextField_HasLabel() {
        TextFieldElement field = getTestCaseSection().$(TextFieldElement.class)
                .id(BasicElementsLayoutBehaviorModelObject.PROPERTY_TEXT);

        assertThat(field.getLabel()).isEqualTo("TextField");
    }

    @Test
    void testTextArea_HasLabel() {
        TextAreaElement field = getTestCaseSection().$(TextAreaElement.class)
                .id(BasicElementsLayoutBehaviorModelObject.PROPERTY_LONGTEXT);

        assertThat(field.getLabel()).isEqualTo("TextArea");
    }

    @Test
    void testIntegerField_HasLabel() {
        TextFieldElement field = getTestCaseSection().$(TextFieldElement.class)
                .id(BasicElementsLayoutBehaviorModelObject.PROPERTY_INTVALUE);

        assertThat(field.getLabel()).isEqualTo("IntegerField");
    }

    @Test
    void testDoubleField_HasLabel() {
        TextFieldElement field = getTestCaseSection().$(TextFieldElement.class)
                .id(BasicElementsLayoutBehaviorModelObject.PROPERTY_DOUBLEVALUE);

        assertThat(field.getLabel()).isEqualTo("DoubleField");
    }

    @Test
    void testDecimalField_HasLabel() {
        TextFieldElement field = getTestCaseSection().$(TextFieldElement.class)
                .id(BasicElementsLayoutBehaviorModelObject.PROPERTY_DECIMALVALUE);

        assertThat(field.getLabel()).isEqualTo("DecimalField");
    }

    @Test
    void testDateField_HasLabel() {
        DatePickerElement field = getTestCaseSection().$(DatePickerElement.class)
                .id(BasicElementsLayoutBehaviorModelObject.PROPERTY_DATE);

        assertThat(field.getLabel()).isEqualTo("DateField");
    }

    @Test
    void testCustomField_HasLabel() {
        PasswordFieldElement field = getTestCaseSection().$(PasswordFieldElement.class)
                .id(BasicElementsLayoutBehaviorModelObject.PROPERTY_SECRET);

        assertThat(field.getLabel()).isEqualTo("CustomField");
    }

    @Test
    void testComboBox_HasLabel() {
        ComboBoxElement field = getTestCaseSection().$(ComboBoxElement.class)
                .id("enumValueComboBox");

        assertThat(field.getLabel()).isEqualTo("ComboBox");
    }

    @Test
    void testRadioButton_HasLabel() {
        RadioButtonGroupElement field = getTestCaseSection().$(RadioButtonGroupElement.class)
                .id("enumValueRadioButton");

        JavascriptExecutor jsExecutor = (JavascriptExecutor)driver;
        WebElement shadowRoot = (WebElement)jsExecutor.executeScript("return arguments[0].shadowRoot",
                                                                     field);
        WebElement groupFieldContainer = shadowRoot.findElement(By.className("vaadin-group-field-container"));

        assertThat(groupFieldContainer.getText()).contains("RadioButtons");
    }

    @Test
    void testCheckBox_HasNoLabel() {
        CheckboxElement checkboxElement = getTestCaseSection().$(CheckboxElement.class)
                .id(BasicElementsLayoutBehaviorModelObject.PROPERTY_BOOLEANVALUE);

        assertThat(hasLabel(checkboxElement)).isFalse();
    }

    @Test
    void testLink_HasNoLabel() {
        AnchorElement anchorElement = getTestCaseSection().$(AnchorElement.class).id("link");

        assertThat(hasLabel(anchorElement)).isFalse();
    }

    @Test
    void testButton_HasNoLabel() {
        ButtonElement buttonElement = getTestCaseSection().$(ButtonElement.class).id("button");

        assertThat(hasLabel(buttonElement)).isFalse();
    }

    private boolean hasLabel(WebElement webElement) {
        return !webElement.findElements(By.className("has-label")).isEmpty();
    }

}
