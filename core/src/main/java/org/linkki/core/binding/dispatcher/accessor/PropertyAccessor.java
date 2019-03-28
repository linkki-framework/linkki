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
package org.linkki.core.binding.dispatcher.accessor;

import static java.util.Objects.requireNonNull;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Allows reading and writing a value from/to an object's property. Also provides the value class of the
 * property.
 * <p>
 * For accessing an object's property, create a {@link PropertyAccessor} for the class to be accessed
 * and the name of the property. The accessor can determine whether the property can be read or written.
 * 
 * @param <T> the type containing the property
 * @param <V> the property's type
 */
public class PropertyAccessor<T, V> {

    private final String propertyName;
    private final ReadMethod<T, V> readMethod;
    private final WriteMethod<T, V> writeMethod;
    private final InvokeMethod<T> invokeMethod;

    public PropertyAccessor(Class<? extends T> boundClass, String propertyName) {
        this.propertyName = requireNonNull(propertyName, "propertyName must not be null");
        requireNonNull(boundClass, "boundClass must not be null");
        PropertyAccessDescriptor<T, V> propertyAccessDescriptor = new PropertyAccessDescriptor<>(boundClass,
                propertyName);
        readMethod = propertyAccessDescriptor.createReadMethod();
        writeMethod = propertyAccessDescriptor.createWriteMethod();
        invokeMethod = propertyAccessDescriptor.createInvokeMethod();
    }

    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Reads the property's value.
     *
     * @throws IllegalStateException if no getter can be found
     */
    public V getPropertyValue(T boundObject) {
        return readMethod.readValue(boundObject);
    }

    /**
     * Sets the property to the given value.
     *
     * @throws IllegalStateException if no setter can be found
     */
    public void setPropertyValue(T boundObject, @CheckForNull V value) {
        writeMethod.writeValue(boundObject, value);
    }

    /**
     * Invokes the method.
     *
     * @throws IllegalStateException if the method to be invoked can not be found
     */
    public void invoke(T boundObject) {
        invokeMethod.invoke(boundObject);
    }

    /**
     * @return <code>true</code> if there is a read method (getter) for the given object and property
     */
    public boolean canWrite() {
        return writeMethod.canWrite();
    }

    /**
     * @return <code>true</code> if there is a method that can be invoked for the given object and
     *         property
     */
    public boolean canInvoke() {
        return invokeMethod.canInvoke();
    }

    /**
     * @return <code>true</code> if there is a write method (setter) for the given object and property
     */
    public boolean canRead() {
        return readMethod.canRead();
    }

    /**
     * @return the return type of the getter, i.e. class of the value the read method returns.
     * @throws IllegalStateException if there is no read method for this property
     */
    public Class<?> getValueClass() {
        return readMethod.getReturnType();
    }
}
