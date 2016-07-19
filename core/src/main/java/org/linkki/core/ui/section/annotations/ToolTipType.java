/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

public enum ToolTipType {

    /**
     * Defined by the attribute text.
     */
    STATIC,

    /**
     * Do not show any tooltip.
     */
    NONE,

    /**
     * Caption is bound to the property using the method get&lt;PropertyName&gt;ToolTip().
     */
    DYNAMIC;
}
