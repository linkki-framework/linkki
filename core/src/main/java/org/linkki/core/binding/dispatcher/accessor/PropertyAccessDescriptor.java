/*
 * Copyright Faktor Zehn GmbH.
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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * This class wraps a {@link PropertyDescriptor} for a bound class and a specified property
 * identified by its name. It could create a {@link WriteMethod} or {@link ReadMethod} object for
 * the bound class and property using the methods {@link #createWriteMethod()} and
 * {@link #createReadMethod()}.
 *
 */
public class PropertyAccessDescriptor {

    private final Class<?> boundClass;
    private final String propertyName;
    private final Optional<PropertyDescriptor> propertyDescriptor;

    public PropertyAccessDescriptor(Class<?> boundClass, String propertyName) {
        this.boundClass = requireNonNull(boundClass, "boundClass must not be null");
        this.propertyName = requireNonNull(propertyName, "propertyName must not be null");

        propertyDescriptor = findDescriptor();
    }

    public WriteMethod createWriteMethod() {
        return new WriteMethod(this);
    }

    public ReadMethod createReadMethod() {
        return new ReadMethod(this);
    }

    Optional<Method> getReflectionWriteMethod() {
        return propertyDescriptor.flatMap(pd -> Optional.ofNullable(pd.getWriteMethod()));
    }

    Optional<Method> getReflectionReadMethod() {
        return propertyDescriptor.flatMap(pd -> Optional.ofNullable(pd.getReadMethod()));
    }

    private Optional<PropertyDescriptor> findDescriptor() {
        return getDefaultMethodPropertyDescriptor();
    }

    private Optional<PropertyDescriptor> getDefaultMethodPropertyDescriptor() {
        return PropertyDescriptorFactory.createPropertyDescriptor(getBoundClass(), getPropertyName());
    }

    public Class<?> getBoundClass() {
        return boundClass;
    }

    public String getPropertyName() {
        return propertyName;
    }

}
