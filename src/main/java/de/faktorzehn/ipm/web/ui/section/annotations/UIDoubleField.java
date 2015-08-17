package de.faktorzehn.ipm.web.ui.section.annotations;

import static de.faktorzehn.ipm.web.ui.section.annotations.EnabledType.ENABLED;
import static de.faktorzehn.ipm.web.ui.section.annotations.RequiredType.NOT_REQUIRED;
import static de.faktorzehn.ipm.web.ui.section.annotations.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.DecimalFormat;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UIDoubleField {

    int position();

    String label() default "";

    EnabledType enabled() default ENABLED;

    RequiredType required() default NOT_REQUIRED;

    VisibleType visible() default VISIBLE;

    int maxLength() default 0;

    /**
     * Format for the ui representation of the value. See {@link DecimalFormat} for the
     * documentation of the pattern
     */
    String format() default "";

    Class<?> modelClass() default NoModelClassProvided.class;

    String modelAttribute() default "";

}
