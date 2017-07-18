/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import static org.linkki.core.ui.section.annotations.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.ui.section.annotations.adapters.LabelBindingDefinition;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBindingDefinition(LabelBindingDefinition.class)
public @interface UILabel {

    int position();

    String label() default "";

    VisibleType visible() default VISIBLE;

    String modelObject() default ModelObject.DEFAULT_NAME;

    String modelAttribute() default "";

    String[] styleNames() default {};

    /**
     * When set to {@code true}, the label's content will be displayed as HTML, otherwise as plain text.
     */
    boolean htmlContent() default false;
}
