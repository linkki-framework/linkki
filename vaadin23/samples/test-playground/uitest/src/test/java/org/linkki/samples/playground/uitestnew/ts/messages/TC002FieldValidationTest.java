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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

/**
 * Test class for {@link TestScenarioView#TS013 TS013 Validation} / {@link TestScenarioView#TC003 TC003
 * Field validation}.
 */
class TC002FieldValidationTest extends PlaygroundUiTest {

    private static final String ERRORS_ONLY_TEXT_FIELD = "allErrorTextField";

    @BeforeEach
    void setup() {
        super.setUp();
        goToTestCase(TestScenarioView.TS013, TestScenarioView.TC002);
    }

    @Test
    void testMessageBinding_ErrorSelected() {
        selectMessageSeverity(Severity.ERROR);
        TextFieldElement textField = $(TextFieldElement.class).id(ERRORS_ONLY_TEXT_FIELD);
        DivElement validationMessage = textField.$(DivElement.class)
                .attribute("slot", "error-message").first();

        assertThat(validationMessage).isNotNull();
        assertThat(validationMessage.isDisplayed()).isTrue();
        assertThat(validationMessage.getText()).isEqualTo("Error validation message");
    }

    @Test
    void testMessageBinding_NoErrorSelected() {
        selectMessageSeverity(Severity.INFO);
        TextFieldElement textField = $(TextFieldElement.class).id(ERRORS_ONLY_TEXT_FIELD);
        DivElement validationMessage = textField.$(DivElement.class)
                .attribute("slot", "error-message").first();

        assertThat(validationMessage.isDisplayed()).isFalse();
    }

    /**
     * Selects the message severity of the combo-box with the id 'comboBoxValue'.
     *
     * @param severity The severity to be selected
     */
    private void selectMessageSeverity(Severity severity) {
        ComboBoxElement messageSeverity = $(ComboBoxElement.class).id("comboBoxValue");
        messageSeverity.openPopup();
        messageSeverity.selectByText(severity.name());
    }
}
