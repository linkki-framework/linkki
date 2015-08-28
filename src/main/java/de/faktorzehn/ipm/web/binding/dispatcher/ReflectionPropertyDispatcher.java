/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding.dispatcher;

import java.util.List;
import java.util.function.Supplier;

import org.faktorips.runtime.MessageList;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;

import de.faktorzehn.ipm.utils.LazyInitializingMap;
import de.faktorzehn.ipm.web.binding.dispatcher.accessor.PropertyAccessor;

/**
 * {@link PropertyDispatcher} that reads properties from an arbitrary object via reflection. Falls
 * back to another dispatcher if no property/method is available in the accessed object.
 *
 * @author widmaier
 */
public class ReflectionPropertyDispatcher implements PropertyDispatcher {

    private final PropertyNamingConvention propertyNamingConvention = new PropertyNamingConvention();

    private final LazyInitializingMap<String, PropertyAccessor> accessorCache;

    private final PropertyDispatcher fallbackDispatcher;

    private final Supplier<?> boundObjectSupplier;

    /**
     * @param boundObjectSupplier An supplier to get the object accessed via reflection. Must not be
     *            <code>null</code>. The object is provided via a supplier because it may change.
     * @param fallbackDispatcher the dispatcher accessed in case a value cannot be read or written
     *            (because no getters/setters exist) from the accessed object. Must not be
     *            <code>null</code>.
     */
    public ReflectionPropertyDispatcher(Supplier<?> boundObjectSupplier, PropertyDispatcher fallbackDispatcher) {
        Preconditions.checkNotNull(boundObjectSupplier);
        Preconditions.checkNotNull(fallbackDispatcher);
        this.fallbackDispatcher = fallbackDispatcher;
        this.boundObjectSupplier = boundObjectSupplier;
        Preconditions.checkNotNull(getBoundObject());
        accessorCache = new LazyInitializingMap<>(
                (String key) -> new PropertyAccessor(getBoundObject().getClass(), key));
    }

    private Object getBoundObject() {
        return boundObjectSupplier.get();
    }

    @Override
    public void prepareUpdateUI() {
        fallbackDispatcher.prepareUpdateUI();
    }

    @Override
    public Class<?> getValueClass(String property) {
        if (canRead(property)) {
            return getAccessor(property).getValueClass();
        } else {
            return fallbackDispatcher.getValueClass(property);
        }
    }

    private PropertyAccessor getAccessor(String property) {
        return accessorCache.get(property);
    }

    @Override
    public Object getValue(String property) {
        if (canRead(property)) {
            return getAccessor(property).getPropertyValue(getBoundObject());
        } else {
            return fallbackDispatcher.getValue(property);
        }
    }

    @Override
    public void setValue(String property, Object value) {
        if (!isReadonly(property)) {
            writeProperty(property, value);
        }
    }

    private void writeProperty(String property, Object value) {
        if (canWrite(property)) {
            getAccessor(property).setPropertyValue(getBoundObject(), value);
        } else {
            fallbackDispatcher.setValue(property, value);
        }
    }

    @Override
    public boolean isReadonly(String property) {
        return !canWrite(property) && fallbackDispatcher.isReadonly(property);
    }

    @Override
    public boolean isEnabled(String property) {
        String enabledProperty = propertyNamingConvention.getEnabledProperty(property);
        throwExceptionIfCannotRead(enabledProperty);
        return (boolean)getAccessor(enabledProperty).getPropertyValue(getBoundObject());
    }

    @Override
    public boolean isVisible(String property) {
        String visibleProperty = propertyNamingConvention.getVisibleProperty(property);
        throwExceptionIfCannotRead(visibleProperty);
        return (boolean)getAccessor(visibleProperty).getPropertyValue(getBoundObject());
    }

    @Override
    public boolean isRequired(String property) {
        String requiredProperty = propertyNamingConvention.getRequiredProperty(property);
        throwExceptionIfCannotRead(requiredProperty);
        return (boolean)getAccessor(requiredProperty).getPropertyValue(getBoundObject());
    }

    @Override
    public List<?> getAvailableValues(String property) {
        String availableValuesProperty = propertyNamingConvention.getAvailableValuesProperty(property);
        throwExceptionIfCannotRead(availableValuesProperty);
        return (List<?>)getAccessor(availableValuesProperty).getPropertyValue(getBoundObject());
    }

    private void throwExceptionIfCannotRead(String property) {
        if (!canRead(property)) {
            throw new IllegalArgumentException("Cannot read property \"" + property + "\"");
        }
    }

    private boolean canRead(String property) {
        return getAccessor(property).canRead();
    }

    private boolean canWrite(String property) {
        return getAccessor(property).canWrite();
    }

    /**
     * Returns an empty {@link MessageList} for all properties.
     */
    @Override
    public MessageList getMessages(String property) {
        return new MessageList();
    }

}
