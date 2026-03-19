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

package org.linkki.testbench.pageobjects;

import java.util.Objects;
import java.util.function.Predicate;

import org.linkki.core.binding.validation.message.Severity;
import org.linkki.framework.ui.LinkkiApplicationTheme;
import org.linkki.framework.ui.dialogs.OkCancelDialog;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.TestBenchElement;

public class OkCancelDialogElement extends DialogElement {

    public static final String ATTR_CLASS = "class";

    public String getCaption() {
        return getDialogHeader().findElement(By.className(LinkkiApplicationTheme.DIALOG_CAPTION)).getText();
    }

    public boolean isMessageAreaPresent(Severity severity) {
        return queryMessageArea().withAttributeContaining(ATTR_CLASS, severity.name()).exists();
    }

    public boolean isMessageAreaPresent() {
        return queryMessageArea().exists() && getMessageArea().isDisplayed();
    }

    public DivElement getMessageArea() {
        return queryMessageArea().single();
    }

    public VerticalLayoutElement getContentArea() {
        return getDialogLayout().$(VerticalLayoutElement.class)
                .withAttributeContaining(ATTR_CLASS, OkCancelDialog.CLASS_NAME_CONTENT_AREA)
                .single();
    }

    public TestBenchElement getDialogLayout() {
        return getChildren().stream()
                .filter(e -> !e.hasAttribute("slot")).findFirst()
                .orElseThrow()
                .findElement(By.className(OkCancelDialog.CLASS_NAME_DIALOG_LAYOUT));
    }

    public ButtonElement getOkButton() {
        return getDialogFooter().$(ButtonElement.class).id(OkCancelDialog.OK_BUTTON_ID);
    }

    public void clickOnOk() {
        getOkButton().click();
    }

    public ButtonElement getCancelButton() {
        return getDialogFooter().$(ButtonElement.class).id(OkCancelDialog.CANCEL_BUTTON_ID);
    }

    public void clickOnCancel() {
        getCancelButton().click();
    }

    @Override
    public Dimension getSize() {
        waitForVaadin();
        var dialogOverlay = $("vaadin-dialog-overlay").single();
        var overlay = dialogOverlay.$(DivElement.class).id("overlay");
        return overlay.getSize();
    }

    private ElementQuery<DivElement> queryMessageArea() {
        return getDialogLayout().$(DivElement.class).withAttributeContaining(ATTR_CLASS,
                                                                             OkCancelDialog.CLASS_NAME_MESSAGE_AREA);
    }

    private TestBenchElement getDialogHeader() {
        return getChildren().stream()
                .filter(e -> Objects.equals(e.getAttribute("slot"), "header-content")).findFirst()
                .orElseThrow();
    }

    public TestBenchElement getDialogFooter() {
        return getChildren().stream()
                .filter(e -> Objects.equals(e.getAttribute("slot"), "footer")).findFirst()
                .orElseThrow();
    }

    /**
     * Can be used with {@link ElementQuery#withCondition(Predicate)}.
     */
    public static Predicate<OkCancelDialogElement> hasCaption(String caption) {
        return dialog -> dialog.getCaption().equals(caption);
    }
}
