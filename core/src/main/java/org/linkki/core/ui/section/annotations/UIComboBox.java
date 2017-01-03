package org.linkki.core.ui.section.annotations;

import static org.linkki.core.ui.section.annotations.EnabledType.ENABLED;
import static org.linkki.core.ui.section.annotations.RequiredType.NOT_REQUIRED;
import static org.linkki.core.ui.section.annotations.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.ui.components.ItemCaptionProvider;
import org.linkki.core.ui.components.ItemCaptionProvider.DefaultCaptionProvider;
import org.linkki.core.ui.section.annotations.adapters.ComboboxBindingDefinition;

/**
 * Creates a combobox with the specified parameters.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBindingDefinition(ComboboxBindingDefinition.class)
public @interface UIComboBox {

    int position();

    String label() default "";

    boolean noLabel() default false;

    /**
     * Specifies the source of the available values, the content of the combo box.
     * 
     * @see AvailableValuesType
     */
    AvailableValuesType content() default AvailableValuesType.ENUM_VALUES_INCL_NULL;

    EnabledType enabled() default ENABLED;

    RequiredType required() default NOT_REQUIRED;

    VisibleType visible() default VISIBLE;

    /**
     * Specifies the width of the field. Use CSS units like em, px or %.
     * <p>
     * For example: "25em" or "100%".
     */
    String width() default "-1px";

    String modelObject() default ModelObject.DEFAULT_NAME;

    String modelAttribute() default "";

    /**
     * Specifies which {@link ItemCaptionProvider} should be used to convert {@link #content()} into
     * String captions.
     * <p>
     * Default value assumes that the value class has a method "getName" and uses this method for
     * the String representation.
     */
    Class<? extends ItemCaptionProvider<?>> itemCaptionProvider() default DefaultCaptionProvider.class;
}
