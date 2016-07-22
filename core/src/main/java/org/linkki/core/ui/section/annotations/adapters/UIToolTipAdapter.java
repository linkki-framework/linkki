/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations.adapters;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ui.section.annotations.ToolTipType;
import org.linkki.core.ui.section.annotations.UIToolTip;
import org.linkki.core.ui.section.annotations.UIToolTipDefinition;

/**
 * The adapter to provide access to an {@link UIToolTip} annotation through the definition
 * interface.
 */
public class UIToolTipAdapter implements UIToolTipDefinition {

    private final UIToolTip toolTipAnnotation;

    /**
     * @param annotation the annotation or <code>null</code> if no {@link UIToolTip} is present
     */
    public UIToolTipAdapter(UIToolTip annotation) {
        this.toolTipAnnotation = annotation;
    }

    @Override
    public ToolTipType toolTipType() {
        return toolTipAnnotation == null ? null : toolTipAnnotation.toolTipType();
    }

    @Override
    public String text() {
        return toolTipAnnotation == null || toolTipType() == null ? StringUtils.EMPTY : toolTipAnnotation.text();
    }
}
