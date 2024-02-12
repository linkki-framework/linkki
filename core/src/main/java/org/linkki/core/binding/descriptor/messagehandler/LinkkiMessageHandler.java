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

package org.linkki.core.binding.descriptor.messagehandler;

import org.linkki.core.binding.Binding;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.wrapper.ComponentWrapper;

/**
 * The message handler is responsible for processing the validation messages of a
 * {@link ValidationService} after the UI has been updated. It is triggered by the {@link Binding}
 * for every bound UI component.
 */
@FunctionalInterface
public interface LinkkiMessageHandler {

    /**
     * Processes the validation messages and updates the component. The given
     * {@link PropertyDispatcher} may be used to filter the relevant messages using
     * {@link PropertyDispatcher#getMessages(MessageList)} if only those messages should be
     * processed.
     * <p>
     * The messages can be set to the component using
     * {@link ComponentWrapper#setValidationMessages(MessageList)}.
     * <p>
     * The messages are processed after all other binding aspects have been processed. That means
     * the component is already in the state as it will be after the update round trip. When
     * changing the component's state, make sure to not unwillingly overwrite another state. For
     * example, a button might get disabled by a binding or when there are error messages. Hence,
     * the message handler should not set the button to enabled if it is already disabled.
     * <p>
     * It is not recommended to process messages which are not part of the input messages. Newly
     * created messages may not be processed by other message handlers!
     *
     * @param messages All messages from last model validation as they are derived from
     *            {@link ValidationService}.
     * @param componentWrapper The {@link ComponentWrapper} that holds the bound component
     * @param propertyDispatcher The dispatcher that handles the binding of the PMO and model
     *            object.
     * @return A list of messages the component is responsible for. Read API note for clarification.
     * @apiNote The returned message list must only contain the messages that are really handled by
     *          the component in a way that the user could fix the problem using this component. In
     *          contrast, a component that shows a list of all existing messages should simply
     *          return an empty message list because the component is not responsible for the
     *          messages. It may be used to link a message to a component.
     */
    MessageList process(MessageList messages, ComponentWrapper componentWrapper, PropertyDispatcher propertyDispatcher);

}
