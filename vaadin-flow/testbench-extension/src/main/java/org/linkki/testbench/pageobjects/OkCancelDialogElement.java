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

import org.linkki.core.binding.validation.message.Severity;
import org.linkki.framework.ui.dialogs.OkCancelDialog;
import org.openqa.selenium.Dimension;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.testbench.ElementQuery;

public class OkCancelDialogElement extends DialogElement {

    public boolean isMessageAreaPresent(Severity severity) {
        return queryMessageArea().attributeContains("class", severity.name()).exists();
    }

    public boolean isMessageAreaPresent() {
        return queryMessageArea().exists() && getMessageArea().isDisplayed();
    }

    public DivElement getMessageArea() {
        return queryMessageArea().first();
    }

    private ElementQuery<DivElement> queryMessageArea() {
        return $(DivElement.class).attributeContains("class", OkCancelDialog.CLASS_NAME_MESSAGE_AREA);
    }

    public VerticalLayoutElement getContentArea() {
        return $(VerticalLayoutElement.class).attributeContains("class", OkCancelDialog.CLASS_NAME_CONTENT_AREA)
                .first();
    }

    public VerticalLayoutElement getDialogLayout() {
        return $(VerticalLayoutElement.class).attributeContains("class", OkCancelDialog.CLASS_NAME_DIALOG_LAYOUT)
                .first();
    }

    public ButtonElement getOkButton() {
        return $(ButtonElement.class).id(OkCancelDialog.OK_BUTTON_ID);
    }

    public void clickOnOk() {
        getOkButton().click();
    }

    public ButtonElement getCancelButton() {
        return $(ButtonElement.class).id(OkCancelDialog.CANCEL_BUTTON_ID);
    }

    public void clickOnCancel() {
        getCancelButton().click();
    }

    @Override
    public Dimension getSize() {
        return $(DivElement.class).id("overlay").getSize();
    }
}
