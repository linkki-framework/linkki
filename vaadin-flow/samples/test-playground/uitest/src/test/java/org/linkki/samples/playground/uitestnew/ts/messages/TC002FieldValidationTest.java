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
 *
 */

package org.linkki.samples.playground.uitestnew.ts.messages;

import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_COMBO_BOX_VALUE;
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_ONLY_ERROR_TEXT_FIELD;
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_READ_ONLY_CHECKBOX;
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_READ_ONLY_COMBO_BOX;
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_READ_ONLY_DATE_TIME_FIELD;
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_READ_ONLY_TEXT_FIELD;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.datetimepicker.testbench.DateTimePickerElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;

/**
 * Test class for {@link TestScenarioView#TS013 TS013 Validation} / {@link TestScenarioView#TC003 TC003
 * Field validation}.
 */
class TC002FieldValidationTest extends PlaygroundUiTest {

    private static final Severity ERROR = Severity.ERROR;

    @BeforeEach
    void setup() {
        super.setUp();
        goToTestCase(TestScenarioView.TS013, TestScenarioView.TC002);
        selectMessageSeverity(ERROR);
    }

    @Test
    void testMessageBinding_ErrorSelected() {
        TextFieldElement textField = $(TextFieldElement.class).id(PROPERTY_ONLY_ERROR_TEXT_FIELD);

        verifyValidationErrorAttributes(textField, false);
        verifyValidationErrorMessage(textField);
    }

    @Test
    void testMessageBinding_NoErrorSelected() {
        selectMessageSeverity(Severity.INFO);
        TextFieldElement textField = $(TextFieldElement.class).id(PROPERTY_ONLY_ERROR_TEXT_FIELD);
        DivElement validationMessage = textField.$(DivElement.class)
                .attribute("slot", "error-message").first();

        assertThat(validationMessage.isDisplayed()).isFalse();
    }

    @Test
    void testReadOnlyValidation_TextField() {
        setReadOnly();
        TextFieldElement textField = $(TextFieldElement.class).id(PROPERTY_READ_ONLY_TEXT_FIELD);

        verifyValidationErrorAttributes(textField, true);
        verifyValidationErrorMessage(textField);
    }

    @Test
    void testReadOnlyValidation_ComboBox() {
        setReadOnly();
        ComboBoxElement comboBox = $(ComboBoxElement.class).id(PROPERTY_READ_ONLY_COMBO_BOX);

        verifyValidationErrorAttributes(comboBox, true);
        verifyValidationErrorMessage(comboBox);
    }

    @Test
    void testReadOnlyValidation_DateTimePicker() {
        setReadOnly();
        DateTimePickerElement dateTimePicker = $(DateTimePickerElement.class).id(PROPERTY_READ_ONLY_DATE_TIME_FIELD);

        verifyValidationErrorAttributes(dateTimePicker, true);
        verifyValidationErrorMessage(dateTimePicker);
    }

    /**
     * Selects the message severity of the combo-box with the id 'comboBoxValue'.
     *
     * @param severity The severity to be selected
     */
    private void selectMessageSeverity(Severity severity) {
        ComboBoxElement messageSeverity = $(ComboBoxElement.class).id(PROPERTY_COMBO_BOX_VALUE);
        messageSeverity.openPopup();
        messageSeverity.selectByText(severity.name());
    }

    private void setReadOnly() {
        CheckboxElement readOnly = $(CheckboxElement.class).id(PROPERTY_READ_ONLY_CHECKBOX);
        readOnly.setChecked(true);
    }

    /**
     * Verifies that the passed invalid element contains the 'invalid' attribute as well as the
     * 'readonly' attribute in case the element is read-only.
     * 
     * @param element The investigated UI element
     * @param isReadOnly {@code true} whether the element should be read-only
     */
    private void verifyValidationErrorAttributes(TestBenchElement element, boolean isReadOnly) {
        assertThat(element.hasAttribute("invalid")).isTrue();
        assertThat(isReadOnly == element.hasAttribute("readonly")).isTrue();
    }

    /**
     * Verifies that the passed invalid element displays an error message.
     * 
     * @param element The investigated UI element
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private void verifyValidationErrorMessage(TestBenchElement element) {
        // investigate all error-message slots since in case of combined input fields like date time
        // picker, there might be multiple hidden error-message slots
        Optional<DivElement> validationMessage = element.$(DivElement.class).attribute("slot", "error-message").all()
                .stream().filter(e -> !e.hasAttribute("hidden"))
                .findFirst();

        assertThat(validationMessage).isPresent()
                .satisfies(message -> assertThat(message.get().getText()).isEqualTo("Error validation message"));
    }
}
