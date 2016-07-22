/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.faktorips.runtime.MessageList;
import org.linkki.core.binding.dispatcher.accessor.PropertyAccessor;

/**
 * {@link PropertyDispatcher} that reads properties from an arbitrary object via reflection. Falls
 * back to another dispatcher if no property/method is available in the accessed object.
 *
 * @author widmaier
 */
public class ReflectionPropertyDispatcher implements PropertyDispatcher {

    private final PropertyNamingConvention propertyNamingConvention = new PropertyNamingConvention();

    @Nonnull
    private final PropertyDispatcher fallbackDispatcher;

    @Nonnull
    private final Supplier<?> boundObjectSupplier;

    @Nonnull
    private final String property;

    /**
     * @param boundObjectSupplier a supplier to get the object accessed via reflection. Must not be
     *            {@code null}. The object is provided via a supplier because it may change.
     * @param property the name of the property of the bound object that this
     *            {@link PropertyDispatcher} will handle
     * @param fallbackDispatcher the dispatcher accessed in case a value cannot be read or written
     *            (because no getters/setters exist) from the accessed object property. Must not be
     *            {@code null}.
     */
    @SuppressWarnings("null")
    public ReflectionPropertyDispatcher(@Nonnull Supplier<?> boundObjectSupplier, @Nonnull String property,
            @Nonnull PropertyDispatcher fallbackDispatcher) {
        this.boundObjectSupplier = requireNonNull(boundObjectSupplier, "boundObjectSupplier must not be null");
        this.property = requireNonNull(property, "property must not be null");
        this.fallbackDispatcher = requireNonNull(fallbackDispatcher, "fallbackDispatcher must not be null");
        requireNonNull(getBoundObject(), "Bound object must be available from supplier");
    }

    @Override
    public String getProperty() {
        return property;
    }

    @Override
    public Object getBoundObject() {
        return boundObjectSupplier.get();
    }

    @Override
    public Class<?> getValueClass() {
        if (canRead(getProperty())) {
            Class<?> valueClass = getAccessor(getProperty()).getValueClass();
            // Workaround for primitive type <-> wrapper type conversion bug, see FIPM-326.
            if (valueClass.isPrimitive()) {
                return ClassUtils.primitiveToWrapper(valueClass);
            }
            return valueClass;
        } else {
            return fallbackDispatcher.getValueClass();
        }
    }

    private PropertyAccessor getAccessor(String propertyToAccess) {
        return PropertyAccessorCache.get(getBoundObject().getClass(), propertyToAccess);
    }

    @Override
    public Object getValue() {
        return get(propertyNamingConvention::getValueProperty, fallbackDispatcher::getValue);
    }

    @Override
    public void setValue(Object value) {
        if (!isReadOnly()) {
            if (canWrite(getProperty())) {
                getAccessor(getProperty()).setPropertyValue(getBoundObject(), value);
            } else {
                fallbackDispatcher.setValue(value);
            }
        }
    }

    @Override
    public boolean isReadOnly() {
        return !canWrite(getProperty()) && fallbackDispatcher.isReadOnly();
    }

    @Override
    public boolean isEnabled() {
        return (boolean)get(propertyNamingConvention::getEnabledProperty, fallbackDispatcher::isEnabled);
    }

    @Override
    public boolean isVisible() {
        return (boolean)get(propertyNamingConvention::getVisibleProperty, fallbackDispatcher::isVisible);
    }

    @Override
    public boolean isRequired() {
        return (boolean)get(propertyNamingConvention::getRequiredProperty, fallbackDispatcher::isRequired);
    }

    @Override
    public Collection<?> getAvailableValues() {
        return (Collection<?>)get(propertyNamingConvention::getAvailableValuesProperty,
                                  fallbackDispatcher::getAvailableValues);
    }

    @Override
    public void invoke() {
        Object boundObject = getBoundObject();
        try {
            MethodUtils.invokeExactMethod(boundObject, getProperty());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean canRead(String propertyToRead) {
        return getAccessor(propertyToRead).canRead();
    }

    private boolean canWrite(String propertyToWrite) {
        return getAccessor(propertyToWrite).canWrite();
    }

    /**
     * Returns an empty {@link MessageList} for all properties.
     */
    @Override
    public MessageList getMessages(MessageList messageList) {
        MessageList msgListForBoundObject = messageList.getMessagesFor(getBoundObject(), getProperty());
        msgListForBoundObject.add(fallbackDispatcher.getMessages(messageList));
        // TODO may additionally call a method like "get<Property>Messages()"
        return msgListForBoundObject;
    }

    @Override
    public String toString() {
        return "ReflectionPropertyDispatcher [boundObject=" + boundObjectSupplier.get() + ", fallbackDispatcher="
                + fallbackDispatcher + "]";
    }

    @Override
    public String getCaption() {
        return (String)get(propertyNamingConvention::getCaptionProperty, fallbackDispatcher::getCaption);
    }

    @Override
    public String getToolTip() {
        return (String)get(propertyNamingConvention::getToolTipProperty, fallbackDispatcher::getToolTip);
    }

    private Object get(Function<String, String> methodNameProvider, Supplier<Object> fallbackProvider) {
        String methodName = methodNameProvider.apply(property);
        if (canRead(methodName)) {
            Object propertyValue = getAccessor(methodName).getPropertyValue(getBoundObject());
            return propertyValue;
        } else {
            return fallbackProvider.get();
        }
    }
}
