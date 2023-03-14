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

import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.framework.ui.dialogs.OkCancelDialog;
import org.linkki.framework.ui.dialogs.PmoBasedDialogFactory;
import org.linkki.samples.playground.ts.messages.FieldValidationPmo;
import org.linkki.util.handler.Handler;

@UIVerticalLayout
public class OkCancelDialogMessagePmo {

    @UIButton(position = 0, caption = "Open dialog")
    public void button() {
        FieldValidationPmo validationDialogPmo = new FieldValidationPmo();
        OkCancelDialog dialog = new PmoBasedDialogFactory(validationDialogPmo::validate)
                .openOkCancelDialog("Validation Dialog PMO", Handler.NOP_HANDLER, validationDialogPmo);
        dialog.setWidth("600px");
    }

    public static class OkCancelDialogValidationPmo extends FieldValidationPmo {

        private boolean showTextArea;

        @UICheckBox(position = 20, caption = "Show text area to test overflow with messages")
        public boolean isShowTextArea() {
            return showTextArea;
        }

        public void setShowTextArea(boolean showTextArea) {
            this.showTextArea = showTextArea;
        }

        @UITextArea(position = 30, label = "Text Area", visible = VisibleType.DYNAMIC, height = "100em")
        public String getTextAreaValue() {
            return "A very high text area to test scrolling behavior of messages in dialog";
        }

        public boolean isTextAreaValueVisible() {
            return showTextArea;
        }

    }
}