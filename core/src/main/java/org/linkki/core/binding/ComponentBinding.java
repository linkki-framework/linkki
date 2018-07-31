/*
 * Copyright Faktor Zehn AG.
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

import javax.annotation.Nullable;

import org.linkki.core.binding.aspect.AspectUpdaters;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.message.MessageList;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.util.handler.Handler;

/**
 * A binding for a single component to a property represented by a {@link PropertyDispatcher}. The
 * binding is responsible to update all aspects that are defined by {@link LinkkiAspectDefinition}.
 */
public class ComponentBinding implements ElementBinding {

    private final ComponentWrapper componentWrapper;
    private final PropertyDispatcher propertyDispatcher;
    private AspectUpdaters aspectUpdaters;

    /**
     * Creates a new {@link ComponentBinding}.
     * 
     * @param componentWrapper a wrapper for the component that should be bound
     * @param propertyDispatcher the {@link PropertyDispatcher} handling the bound property in the model
     *            object
     * @param modelChanged a {@link Handler} that is called when this {@link Binding} desires an update
     *            of the UI because the model has changed. Usually declared in
     *            {@link BindingContext#modelChanged()}.
     */
    public ComponentBinding(ComponentWrapper componentWrapper, PropertyDispatcher propertyDispatcher,
            Handler modelChanged, List<LinkkiAspectDefinition> aspectDefinitions) {
        this.componentWrapper = requireNonNull(componentWrapper, "componentWrapper must not be null");
        this.propertyDispatcher = requireNonNull(propertyDispatcher, "propertyDispatcher must not be null");

        aspectUpdaters = new AspectUpdaters(aspectDefinitions, propertyDispatcher,
                componentWrapper, modelChanged);
    }

    @Override
    public PropertyDispatcher getPropertyDispatcher() {
        return propertyDispatcher;
    }

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

    @Override
    public Object getBoundComponent() {
        return componentWrapper.getComponent();
    }

    @Override
    public MessageList displayMessages(@Nullable MessageList messages) {
        MessageList messagesForProperty = getRelevantMessages(messages != null ? messages : new MessageList());
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
        return "ComponentBinding: " + componentWrapper + " <=> " + propertyDispatcher;
    }

}