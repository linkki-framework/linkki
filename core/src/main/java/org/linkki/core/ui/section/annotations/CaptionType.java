/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

public enum CaptionType {

    /**
     * Defined by the attribute caption.
     */
    STATIC,

    /**
     * Do not show any caption.
     */
    NONE,

    /**
     * Caption is bound to the property using the method get&lt;PropertyName&gt;Caption().
     */
    DYNAMIC;
}
