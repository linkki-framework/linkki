package org.linkki.core.binding;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.util.MessageListUtil;
import org.linkki.core.util.MessageUtil;
import org.vaadin.viritin.ListContainer;

import com.google.gwt.thirdparty.guava.common.collect.Iterables;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
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

    private final BindingContext bindingContext;
    private final Field<T> field;
    private final Label label;
    private final Object pmo;
    private final String propertyName;
    private final PropertyDispatcher propertyDispatcher;
    private final FieldBindingDataSource<T> propertyDataSource;

    private final ListContainer<T> containerDataSource;

    public FieldBinding(BindingContext bindingContext, Object pmo, String propertyName, Label label, Field<T> field,
            PropertyDispatcher propertyDispatcher) {

        this.bindingContext = checkNotNull(bindingContext);
        this.pmo = checkNotNull(pmo);
        this.propertyName = checkNotNull(propertyName);
        this.label = label;
        this.propertyDispatcher = checkNotNull(propertyDispatcher);
        this.field = checkNotNull(field);

        if (isAvailableValuesComponent()) {
            containerDataSource = new ListContainer<T>(getValueClass());
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
     * LIN-90, LIN-95: if a field is required and the user enters blank into the field, VAADIN does
     * not transfer NULL into the data source. This leads to the effect that if the user enters a
     * value, the value is transfered to the model, if the user then enters blank, he sees an empty
     * field but the value in the model is still set to the old value. How do we avoid this? If the
     * field has no converter, we set invalidCommitted to TRUE. NULL is regarded as invalid value,
     * but it is transferable to the model. This does not work for fields with a converter. NULL
     * handling is ok for those fields, but if the user enters a none convertable value, VADDIN
     * tries to commit the value to the data source, but it has to be converted, which leads to an
     * exception. Example: Enter an invalid number like '123a' into a number field. So in these
     * cases we can't enter commit the value if they are invalid. To get this to work, those fields
     * have to override {@link }AbstractField#validate()} to get rid of the unwanted check that in
     * required fields the value <code>null</code> leads to a validation exception.
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
        return (Class<T>)propertyDispatcher.getValueClass(propertyName);
    }

    @Override
    public PropertyDispatcher getPropertyDispatcher() {
        return propertyDispatcher;
    }

    public String getPropertyName() {
        return propertyName;
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
            boolean visible = isVisible();
            field.setVisible(visible);
            if (label != null) {
                // label is null in case of a table
                label.setVisible(visible);
            }
            if (isAvailableValuesComponent()) {
                Collection<T> availableValues = getAvailableValues();
                containerDataSource.setCollection(availableValues);
            }
            // CSOFF: IllegalCatch
        } catch (RuntimeException e) {
            throw new RuntimeException(
                    "Error while updating field " + field.getClass() + ", value property=" + propertyName, e);
        }
        // CSON: IllegalCatch
    }

    private boolean isAvailableValuesComponent() {
        return field instanceof AbstractSelect;
    }

    @SuppressWarnings("unchecked")
    public T getValue() {
        return (T)getPropertyDispatcher().getValue(getPropertyName());
    }

    public void setValue(T newValue) {
        getPropertyDispatcher().setValue(getPropertyName(), newValue);
        bindingContext.updateUI();
    }

    public boolean isEnabled() {
        return propertyDispatcher.isEnabled(getPropertyName());
    }

    public boolean isRequired() {
        return propertyDispatcher.isRequired(getPropertyName());
    }

    public boolean isVisible() {
        return propertyDispatcher.isVisible(getPropertyName());
    }

    public boolean isReadOnly() {
        return propertyDispatcher.isReadonly(getPropertyName());
    }

    private String formatMessages(MessageList messages) {
        List<Message> messagesAsList = Lists.newArrayList(messages);
        return messagesAsList.stream().map(m -> m.getText()).collect(Collectors.joining("\n"));
    }

    public Field<T> getField() {
        return field;
    }

    /**
     * Returns the "list" of allowed values for the field.
     */
    @SuppressWarnings("unchecked")
    public Collection<T> getAvailableValues() {
        return (Collection<T>)propertyDispatcher.getAvailableValues(getPropertyName());
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
        return "FieldBinding [propertyName=" + propertyName + ", pmo=" + pmo + ", field=" + field + ", bindingContext="
                + bindingContext.getName() + ", propertyDispatcher=" + propertyDispatcher + "]";
    }

    /**
     * Creates a field binding and add the created binding to the given {@link BindingContext}
     * 
     * @param bindingContext {@link BindingContext} to create the binding and add the created
     *            binding to t
     * @param propertyName The name of the bound property
     * @param label the label for the bound field
     * @param field the field that needs to be bound
     * @param propertyDispatcher The {@link PropertyDispatcher} to get the bound values from
     * @return Returns the newly created field binding
     */
    public static <T> FieldBinding<T> create(BindingContext bindingContext,
            Object pmo,
            String propertyName,
            Label label,
            Field<T> field,
            PropertyDispatcher propertyDispatcher) {
        FieldBinding<T> fieldBinding = new FieldBinding<T>(bindingContext, pmo, propertyName, label, field,
                propertyDispatcher);
        bindingContext.add(fieldBinding);
        return fieldBinding;
    }

    @Override
    public Field<T> getBoundComponent() {
        return field;
    }

    @Override
    public Object getPmo() {
        return pmo;
    }

    @Override
    public MessageList displayMessages(MessageList messages) {
        MessageList messagesForProperty = getRelevantMessages(messages);
        if (field instanceof AbstractField) {
            ((AbstractField<T>)field).setComponentError(getErrorHandler(messagesForProperty));
        }
        return messagesForProperty;
    }

    private MessageList getRelevantMessages(MessageList messages) {
        MessageList messagesForProperty = propertyDispatcher.getMessages(messages, propertyName);
        removeMandatoryFieldMessages(messagesForProperty);
        addFatalError(messages, messagesForProperty);
        return messagesForProperty;
    }

    private void removeMandatoryFieldMessages(MessageList ml) {
        Iterables.removeIf(ml, MessageUtil::isMandatoryFieldMessage);
    }

    private void addFatalError(MessageList messages, MessageList messagesForProperty) {
        Message fatalErrorMessage = messages.getMessageByCode(ValidationService.FATAL_ERROR_MESSAGE_CODE);
        if (fatalErrorMessage != null) {
            messagesForProperty.add(fatalErrorMessage);
        }
    }

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
        public T getValue() {
            return fieldBinding.getValue();
        }

        @Override
        public void setValue(T newValue) throws com.vaadin.data.Property.ReadOnlyException {
            fieldBinding.setValue(newValue);
        }

        @Override
        public Class<? extends T> getType() {
            return fieldBinding.getValueClass();
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