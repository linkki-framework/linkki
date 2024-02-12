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

package org.linkki.samples.playground.uitestnew.ts.dialogs;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.OkCancelDialogElement;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;

class OkCancelDialogMessageTest extends PlaygroundUiTest {

    private OkCancelDialogElement dialog;

    @BeforeEach
    void goToTestCaseAndOpenDialog() {
        TestCaseComponentElement testCase = goToTestCaseByUrl(TestScenarioView.TS011, TestScenarioView.TC008);
        testCase.$(ButtonElement.class).id("button").click();
        dialog = $(OkCancelDialogElement.class).waitForFirst();
    }

    @Test
    void testShowMessageOnInfo() {
        $(ComboBoxElement.class).first().selectByText(Severity.INFO.name());

        assertThat(dialog.isMessageAreaPresent(Severity.INFO)).isTrue();
    }

    @Test
    void testShowMessageOnWarning() {
        $(ComboBoxElement.class).first().selectByText(Severity.WARNING.name());

        assertThat(dialog.isMessageAreaPresent(Severity.WARNING)).isTrue();
    }

    @Test
    void testShowMessageOnError() {
        $(ComboBoxElement.class).first().selectByText(Severity.ERROR.name());

        assertThat(dialog.isMessageAreaPresent(Severity.ERROR)).isTrue();
    }

    @Test
    void testHideIndicatorIfMessageResolves() {
        $(ComboBoxElement.class).first().selectByText(Severity.ERROR.name());
        assertThat(dialog.isMessageAreaPresent(Severity.ERROR)).isTrue();

        $(ComboBoxElement.class).first().clear();

        assertThat(dialog.isMessageAreaPresent()).isFalse();
    }

    @Test
    void testLinebreakOnLongMessage() {
        $(ComboBoxElement.class).first().selectByText(Severity.INFO.name());
        int heightForShortMessage = dialog.getMessageArea().getSize().getHeight();

        $(CheckboxElement.class).id("showLongValidationMessage").setChecked(true);

        assertThat(dialog.getMessageArea().getSize().getHeight())
                .isGreaterThan(heightForShortMessage);
    }
}
