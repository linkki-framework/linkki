package org.linkki.core.binding.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.VisibleType;

/**
 * @author ortmann
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
public @interface Bind {

    /** The name of the PMO's property to which the UI element is bound. */
    String pmoProperty();

    /** If and how the enabled state of the UI element is bound to the PMO. */
    EnabledType enabled() default EnabledType.ENABLED;

    /** If and how the visible state of the UI element is bound to the PMO. */
    VisibleType visible() default VisibleType.VISIBLE;

    /**
     * If and how the required state of the UI element is bound to the PMO. Ignored for UI elements
     * that cannot be required, e.g. buttons.
     */
    RequiredType required() default RequiredType.NOT_REQUIRED;

    /**
     * If and how the available values are bound to the PMO. Relevant only for UI elements that have
     * available values (e.g. combo boxes), ignored for all other elements.
     */
    AvailableValuesType availableValues() default AvailableValuesType.NO_VALUES;

}
