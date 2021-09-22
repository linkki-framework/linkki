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

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.dialogs.SimpleDialogPmo;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.html.testbench.H3Element;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class DialogTest extends AbstractUiTest {

    private static final String OVERLAY = "overlay";
    private static final String OK_BUTTON = "okButton";

    @Test
    public void testDialogOnEntry() {
        getDriver().get(DriverProperties.getTestUrl(""));
        clickMenuItem("Dialogs");
        waitUntil(ExpectedConditions.visibilityOfElementLocated(By.id(OVERLAY)));

        assertThat($(DialogElement.class).all().size(), is(1));

        // close dialog
        clickButton(OK_BUTTON);
        waitUntil(invisibilityOfElementLocated(By.id(OVERLAY)));
    }

    @Test
    public void testDialog_ClosedOnOk() {
        openDialogViewAndCloseInitialDialog();
        clickButton("showDialog");

        waitUntil(visibilityOfElementLocated(By.id(OVERLAY)));
        assertThat($(DialogElement.class).all().size(), is(1));

        $(ButtonElement.class).id(OK_BUTTON).click();

        waitForDialogToBeClosed();
    }

    // TODO LIN-2226: test DialogErrorHandler

    @Test
    public void testOkCancelDialog() {
        openDialogViewAndCloseInitialDialog();
        VerticalLayoutElement section = $(VerticalLayoutElement.class).id(SimpleDialogPmo.class.getSimpleName());

        section.$(TextFieldElement.class).id("caption").setValue("Awesome dialog");
        section.$(TextFieldElement.class).id("content").setValue("This is awesome!");
        section.$(TextFieldElement.class).id("okCaption").setValue("Hell yeah");
        section.$(TextFieldElement.class).id("cancelCaption").setValue("Not really");
        section.$(ButtonElement.class).id("showDialog").click();

        waitUntil(ExpectedConditions.visibilityOfElementLocated(By.id(OVERLAY)));

        DialogElement dialog = $(DialogElement.class).first();
        assertThat(dialog.$(H3Element.class).first().getText(), is("Awesome dialog"));
        assertThat(dialog.$(VerticalLayoutElement.class).attribute("class", "content-area").first().getText(),
                   is("This is awesome!"));
        assertThat(dialog.$(ButtonElement.class).id(OK_BUTTON).getText(), is("Hell yeah"));
        assertThat(dialog.$(ButtonElement.class).id("cancelButton").getText(), is("Not really"));
        // close dialog
        $(DialogElement.class).first().$(ButtonElement.class).first().click();
    }

    @Test
    public void testDialogWithCloseHandler_CloseOnOk_WithDoubleClick() {
        openDialogViewAndCloseInitialDialog();
        clickButton("okHandlerDialog");
        waitUntil(visibilityOfElementLocated(By.id(OVERLAY)));
        assertThat($(DialogElement.class).all().size(), is(1));

        $(ButtonElement.class).id(OK_BUTTON).doubleClick();
        waitForDialogToBeClosed();

        assertThat($(NotificationElement.class).all().size(), is(1));
    }

    @Test
    public void testDialogWithCloseHandler_CloseOnEnter_WithDoubleEnter() {
        openDialogViewAndCloseInitialDialog();
        clickButton("okHandlerDialog");
        waitUntil(visibilityOfElementLocated(By.id(OVERLAY)));
        assertThat($(DialogElement.class).all().size(), is(1));

        sendKeys(Keys.ENTER, Keys.ENTER);
        waitForDialogToBeClosed();

        assertThat($(NotificationElement.class).all().size(), is(1));
    }

    @Test
    public void testDialogWithCloseHandler_CloseOnEnter_WithFocusOnOkButton() {
        openDialogViewAndCloseInitialDialog();

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
    public void testDialog_CloseOnViewChange() {
        openDialogViewAndCloseInitialDialog();
        VerticalLayoutElement section = $(VerticalLayoutElement.class).id(SimpleDialogPmo.class.getSimpleName());
        section.$(ButtonElement.class).id("showDialog").click();
        waitUntil(d -> $(DialogElement.class).exists());

        driver.navigate().back();

        // dialog should close on its own
        waitUntil(d -> !$(DialogElement.class).exists());
    }

    private void openDialogViewAndCloseInitialDialog() {
        getDriver().get(DriverProperties.getTestUrl(""));
        clickMenuItem("Dialogs");
        clickButton(OK_BUTTON);
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
