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
package org.linkki.core.binding.dispatcher.accessor;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Allows reading and writing a value from/to an object's property. Also provides the value class of
 * the property.
 * <p>
 * For accessing an object's property, create a {@link PropertyAccessor} for the class to be
 * accessed and the name of the property. The accessor can determine whether the property can be
 * read or written.
 */
public class PropertyAccessor {

    private final String propertyName;
    private final ReadMethod readMethod;
    private final WriteMethod writeMethod;

    public PropertyAccessor(Class<?> boundClass, String propertyName) {
        this.propertyName = requireNonNull(propertyName, "propertyName must not be null");
        requireNonNull(boundClass, "boundClass must not be null");
        PropertyAccessDescriptor propertyAccessDescriptor = new PropertyAccessDescriptor(boundClass, propertyName);
        readMethod = propertyAccessDescriptor.createReadMethod();
        writeMethod = propertyAccessDescriptor.createWriteMethod();
    }

    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Reads the property's value.
     *
     * @throws IllegalStateException if no getter can be found
     */
    public Object getPropertyValue(Object boundObject) {
        return readMethod.readValue(boundObject);
    }

    /**
     * Only writes the value, if necessary. That is if the property's current value is different
     * from the value to be written. This avoids infinite feedback loops in data binding, as most
     * fields fire events if their value changes.
     * <p>
     * However, if the property is write only the value will always be written (as the current value
     * cannot be retrieved in this case).
     *
     * @throws IllegalStateException if no setter can be found
     */
    public void setPropertyValue(Object boundObject, @Nullable Object value) {
        if (requiresWrite(boundObject, value)) {
            writeMethod.writeValue(boundObject, value);
        }
    }

    private boolean requiresWrite(Object boundObject, @Nullable Object value) {
        return !canRead() || !Objects.equals(value, readMethod.readValue(boundObject));
    }

    /**
     * @return <code>true</code> if there is a read method (getter) for the given object and
     *         property
     */
    public boolean canWrite() {
        return writeMethod.canWrite();
    }

    /**
     * @return <code>true</code> if there is a write method (setter) for the given object and
     *         property
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
