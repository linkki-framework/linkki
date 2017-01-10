/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import com.vaadin.ui.Component;

/**
 * Holds information about a bound UI element (such as the settings for visibility, enabled-state
 * etc.) and on how to create and display such an UI element.
 */
public abstract class ElementDescriptor extends BindingDescriptor {

    public ElementDescriptor(BindingDefinition bindingDefinition, UIToolTipDefinition toolTipDefinition) {
        super(bindingDefinition, toolTipDefinition);
    }

    @Override
    protected UIElementDefinition getBindingDefinition() {
        return (UIElementDefinition)super.getBindingDefinition();
    }

    /** The position of the UI element in its parent/container. */
    public int getPosition() {
        return getBindingDefinition().position();
    }

    /** The text for the UI element's label. */
    public abstract String getLabelText();

    /** Creates a new Vaadin UI component for this UI element. */
    public Component newComponent() {
        return getBindingDefinition().newComponent();
    }

}
