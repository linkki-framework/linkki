package de.faktorzehn.ipm.web.binding;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.vaadin.data.Property;
import com.vaadin.server.AbstractErrorMessage.ContentMode;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;
import de.faktorzehn.ipm.web.ui.util.UiUtil;

/**
 * A binding for a single Vaadin field to properties of a presentation model object. The binding
 * binds the value shown in the field to a property providing the value. It also binds other field
 * properties like enabled required.
 */
public class FieldBinding<T> implements Property<T>, ElementBinding {

    private static final long serialVersionUID = 1L;

    private final BindingContext bindingContext;
    private final Field<T> field;
    private final Label label;

    private final String propertyName;
    private final PropertyDispatcher propertyDispatcher;

    public FieldBinding(BindingContext bindingContext, String propertyName, Label label, Field<T> field,
            PropertyDispatcher propertyDispatcher) {
        checkNotNull(bindingContext);
        checkNotNull(propertyName);
        checkNotNull(field);
        checkNotNull(propertyDispatcher);
        this.bindingContext = bindingContext;
        this.propertyName = propertyName;
        this.label = label;
        this.field = field;
        this.propertyDispatcher = propertyDispatcher;
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
            // TODO see FIPM-57
            field.setPropertyDataSource(null);
            field.setPropertyDataSource(this);

            field.setRequired(isRequired());
            if (isRequired() && field instanceof AbstractSelect) {
                ((AbstractSelect)field).setNullSelectionAllowed(false);
            }
            field.setReadOnly(isReadOnly());
            field.setEnabled(isEnabled());
            boolean visible = isVisible();
            field.setVisible(visible);
            if (label != null) {
                // label is null in case of a table
                label.setVisible(visible);
            }
            if (field instanceof AbstractSelect) {
                AbstractSelect select = (AbstractSelect)field;
                UiUtil.fillSelectWithItems(select, getAvailableValues());
            }
            if (field instanceof AbstractField) {
                ((AbstractField<T>)field).setComponentError(getErrorHandler());
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while updating field " + field.getClass() + ", value property="
                    + propertyName, e);
        }
    }

    private UserError getErrorHandler() {
        MessageList messages = propertyDispatcher.getMessages(propertyName);
        if (messages.isEmpty()) {
            return null;
        } else {
            return new UserError(formatMessages(messages), ContentMode.PREFORMATTED,
                    MessageListUtil.getErrorLevel(messages));
        }
    }

    private String formatMessages(MessageList messages) {
        List<Message> messagesAsList = Lists.newArrayList(messages);
        return messagesAsList.stream().map(m -> m.getText()).collect(Collectors.joining("\n"));
    }

    public Field<T> getField() {
        return field;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getValue() {
        return (T)propertyDispatcher.getValue(getPropertyName());
    }

    @Override
    public void setValue(T newValue) {
        pushValueToPmo(newValue);
        bindingContext.updateUI();
    }

    private void pushValueToPmo(T newValue) {
        propertyDispatcher.setValue(getPropertyName(), newValue);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends T> getType() {
        return (Class<? extends T>)propertyDispatcher.getValueClass(getPropertyName());
    }

    public boolean isEnabled() {
        return propertyDispatcher.isEnabled(getPropertyName());
    }

    @Override
    public boolean isReadOnly() {
        return propertyDispatcher.isReadonly(getPropertyName());
    }

    public boolean isRequired() {
        return propertyDispatcher.isRequired(getPropertyName());
    }

    public boolean isVisible() {
        return propertyDispatcher.isVisible(getPropertyName());
    }

    @Override
    public void setReadOnly(boolean newStatus) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the "list" of allowed values for the field.
     */
    public Collection<?> getAvailableValues() {
        return propertyDispatcher.getAvailableValues(getPropertyName());
    }

    @Override
    public String toString() {
        return "FieldBinding [bindingContext=" + bindingContext.getName() + ", field=" + field
                + ", propertyDispatcher=" + propertyDispatcher + " propertyName=" + propertyName;
    }

    public static <T> FieldBinding<T> create(BindingContext bindingContext,
            String propertyName,
            Label label,
            Field<T> field,
            PropertyDispatcher propertyDispatcher) {
        return new FieldBinding<T>(bindingContext, propertyName, label, field, propertyDispatcher);
    }

    @Override
    public Field<T> getBoundComponent() {
        return field;
    }
}