/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import org.faktorips.runtime.MessageList;

import com.vaadin.ui.Component;

/**
 * Common interface for bindings handled by the {@link BindingContext}. A binding has a PMO and an
 * UI control and is able to update this control when the {@link BindingContext} calls
 * {@link #updateFromPmo()}.
 *
 */
public interface Binding {

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

    /**
     * Called by the {@link BindingContext} and trigger control updating. This includes the update
     * of the value, the states (read-only, enabled, visible) and if supported the list of available
     * values.
     * 
     * This method does not update the validation message.
     * 
     * @see #displayMessages(MessageList)
     */
    void updateFromPmo();

    /**
     * Retrieves those messages from the given list that are relevant for this binding and displays
     * them directly at the bound component. An error message will mark the component property.
     * 
     * @param messages a list of messages
     * @return those messages from the given list that are displayed; an empty list if no messages
     *         are displayed.
     */
    MessageList displayMessages(MessageList messages);

}