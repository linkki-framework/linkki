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

package org.linkki.samples.playground.uitestnew.ts.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.aspects.BindAutoFocusPmo;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

class BindAutoFocusUiTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCaseByUrl(TestScenarioView.TS008, TestScenarioView.TC013);
    }

    @Test
    void testBindAutoFocus() {
        var section = getSection(BindAutoFocusPmo.class);

        var notAutoFocusedField = section.$(TextFieldElement.class).id("notAutoFocusedTextField");
        assertThat(notAutoFocusedField.hasAttribute("autofocus"), is(false));
        assertThat(notAutoFocusedField.hasAttribute("focused"), is(false));
        assertThat(notAutoFocusedField.hasAttribute("focus-ring"), is(false));

        var autoFocusedField = section.$(TextFieldElement.class).id("autoFocusedTextField");
        assertThat(autoFocusedField.hasAttribute("autofocus"), is(true));
        assertThat(autoFocusedField.hasAttribute("focused"), is(true));
        assertThat(autoFocusedField.hasAttribute("focus-ring"), is(true));
    }
}
