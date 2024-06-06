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
 *
 */

package org.linkki.samples.playground.uitestnew.ts.messages;

import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_COMBOBOX;
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_DATE_TIME_FIELD;
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_FIELDS_READ_ONLY;
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_MULTISELECT;
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_ONLY_ERRORS_FIELD;
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_SEVERITY;
import static org.linkki.samples.playground.ts.messages.FieldValidationPmo.PROPERTY_TEXT_FIELD;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.combobox.testbench.MultiSelectComboBoxElement;
import com.vaadin.flow.component.datepicker.testbench.DatePickerElement;
import com.vaadin.flow.component.datetimepicker.testbench.DateTimePickerElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.flow.component.timepicker.testbench.TimePickerElement;
import com.vaadin.testbench.TestBenchElement;

/**
 * Test class for {@link TestScenarioView#TS013 TS013 Validation} / {@link TestScenarioView#TC003
 * TC003 Field validation}.
 */
class TC002FieldValidationTest extends PlaygroundUiTest {

    private TextFieldElement textField;
    private ComboBoxElement combobox;

    private List<TestBenchElement> allElements;
    private List<TestBenchElement> allSingleElements;

    private TextFieldElement onlyErrorsTextField;

    @BeforeEach
    void setup() {
        goToTestCase(TestScenarioView.TS013, TestScenarioView.TC002);
        selectMessageSeverity(null);
        setReadOnly(false);

        textField = $(TextFieldElement.class).id(PROPERTY_TEXT_FIELD);
        textField.setValue(StringUtils.EMPTY);
        combobox = $(ComboBoxElement.class).id(PROPERTY_COMBOBOX);
        var multiselect = $(MultiSelectComboBoxElement.class).id(PROPERTY_MULTISELECT);

        // datetimePicker consists of two separate elements
        var dateTimePicker = $(DateTimePickerElement.class).id(PROPERTY_DATE_TIME_FIELD);
        var datePicker = dateTimePicker.$(DatePickerElement.class).first();
        var timePicker = dateTimePicker.$(TimePickerElement.class).first();

        allElements = List.of(textField, combobox, multiselect, dateTimePicker);
        allSingleElements = List.of(textField, combobox, multiselect, datePicker, timePicker);

        onlyErrorsTextField = $(TextFieldElement.class).id(PROPERTY_ONLY_ERRORS_FIELD);
    }

    @Test
    void testErrorColors() {
        selectMessageSeverity(Severity.ERROR);

        allSingleElements.forEach(e -> assertColors(e, this::assertErrorColors));
    }

    @Test
    void testWarningColors() {
        selectMessageSeverity(Severity.WARNING);

        allSingleElements.forEach(e -> assertColors(e, this::assertWarningColors));
    }

    @Test
    void testInfoColors() {
        selectMessageSeverity(Severity.INFO);

        allSingleElements.forEach(e -> assertColors(e, this::assertInfoColors));
    }

    @Test
    void testErrorColors_ReadOnly() {
        selectMessageSeverity(Severity.ERROR);
        setReadOnly(true);

        allSingleElements.forEach(e -> assertColors(e, this::assertReadOnlyErrorColors));
    }

    @Test
    void testWarningColors_ReadOnly() {
        selectMessageSeverity(Severity.WARNING);
        setReadOnly(true);

        allSingleElements.forEach(e -> assertColors(e, this::assertReadOnlyWarningColors));
    }

    @Test
    void testInfoColors_ReadOnly() {
        selectMessageSeverity(Severity.INFO);
        setReadOnly(true);

        allSingleElements.forEach(e -> assertColors(e, this::assertReadOnlyInfoColors));
    }

    @Test
    void testMessageBinding_ErrorMustBeVisibleAfterBlur() {
        textField.sendKeys("A too long text input for this component");
        combobox.focus();

        assertValidationErrorMessage(textField, "@UITextField: must be at most one character!");
    }

    @Test
    void testValidationMessages_Error() {
        selectMessageSeverity(Severity.ERROR);

        allElements.forEach(this::assertValidationErrorMessage);
        assertValidationErrorMessage(onlyErrorsTextField);
    }

    @Test
    void testValidationMessages_Warning() {
        selectMessageSeverity(Severity.WARNING);

        allElements.forEach(this::assertValidationWarningMessage);
        assertNotInvalid(onlyErrorsTextField);
    }

    @Test
    void testValidationMessages_Info() {
        selectMessageSeverity(Severity.INFO);

        allElements.forEach(this::assertValidationInfoMessage);
        assertNotInvalid(onlyErrorsTextField);
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

    private void assertColors(TestBenchElement element, Consumer<TestBenchElement> consumer) {
        assertColorsOfContainer(element, "vaadin-input-container", consumer);
        assertColorsOfContainer(element, "vaadin-multi-select-combo-box-container", consumer);
    }

    private void assertColorsOfContainer(TestBenchElement element,
            String inputContainer,
            Consumer<TestBenchElement> consumer) {
        element.$(inputContainer).all().forEach(consumer);
    }

    private void assertErrorColors(TestBenchElement container) {
        assertReadOnly(container, false);

        String bgColor = container.getCssValue("background-color");
        String borderWidth = container.getCssValue("border-width");

        assertThat(bgColor).isEqualTo("rgba(231, 29, 19, 0.1)");
        assertThat(borderWidth).isEqualTo("0px");
    }

    private void assertWarningColors(TestBenchElement container) {
        assertReadOnly(container, false);

        String bgColor = container.getCssValue("background-color");
        String borderWidth = container.getCssValue("border-width");

        assertThat(bgColor).isEqualTo("rgba(255, 204, 51, 0.3)");
        assertThat(borderWidth).isEqualTo("0px");
    }

    private void assertInfoColors(TestBenchElement container) {
        assertReadOnly(container, false);

        String bgColor = container.getCssValue("background-color");
        String borderWidth = container.getCssValue("border-width");

        assertThat(bgColor).isEqualTo("rgba(25, 115, 225, 0.1)");
        assertThat(borderWidth).isEqualTo("0px");
    }

    private void assertReadOnlyErrorColors(TestBenchElement container) {
        assertReadOnly(container, true);

        String bgColor = container.getCssValue("background-color");
        String borderColor = container.getCssValue("border-color");

        assertThat(bgColor).isEqualTo("rgba(0, 0, 0, 0)");
        assertThat(borderColor).isEqualTo("rgba(231, 29, 19, 0.5)");
    }

    private void assertReadOnlyWarningColors(TestBenchElement container) {
        assertReadOnly(container, true);

        String bgColor = container.getCssValue("background-color");
        String borderColor = container.getCssValue("border-color");

        assertThat(bgColor).isEqualTo("rgba(0, 0, 0, 0)");
        assertThat(borderColor).isEqualTo("rgba(255, 204, 51, 0.5)");
    }

    private void assertReadOnlyInfoColors(TestBenchElement container) {
        assertReadOnly(container, true);

        String bgColor = container.getCssValue("background-color");
        String borderColor = container.getCssValue("border-color");

        assertThat(bgColor).isEqualTo("rgba(0, 0, 0, 0)");
        assertThat(borderColor).isEqualTo("rgba(25, 115, 225, 0.5)");
    }

    private void assertReadOnly(TestBenchElement element, boolean isReadOnly) {
        assertThat(isReadOnly == element.hasAttribute("readonly")).isTrue();
    }

    private void assertValidationErrorMessage(TestBenchElement element) {
        assertValidationErrorMessage(element, "Generic error validation message on all fields");
    }

    private void assertValidationErrorMessage(TestBenchElement element, String message) {
        assertValidationMessage(element, message, "rgba(202, 21, 12, 1)");
    }

    private void assertValidationWarningMessage(TestBenchElement element) {
        assertValidationMessage(element, "Generic warning validation message on all fields", "rgba(189, 164, 0, 1)");
    }

    private void assertValidationInfoMessage(TestBenchElement element) {
        assertValidationMessage(element, "Generic info validation message on all fields", "rgba(26, 117, 230, 1)");
    }

    private void assertValidationMessage(TestBenchElement element, String messageText, String color) {
        assertThat(element.hasAttribute("invalid")).isTrue();
        // investigate all error-message slots since in case of combined input fields like date time
        // picker, there might be multiple hidden error-message slots
        Optional<DivElement> validationMessage =
                element.$(DivElement.class).withAttribute("slot", "error-message").all()
                        .stream().filter(e -> !e.hasAttribute("hidden"))
                        .findFirst();

        assertThat(validationMessage).hasValueSatisfying(m -> {
            assertThat(m.getText()).isEqualTo(messageText);
            assertThat(m.getCssValue("color")).isEqualTo(color);
        });
    }

    private void assertNotInvalid(TestBenchElement element) {
        assertThat(element.hasAttribute("invalid")).isFalse();
        var errorMessages = element.$(DivElement.class).withAttribute("slot", "error-message").first();
        assertThat(errorMessages.hasAttribute("hidden")).isTrue();
        assertThat(errorMessages.getText()).isEmpty();
    }
}
