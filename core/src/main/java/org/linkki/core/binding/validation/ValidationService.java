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
     * Message code for a {@link org.linkki.core.binding.validation.message.Message Message} indicating a non-specific
     * fatal validation error.
     */
    String FATAL_ERROR_MESSAGE_CODE = "fatalValidationError";

    /** A validation service that always returns an empty message list. */
    // Using MessageList::new instead of () -> new MessageList() causes strange JDT problems?!?
    ValidationService NOP_VALIDATION_SERVICE = () -> new MessageList();

    /**
     * This message returns the validation messages that should be displayed in the UI. It should call
     * every necessary validations.
     * 
     * @return A {@link MessageList} containing all validation messages or an empty {@link MessageList}
     *         if there are no messages at all.
     */
    MessageList getValidationMessages();

    /** Returns a new validation service that always returns the given message list. */
    static ValidationService of(MessageList messages) {
        return () -> messages;
    }
}
