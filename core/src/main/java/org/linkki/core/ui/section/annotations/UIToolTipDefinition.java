/*******************************************************************************
 * Copyright (c) 2016 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import javax.annotation.Nullable;

/**
 * The element definition for the {@link UIToolTip} annotation.
 */
public interface UIToolTipDefinition {

    /** The displayed text for {@link ToolTipType#STATIC} */
    @Nullable
    public ToolTipType toolTipType();

    /** Defines how the tooltip text should be retrieved */
    public String text();
}
