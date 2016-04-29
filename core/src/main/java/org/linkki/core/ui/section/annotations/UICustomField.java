package org.linkki.core.ui.section.annotations;

import static org.linkki.core.ui.section.annotations.EnabledType.ENABLED;
import static org.linkki.core.ui.section.annotations.RequiredType.NOT_REQUIRED;
import static org.linkki.core.ui.section.annotations.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.vaadin.ui.Field;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UICustomField {

    int position();

    String label() default "";

    boolean noLabel() default false;

    String modelObject() default ModelObject.DEFAULT_NAME;

    String modelAttribute() default "";

    EnabledType enabled() default ENABLED;

    RequiredType required() default NOT_REQUIRED;

    VisibleType visible() default VISIBLE;

    /**
     * Specifies the source of the available values, the content of the custom field if it supports
     * a content. May be a list of selectable items.
     * 
     * @see AvailableValuesType
     */
    AvailableValuesType content() default AvailableValuesType.ENUM_VALUES_INCL_NULL;

    /**
     * The class implementing the custom field. This class has to have a default constructor.
     */
    Class<? extends Field<?>> uiControl();

}
