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
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ui.dialogs.SimpleDialogPmo;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.html.testbench.H3Element;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class DialogTest extends AbstractUiTest {

    @Test
    @Order(1)
    public void testDialogOnEntry() {
        clickMenuItem("Dialogs");
        waitUntil(visibilityOfElementLocated(By.id("overlay")));

        assertThat($(DialogElement.class).all().size(), is(1));

        // close dialog
        $(DialogElement.class).first().$(ButtonElement.class).first().click();
    }

    @Test
    @Order(2)
    public void testDialog_ClosedOnOk() {
        waitUntil(invisibilityOfElementLocated(By.id("overlay")));
        clickButton("showDialog");

        waitUntil(visibilityOfElementLocated(By.id("overlay")));
        assertThat($(DialogElement.class).all().size(), is(1));

        waitUntil(ExpectedConditions.elementToBeClickable(By.id("okButton")));
        $(ButtonElement.class).id("okButton").click();
        assertThat($(DialogElement.class).all().size(), is(0));
    }

    // TODO LIN-2226: test DialogErrorHandler

    @Test
    @Order(3)
    public void testOkCancelDialog() {
        VerticalLayoutElement section = $(VerticalLayoutElement.class).id(SimpleDialogPmo.class.getSimpleName());

        section.$(TextFieldElement.class).id("caption").setValue("Awesome dialog");
        section.$(TextFieldElement.class).id("content").setValue("This is awesome!");
        section.$(TextFieldElement.class).id("okCaption").setValue("Hell yeah");
        section.$(TextFieldElement.class).id("cancelCaption").setValue("Not really");
        section.$(ButtonElement.class).id("showDialog").click();

        waitUntil(visibilityOfElementLocated(By.id("overlay")));

        DialogElement dialog = $(DialogElement.class).first();
        assertThat(dialog.$(H3Element.class).first().getText(), is("Awesome dialog"));
        assertThat(dialog.$(VerticalLayoutElement.class).attribute("class", "content-area").first().getText(),
                   is("This is awesome!"));
        assertThat(dialog.$(ButtonElement.class).id("okButton").getText(), is("Hell yeah"));
        assertThat(dialog.$(ButtonElement.class).id("cancelButton").getText(), is("Not really"));
        // close dialog
        $(DialogElement.class).first().$(ButtonElement.class).first().click();
    }

    @Test
    @Order(4)
    public void testDialog_CloseOnViewChange() {
        VerticalLayoutElement section = $(VerticalLayoutElement.class).id(SimpleDialogPmo.class.getSimpleName());
        section.$(ButtonElement.class).id("showDialog").click();
        waitUntil(d -> $(DialogElement.class).exists());

        driver.navigate().back();

        // dialog should close on its own
        waitUntil(d -> !$(DialogElement.class).exists());
    }

}
