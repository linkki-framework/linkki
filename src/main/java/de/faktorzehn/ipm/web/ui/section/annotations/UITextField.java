package de.faktorzehn.ipm.web.ui.section.annotations;

import static de.faktorzehn.ipm.web.ui.section.annotations.EnabledType.ENABLED;
import static de.faktorzehn.ipm.web.ui.section.annotations.RequiredType.NOT_REQUIRED;
import static de.faktorzehn.ipm.web.ui.section.annotations.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UITextField {

    int position();

    String label() default "";

    Class<?> modelClass() default NoModelClassProvided.class;

    String modelAttribute() default "";

    EnabledType enabled() default ENABLED;

    RequiredType required() default NOT_REQUIRED;

    VisibleType visible() default VISIBLE;

    int columns() default 0;

    int maxLength() default 0;

}
