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

package org.linkki.samples.playground.bugs.lin2555;

import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.KeyCode;
import org.linkki.framework.ui.notifications.NotificationUtil;

import com.vaadin.flow.component.button.ButtonVariant;

@UISection(caption = TextfieldWithEnterButtonPmo.CAPTION)
public class TextfieldWithEnterButtonPmo {

    public static final String CAPTION = "LIN-2555";

    private String text = "initialText";

    @UILabel(position = 0, htmlContent = true)
    public String getDescription() {
        return "Steps to reproduce:" +
                "<ol>" +
                "<li>focus text field</li>" +
                "<li>change the value in text field</li>" +
                "<li>press enter without leaving the text field</li>" +
                "</ol>" +
                "Expected behavior: a notification is present showing the changed value <br>" +
                "Bug behavior: a notification is present showing the value before change";
    }

    @UITextField(position = 1)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @UIButton(position = 2, shortcutKeyCode = KeyCode.ENTER, variants = ButtonVariant.LUMO_PRIMARY, caption = "Submit")
    public void enterButton() {
        NotificationUtil.createNotification(Severity.INFO, text).open();
    }
}
