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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.framework.ui.dialogs.OkCancelDialog;
import org.linkki.samples.playground.ts.dialogs.OkCancelDialogHandlerPmo;
import org.linkki.samples.playground.ui.PlaygroundApplicationView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;


public class OkCancelDialogHandlerTest extends PlaygroundUiTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        goToTestCase(PlaygroundApplicationView.TS011, PlaygroundApplicationView.TC001);
    }

    @AfterEach
    void closeNotifications() {
        waitUntil(d -> !$(NotificationElement.class).exists());
    }

    @Test
    public void testOkHandler() {
        DialogElement dialog = openOkCancelDialog();

        dialog.$(ButtonElement.class).id(OkCancelDialog.OK_BUTTON_ID).click();

        assertHandlerCalled(OkCancelDialogHandlerPmo.MESSAGE_OK);
    }

    @Test
    public void testCancelHandler() {
        DialogElement dialog = openOkCancelDialog();

        dialog.$(ButtonElement.class).id(OkCancelDialog.CANCEL_BUTTON_ID).click();

        assertHandlerCalled(OkCancelDialogHandlerPmo.MESSAGE_CANCEL);
    }

    @Test
    public void testEscape() {
        openOkCancelDialog();

        // selenium doesn't like it when elements disappear when sending keys
        // so we cannot send it to the dialog
        findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);

        assertHandlerCalled(OkCancelDialogHandlerPmo.MESSAGE_CANCEL);
    }

    @Test
    public void testClickOutside() {
        openOkCancelDialog();

        findElement(By.tagName("body")).click();

        assertHandlerCalled(OkCancelDialogHandlerPmo.MESSAGE_CANCEL);
    }

    private DialogElement openOkCancelDialog() {
        $(ButtonElement.class).id(OkCancelDialogHandlerPmo.SHOW_DIALOG_BUTTON_ID).click();
        return $(DialogElement.class).waitForFirst();
    }

    private void assertHandlerCalled(String handler) {
        // check that handler is not triggered multiple times
        assertThrows(TimeoutException.class, () -> {
            waitUntil(d -> $(NotificationElement.class).all().size() > 1, 1);
        });

        NotificationElement notification = $(NotificationElement.class).first();
        assertThat(notification.getText(), is(handler));
    }

}
