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

package org.linkki.samples.playground.uitestnew.ts.components;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.StaleElementReferenceException;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

class DynamicComponentTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS005, TestScenarioView.TC012);
    }

    @Test
    void testDynamicComponent_SelectBaaAndSwitchToTextField_TextMustBeBaa() {
        ComboBoxElement dynamicCombobox = $(ComboBoxElement.class).id("value");
        assertThat(dynamicCombobox).isNotNull();
        assertThat(dynamicCombobox.getSelectedText()).isEqualTo("foo");
        dynamicCombobox.selectByText("baa");

        ComboBoxElement typeSelector = $(ComboBoxElement.class).id("type");

        try {
            typeSelector.selectByText("UITextField");
        } catch (StaleElementReferenceException e) {
            // ignore because we know that the element is not on the page anymore after the selection
            // was changed
        }

        TextFieldElement dynamicTextField = $(TextFieldElement.class).id("value");

        assertThat(dynamicTextField).isNotNull();
        assertThat(dynamicTextField.getValue()).isEqualTo("baa");
    }

}
