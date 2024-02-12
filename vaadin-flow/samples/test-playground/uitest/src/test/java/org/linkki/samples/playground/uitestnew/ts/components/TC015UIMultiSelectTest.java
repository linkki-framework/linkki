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
 */

package org.linkki.samples.playground.uitestnew.ts.components;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.combobox.testbench.MultiSelectComboBoxElement;

class TC015UIMultiSelectTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS005, TestScenarioView.TC015);
    }

    @Test
    void testDaySelection_OneElement() {
        MultiSelectComboBoxElement daysSelector = $(MultiSelectComboBoxElement.class).id("days");
        MultiSelectComboBoxElement selectedDays = $(MultiSelectComboBoxElement.class).id("selectedDays");

        daysSelector.selectByText("WEDNESDAY");

        assertThat(selectedDays.getSelectedTexts()).contains("WEDNESDAY");
    }

    @Test
    void testDaySelection_MultipleElements() {
        MultiSelectComboBoxElement daysSelector = $(MultiSelectComboBoxElement.class).id("days");
        MultiSelectComboBoxElement selectedDays = $(MultiSelectComboBoxElement.class).id("selectedDays");

        daysSelector.selectByText("MONDAY");
        daysSelector.selectByText("WEDNESDAY");
        daysSelector.selectByText("FRIDAY");

        assertThat(selectedDays.getSelectedTexts()).contains("MONDAY", "FRIDAY", "WEDNESDAY");
    }

    @Test
    void testDaySelection_DeselectElements() {
        MultiSelectComboBoxElement daysSelector = $(MultiSelectComboBoxElement.class).id("days");
        MultiSelectComboBoxElement selectedDays = $(MultiSelectComboBoxElement.class).id("selectedDays");

        daysSelector.selectByText("MONDAY");
        daysSelector.selectByText("WEDNESDAY");
        daysSelector.selectByText("FRIDAY");

        assertThat(selectedDays.getSelectedTexts()).contains("MONDAY", "FRIDAY", "WEDNESDAY");

        daysSelector.deselectByText("WEDNESDAY");
        daysSelector.deselectByText("MONDAY");

        assertThat(selectedDays.getSelectedTexts()).contains("FRIDAY");
    }

}
