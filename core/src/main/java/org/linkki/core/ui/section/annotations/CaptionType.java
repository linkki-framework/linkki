/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

/**
 * Available options how the caption is retrieved
 */
public enum CaptionType {

    /**
     * Caption is defined by the attribute <code>caption</code>.
     */
    STATIC,

    /**
     * Do not show any caption.
     */
    NONE,

    /**
     * Caption is bound to the property using the method
     * <code>get&lt;PropertyName&gt;Caption()</code>.
     */
    DYNAMIC;
}
