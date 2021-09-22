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

package org.linkki.samples.playground.dialogs;

import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.framework.ui.dialogs.OkCancelDialog;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.notification.Notification;

@UISection(caption = "Simple OkCancelDialog")
public class SimpleDialogPmo {

    private String caption = "Sample Dialog";
    private String content = "Do you really want to do this?";
    private String okCaption = "Yep";
    private String cancelCaption = "No thanks";

    @UITextField(position = 0)
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @UITextField(position = 10)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @UITextField(position = 20)
    public String getOkCaption() {
        return okCaption;
    }

    public void setOkCaption(String okCaption) {
        this.okCaption = okCaption;
    }

    @UITextField(position = 30)
    public String getCancelCaption() {
        return cancelCaption;
    }

    public void setCancelCaption(String cancelCaption) {
        this.cancelCaption = cancelCaption;
    }

    @UIButton(position = 40, caption = "Open dialog")
    public void showDialog() {
        OkCancelDialog.builder(caption)
                .content(new Text(content))
                .okCaption(okCaption)
                .cancelCaption(cancelCaption)
                .build()
                .open();
    }

    @UIButton(position = 50, caption = "Show exception dialog")
    public void showExceptionDialog() {
        throw new RuntimeException("Manually triggered exception");
    }

    @UIButton(position = 60, caption = "Open dialog with okHandler")
    public void okHandlerDialog() {
        Html dialogContent = new Html("<div>"
                + "When closing this dialog, the notification should only be shown once, whether if this dialog is closed by<br>"
                + "<ul>"
                + "<li>clicking Ok</li>"
                + "<li>hitting Enter</li>"
                + "<li>or hitting enter with focus set on the Ok button</li>"
                + "<ul>"
                + "<div>");

        OkCancelDialog.builder("Dialog with okHandler")
                .content(dialogContent)
                .okCaption("Ok")
                .cancelCaption("Cancel")
                .okHandler(() -> {
                    Notification.show("This notification should always be shown exactly once.");
                })
                .build()
                .open();
    }


}
