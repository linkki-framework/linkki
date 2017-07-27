/*******************************************************************************
 * Copyright (c) 2016 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang3.StringUtils;

/**
 * Shows a tooltip next to a UI-Element. The annotation can be added to the method the UI-Element is
 * bound.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
public @interface UIToolTip {

    /** The displayed text for {@link ToolTipType#STATIC} */
    String text() default StringUtils.EMPTY;

    /** Defines how the tooltip text should be retrieved */
    ToolTipType toolTipType() default ToolTipType.STATIC;
}
