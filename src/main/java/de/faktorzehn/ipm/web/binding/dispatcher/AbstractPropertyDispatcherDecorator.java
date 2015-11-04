/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding.dispatcher;

import java.util.Collection;

import org.faktorips.runtime.MessageList;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;

/**
 * Base class for all decorating {@link PropertyDispatcher property dispatchers}. Forwards all calls
 * to the dispatcher it was created with.
 * <p>
 * Subclasses can override methods and provide their own data.
 *
 * @author widmaier
 */
public abstract class AbstractPropertyDispatcherDecorator implements PropertyDispatcher {

    private final PropertyDispatcher wrappedDispatcher;

    public AbstractPropertyDispatcherDecorator(PropertyDispatcher wrappedDispatcher) {
        Preconditions.checkNotNull(wrappedDispatcher);
        this.wrappedDispatcher = wrappedDispatcher;
    }

    @Override
    public void prepareUpdateUI() {
        getWrappedDispatcher().prepareUpdateUI();
    }

    @Override
    public Class<?> getValueClass(String property) {
        return wrappedDispatcher.getValueClass(property);
    }

    @Override
    public Object getValue(String property) {
        return getWrappedDispatcher().getValue(property);
    }

    @Override
    public void setValue(String property, Object value) {
        getWrappedDispatcher().setValue(property, value);
    }

    @Override
    public boolean isReadonly(String property) {
        return getWrappedDispatcher().isReadonly(property);
    }

    @Override
    public boolean isEnabled(String property) {
        return getWrappedDispatcher().isEnabled(property);
    }

    @Override
    public boolean isVisible(String property) {
        return getWrappedDispatcher().isVisible(property);
    }

    @Override
    public boolean isRequired(String property) {
        return getWrappedDispatcher().isRequired(property);
    }

    @Override
    public Collection<?> getAvailableValues(String property) {
        return getWrappedDispatcher().getAvailableValues(property);
    }

    @Override
    public MessageList getMessages(String property) {
        return getWrappedDispatcher().getMessages(property);
    }

    @Override
    public void invoke(String property) {
        getWrappedDispatcher().invoke(property);
    }

    protected PropertyDispatcher getWrappedDispatcher() {
        return wrappedDispatcher;
    }

}
