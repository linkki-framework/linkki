/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations.adapters;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ui.section.annotations.ToolTipType;
import org.linkki.core.ui.section.annotations.UIToolTip;
import org.linkki.core.ui.section.annotations.UIToolTipDefinition;

/**
 * The adapter to provide access to an {@link UIToolTip} annotation through the definition
 * interface.
 */
public class UIToolTipAdapter implements UIToolTipDefinition {

    @Nullable
    private final UIToolTip toolTipAnnotation;

    /**
     * @param annotation the annotation or <code>null</code> if no {@link UIToolTip} is present
     */
    public UIToolTipAdapter(@Nullable UIToolTip annotation) {
        this.toolTipAnnotation = annotation;
    }

    @Override
    @CheckForNull
    public ToolTipType toolTipType() {
        return toolTipAnnotation != null ? toolTipAnnotation.toolTipType() : null;
    }

    @Override
    public String text() {
        return toolTipAnnotation != null ? toolTipAnnotation.text() : StringUtils.EMPTY;
    }
}
