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

package org.linkki.samples.playground.uitest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfElementsToBe;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.framework.ui.dialogs.OkCancelDialog;
import org.linkki.samples.playground.dialogs.DialogsView;
import org.linkki.samples.playground.dialogs.SimpleDialogPmo;
import org.linkki.samples.playground.dialogs.VerticalLayoutContentDialog.VerticalLayoutContentDialogPmo;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;
import org.linkki.testbench.pageobjects.OkCancelDialogElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.html.testbench.H4Element;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

class DialogTest extends AbstractUiTest {

    private static final String OVERLAY = "overlay";
    private static final String OK_BUTTON = "okButton";

    @BeforeEach
    void goToDialogsView() {
        goToView(DialogsView.NAME);
    }

    @Test
    void testDialogOnEntry() {
        getDriver().get(DriverProperties.getTestUrl(""));
        clickMenuItemById("dialogs");
        waitUntil(ExpectedConditions.visibilityOfElementLocated(By.id(OVERLAY)));

        assertThat($(DialogElement.class).all().size(), is(1));

        // close dialog
        clickButton(OK_BUTTON);
        waitUntil(invisibilityOfElementLocated(By.id(OVERLAY)));
    }

    @Test
    void testDialog_ClosedOnOk() {
        closeInitialDialog();
        clickButton("showDialog");

        waitUntil(visibilityOfElementLocated(By.id(OVERLAY)));
        assertThat($(DialogElement.class).all().size(), is(1));

        $(ButtonElement.class).id(OK_BUTTON).click();

        waitForDialogToBeClosed();
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

        waitUntil(ExpectedConditions.visibilityOfElementLocated(By.id(OVERLAY)));

        DialogElement dialog = $(DialogElement.class).first();
        assertThat(dialog.$(H4Element.class).first().getText(), is("Awesome dialog"));
        assertThat(dialog.$(VerticalLayoutElement.class).attribute("class", OkCancelDialog.CLASS_NAME_CONTENT_AREA)
                .first().getText(),
                   is("This is awesome!"));
        assertThat(dialog.$(ButtonElement.class).id(OK_BUTTON).getText(), is("Hell yeah"));
        assertThat(dialog.$(ButtonElement.class).id("cancelButton").getText(), is("Not really"));
        // close dialog
        $(DialogElement.class).first().$(ButtonElement.class).first().click();
    }

    @Test
    void testDialogWithCloseHandler_CloseOnOk_WithDoubleClick() {
        closeInitialDialog();
        clickButton("okHandlerDialog");
        waitUntil(visibilityOfElementLocated(By.id(OVERLAY)));
        assertThat($(DialogElement.class).all().size(), is(1));

        $(ButtonElement.class).id(OK_BUTTON).doubleClick();
        waitForDialogToBeClosed();

        assertThat($(NotificationElement.class).all().size(), is(1));
    }

    @Test
    void testDialogWithCloseHandler_CloseOnEnter_WithDoubleEnter() {
        closeInitialDialog();
        clickButton("okHandlerDialog");
        waitUntil(visibilityOfElementLocated(By.id(OVERLAY)));
        assertThat($(DialogElement.class).all().size(), is(1));

        sendKeys(Keys.ENTER, Keys.ENTER);
        waitForDialogToBeClosed();

        assertThat($(NotificationElement.class).all().size(), is(1));
    }

    @Test
    void testDialogWithCloseHandler_CloseOnEnter_WithFocusOnOkButton() {
        closeInitialDialog();

        clickButton("okHandlerDialog");
        waitUntil(visibilityOfElementLocated(By.id(OVERLAY)));
        assertThat($(DialogElement.class).all().size(), is(1));

        $(ButtonElement.class).id(OK_BUTTON).focus();
        sendKeys(Keys.ENTER);
        waitForDialogToBeClosed();

        assertThat($(NotificationElement.class).all().size(), is(1));

        waitForNotificationsClosed();
    }

    @Test
    void testDialog_CloseOnViewChange() {
        closeInitialDialog();
        LinkkiSectionElement section = getSection(SimpleDialogPmo.class);
        section.$(ButtonElement.class).id("showDialog").click();
        waitUntil(d -> $(DialogElement.class).exists());

        driver.navigate().back();

        // dialog should close on its own
        waitUntil(d -> !$(DialogElement.class).exists());
    }

    @Test
    void testDialog_WithVerticalLayout() {
        closeInitialDialog();
        getSection(VerticalLayoutContentDialogPmo.class).$(ButtonElement.class).id("button").click();
        waitUntil(d -> $(DialogElement.class).exists());

        $(ButtonElement.class).id(OK_BUTTON).click();

        waitForDialogToBeClosed();
    }

    private void closeInitialDialog() {
        $(OkCancelDialogElement.class).waitForFirst().clickOnOk();
        waitUntil(invisibilityOfElementLocated(By.id(OVERLAY)));
        waitUntil(numberOfElementsToBe(By.tagName("vaadin-dialog"), 0));
        assertThat($(NotificationElement.class).all().size(), is(0));
        assertThat($(DialogElement.class).all().size(), is(0));
    }

    private void waitForNotificationsClosed() {
        waitUntil(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return $(NotificationElement.class).all().size() == 0;
            }
        });
    }

    private void sendKeys(CharSequence... keys) {
        Actions actionProvider = new Actions(getDriver());
        Action keydown = actionProvider.sendKeys(keys).build();
        keydown.perform();
    }

    private void waitForDialogToBeClosed() {
        // needed due to dialog closing animation
        waitUntil(invisibilityOfElementLocated(By.id(OVERLAY)));
        assertThat($(DialogElement.class).all().size(), is(0));
    }
}
