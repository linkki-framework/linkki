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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.linkki.core.binding.aspect.AspectUpdaters;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.message.Message;
import org.linkki.core.message.MessageList;
import org.linkki.core.ui.components.LabelComponentWrapper;
import org.linkki.util.handler.Handler;

import com.vaadin.server.AbstractErrorMessage.ContentMode;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

/**
 * A binding for a single Vaadin field to properties of a presentation model object. The binding
 * binds the value shown in the field to a property providing the value. It also binds other field
 * properties like enabled / required etc.
 */
public class FieldBinding<T> implements ElementBinding {

    private final AbstractField<T> field;
    private final Optional<Label> label;
    private final PropertyDispatcher propertyDispatcher;
    private AspectUpdaters aspectUpdaters;

    /**
     * Creates a new {@link FieldBinding}.
     * 
     * @param label the button's label (optional)
     * @param field the {@link Field} to be bound
     * @param propertyDispatcher the {@link PropertyDispatcher} handling the bound property in the
     *            model object
     * @param modelChanged a {@link Handler} that is called when this {@link Binding} desires an
     *            update of the UI because the model has changed. Usually declared in
     *            {@link BindingContext#updateUI()}.
     */
    public FieldBinding(@Nullable Label label, AbstractField<T> field, PropertyDispatcher propertyDispatcher,
            Handler modelChanged, List<LinkkiAspectDefinition> aspectDefinitions) {
        this.label = Optional.ofNullable(label);
        this.field = requireNonNull(field, "field must not be null");
        this.propertyDispatcher = requireNonNull(propertyDispatcher, "propertyDispatcher must not be null");

        aspectUpdaters = new AspectUpdaters(aspectDefinitions, propertyDispatcher,
                new LabelComponentWrapper(label, field),
                modelChanged);
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
            throw new RuntimeException("Error while updating field " + field.getClass() + ", value property="
                    + propertyDispatcher.getProperty(), e);
        }
        // CSON: IllegalCatch
    }

    private String formatMessages(MessageList messages) {
        return StreamSupport.stream(messages.spliterator(), false)
                .map(Message::getText)
                .collect(Collectors.joining("\n"));
    }

    public Field<T> getField() {
        return field;
    }

    /**
     * {@inheritDoc}
     * 
     * @deprecated Damn Vaadin has deprecated the toString method :( Also set deprecated to avoid
     *             warnings
     */
    @Deprecated
    @Override
    public String toString() {
        return "FieldBinding [field=" + field + ", label=" + label + ", propertyDispatcher=" + propertyDispatcher + "]";
    }

    @Override
    public Field<T> getBoundComponent() {
        return field;
    }

    @Override
    public MessageList displayMessages(@Nullable MessageList messages) {
        MessageList messagesForProperty = getRelevantMessages(messages != null ? messages : new MessageList());
        field.setComponentError(getErrorHandler(messagesForProperty));
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

    @CheckForNull
    private UserError getErrorHandler(MessageList messages) {
        return messages.getErrorLevel()
                .map(e -> new UserError(formatMessages(messages), ContentMode.PREFORMATTED, e))
                .orElse(null);
    }
}