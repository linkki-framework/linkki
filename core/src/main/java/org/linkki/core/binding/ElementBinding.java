/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import java.util.List;

import org.linkki.core.binding.aspect.AspectUpdaters;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.message.MessageList;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.util.handler.Handler;

/**
 * A binding for a single component to a property represented by a {@link PropertyDispatcher}. The
 * binding is responsible to update all aspects that are defined by {@link LinkkiAspectDefinition
 * LinkkiAspectDefinitions}.
 */
public class ElementBinding implements Binding {

    private final ComponentWrapper componentWrapper;
    private final PropertyDispatcher propertyDispatcher;
    private final AspectUpdaters aspectUpdaters;

    /**
     * Creates a new {@link ElementBinding}.
     * 
     * @param componentWrapper a wrapper for the component that should be bound
     * @param propertyDispatcher the {@link PropertyDispatcher} handling the bound property in the model
     *            object
     * @param modelChanged a {@link Handler} that is called when this {@link ElementBinding} desires an
     *            update of the UI because the model has changed. Usually declared in
     *            {@link BindingContext#modelChanged()}.
     */
    public ElementBinding(ComponentWrapper componentWrapper, PropertyDispatcher propertyDispatcher,
            Handler modelChanged, List<LinkkiAspectDefinition> aspectDefinitions) {
        requireNonNull(modelChanged, "modelChanged must not be null");
        this.componentWrapper = requireNonNull(componentWrapper, "componentWrapper must not be null");
        this.propertyDispatcher = requireNonNull(propertyDispatcher, "propertyDispatcher must not be null");

        aspectUpdaters = new AspectUpdaters(aspectDefinitions, propertyDispatcher, componentWrapper,
                modelChanged);
    }

    public PropertyDispatcher getPropertyDispatcher() {
        return propertyDispatcher;
    }

    /**
     * Called by the {@link BindingContext} and trigger control updating. This includes the update of
     * the value, the states (read-only, enabled, visible) and if supported the list of available
     * values.
     * 
     * This method does not update the validation message.
     * 
     * @see #displayMessages(MessageList)
     */
    @Override
    public void updateFromPmo() {
        try {
            aspectUpdaters.updateUI();
            // CSOFF: IllegalCatch
        } catch (RuntimeException e) {
            throw new LinkkiBindingException(
                    "Error while updating UI (" + e.getMessage() + ") in "
                            + toString(),
                    e);
        }
        // CSON: IllegalCatch
    }

    /**
     * Returns the component that is updated by this binding.
     * 
     * @return The component that updated by this binding
     */
    @Override
    public Object getBoundComponent() {
        return componentWrapper.getComponent();
    }

    /**
     * Returns the presentation model object that is bound to the component by this binding.
     * 
     * @return The presentation model object that is bound to the component by this binding
     */
    @Override
    public Object getPmo() {
        return requireNonNull(getPropertyDispatcher().getBoundObject());
    }

    /**
     * Retrieves those messages from the given list that are relevant for this binding and displays them
     * directly at the bound component. An error message will mark the component property.
     * 
     * @param messages a list of messages
     * @return those messages from the given list that are displayed; an empty list if no messages are
     *         displayed.
     */
    @Override
    public MessageList displayMessages(MessageList messages) {
        MessageList messagesForProperty = getRelevantMessages(messages);
        componentWrapper.setValidationMessages(messagesForProperty);
        return messagesForProperty;
    }

    private MessageList getRelevantMessages(MessageList messages) {
        MessageList messagesForProperty = propertyDispatcher.getMessages(messages);
        addFatalError(messages, messagesForProperty);
        return messagesForProperty;
    }

    private void addFatalError(MessageList messages, MessageList messagesForProperty) {
        messages.getMessageByCode(ValidationService.FATAL_ERROR_MESSAGE_CODE)
                .ifPresent(messagesForProperty::add);
    }

    @Override
    public String toString() {
        return "Binding: " + componentWrapper + " <=> " + propertyDispatcher;
    }

}