/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.validation;

import javax.annotation.Nonnull;

import org.faktorips.runtime.MessageList;

/**
 * A validation service is used to get validation messages that should be displayed in the UI.
 */
@FunctionalInterface
public interface ValidationService {

    public static final String FATAL_ERROR_MESSAGE_CODE = "fatalValiationError";

    /**
     * This message returns the validation messages that should be displayed in the UI. It should
     * call every necessary validations.
     * 
     * @return A {@link MessageList} containing all validation messages or an empty
     *         {@link MessageList} if there are no messages at all.
     */
    @Nonnull
    public MessageList getValidationMessages();

}
