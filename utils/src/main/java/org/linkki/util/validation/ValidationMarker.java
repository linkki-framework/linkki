/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.util.validation;

import org.faktorips.runtime.IMarker;

/** Interface for validation message markers. */
@FunctionalInterface
public interface ValidationMarker extends IMarker {

    /** Returns {@code true} if the marker marks a validation for a mandatory field. */
    boolean isMandatoryFieldValidation();
}
