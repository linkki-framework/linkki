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
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.framework.ui.dialogs.OkCancelDialog;
import org.linkki.samples.playground.ts.dialogs.QuestionAndConfirmationDialogPmo;
import org.linkki.samples.playground.ui.PlaygroundApplicationView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;


class QuestionAndConfirmationDialogTest extends PlaygroundUiTest {

    @BeforeEach
    void gotToTestCase() {
        goToTestCase(PlaygroundApplicationView.TS011, PlaygroundApplicationView.TC002);
    }

    @AfterEach
    void closeNotifications() {
        waitUntil(d -> !$(NotificationElement.class).exists());
    }

    @Test
    void testQuestionDialogUsingHtml() {
        DialogElement dialog = openDialog(QuestionAndConfirmationDialogPmo.SHOW_QUESTION_BUTTON_ID);

        String text = dialog.$(VerticalLayoutElement.class).attribute("class", "content-area").first().getText();

        assertThat(text, equalTo("Is this one line\n"
                + "and this is another one?"));

        dialog.$(ButtonElement.class).id(OkCancelDialog.OK_BUTTON_ID).click();
    }

    @Test
    void testConfirmationDialogUsingHtml() {
        DialogElement dialog = openDialog(QuestionAndConfirmationDialogPmo.SHOW_CONFIRMATION_BUTTON_ID);

        String text = dialog.$(VerticalLayoutElement.class).attribute("class", "content-area").first().getText();

        assertThat(text, equalTo("Confirm that this is one line\n"
                + "and this is another one."));

        dialog.$(ButtonElement.class).id(OkCancelDialog.OK_BUTTON_ID).click();
    }

    private DialogElement openDialog(String buttonId) {
        $(ButtonElement.class).id(buttonId).click();
        return $(DialogElement.class).waitForFirst();
    }

}
