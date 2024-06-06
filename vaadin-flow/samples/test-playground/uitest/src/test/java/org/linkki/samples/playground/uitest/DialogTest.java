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

package org.linkki.samples.playground.uitest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.framework.ui.dialogs.OkCancelDialog;
import org.linkki.samples.playground.dialogs.DialogsView;
import org.linkki.samples.playground.dialogs.SimpleDialogPmo;
import org.linkki.samples.playground.dialogs.VerticalLayoutContentDialog.VerticalLayoutContentDialogPmo;
import org.linkki.testbench.conditions.VaadinElementConditions;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;
import org.linkki.testbench.pageobjects.OkCancelDialogElement;
import org.linkki.testbench.util.DriverProperties;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

class DialogTest extends AbstractLinkkiUiTest {

    private static final String OK_BUTTON = "okButton";

    @BeforeEach
    void goToDialogsView() {
        goToView(DialogsView.NAME);
    }

    @Test
    void testDialogOnEntry() {
        getDriver().get(DriverProperties.getTestUrl(DEFAULT_CONTEXT_PATH, ""));
        clickMenuItemById("dialogs");
        DialogElement dialog = findDialog("Entering dialog view");

        assertThat($(DialogElement.class).all().size(), is(1));

        // close dialog
        confirmDialog(dialog);
    }

    @Test
    void testDialog_ClosedOnOk() {
        closeInitialDialog();
        clickButton("showDialog");

        DialogElement dialog = findDialog("Sample Dialog");

        assertThat($(DialogElement.class).all().size(), is(1));

        confirmDialog(dialog);
    }

    // TODO LIN-2226: test DialogErrorHandler

    @Test
    void testOkCancelDialog() {
        closeInitialDialog();
        LinkkiSectionElement section = getSection(SimpleDialogPmo.class);

        section.$(TextFieldElement.class).id("caption").setValue("Awesome dialog");
        section.$(TextFieldElement.class).id("content").setValue("This is awesome!");
        section.$(TextFieldElement.class).id("okCaption").setValue("Hell yeah");
        section.$(TextFieldElement.class).id("cancelCaption").setValue("Not really");
        section.$(ButtonElement.class).id("showDialog").click();

        DialogElement dialog = findDialog("Awesome dialog");

        assertThat(dialog.$(VerticalLayoutElement.class).withAttribute("class", OkCancelDialog.CLASS_NAME_CONTENT_AREA)
                .first().getText(),
                   is("This is awesome!"));
        assertThat(dialog.$(ButtonElement.class).id(OK_BUTTON).getText(), is("Hell yeah"));
        assertThat(dialog.$(ButtonElement.class).id("cancelButton").getText(), is("Not really"));
        // close dialog
        confirmDialog(dialog);
    }

    @Test
    void testDialogWithCloseHandler_CloseOnOk_WithDoubleClick() {
        closeInitialDialog();
        clickButton("okHandlerDialog");
        DialogElement dialog = findDialog("Dialog with okHandler");

        assertThat($(DialogElement.class).all().size(), is(1));

        dialog.$(ButtonElement.class).id(OK_BUTTON).doubleClick();
        waitUntil(VaadinElementConditions.isClosed(dialog));

        assertThat($(NotificationElement.class).all().size(), is(1));
    }

    @Test
    void testDialogWithCloseHandler_CloseOnEnter_WithDoubleEnter() {
        closeInitialDialog();
        clickButton("okHandlerDialog");
        DialogElement dialog = findDialog("Dialog with okHandler");

        assertThat($(DialogElement.class).all().size(), is(1));

        sendKeys(Keys.ENTER, Keys.ENTER);
        waitUntil(VaadinElementConditions.isClosed(dialog));

        assertThat($(NotificationElement.class).all().size(), is(1));
    }

    @Test
    void testDialogWithCloseHandler_CloseOnEnter_WithFocusOnOkButton() {
        closeInitialDialog();

        clickButton("okHandlerDialog");
        DialogElement dialog = findDialog("Dialog with okHandler");

        assertThat($(DialogElement.class).all().size(), is(1));

        dialog.$(ButtonElement.class).id(OK_BUTTON).focus();
        sendKeys(Keys.ENTER);
        waitUntil(VaadinElementConditions.isClosed(dialog));

        assertThat($(NotificationElement.class).all().size(), is(1));

        waitForNotificationsClosed();
    }

    @Test
    void testDialog_CloseOnViewChange() {
        closeInitialDialog();
        LinkkiSectionElement section = getSection(SimpleDialogPmo.class);
        section.$(ButtonElement.class).id("showDialog").click();
        DialogElement dialog = findDialog("Sample Dialog");

        getDriver().navigate().back();

        // dialog should close on its own
        waitUntil(VaadinElementConditions.isClosed(dialog));
    }

    @Test
    void testDialog_WithVerticalLayout() {
        closeInitialDialog();
        getSection(VerticalLayoutContentDialogPmo.class).$(ButtonElement.class).id("button").click();
        DialogElement dialog = findDialog("Dialog containing @UIVerticalLayout");

        confirmDialog(dialog);
    }

    private void closeInitialDialog() {
        OkCancelDialogElement okCancelDialogElement = $(OkCancelDialogElement.class).waitForFirst();
        confirmDialog(okCancelDialogElement);
        assertThat($(NotificationElement.class).all().size(), is(0));
        assertThat($(DialogElement.class).all().size(), is(0));
    }

    private void waitForNotificationsClosed() {
        waitUntil(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return $(NotificationElement.class).all().isEmpty();
            }
        });
    }

    private void sendKeys(CharSequence... keys) {
        Actions actionProvider = new Actions(getDriver());
        Action keydown = actionProvider.sendKeys(keys).build();
        keydown.perform();
    }

}
