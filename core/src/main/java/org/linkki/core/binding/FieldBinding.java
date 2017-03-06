package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.ClassUtils;
import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.container.LinkkiInMemoryContainer;
import org.linkki.core.util.MessageListUtil;
import org.linkki.util.handler.Handler;

import com.vaadin.data.util.AbstractProperty;
import com.vaadin.server.AbstractErrorMessage.ContentMode;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.DateField;
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
    private final Handler updateUi;
    private final FieldBindingDataSource<T> propertyDataSource;
    @Nullable
    private final LinkkiInMemoryContainer<T> containerDataSource;

    /**
     * Creates a new {@link FieldBinding}.
     * 
     * @param label the button's label (optional)
     * @param field the {@link Field} to be bound
     * @param propertyDispatcher the {@link PropertyDispatcher} handling the bound property in the
     *            model object
     * @param updateUi a {@link Handler} that is called when this {@link Binding} desires an update
     *            of the UI. Usually declared in {@link BindingContext#updateUI()}.
     */
    public FieldBinding(@Nullable Label label, AbstractField<T> field, PropertyDispatcher propertyDispatcher,
            Handler updateUi) {
        this.label = Optional.ofNullable(label);
        requireNonNull(field, "field must not be null");
        this.field = field;
        requireNonNull(propertyDispatcher, "propertyDispatcher must not be null");
        this.propertyDispatcher = propertyDispatcher;
        requireNonNull(updateUi, "updateUi must not be null");
        this.updateUi = updateUi;

        if (isAvailableValuesComponent()) {
            containerDataSource = new LinkkiInMemoryContainer<T>();
            AbstractSelect abstractSelect = (AbstractSelect)field;
            abstractSelect.setContainerDataSource(containerDataSource);
        } else {
            containerDataSource = null;
        }

        prepareFieldToHandleNullForRequiredFields();

        /*
         * Property data source must be set:
         * 
         * - after container data source, as setContainerDataSource() throws an exception, if field
         * is readonly
         * 
         * - after dispatcher is available, as value and readOnly-state are requested from the data
         * source during 'set', and we need the dispatcher in these methods.
         */
        this.propertyDataSource = new FieldBindingDataSource<T>(this);
        this.field.setPropertyDataSource(propertyDataSource);
    }

    /**
     * LIN-90, LIN-95: if a field is required and the user enters blank into the field, Vaadin does
     * not transfer {@code null} into the data source. This leads to the effect that if the user
     * enters a value, the value is transfered to the model, if the user then enters blank, he sees
     * an empty field but the value in the model is still set to the old value.
     * <p>
     * How do we avoid this? If the field has no converter, we set invalidCommitted to {@code true}.
     * {@code null} is regarded as invalid value, but it is transferable to the model. This does not
     * work for fields with a converter. {@code null} handling is OK for those fields, but if the
     * user enters a value that cannot be converted, Vaadin tries to commit the value to the data
     * source doing so tries to convert it. This leads to an exception (as the value cannot be
     * converted).
     * <p>
     * Example: Enter an invalid number like '123a' into a number field. We can't commit the value
     * as it is invalid and cannot be converted. To get this to work, those fields have to override
     * {@link AbstractField#validate()} to get rid of the unwanted check that leads to a validation
     * exception for {@code null} values in required fields.
     * 
     * @see AbstractField#validate()
     */
    @SuppressWarnings("rawtypes")
    private void prepareFieldToHandleNullForRequiredFields() {
        // note: we prepare the field if it is required or not, as the required state
        // can be changed dynamically.
        boolean commitInvalid = true;
        if (((AbstractField)field).getConverter() != null && !compatibleTypeConverter()) {
            ensureThatFieldsWithAConverterOverrideValidate();
            commitInvalid = false;
        }
        field.setInvalidCommitted(commitInvalid);
    }

    private void ensureThatFieldsWithAConverterOverrideValidate() {
        Method validateMethod;
        try {
            validateMethod = field.getClass().getMethod("validate");
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
        if (validateMethod.getDeclaringClass().getName().startsWith("com.vaadin")) {
            throw new IllegalStateException(
                    "A field that has a converter must override validate() to disable Vaadin's required field handling! "
                            + " See FieldBinding.prepareFieldToHandleNullForRequiredFields for the explanation");
        }
    }

    /**
     * Some fields could have converters because they will never throw a conversion exception.
     * <ul>
     * <li>DateField only converts from Date to LocalDate (compatible data type)</li>
     * </ul>
     * 
     * @return
     */
    private boolean compatibleTypeConverter() {
        return field instanceof DateField;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getValueClass() {
        return (Class<T>)propertyDispatcher.getValueClass();
    }

    @Override
    public PropertyDispatcher getPropertyDispatcher() {
        return propertyDispatcher;
    }

    @Override
    public void updateFromPmo() {
        try {
            // Value and ReadOnly-state are provided by the field binding using the
            // propertyDataSource. The update is triggered by firing change events.
            propertyDataSource.fireValueChange();
            propertyDataSource.fireReadOnlyStatusChange();

            field.setRequired(isRequired());
            field.setEnabled(isEnabled());
            String toolTip = propertyDispatcher.getToolTip();
            field.setDescription(toolTip);
            label.ifPresent(l -> l.setDescription(toolTip));
            boolean visible = isVisible();
            field.setVisible(visible);
            label.ifPresent(l -> l.setVisible(visible));
            if (isAvailableValuesComponent()) {
                // yes, it is set if isAvailableValuesComponent() is true, but that is unknown to
                // eclipse' checker (and could be false in subclasses)
                if (containerDataSource != null) {
                    updateAvailableValues(containerDataSource);
                }
            }
            // CSOFF: IllegalCatch
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while updating field " + field.getClass() + ", value property="
                    + propertyDispatcher.getProperty(), e);
        }
        // CSON: IllegalCatch
    }

    private void updateAvailableValues(LinkkiInMemoryContainer<T> container) {
        Collection<T> availableValues = getAvailableValues();
        container.removeAllItems();
        container.addAllItems(availableValues);
    }

    private boolean isAvailableValuesComponent() {
        return field instanceof AbstractSelect;
    }

    @SuppressWarnings("unchecked")
    @CheckForNull
    public T getValue() {
        return (T)getPropertyDispatcher().getValue();
    }

    public void setValue(
            @Nullable T newValue) {
        getPropertyDispatcher().setValue(newValue);
        updateUi.apply();
    }

    public boolean isEnabled() {
        return propertyDispatcher.isEnabled();
    }

    public boolean isRequired() {
        return propertyDispatcher.isRequired();
    }

    public boolean isVisible() {
        return propertyDispatcher.isVisible();
    }

    public boolean isReadOnly() {
        return propertyDispatcher.isReadOnly();
    }

    private String formatMessages(MessageList messages) {
        return StreamSupport.stream(messages.spliterator(), false).map(Message::getText)
                .collect(Collectors.joining("\n"));
    }

    public Field<T> getField() {
        return field;
    }

    /**
     * Returns the "list" of allowed values for the field.
     */
    @SuppressWarnings("unchecked")
    public Collection<T> getAvailableValues() {
        return (Collection<T>)propertyDispatcher.getAvailableValues();
    }

    /**
     * {@inheritDoc}
     * 
     * @deprecated Damn vaadin has deprecated the toString method :( Also set deprecated to avoid
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
        Message fatalErrorMessage = messages.getMessageByCode(ValidationService.FATAL_ERROR_MESSAGE_CODE);
        if (fatalErrorMessage != null) {
            messagesForProperty.add(fatalErrorMessage);
        }
    }

    @CheckForNull
    private UserError getErrorHandler(MessageList messages) {
        if (messages.isEmpty()) {
            return null;
        } else {
            return new UserError(formatMessages(messages), ContentMode.PREFORMATTED,
                    MessageListUtil.getErrorLevel(messages));
        }
    }

    private static final class FieldBindingDataSource<T> extends AbstractProperty<T> {

        private static final long serialVersionUID = 1L;
        private FieldBinding<T> fieldBinding;

        public FieldBindingDataSource(FieldBinding<T> fieldBinding) {
            this.fieldBinding = fieldBinding;
        }

        @Override
        @CheckForNull
        public T getValue() {
            return fieldBinding.getValue();
        }

        @Override
        public void setValue(@Nullable T newValue) throws com.vaadin.data.Property.ReadOnlyException {
            fieldBinding.setValue(newValue);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Class<? extends T> getType() {
            return (Class<? extends T>)ClassUtils.primitiveToWrapper(fieldBinding.getValueClass());
        }

        /*
         * Override for visibility in FieldBinding
         */
        @Override
        protected void fireValueChange() {
            super.fireValueChange();
        }

        /*
         * Override for visibility in FieldBinding
         */
        @Override
        protected void fireReadOnlyStatusChange() {
            super.fireReadOnlyStatusChange();
        }

        @Override
        public boolean isReadOnly() {
            return fieldBinding.isReadOnly();
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            throw new UnsupportedOperationException();
        }

    }

}