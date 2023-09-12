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

package org.linkki.samples.playground.ts.messages;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.framework.ui.component.MessageUiComponents;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MessageTableSection {

    private MessageTableSection() {
        throw new IllegalStateException("Utility class");
    }

    public static Component create() {
        MessageTablePmo messageTablePmo = new MessageTablePmo();
        ValidationService validationService = messageTablePmo::validate;

        // tag::messages-component[]
        DefaultBindingManager bindingManager = new DefaultBindingManager(validationService);
        // end::messages-component[]
        BindingContext messageTableBindingContext = new BindingContext("messages");

        // tag::messages-component[]
        bindingManager.addUiUpdateObserver(messageTableBindingContext);

        Component messageTable = MessageUiComponents.createMessageTable("Message Table",
                                                                        validationService::getFilteredMessages,
                                                                        messageTableBindingContext);
        // end::messages-component[]

        return new VerticalLayout(
                VaadinUiCreator.createComponent(messageTablePmo, bindingManager.getContext(MessageTablePmo.class)),
                messageTable);
    }

    @UIHorizontalLayout
    public static class MessageTablePmo {

        private boolean messagesPresent = true;

        @UICheckBox(position = 0, caption = "Toggle presence of messages to test messages update")
        public boolean isMessagesPresent() {
            return messagesPresent;
        }

        public void setMessagesPresent(boolean messagesPresent) {
            this.messagesPresent = messagesPresent;
        }

        public MessageList validate() {
            if (messagesPresent) {
                return new MessageList(
                        Message.builder("An error message", Severity.ERROR).create(),
                        //one duplicate message to test LIN-3530
                        Message.builder("An error message", Severity.ERROR).create(),
                        Message.builder("A warning message", Severity.WARNING).create(),
                        Message.builder("An info message", Severity.INFO).create());
            } else {
                return new MessageList();
            }
        }
    }
}
