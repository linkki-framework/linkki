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
package org.linkki.core.binding.dispatcher;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.dispatcher.accessor.PropertyAccessor;
import org.linkki.core.binding.dispatcher.accessor.PropertyAccessorCache;
import org.linkki.core.message.MessageList;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * {@link PropertyDispatcher} that reads properties from an arbitrary object via reflection. Falls
 * back to another dispatcher if no property/method is available in the accessed object.
 */
public class ReflectionPropertyDispatcher implements PropertyDispatcher {

    private final PropertyNamingConvention propertyNamingConvention = new PropertyNamingConvention();

    private final PropertyDispatcher fallbackDispatcher;

    private final Supplier<?> boundObjectSupplier;

    private final String property;

    /**
     * @param boundObjectSupplier a supplier to get the object accessed via reflection. Must not be
     *            {@code null}. The object is provided via a supplier because it may change.
     * @param property the name of the property of the bound object that this {@link PropertyDispatcher}
     *            will handle
     * @param fallbackDispatcher the dispatcher accessed in case a value cannot be read or written
     *            (because no getters/setters exist) from the accessed object property. Must not be
     *            {@code null}.
     */
    public ReflectionPropertyDispatcher(Supplier<?> boundObjectSupplier, String property,
            PropertyDispatcher fallbackDispatcher) {
        this.boundObjectSupplier = requireNonNull(boundObjectSupplier, "boundObjectSupplier must not be null");
        this.property = requireNonNull(property, "property must not be null");
        this.fallbackDispatcher = requireNonNull(fallbackDispatcher, "fallbackDispatcher must not be null");
    }

    @Override
    public String getProperty() {
        return property;
    }

    @CheckForNull
    @Override
    public Object getBoundObject() {
        return boundObjectSupplier.get();
    }

    @Nonnull
    @SuppressFBWarnings()
    private Object getExistingBoundObject() {
        return requireNonNull(getBoundObject());
    }

    @Override
    public Class<?> getValueClass() {
        Object boundObject = getBoundObject();
        if (boundObject != null && hasReadMethod(getProperty())) {
            Class<?> valueClass = getAccessor(getProperty()).getValueClass();
            return valueClass;
        } else {
            return fallbackDispatcher.getValueClass();
        }
    }

    @CheckForNull
    @Override
    @SuppressWarnings("unchecked")
    public <T> T pull(Aspect<T> aspect) {
        if (aspect.isValuePresent()) {
            throw new IllegalStateException(String
                    .format("Static aspect %s should not be handled by %s. It seems like the dispatcher chain is broken, check your %s",
                            aspect, getClass().getSimpleName(), BindingContext.class.getSimpleName()));
        }
        Object boundObject = getBoundObject();
        String propertyAspectName = getPropertyAspectName(aspect);
        if (boundObject != null && hasReadMethod(propertyAspectName)) {
            return (T)getAccessor(propertyAspectName).getPropertyValue(boundObject);
        } else {
            return fallbackDispatcher.pull(aspect);
        }
    }


    private boolean hasReadMethod(String propertyToRead) {
        return getAccessor(propertyToRead).canRead();
    }

    @Override
    public <T> void push(Aspect<T> aspect) {
        Object boundObject = getBoundObject();
        if (boundObject != null) {
            if (aspect.isValuePresent()) {
                callSetter(aspect);
            } else {
                invoke(aspect);
            }
        }
    }

    private <T> void callSetter(Aspect<T> aspect) {
        String propertyAspectName = getPropertyAspectName(aspect);
        if (hasWriteMethod(propertyAspectName)) {
            getAccessor(propertyAspectName).setPropertyValue(getExistingBoundObject(), aspect.getValue());
        } else {
            fallbackDispatcher.push(aspect);
        }
    }

    private <T> void invoke(Aspect<T> aspect) {
        String propertyAspectName = getPropertyAspectName(aspect);
        Method method = getExactMethod(propertyAspectName);
        if (method != null) {
            try {
                method.invoke(getExistingBoundObject());
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new IllegalStateException(String.format("Error invoking method %s", propertyAspectName), e);
            }
        } else {
            fallbackDispatcher.push(aspect);
        }
    }

    @CheckForNull
    private Method getExactMethod(String methodName) {
        return MethodUtils.getAccessibleMethod(getExistingBoundObject().getClass(), methodName);
    }

    @Override
    public <T> boolean isPushable(Aspect<T> aspect) {
        Object boundObject = getBoundObject();
        return (boundObject != null && hasWriteMethod(getPropertyAspectName(aspect)))
                || fallbackDispatcher.isPushable(aspect);
    }

    /**
     * Returns if the {@link #getBoundObject()} has a setter method for the given property.
     * 
     * @param propertyToWrite property name of the bound object
     * @return whether the bound object has a setter method for the property.
     */
    private boolean hasWriteMethod(String propertyToWrite) {
        return getAccessor(propertyToWrite).canWrite();
    }

    private PropertyAccessor getAccessor(String propertyToAccess) {
        return PropertyAccessorCache.get(getExistingBoundObject().getClass(), propertyToAccess);
    }

    private String getPropertyAspectName(Aspect<?> aspect) {
        return propertyNamingConvention.getCombinedPropertyName(getProperty(), aspect.getName());
    }

    /**
     * Returns the messages stating the {@link #getBoundObject() bound object} as
     * {@link org.linkki.core.message.Message#getInvalidObjectProperties() invalid} and those the
     * {@code fallbackDispatcher} returns.
     */
    @Override
    public MessageList getMessages(MessageList messageList) {
        Object boundObject = getBoundObject();
        if (boundObject == null) {
            return new MessageList();
        } else {
            MessageList msgListForBoundObject = messageList.getMessagesFor(boundObject, getProperty());
            msgListForBoundObject.add(fallbackDispatcher.getMessages(messageList));
            // TODO may additionally call a method like "get<Property>NlsText()"
            return msgListForBoundObject;
        }
    }

    @Override
    public String toString() {
        return "ReflectionPropertyDispatcher [boundObject=" + boundObjectSupplier.get() + ", fallbackDispatcher="
                + fallbackDispatcher + "]";
    }
}
