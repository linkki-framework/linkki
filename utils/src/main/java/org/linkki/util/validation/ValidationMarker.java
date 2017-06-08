/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.util.validation;

/** Interface for validation message markers. */
@FunctionalInterface
public interface ValidationMarker {

    /** Returns {@code true} if the marker marks a validation for a mandatory field. */
    boolean isRequiredInformationMissing();
}
