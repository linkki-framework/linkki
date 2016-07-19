/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations.adapters;

import org.linkki.core.ui.section.annotations.ToolTipType;
import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UIToolTip;
import org.linkki.core.ui.section.annotations.UIToolTipDefinition;

/**
 * The adapter to provide access to an {@link UIButton} annotation through the definition interface.
 */
public class UIToolTipAdapter implements UIToolTipDefinition {

    private final UIToolTip toolTipAnnotation;

    public UIToolTipAdapter(UIToolTip annotation) {
        this.toolTipAnnotation = annotation;
    }

    @Override
    public ToolTipType toolTipType() {
        return toolTipAnnotation.toolTipType();
    }

    @Override
    public String text() {
        return toolTipAnnotation.text();
    }
}
