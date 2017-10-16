/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.binding.validation;

import static org.linkki.core.message.MessageListCollector.toMessageList;

import org.linkki.core.message.MessageList;

/** The state regarding which validation messages are displayed. */
public enum ValidationDisplayState {

    /** Show all validation messages. */
    SHOW_ALL {
        @Override
        public MessageList filter(MessageList messageList) {
            return messageList;
        }
    },

    /** Hide messages for mandatory field validations. */
    HIDE_MANDATORY_FIELD_VALIDATIONS {
        @Override
        public MessageList filter(MessageList messageList) {
            return messageList.stream()
                    .filter(m -> !m.isMandatoryFieldMessage())
                    .collect(toMessageList());
        }
    };

    /**
     * Returns a message list containing all the messages that are to be displayed from the given
     * message list according to this state.
     */
    public abstract MessageList filter(MessageList messageList);
}
