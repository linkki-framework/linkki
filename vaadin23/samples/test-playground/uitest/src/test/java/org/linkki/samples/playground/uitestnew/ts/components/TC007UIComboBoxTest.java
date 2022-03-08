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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;

class TC007UIComboBoxTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS005, TestScenarioView.TC007);
    }

    @Test
    void testComboBoxWithNullHasClearButton() {
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id("directionWithNull");

        assertThat(hasClearButton(comboBoxElement), is(true));
    }

    @Test
    void testComboBoxWithoutNullDoesNotHaveClearButton() {
        ComboBoxElement comboBoxElement = $(ComboBoxElement.class).id("directionWithoutNull");

        assertThat(hasClearButton(comboBoxElement), is(false));
    }

    private boolean hasClearButton(ComboBoxElement element) {
        return element.hasAttribute("clear-button-visible");
    }
}
