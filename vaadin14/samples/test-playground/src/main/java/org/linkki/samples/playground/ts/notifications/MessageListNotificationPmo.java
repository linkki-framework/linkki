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

package org.linkki.samples.playground.ts.notifications;

import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.defaults.ui.aspects.types.AlignmentType;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UIRadioButtons;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.framework.ui.notifications.NotificationUtil;

@UISection
public class MessageListNotificationPmo {

    private String title = "Something happened!";

    private String text1 = "The first message in a MessageList";
    private Severity severity1 = Severity.INFO;
    private String text2 = "Another message";
    private Severity severity2 = Severity.INFO;
    private String text3 = "This is the last message";
    private Severity severity3 = Severity.INFO;

    @UITextField(position = 0, label = "Title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @UITextField(position = 10, label = "Text 1")
    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    @UIRadioButtons(position = 11, label = "Severity 1", content = AvailableValuesType.ENUM_VALUES_EXCL_NULL, buttonAlignment = AlignmentType.HORIZONTAL)
    public Severity getSeverity1() {
        return severity1;
    }

    public void setSeverity1(Severity severity1) {
        this.severity1 = severity1;
    }

    @UITextField(position = 20, label = "Text 2")
    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    @UIRadioButtons(position = 21, label = "Severity 2", content = AvailableValuesType.ENUM_VALUES_EXCL_NULL, buttonAlignment = AlignmentType.HORIZONTAL)
    public Severity getSeverity2() {
        return severity2;
    }

    public void setSeverity2(Severity severity2) {
        this.severity2 = severity2;
    }

    @UITextField(position = 30, label = "Text 3")
    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    @UIRadioButtons(position = 31, label = "Severity 3", content = AvailableValuesType.ENUM_VALUES_EXCL_NULL, buttonAlignment = AlignmentType.HORIZONTAL)
    public Severity getSeverity3() {
        return severity3;
    }

    public void setSeverity3(Severity severity3) {
        this.severity3 = severity3;
    }

    @UIButton(position = 40, caption = "Show")
    public void show() {
        NotificationUtil.show(title, createMessageList());
    }

    private MessageList createMessageList() {
        MessageList messages = new MessageList();
        if (!text1.isEmpty()) {
            messages.add(new Message("msg1", text1, severity1));
        }
        if (!text2.isEmpty()) {
            messages.add(new Message("msg2", text2, severity2));
        }
        if (!text3.isEmpty()) {
            messages.add(new Message("msg3", text3, severity3));
        }

        return messages;
    }

}