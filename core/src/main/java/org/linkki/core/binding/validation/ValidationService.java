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
package org.linkki.core.binding.validation;

import org.linkki.core.binding.validation.message.MessageList;

/**
 * A validation service is used to get validation messages that should be displayed in the UI.
 */
@FunctionalInterface
public interface ValidationService {

    /**
     * Message code for a {@link org.linkki.core.binding.validation.message.Message Message} indicating
     * a non-specific fatal validation error.
     */
    String FATAL_ERROR_MESSAGE_CODE = "fatalValidationError";

    /** A validation service that always returns an empty message list. */
    // Using MessageList::new instead of () -> new MessageList() causes strange JDT problems?!?
    ValidationService NOP_VALIDATION_SERVICE = () -> new MessageList();

    /**
     * Returns the validation messages that should be displayed in the UI. It should call all necessary
     * validations.
     * 
     * @return a {@link MessageList} containing all validation messages or an empty {@link MessageList}
     *         if there are no messages at all
     * @see #getFilteredMessages() {@code getFilteredMessages()} to obtain messages filtered by the
     *      {@link #getValidationDisplayState()}
     */
    MessageList getValidationMessages();

    /**
     * Returns the validation messages returned by {@link #getValidationMessages()} after applying the
     * {@link #getValidationDisplayState() ValidationDisplayState's}
     * {@link ValidationDisplayState#filter(MessageList) filter(MessageList)} method to them.
     * 
     * @implNote As {@link #getValidationDisplayState()} returns {@link ValidationDisplayState#SHOW_ALL}
     *           by default, the {@link MessageList} is returned unfiltered unless
     *           {@link #getValidationDisplayState()} is overridden by an implementation.
     * 
     * @return a {@link MessageList} containing all validation messages that match the
     *         {@link ValidationDisplayState} or an empty {@link MessageList} if there are no messages
     *         at all
     * @see #getValidationMessages()
     * @see #getValidationDisplayState()
     */
    default MessageList getFilteredMessages() {
        return getValidationDisplayState().filter(getValidationMessages());
    }

    /**
     * Returns the {@link ValidationDisplayState} that is used for {@link #getFilteredMessages()}.
     * 
     * @implNote By default, this is always {@link ValidationDisplayState#SHOW_ALL}.
     * @return the {@link ValidationDisplayState} that is used for {@link #getFilteredMessages()}.
     */
    default ValidationDisplayState getValidationDisplayState() {
        return ValidationDisplayState.SHOW_ALL;
    }

    /** Returns a new validation service that always returns the given message list. */
    static ValidationService of(MessageList messages) {
        return () -> messages;
    }
}
