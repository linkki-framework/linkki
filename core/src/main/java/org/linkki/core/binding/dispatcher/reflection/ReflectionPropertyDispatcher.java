/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.core.binding.dispatcher.reflection;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.fallback.ExceptionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.reflection.accessor.PropertyAccessor;
import org.linkki.core.binding.dispatcher.reflection.accessor.PropertyAccessorCache;
import org.linkki.core.binding.validation.message.MessageList;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * {@link PropertyDispatcher} that reads properties from an arbitrary object via reflection. Falls back
 * to another dispatcher if no property/method is available in the accessed object.
 */
public class ReflectionPropertyDispatcher implements PropertyDispatcher {

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

    @NonNull
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

    @Override
    @SuppressWarnings("unchecked")
    public <V> V pull(Aspect<V> aspect) {
        if (aspect.isValuePresent()) {
            throw new IllegalStateException(String
                    .format("Static aspect %s should not be handled by %s. It seems like the dispatcher chain is broken, check your %s",
                            aspect, getClass().getSimpleName(), BindingContext.class.getSimpleName()));
        }
        @CheckForNull
        Object boundObject = getBoundObject();
        String propertyAspectName = getPropertyAspectName(aspect);
        if (boundObject != null && hasReadMethod(propertyAspectName)) {
            PropertyAccessor<Object, V> accessor = (PropertyAccessor<Object, V>)getAccessor(propertyAspectName);
            return accessor.getPropertyValue(boundObject);
        } else {
            return fallbackDispatcher.pull(aspect);
        }
    }

    /**
     * Returns whether the {@link #getBoundObject()} has a getter method for the given property.
     * 
     * @param propertyToRead property name of the bound object
     * @return whether the bound object has a getter method for the property
     */
    private boolean hasReadMethod(String propertyToRead) {
        return getAccessor(propertyToRead).canRead();
    }

    @Override
    public <V> void push(Aspect<V> aspect) {
        @CheckForNull
        Object boundObject = getBoundObject();
        if (boundObject != null) {
            if (aspect.isValuePresent()) {
                callSetter(aspect);
            } else {
                invoke(aspect);
            }
        }
    }

    private <V> void callSetter(Aspect<V> aspect) {
        String propertyAspectName = getPropertyAspectName(aspect);
        if (hasReadMethod(propertyAspectName)) {
            if (hasWriteMethod(propertyAspectName)) {
                @SuppressWarnings("unchecked")
                PropertyAccessor<Object, V> accessor = (PropertyAccessor<Object, V>)getAccessor(propertyAspectName);
                accessor.setPropertyValue(getExistingBoundObject(), aspect.getValue());
            } else {
                throw new IllegalArgumentException(
                        ExceptionPropertyDispatcher.missingMethodMessage("set",
                                                                         Arrays.asList(getBoundObject())));
            }
        } else {
            fallbackDispatcher.push(aspect);
        }
    }

    private <V> void invoke(Aspect<V> aspect) {
        String propertyAspectName = getPropertyAspectName(aspect);
        if (hasInvokeMethod(propertyAspectName)) {
            @SuppressWarnings("unchecked")
            PropertyAccessor<Object, V> accessor = (PropertyAccessor<Object, V>)getAccessor(propertyAspectName);
            accessor.invoke(getExistingBoundObject());
        } else {
            fallbackDispatcher.push(aspect);
        }
    }

    @Override
    public <V> boolean isPushable(Aspect<V> aspect) {
        @CheckForNull
        Object boundObject = getBoundObject();

        if (boundObject == null) {
            return fallbackDispatcher.isPushable(aspect);
        }

        String propertyAspectName = getPropertyAspectName(aspect);
        if (aspect.isValuePresent()) {
            // the FallbackDispatcher should only be called if the BoundObject has no read method.
            // Otherwise a #push() could result in a #write() to the ModelObject even if it is not
            // declared within the PMO
            return (hasReadMethod(propertyAspectName) && hasWriteMethod(propertyAspectName))
                    || (!hasReadMethod(propertyAspectName) && fallbackDispatcher.isPushable(aspect));
        } else {
            return hasInvokeMethod(propertyAspectName)
                    || fallbackDispatcher.isPushable(aspect);
        }
    }

    /**
     * Returns whether the {@link #getBoundObject()} has a setter method for the given property.
     * 
     * @param propertyToWrite property name of the bound object
     * @return whether the bound object has a setter method for the property
     */
    private boolean hasWriteMethod(String propertyToWrite) {
        return getAccessor(propertyToWrite).canWrite();
    }

    /**
     * Returns whether the {@link #getBoundObject()} has a method for the given method name.
     * 
     * @param method name of the method that should be invoked
     * @return whether the bound object has a method with the provided name
     */
    private boolean hasInvokeMethod(String method) {
        return getAccessor(method).canInvoke();
    }

    private PropertyAccessor<?, ?> getAccessor(String propertyToAccess) {
        return PropertyAccessorCache.get(getExistingBoundObject().getClass(), propertyToAccess);
    }

    private String getPropertyAspectName(Aspect<?> aspect) {
        if (StringUtils.isEmpty(getProperty())) {
            return aspect.getName();
        } else {
            return StringUtils.uncapitalize(property + StringUtils.capitalize(aspect.getName()));
        }
    }

    /**
     * Returns the messages stating the {@link #getBoundObject() bound object} as
     * {@link org.linkki.core.binding.validation.message.Message#getInvalidObjectProperties() invalid}
     * and those the {@code fallbackDispatcher} returns.
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
        Object boundObject = getBoundObject();
        return getClass().getSimpleName() + "["
                + (boundObject != null ? boundObject.getClass().getSimpleName() : "<no object>") + "#" + getProperty()
                + "]\n\t-> " + fallbackDispatcher;
    }

}
