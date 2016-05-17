/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.validation;

import javax.annotation.Nonnull;

import org.faktorips.runtime.MessageList;
import org.linkki.core.util.MessageUtil;
import org.linkki.util.StreamUtil;

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
            MessageList filteredMessages = new MessageList();
            StreamUtil.stream(messageList).filter(m -> !MessageUtil.isMandatoryFieldMessage(m))
                    .forEach(filteredMessages::add);
            return filteredMessages;
        }
    };

    /**
     * Returns a message list containing all the messages that are to be displayed from the given
     * message list according to this state.
     */
    @Nonnull
    public abstract MessageList filter(@Nonnull MessageList messageList);
}
