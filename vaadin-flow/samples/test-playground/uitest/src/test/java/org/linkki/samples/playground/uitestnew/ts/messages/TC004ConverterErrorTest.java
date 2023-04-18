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
class TC004ConverterErrorTest extends PlaygroundUiTest {

    @BeforeEach
    void setup() {
        super.setUp();
        goToTestCase(TestScenarioView.TS013, TestScenarioView.TC004);
    }

    @Test
    void testInvalidInputInIntegerField_OriginalValueMustRestore() {
        var intField = $(TextFieldElement.class).id("integerField");
        intField.setValue("43");
        verifyNoErrorMessage(intField);
        intField.doubleClick();
        intField.sendKeys(Keys.DELETE, Keys.TAB);

        verifyValidationErrorMessagePresent(intField);

        assertThat(intField.getValue()).isEqualTo("43");
    }

    private void verifyNoErrorMessage(TestBenchElement element) {
        assertThat(element.hasAttribute("invalid")).isFalse();
        DivElement validationMessage = element.$(DivElement.class)
                .attribute("slot", "error-message").first();

        assertThat(validationMessage.isDisplayed()).isFalse();
    }


    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private void verifyValidationErrorMessagePresent(TestBenchElement element) {
        assertThat(element.hasAttribute("invalid")).isTrue();
        // investigate all error-message slots since in case of combined input fields like date time
        // picker, there might be multiple hidden error-message slots
        Optional<DivElement> validationMessage = element.$(DivElement.class).attribute("slot", "error-message").all()
                .stream().filter(e -> !e.hasAttribute("hidden"))
                .findFirst();

        assertThat(validationMessage).isPresent();
    }
}

