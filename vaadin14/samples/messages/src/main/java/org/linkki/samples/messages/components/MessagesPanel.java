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
package org.linkki.samples.messages.components;

import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class MessagesPanel extends Panel {

    private static final long serialVersionUID = 1239168264524211917L;

    public void updateMessages(MessageList messages) {
        setVisible(!messages.isEmpty());
        VerticalLayout layout = new VerticalLayout();
        for (Message message : messages) {
            Label messageLabel = new Label();
            messageLabel.setIcon(getMessageIcon(message));
            String text = message.getText();
            if (message.isMandatoryFieldMessage()) {
                text += " *";
            }
            messageLabel.setCaption(text);
            messageLabel.setContentMode(ContentMode.PREFORMATTED);
            layout.addComponent(messageLabel);
        }
        setContent(layout);
    }

    private VaadinIcons getMessageIcon(Message message) {
        switch (message.getSeverity()) {
        case INFO:
            return VaadinIcons.INFO_CIRCLE;
        case WARNING:
            return VaadinIcons.WARNING;
        default:
            return VaadinIcons.EXCLAMATION_CIRCLE;
        }
    }

}