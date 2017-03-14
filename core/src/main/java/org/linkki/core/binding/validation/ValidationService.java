/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.validation;

import org.faktorips.runtime.MessageList;

/**
 * A validation service is used to get validation messages that should be displayed in the UI.
 */
@FunctionalInterface
public interface ValidationService {

    /**
     * Message code for a {@link org.faktorips.runtime.Message Message} indicating a non-specific
     * fatal validation error.
     */
    String FATAL_ERROR_MESSAGE_CODE = "fatalValiationError";

    /** A validation service that always returns an empty message list. */
    // Using MessageList::new instead of () -> new MessageList() causes strange JDT problems?!?
    ValidationService NOP_VALIDATION_SERVICE = () -> new MessageList();

    /**
     * This message returns the validation messages that should be displayed in the UI. It should
     * call every necessary validations.
     * 
     * @return A {@link MessageList} containing all validation messages or an empty
     *         {@link MessageList} if there are no messages at all.
     */
    MessageList getValidationMessages();

    /** Returns a new validation service that always returns the given message list. */
    static ValidationService of(MessageList messages) {
        return () -> messages;
    }
}
