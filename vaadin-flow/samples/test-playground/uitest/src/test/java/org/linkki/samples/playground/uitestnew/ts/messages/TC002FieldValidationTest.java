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
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_ALL_ERRORS_TEXT_FIELD;
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_COMBOBOX;
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_DATE_TIME_FIELD;
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_FIELDS_READ_ONLY;
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_SEVERITY;
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_TEXT_FIELD;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.Key;
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

    @BeforeEach
    void setup() {
        super.setUp();
        goToTestCase(TestScenarioView.TS013, TestScenarioView.TC002);
        selectMessageSeverity(null);
        setReadOnly(false);
    }

    @Test
    void testMessageBinding_ErrorMustBeVisibleAfterBlur() {
        selectMessageSeverity(Severity.ERROR);
        TextFieldElement textField = $(TextFieldElement.class).id(PROPERTY_ALL_ERRORS_TEXT_FIELD);

        verifyReadOnly(textField, false);
        verifyValidationErrorMessage(textField);

        textField.sendKeys(Key.TAB.getKeys().get(0));

        verifyReadOnly(textField, false);
        verifyValidationErrorMessage(textField);
    }

    @Test
    void testMessageBinding_NoErrorSelected() {
        selectMessageSeverity(Severity.INFO);

        TextFieldElement textField = $(TextFieldElement.class).id(PROPERTY_ALL_ERRORS_TEXT_FIELD);

        verifyNoErrorMessage(textField);
    }

    @Test
    void testReadOnlyValidation_TextField() {
        selectMessageSeverity(Severity.ERROR);
        setReadOnly(true);
        TextFieldElement textField = $(TextFieldElement.class).id(PROPERTY_TEXT_FIELD);

        verifyReadOnly(textField, true);
        verifyValidationErrorMessage(textField);
    }

    @Test
    void testReadOnlyValidation_ComboBox() {
        selectMessageSeverity(Severity.ERROR);
        setReadOnly(true);
        ComboBoxElement comboBox = $(ComboBoxElement.class).id(PROPERTY_COMBOBOX);

        verifyReadOnly(comboBox, true);
        verifyValidationErrorMessage(comboBox);
    }

    @Test
    void testReadOnlyValidation_DateTimePicker() {
        selectMessageSeverity(Severity.ERROR);
        setReadOnly(true);
        DateTimePickerElement dateTimePicker = $(DateTimePickerElement.class).id(PROPERTY_DATE_TIME_FIELD);

        verifyReadOnly(dateTimePicker, true);
        verifyValidationErrorMessage(dateTimePicker);
    }

    private void selectMessageSeverity(Severity severity) {
        ComboBoxElement messageSeverity = $(ComboBoxElement.class).id(PROPERTY_SEVERITY);
        if (severity == null) {
            messageSeverity.clear();
        } else {
            messageSeverity.selectByText(severity.name());
        }
    }

    private void setReadOnly(boolean readOnlyState) {
        CheckboxElement readOnly = $(CheckboxElement.class).id(PROPERTY_FIELDS_READ_ONLY);
        readOnly.setChecked(readOnlyState);
    }

    /**
     * Verifies that the passed invalid element contains the 'invalid' attribute as well as the
     * 'readonly' attribute in case the element is read-only.
     * 
     * @param element The investigated UI element
     * @param isReadOnly {@code true} whether the element should be read-only
     */
    private void verifyReadOnly(TestBenchElement element, boolean isReadOnly) {
        assertThat(isReadOnly == element.hasAttribute("readonly")).isTrue();
    }

    private void verifyNoErrorMessage(TestBenchElement element) {
        assertThat(element.hasAttribute("invalid")).isFalse();
        DivElement validationMessage = element.$(DivElement.class)
                .attribute("slot", "error-message").first();

        assertThat(validationMessage.isDisplayed()).isFalse();
    }

    /**
     * Verifies that the passed invalid element displays an error message.
     * 
     * @param element The investigated UI element
     * @param messageText
     */
    private void verifyValidationErrorMessage(TestBenchElement element) {
        verifyValidationErrorMessage(element, "Error validation message");
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private void verifyValidationErrorMessage(TestBenchElement element, String messageText) {
        assertThat(element.hasAttribute("invalid")).isTrue();
        // investigate all error-message slots since in case of combined input fields like date time
        // picker, there might be multiple hidden error-message slots
        Optional<DivElement> validationMessage = element.$(DivElement.class).attribute("slot", "error-message").all()
                .stream().filter(e -> !e.hasAttribute("hidden"))
                .findFirst();

        assertThat(validationMessage).isPresent()
                .satisfies(message -> assertThat(message.get().getText()).isEqualTo(messageText));
    }
}

