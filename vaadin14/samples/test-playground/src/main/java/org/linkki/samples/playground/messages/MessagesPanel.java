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
package org.linkki.samples.playground.messages;

import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MessagesPanel extends VerticalLayout {

    private static final long serialVersionUID = 1239168264524211917L;

    public void updateMessages(MessageList messages) {
        setVisible(!messages.isEmpty());
        removeAll();
        
        for (Message message : messages) {
            Span messageLabel = new Span(getMessageIcon(message));
            
            String text = message.getText();
            if (message.isMandatoryFieldMessage()) {
                text += " *";
            }
            
            messageLabel.setText(text);
            this.add(messageLabel);
        }
    }

    private Icon getMessageIcon(Message message) {
        switch (message.getSeverity()) {
        case INFO:
            return VaadinIcon.INFO_CIRCLE.create();
        case WARNING:
            return VaadinIcon.WARNING.create();
        default:
            return VaadinIcon.EXCLAMATION_CIRCLE.create();
        }
    }

}