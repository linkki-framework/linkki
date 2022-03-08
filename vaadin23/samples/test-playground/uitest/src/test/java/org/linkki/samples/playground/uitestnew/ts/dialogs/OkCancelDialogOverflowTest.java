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
import org.linkki.samples.playground.ts.dialogs.OkCancelDialogOverflowPmo;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.OkCancelDialogElement;

import com.vaadin.flow.component.button.testbench.ButtonElement;


class OkCancelDialogOverflowTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS011, TestScenarioView.TC003);
    }

    @Test
    void testOverflow() {
        $(ButtonElement.class).id(OkCancelDialogOverflowPmo.SHOW_DIALOG_BUTTON_ID).click();
        OkCancelDialogElement dialogElement = $(OkCancelDialogElement.class).waitForFirst();

        // Content area has height 100%. If it overflows correctly, it should have a significantly lower
        // height as the caption and buttons take up some space. Thus, content area having 100% height
        // is a indicator that it does not overflow correctly.
        assertThat(dialogElement.getContentArea().getSize().getHeight())
                .isLessThan(dialogElement.getDialogLayout().getSize().getHeight());
    }
}
