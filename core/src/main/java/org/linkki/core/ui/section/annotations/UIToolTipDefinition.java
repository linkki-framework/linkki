/*******************************************************************************
 * Copyright (c) 2016 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import javax.annotation.Nullable;

public interface UIToolTipDefinition {

    @Nullable
    public ToolTipType toolTipType();

    public String text();
}
