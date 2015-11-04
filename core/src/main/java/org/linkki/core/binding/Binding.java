/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import org.linkki.core.binding.annotations.BindContext;

import com.vaadin.ui.Component;

/**
 * Common interface for bindings handled by the {@link BindingContext}. A binding has a PMO and an
 * UI control and is able to update this control when the {@link BindContext} calls
 * {@link #updateFromPmo()}.
 *
 */
public interface Binding {

    /** Called by the {@link BindContext} and trigger control updating. */
    void updateFromPmo();

    /**
     * Returns the component that is updated by this binding.
     * 
     * @return The component that updated by this binding
     */
    Component getBoundComponent();

    /**
     * Returns the presentation model object that is bound to the component by this binding.
     * 
     * @return The presentation model object that is bound to the component by this binding
     */
    Object getPmo();

}