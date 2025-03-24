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
import static org.linkki.samples.playground.ts.messages.RequiredValidationPmo.WITH_BINDING_MANAGER;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.Keys;

import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;

class TC005RequiredValidationTest extends PlaygroundUiTest {

    @BeforeEach
    void setup() {
        goToTestCase(TestScenarioView.TS013, TestScenarioView.TC005);
    }

    @Test
    void testRequiredIndicator_WithBindingManager_DisplaysValidationResult() {
        var element = $(TestBenchElement.class).id(WITH_BINDING_MANAGER).$(TextFieldElement.class).first();
        assertThat(element.hasAttribute("invalid")).isFalse();
        assertThat(getErrorMessageComponent(element).isDisplayed()).isFalse();

        element.setValue("");
        element.sendKeys(Keys.TAB);

        assertThat(element.hasAttribute("invalid")).isTrue();
        assertThat(getErrorMessageComponent(element).isDisplayed()).isTrue();
        assertThat(getErrorMessageComponent(element).getText()).isEqualTo("Input is required");

        element.setValue("New input");

        assertThat(element.hasAttribute("invalid")).isFalse();
        assertThat(getErrorMessageComponent(element).isDisplayed()).isFalse();
    }

    private TestBenchElement getErrorMessageComponent(TestBenchElement element) {
        return element.$(DivElement.class)
                .withAttribute("slot", "error-message").first();
    }
}
