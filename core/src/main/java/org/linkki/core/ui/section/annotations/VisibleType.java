/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

/** Defines whether an UI component is visible. */
public enum VisibleType {

    /** The UI component is always visible. */
    VISIBLE,

    /** The UI component is never visible. */
    INVISIBLE,

    /**
     * The visible state is read from the PMO by invoking a method named is[PropertyName]Visible().
     */
    DYNAMIC;

}
