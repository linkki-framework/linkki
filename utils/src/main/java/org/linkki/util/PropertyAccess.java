/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.util;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Gives fast access to a class' properties.
 */
public class PropertyAccess {

    private Class<?> clazz;
    private Map<String, PropertyDescriptor> propertiesByName = new HashMap<>();

    public PropertyAccess(Class<?> clazz) {
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
