/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import static java.util.Objects.requireNonNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.faktorips.runtime.MessageList;

/**
 * {@link PropertyDispatcher} that throws exception on every method call except
 * {@link #isReadOnly()} which returns <code>true</code> and {@link #getMessages(MessageList)},
 * which returns an empty {@link MessageList}.
 *
 * Serves as a last resort fallback to simplify exception creation in other dispatchers.
 *
 * @author widmaier
 */
public final class ExceptionPropertyDispatcher implements PropertyDispatcher {

    private final List<Object> objects = new ArrayList<>();
    private String property;

    /**
     * @param property The name of the property
     * @param objects used for error messages
     */
    public ExceptionPropertyDispatcher(String property, Object... objects) {
        requireNonNull(property, "property must not be null");
        this.property = property;
        this.objects.addAll(Arrays.asList(objects));
    }

    @Override
    public Class<?> getValueClass() {
        throw new IllegalArgumentException(getExceptionText("find getter method for"));
    }

    private String getExceptionText(String action) {
        return MessageFormat.format("Cannot {0} property \"{1}\" in any of {2}", action, property, objects);
    }

    @Override
    @CheckForNull
    public Object getValue() {
        throw new IllegalArgumentException(getExceptionText("read"));
    }

    @Override
    public void setValue(@Nullable Object value) {
        throw new IllegalArgumentException(getExceptionText("write"));
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        throw new IllegalArgumentException(getExceptionText("get enabled state for"));
    }

    @Override
    public boolean isVisible() {
        throw new IllegalArgumentException(getExceptionText("get visibility for"));
    }

    @Override
    public boolean isRequired() {
        throw new IllegalArgumentException(getExceptionText("get required state for"));
    }

    @Override
    public Collection<?> getAvailableValues() {
        throw new IllegalArgumentException(getExceptionText("get available values for"));
    }

    /**
     * Returns an empty {@link MessageList} for all properties.
     */
    @Override
    public MessageList getMessages(MessageList messageList) {
        return new MessageList();
    }

    @Override
    public void invoke() {
        throw new IllegalArgumentException(
                MessageFormat.format("Cannot invoke \"{0}\" on any of {1}", property, objects));
    }

    @Override
    public String toString() {
        return "ExceptionPropertyDispatcher[" + property + "]";
    }

    @Override
    public String getProperty() {
        return property;
    }

    @Override
    public Object getBoundObject() {
        if (objects.size() > 0) {
            return objects.get(0);
        } else {
            throw new IllegalStateException("ExceptionPropertyDispatcher has no presentation model object");
        }
    }

    @Override
    @CheckForNull
    public String getCaption() {
        throw new IllegalStateException(getExceptionText("find caption method for"));
    }

    @Override
    @CheckForNull
    public String getToolTip() {
        throw new IllegalStateException(getExceptionText("find tooltip method for"));
    }
}
