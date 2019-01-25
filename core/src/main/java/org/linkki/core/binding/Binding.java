/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.linkki.core.binding.aspect.AspectUpdaters;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.message.MessageList;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.util.handler.Handler;

/**
 * A binding for a single component to a property represented by a {@link PropertyDispatcher}. The
 * binding is responsible to update all aspects that are defined by {@link LinkkiAspectDefinition
 * LinkkiAspectDefinitions}.
 * <p>
 * A binding can contain other bindings, working as {@link BindingContext} for them.
 */
public class Binding<@NonNull C> extends BindingContext {

    private final Handler modelChanged;
    private final ComponentWrapper componentWrapper;
    private final PropertyDispatcher propertyDispatcher;
    private final AspectUpdaters aspectUpdaters;

    /**
     * Creates a new {@link Binding}.
     * 
     * @param componentWrapper a wrapper for the component that should be bound
     * @param propertyDispatcher the {@link PropertyDispatcher} handling the bound property in the model
     *            object
     * @param modelChanged a {@link Handler} that is called when this {@link Binding} desires an update
     *            of the UI because the model has changed. Usually declared in
     *            {@link BindingContext#modelChanged()}.
     * @param behaviorProvider the {@link PropertyBehaviorProvider} that were configured for the parent
     *            {@link BindingContext}. The behaviors are only applied if this binding behaves as a
     *            {@link BindingContext} that means it is only applied for its children.
     */
    public Binding(ComponentWrapper componentWrapper, PropertyDispatcher propertyDispatcher,
            Handler modelChanged, List<LinkkiAspectDefinition> aspectDefinitions,
            PropertyBehaviorProvider behaviorProvider) {
        super("subcontext: " + componentWrapper, behaviorProvider, Handler.NOP_HANDLER);
        this.modelChanged = requireNonNull(modelChanged, "modelChanged must not be null");
        this.componentWrapper = requireNonNull(componentWrapper, "componentWrapper must not be null");
        this.propertyDispatcher = requireNonNull(propertyDispatcher, "propertyDispatcher must not be null");

        aspectUpdaters = new AspectUpdaters(aspectDefinitions, propertyDispatcher, componentWrapper,
                this::modelChanged);
    }

    public PropertyDispatcher getPropertyDispatcher() {
        return propertyDispatcher;
    }

    @Override
    public void modelChanged() {
        modelChanged.apply();
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
        // first: own aspects
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
        // second: aspects of child bindings
        super.updateFromPmo();
    }

    /**
     * Returns the component that is updated by this binding.
     * 
     * @return The component that updated by this binding
     */
    public C getBoundComponent() {
        @SuppressWarnings("unchecked")
        @NonNull
        C component = (@NonNull C)componentWrapper.getComponent();
        return component;
    }

    /**
     * Returns the presentation model object that is bound to the component by this binding.
     * 
     * @return The presentation model object that is bound to the component by this binding
     */
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