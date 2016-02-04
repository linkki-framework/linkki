/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.function.Supplier;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.faktorips.runtime.MessageList;
import org.linkki.core.binding.dispatcher.accessor.PropertyAccessor;
import org.linkki.util.LazyInitializingMap;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;

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
            Class<?> valueClass = getAccessor(property).getValueClass();
            // Workaround for primitive type <-> wrapper type conversion bug, see FIPM-326.
            if (valueClass.isPrimitive()) {
                return ClassUtils.primitiveToWrapper(valueClass);
            }
            return valueClass;
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
        return (boolean)read(enabledProperty);
    }

    @Override
    public boolean isVisible(String property) {
        String visibleProperty = propertyNamingConvention.getVisibleProperty(property);
        return (boolean)read(visibleProperty);
    }

    @Override
    public boolean isRequired(String property) {
        String requiredProperty = propertyNamingConvention.getRequiredProperty(property);
        return (boolean)read(requiredProperty);
    }

    @Override
    public Collection<?> getAvailableValues(String property) {
        String availableValuesProperty = propertyNamingConvention.getAvailableValuesProperty(property);
        return (Collection<?>)read(availableValuesProperty);
    }

    @Override
    public void invoke(String property) {
        Object boundObject = getBoundObject();
        try {
            MethodUtils.invokeExactMethod(boundObject, property);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads and returns the given property. If the property cannot be read an exception is thrown.
     */
    private Object read(String property) {
        if (!canRead(property)) {
            throw new IllegalArgumentException(
                    "Cannot read property \"" + property + "\" on object \"" + boundObjectSupplier.get() + "\"");
        } else {
            return getAccessor(property).getPropertyValue(getBoundObject());
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
    public MessageList getMessages(MessageList messageList, String property) {
        MessageList msgListForBoundObject = messageList.getMessagesFor(getBoundObject(), property);
        msgListForBoundObject.add(fallbackDispatcher.getMessages(messageList, property));
        // TODO may addional call a method like "get<Property>Messages()"
        return msgListForBoundObject;
    }

}
