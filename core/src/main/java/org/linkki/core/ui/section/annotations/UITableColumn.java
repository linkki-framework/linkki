package org.linkki.core.ui.section.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that allows to customize the column for a PMO's field/method that is rendered in a
 * table column.
 * <p>
 * Note that this annotation is <em>not</em> required for a field/method to be rendered in a column.
 * All fields/methods with one of the {@code @UI...} annotations ({@code @UITextField},
 * {@code @UICheckBox} etc.) are rendered automatically. This annotation allows optional
 * customization an can be omitted if no customization is needed.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UITableColumn {

    static final int UNDEFINED_WIDTH = -1;
    static final float UNDEFINED_EXPAND_RATIO = -1.0f;

    /**
     * Configures the width in pixels for the column. This attribute is mutually exclusive with
     * {@link #expandRation()}.
     */
    int width() default UNDEFINED_WIDTH;

    /**
     * Configures the expand ration for the column. This attribute is mutually exclusive with
     * {@link #width()}.
     */
    float expandRation() default UNDEFINED_EXPAND_RATIO;

}
