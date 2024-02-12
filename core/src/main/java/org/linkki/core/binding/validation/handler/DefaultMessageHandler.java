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

package org.linkki.core.binding.validation.handler;

import org.linkki.core.binding.descriptor.messagehandler.LinkkiMessageHandler;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.wrapper.ComponentWrapper;

/**
 * The default message handler for all components. It uses the {@link PropertyDispatcher} to
 * retrieve the messages which are relevant for the specific binding. If there are any messages they
 * will be set to the component and mark the component as invalid using
 * {@link ComponentWrapper#setValidationMessages(MessageList)}.
 * <p>
 * If there is no specific message handler configured for a component, this message handler will be
 * used as default.
 */
public class DefaultMessageHandler implements LinkkiMessageHandler {

    public static final DefaultMessageHandler INSTANCE = new DefaultMessageHandler();

    @Override
    public MessageList process(MessageList messages,
            ComponentWrapper componentWrapper,
            PropertyDispatcher propertyDispatcher) {
        MessageList messagesForProperty = getRelevantMessages(messages, propertyDispatcher);
        componentWrapper.setValidationMessages(messagesForProperty);
        return messagesForProperty;
    }

    /**
     * Selects the messages that are relevant for this specific binding. The default implementation
     * uses the given {@link PropertyDispatcher} but the behavior could be overwritten to select
     * other or additional messages.
     * 
     * @param messages The messages from {@link ValidationService}
     * @param propertyDispatcher The {@link PropertyDispatcher} for the currently handled binding.
     * @return The selected relevant messages
     */
    protected MessageList getRelevantMessages(MessageList messages, PropertyDispatcher propertyDispatcher) {
        MessageList messagesForProperty = propertyDispatcher.getMessages(messages);
        addFatalError(messages, messagesForProperty);
        return messagesForProperty;
    }

    private void addFatalError(MessageList messages, MessageList messagesForProperty) {
        messages.getMessageByCode(ValidationService.FATAL_ERROR_MESSAGE_CODE)
                .ifPresent(messagesForProperty::add);
    }

}
