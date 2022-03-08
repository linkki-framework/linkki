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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.dialogs.SetFormItemLabelWidthDialogPmo;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;
import org.linkki.testbench.pageobjects.OkCancelDialogElement;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.DivElement;

class SetFormItemLabelWidthTest extends PlaygroundUiTest {

    @BeforeEach
    void gotToTestCase() {
        goToTestCase(TestScenarioView.TS011, TestScenarioView.TC005);
    }

    @Test
    void testDialog_WithCustomLabelWidth() {
        LinkkiSectionElement section = getSection(SetFormItemLabelWidthDialogPmo.class);
        section.$(ButtonElement.class).id("showDialog").click();

        OkCancelDialogElement dialogElement = $(OkCancelDialogElement.class).waitForFirst();
        DivElement label = dialogElement
                .$(LinkkiSectionElement.class).first()
                .getContent()
                .$("vaadin-form-item").first()
                .$(DivElement.class).id("label");

        assertThat(label.getCssValue("width")).isEqualTo("240px");

        dialogElement.clickOnOk();
    }
}
