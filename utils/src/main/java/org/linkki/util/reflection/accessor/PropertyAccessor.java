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
package org.linkki.util.reflection.accessor;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.linkki.util.LazyInitializingMap;

import static java.util.Objects.requireNonNull;

/**
 * Allows reading and writing a value from/to an object's property. Also provides the value class of the
 * property.
 * <p>
 * For accessing an object's property, create a {@link PropertyAccessor} for the class to be accessed
 * and the name of the property. The accessor can determine whether the property can be read, written or invoked.
 *
 * @param <T> the type containing the property
 * @param <V> the property's type
 */
public final class PropertyAccessor<T, V> {

    private static final LazyInitializingMap<CacheKey, PropertyAccessor<?, ?>> ACCESSOR_CACHE =
            new LazyInitializingMap<>(key -> new PropertyAccessor<>(key.clazz, key.property));

    private final String propertyName;
    private final ReadMethod<T, V> readMethod;
    private final WriteMethod<T, V> writeMethod;
    private final InvokeMethod<T> invokeMethod;

    PropertyAccessor(Class<? extends T> boundClass, String propertyName) {
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
     * @throws RuntimeException if the method cannot be called
     */
    public V getPropertyValue(T boundObject) {
        return readMethod.readValue(boundObject);
    }

    /**
     * Sets the property to the given value.
     *
     * @throws RuntimeException if the method cannot be called
     */
    public void setPropertyValue(T boundObject, @CheckForNull V value) {
        writeMethod.writeValue(boundObject, value);
    }

    /**
     * Invokes the method.
     *
     * @throws RuntimeException if the method cannot be called
     */
    public void invoke(T boundObject) {
        invokeMethod.invoke(boundObject);
    }

    /**
     * @return <code>true</code> if there is a read method (getter) for the given object and property
     */
    public boolean canWrite() {
        return writeMethod.isPresent();
    }

    /**
     * @return <code>true</code> if there is a method that can be invoked for the given object and
     * property
     */
    public boolean canInvoke() {
        return invokeMethod.isPresent();
    }

    /**
     * @return <code>true</code> if there is a write method (setter) for the given object and property
     */
    public boolean canRead() {
        return readMethod.isPresent();
    }

    /**
     * @return the return type of the getter, i.e. class of the value the read method returns.
     * @throws IllegalStateException if there is no read method for this property
     */
    public Class<?> getValueClass() {
        return readMethod.getReturnType();
    }

    /**
     * @param clazz    a class
     * @param property a property of the class
     * @return a {@link PropertyAccessor} to access the property of instances of the class
     */
    @SuppressWarnings("unchecked")
    public static <T> PropertyAccessor<T, ?> get(Class<T> clazz, String property) {
        return (PropertyAccessor<T, ?>)ACCESSOR_CACHE.get(new CacheKey(clazz, property));
    }

    private record CacheKey(Class<?> clazz, String property) {
        private CacheKey(@NonNull Class<?> clazz, String property) {
            this.clazz = requireNonNull(clazz, "clazz must not be null");
            this.property = requireNonNull(property, "property must not be null");
        }
    }
}
