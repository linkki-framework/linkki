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
import static org.linkki.samples.playground.ts.messages.RequiredValidationPmo.WITHOUT_BINDING_MANAGER;
import static org.linkki.samples.playground.ts.messages.RequiredValidationPmo.WITH_BINDING_MANAGER;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.Keys;

import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;

/**
 * Test class for {@link TestScenarioView#TS013 Validation} / {@link TestScenarioView#TC004 TC004
 * Converter Error}.
 */
class TC005RequiredValidationTest extends PlaygroundUiTest {

    @BeforeEach
    void setup() {
        super.setUp();
        goToTestCase(TestScenarioView.TS013, TestScenarioView.TC005);
    }

    @Test
    void testRequiredIndicatorOnComponentWithoutBindingManagerMustDisappear() {
        var element = getTextFieldElementByLabel(WITHOUT_BINDING_MANAGER);
        verifyNoErrorMessage(element);
        element.setValue("");
        element.sendKeys(Keys.TAB);
        assertThat(element.hasAttribute("invalid")).isTrue();
        element.setValue("New input");
        assertThat(element.hasAttribute("invalid")).isFalse();
    }

    @Test
    void testRequiredIndicatorOnComponentWithBindingManagerMustDisappear() {
        var element = getTextFieldElementByLabel(WITH_BINDING_MANAGER);
        verifyNoErrorMessage(element);

        element.setValue("");
        element.sendKeys(Keys.TAB);

        assertThat(element.hasAttribute("invalid")).isTrue();
        verifyValidationErrorMessage(element, "Input is required");

        element.setValue("New input");
        verifyNoErrorMessage(element);
    }

    private TextFieldElement getTextFieldElementByLabel(String label) {
        return $(TextFieldElement.class).withAttribute("id", "textField")
                .all()
                .stream()
                .filter(tf -> label.equals(tf.getLabel()))
                .findFirst()
                .orElseThrow();
    }

    private void verifyNoErrorMessage(TestBenchElement element) {
        assertThat(element.hasAttribute("invalid")).isFalse();
        DivElement validationMessage = element.$(DivElement.class)
                .withAttribute("slot", "error-message").first();

        assertThat(validationMessage.isDisplayed()).isFalse();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private void verifyValidationErrorMessage(TestBenchElement element, String messageText) {
        assertThat(element.hasAttribute("invalid")).isTrue();
        // investigate all error-message slots since in case of combined input fields like date time
        // picker, there might be multiple hidden error-message slots
        Optional<DivElement> validationMessage =
                element.$(DivElement.class).withAttribute("slot", "error-message").all()
                .stream().filter(e -> !e.hasAttribute("hidden"))
                .findFirst();

        assertThat(validationMessage).isPresent()
                .satisfies(message -> assertThat(message.get().getText()).isEqualTo(messageText));
    }
}
