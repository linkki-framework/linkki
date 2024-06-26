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

package org.linkki.samples.playground.uitestnew.ts.ips;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;
import org.linkki.testbench.pageobjects.OkCancelDialogElement;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class TC005IpsPropertyDispatcherEnabledTest extends PlaygroundUiTest {

    private TestCaseComponentElement testCaseSection;
    private LinkkiSectionElement section;

    @BeforeEach
    void setup() {
        testCaseSection = goToTestCase(TestScenarioView.TS004, TestScenarioView.TC005);
        section = testCaseSection.getContentWrapper().$(LinkkiSectionElement.class).first();
    }

    @Test
    void testEnabled_NotEmptyValueSet_ShouldBeEnabled() {
        TextFieldElement textField = section.$(TextFieldElement.class).id("notEmptyValueSet");

        assertThat(textField.isEnabled(), is(true));
    }

    @Test
    void testEnabled_EmptyValueSet_ShouldBeEnabled() {
        ComboBoxElement comboBox = section.$(ComboBoxElement.class).id("emptyValueSet");

        assertThat(comboBox.isEnabled(), is(false));
    }

    @Test
    void testEnabled_DynamicEnabledEmptyValueSet_ShouldBeVisibleButDisabled() {
        ComboBoxElement combobox = section.$(ComboBoxElement.class).id("dynamicEnabledEmptyValueSet");

        assertThat(combobox.isEnabled(), is(true));
    }

    @Test
    void testDialog_IpsDispatcher() {
        $(ButtonElement.class).id("showDialogWithBindingManager").click();
        OkCancelDialogElement dialog = $(OkCancelDialogElement.class).waitForFirst();

        assertTrue(dialog.isOpen());

        assertThat(section.$(TextFieldElement.class).id("notEmptyValueSet").isEnabled(), is(true));
        assertThat(section.$(ComboBoxElement.class).id("emptyValueSet").isEnabled(), is(false));
        assertThat(section.$(ComboBoxElement.class).id("dynamicEnabledEmptyValueSet").isEnabled(), is(true));

        dialog.clickOnCancel();
        assertFalse(dialog.isOpen());
    }
}
