/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * Holds information about a UI element such as the settings for visibility, enabled-state etc.
 */
public interface ElementDescriptor {

    /** If and how the UI element is enabled. */
    EnabledType enabled();

    /** If and how the UI element is visible. */
    VisibleType visible();

    /** The position of the UI element in its parent/container. */
    int getPosition();

    /** The text for the UI element's label. */
    String getLabelText();

    /** Creates a new Vaadin UI component for this UI element. */
    Component newComponent();

    /**
     * The name of the property that this UI element displays. For an UI element that accesses the
     * field of model/PMO class, this is the name of that field. For an UI element that invokes a
     * method (i.e. a button) this is the name of the method.
     */
    String getPropertyName();

    /**
     * Creates a binding for this UI element to the given Vaadin UI components in the given binding
     * context using the given property dispatcher.
     */
    ElementBinding createBinding(BindingContext bindingContext,
            Object pmo,
            Label label,
            Component component,
            PropertyDispatcher propertyDispatcher);
}
