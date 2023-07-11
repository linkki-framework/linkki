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

package org.linkki.samples.playground.ts.dialogs;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.framework.ui.dialogs.DialogBindingManager;
import org.linkki.framework.ui.dialogs.OkCancelDialog;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;

@UISection
public class OkCancelDialogHandlerPmo {

    public static final String SHOW_DIALOG_BUTTON_ID = "showDialog";
    public static final String SHOW_DIALOG_WITH_BINDING_MANAGER_BUTTON_ID = "showDialogWithBindingManager";
    public static final String RESET_BUTTON_ID = "reset";

    private final BindingContext bindingContext;
    private int okCounter;
    private int cancelCounter;

    public OkCancelDialogHandlerPmo(BindingContext bindingContext) {
        this.bindingContext = bindingContext;
    }

    public static Component create() {
        BindingContext bindingContext = new BindingContext();
        return VaadinUiCreator.createComponent(new OkCancelDialogHandlerPmo(bindingContext), bindingContext);
    }

    @UILabel(position = 0, label = "okHandler called")
    public int getOkCounter() {
        return okCounter;
    }

    @UILabel(position = 1, label = "cancelHandler called")
    public int getCancelCounter() {
        return cancelCounter;
    }

    @UIButton(position = 10, caption = "Open dialog")
    public void showDialog() {
        OkCancelDialog.builder("OkCancelDialog")
                .okHandler(this::onOk)
                .cancelHandler(this::onCancel)
                .build()
                .open();
    }

    @UIButton(position = 11, caption = "Open dialog with DialogBindingManager")
    public void showDialogWithBindingManager() {
        OkCancelDialog dialog = OkCancelDialog.builder("OkCancelDialog")
                .okHandler(this::onOk)
                .cancelHandler(this::onCancel)
                .build();

        @SuppressWarnings("checkstyle:unusedlocalvariable")
        DialogBindingManager bindingManager = new DialogBindingManager(dialog,
                ValidationService.NOP_VALIDATION_SERVICE);

        dialog.addContent(new Span(
                "Pressing enter while the OK button was selected used to trigger the okHandler twice"));
        dialog.open();
    }

    private void onOk() {
        okCounter += 1;
        bindingContext.modelChanged();
    }

    private void onCancel() {
        cancelCounter += 1;
        bindingContext.modelChanged();
    }

    @UIButton(position = 20, caption = "Reset")
    public void reset() {
        okCounter = 0;
        cancelCounter = 0;
    }
}
