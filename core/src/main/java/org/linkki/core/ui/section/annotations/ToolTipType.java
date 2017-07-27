/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

/**
 * Provides options to define how the value of tooltip is retrieved.
 */
public enum ToolTipType {

    /**
     * Defined by {@link UIToolTip#text()}.
     */
    STATIC,

    /**
     * Tooltip is bound to the property using the method get&lt;PropertyName&gt;ToolTip().
     */
    DYNAMIC;
}
