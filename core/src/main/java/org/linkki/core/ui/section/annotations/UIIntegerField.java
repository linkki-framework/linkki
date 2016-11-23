package org.linkki.core.ui.section.annotations;

import static org.linkki.core.ui.section.annotations.EnabledType.ENABLED;
import static org.linkki.core.ui.section.annotations.RequiredType.NOT_REQUIRED;
import static org.linkki.core.ui.section.annotations.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.ui.section.annotations.adapters.IntegerFieldBindingDefinition;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBindingDefinition(IntegerFieldBindingDefinition.class)
public @interface UIIntegerField {

    int position();

    String label() default "";

    boolean noLabel() default false;

    EnabledType enabled() default ENABLED;

    RequiredType required() default NOT_REQUIRED;

    VisibleType visible() default VISIBLE;

    int maxLength() default 0;

    String format() default "";

    String modelObject() default ModelObject.DEFAULT_NAME;

    String modelAttribute() default "";

}
