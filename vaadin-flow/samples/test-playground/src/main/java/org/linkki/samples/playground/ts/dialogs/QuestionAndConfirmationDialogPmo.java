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

import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.framework.ui.dialogs.ConfirmationDialog;
import org.linkki.framework.ui.dialogs.QuestionDialog;

import com.vaadin.flow.component.notification.Notification;

@UIVerticalLayout
public class QuestionAndConfirmationDialogPmo {

    public static final String SHOW_QUESTION_BUTTON_ID = "questionDialog";
    public static final String SHOW_CONFIRMATION_BUTTON_ID = "confirmationDialog";
    public static final String MESSAGE_OK = "OK";
    public static final String MESSAGE_CANCEL = "Cancel";

    @UIButton(position = 10, caption = "Open Question Dialog")
    public void questionDialog() {
        QuestionDialog.open("Confirm", "Is this one line</br>and this is another one?",
                            () -> Notification.show("Ok clicked"));
    }

    @UIButton(position = 20, caption = "Open Confirmation Dialog")
    public void confirmationDialog() {
        ConfirmationDialog.open("Confirm", "Confirm that this is one line</br>and this is another one.");
    }


}
