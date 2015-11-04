/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.section.annotations;

import static de.faktorzehn.ipm.web.ui.section.annotations.EnabledType.ENABLED;
import static de.faktorzehn.ipm.web.ui.section.annotations.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.vaadin.server.FontAwesome;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UIButton {

    int position();

    String label() default "";

    boolean showLabel() default false;

    String caption() default "";

    boolean showCaption() default true;

    EnabledType enabled() default ENABLED;

    VisibleType visible() default VISIBLE;

    FontAwesome icon() default FontAwesome.PLUS;

    boolean showIcon() default false;

    String[] styleNames() default {};
}
