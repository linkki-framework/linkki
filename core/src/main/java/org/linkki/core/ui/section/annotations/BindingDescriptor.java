/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import javax.annotation.Nonnull;

import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * Holds information about a bound UI element such as the settings for visibility, enabled-state
 * etc.
 */
public interface BindingDescriptor {

    /** If and how the bound UI element is enabled. */
    EnabledType enabled();

    /** If and how the bound UI element is visible. */
    VisibleType visible();

    /** If and how the bound UI element is required. */
    RequiredType required();

    /** If and how available values can be obtained. */
    AvailableValuesType availableValues();

    /**
     * The name of the property that the bound UI element displays. For an UI element that accesses
     * the field of a model/PMO class, this is the name of that field. For an UI element that
     * invokes a method (i.e. a button) this is the name of the method.
     */
    String getPropertyName();

    /**
     * The name of the model object containing the {@link #getPropertyName() property} if the bound
     * PMO itself does not contain the property. The PMO has to have a
     * {@link ModelObject @ModelObject} annotation with that name on the method that returns the
     * model object.
     */
    String getModelObjectName();

    /**
     * Creates a binding with the given dispatcher, the given handler for updating the UI and the
     * given UI components using the binding information from this descriptor.
     */
    ElementBinding createBinding(@Nonnull PropertyDispatcher propertyDispatcher,
            @Nonnull Handler updateUi,
            @Nonnull Component component,
            Label label);

}
