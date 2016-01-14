/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.faktorips.runtime.MessageList;

/**
 * {@link PropertyDispatcher} that throws exception on every method call except
 * {@link #isReadonly(String)} which returns <code>true</code> and
 * {@link #getMessages(MessageList, String)}, which returns an empty {@link MessageList}.
 *
 * Serves as a last resort fallback to simplify exception creation in other dispatchers.
 *
 * @author widmaier
 */
public final class ExceptionPropertyDispatcher implements PropertyDispatcher {

    private final List<Object> objects = new ArrayList<>();

    /**
     * @param objects used for error messages
     */
    public ExceptionPropertyDispatcher(Object... objects) {
        this.objects.addAll(Arrays.asList(objects));
    }

    @Override
    public void prepareUpdateUI() {
        // do nothing
    }

    @Override
    public Class<?> getValueClass(String property) {
        throw new IllegalArgumentException(getExceptionText(property, "find getter method for"));
    }

    private String getExceptionText(String property, String action) {
        return MessageFormat.format("Cannot {0} property \"{1}\" in any of {2}", action, property, objects);
    }

    @Override
    public Object getValue(String property) {
        throw new IllegalArgumentException(getExceptionText(property, "read"));
    }

    @Override
    public void setValue(String property, Object value) {
        throw new IllegalArgumentException(getExceptionText(property, "write"));
    }

    @Override
    public boolean isReadonly(String property) {
        return true;
    }

    @Override
    public boolean isEnabled(String property) {
        throw new IllegalArgumentException(getExceptionText(property, "get enabled state for"));
    }

    @Override
    public boolean isVisible(String property) {
        throw new IllegalArgumentException(getExceptionText(property, "get visibility for"));
    }

    @Override
    public boolean isRequired(String property) {
        throw new IllegalArgumentException(getExceptionText(property, "get required state for"));
    }

    @Override
    public List<?> getAvailableValues(String property) {
        throw new IllegalArgumentException(getExceptionText(property, "get available values for"));
    }

    /**
     * Returns an empty {@link MessageList} for all properties.
     */
    @Override
    public MessageList getMessages(MessageList messageList, String property) {
        return new MessageList();
    }

    @Override
    public void invoke(String property) {
        throw new IllegalArgumentException(
                MessageFormat.format("Cannot invoke \"{0}\" on any of {1}", property, objects));
    }

}
