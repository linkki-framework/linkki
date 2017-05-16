/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations.adapters;

import org.linkki.core.ui.section.annotations.CaptionType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UIButtonDefinition;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.ComponentFactory;

import com.vaadin.ui.Button;

/**
 * The adapter to provide access to an {@link UIButton} annotation through the definition interface.
 */
public class ButtonBindingDefinition implements UIButtonDefinition {

    private final UIButton buttonAnnotation;

    public ButtonBindingDefinition(UIButton annotation) {
        this.buttonAnnotation = annotation;
    }

    @Override
    public Button newComponent() {
        Button button = ComponentFactory.newButton();
        if (buttonAnnotation.showIcon()) {
            button.setIcon(buttonAnnotation.icon());
        }
        for (String styleName : buttonAnnotation.styleNames()) {
            button.addStyleName(styleName);
        }
        if (buttonAnnotation.shortcutKeyCode() != -1) {
            button.setClickShortcut(buttonAnnotation.shortcutKeyCode(), buttonAnnotation.shortcutModifierKeys());
        }
        return button;
    }

    @Override
    public int position() {
        return buttonAnnotation.position();
    }

    @Override
    public String label() {
        return buttonAnnotation.label();
    }

    @Override
    public boolean showLabel() {
        return buttonAnnotation.showLabel();
    }

    @Override
    public EnabledType enabled() {
        return buttonAnnotation.enabled();
    }

    @Override
    public VisibleType visible() {
        return buttonAnnotation.visible();
    }

    @Override
    public RequiredType required() {
        return RequiredType.NOT_REQUIRED;
    }

    @Override
    public CaptionType captionType() {
        return buttonAnnotation.captionType();
    }

    @Override
    public String caption() {
        return buttonAnnotation.caption();
    }

}
