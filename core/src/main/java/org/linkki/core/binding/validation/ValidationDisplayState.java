/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

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
            return messageList.stream().filter(m -> !m.isMandatoryFieldMessage()).collect(toMessageList());
        }
    };

    /**
     * Returns a message list containing all the messages that are to be displayed from the given
     * message list according to this state.
     */
    public abstract MessageList filter(MessageList messageList);
}
