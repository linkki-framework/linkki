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
package org.linkki.util;

import static java.util.Objects.requireNonNull;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Gives fast access to a class' properties.
 */
public class PropertyAccess {

    private Class<?> clazz;

    private Map<String, PropertyDescriptor> propertiesByName = new HashMap<>();

    public PropertyAccess(Class<?> clazz) {
        this.clazz = requireNonNull(clazz, "clazz must not be null");
        BeanInfo beanInfo = BeanUtils.getBeanInfo(clazz);
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            propertiesByName.put(descriptor.getName(), descriptor);
        }
    }

    /**
     * Returns the class' property descriptor for the given property name.
     *
     * @throws IllegalArgumentException if the class hasn't got a property with the given property
     *             name.
     */
    public PropertyDescriptor getPropertyDescriptor(String propertyName) {
        PropertyDescriptor descriptor = propertiesByName.get(propertyName);
        if (descriptor == null) {
            throw new IllegalArgumentException("Class '" + clazz + "' has not property'" + propertyName + "'.");
        }
        return descriptor;
    }

    /**
     * Returns the object's value for the given property.
     */
    @Nullable
    public Object getValue(Object object, String propertyName) {
        PropertyDescriptor descriptor = getPropertyDescriptor(propertyName);
        try {
            return descriptor.getReadMethod().invoke(object);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the object's value for the given property.
     */
    public void setValue(Object object, String propertyName, Object value) {
        PropertyDescriptor descriptor = getPropertyDescriptor(propertyName);
        try {
            descriptor.getWriteMethod().invoke(object, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
