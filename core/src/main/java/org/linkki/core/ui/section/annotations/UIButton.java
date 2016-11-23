/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import static org.linkki.core.ui.section.annotations.EnabledType.ENABLED;
import static org.linkki.core.ui.section.annotations.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.ui.section.annotations.adapters.ButtonBindingDefinition;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.server.FontAwesome;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBindingDefinition(ButtonBindingDefinition.class)
public @interface UIButton {
    int position();

    String label() default "";

    boolean showLabel() default false;

    String caption() default "";

    CaptionType captionType() default CaptionType.STATIC;

    /**
     * @deprecated Deprecated, use captionType=CaptionType.STATIC instead. Will be removed in next
     *             version!
     */
    @Deprecated
    boolean showCaption() default true;

    EnabledType enabled() default ENABLED;

    VisibleType visible() default VISIBLE;

    FontAwesome icon() default FontAwesome.PLUS;

    boolean showIcon() default false;

    String[] styleNames() default {};

    /**
     * Set a short cut for the button, use constants in {@link KeyCode}
     */
    int shortcutKeyCode() default -1;

    /**
     * Set a modifier for the short cut. Only useful in combination with a
     * {@link #shortcutKeyCode()}. Use constants from {@link ModifierKey}.
     */
    int[] shortcutModifierKeys() default {};

}
