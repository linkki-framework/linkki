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

package org.linkki.samples.playground.uitestnew.ts.dialogs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.dialogs.DialogWithCustomSizePmo;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;
import org.linkki.testbench.pageobjects.OkCancelDialogElement;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

class OkCancelDialogSizeTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS011, TestScenarioView.TC006);
    }

    @Test
    void testDialog_CustomSize() {
        LinkkiSectionElement section = getSection(DialogWithCustomSizePmo.class);
        section.$(TextFieldElement.class).id("width").setValue("600px");
        section.$(TextFieldElement.class).id("height").setValue("600px");
        section.$(ButtonElement.class).id("showDialog").click();

        OkCancelDialogElement dialog = $(OkCancelDialogElement.class).waitForFirst();
        var dialogWidth = dialog.getSize().getWidth();
        var dialogHeight = dialog.getSize().getHeight();

        assertThat(dialogWidth, is(600));
        assertThat(dialogHeight, is(600));
    }

}
