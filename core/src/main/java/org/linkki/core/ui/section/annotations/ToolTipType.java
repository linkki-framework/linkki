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
     * Caption is bound to the property using the method get&lt;PropertyName&gt;ToolTip().
     */
    DYNAMIC;
}
