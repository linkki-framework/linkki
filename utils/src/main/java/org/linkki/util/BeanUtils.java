/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BeanUtils {

    /**
     * Returns the bean info for the given class.
     */
    public static BeanInfo getBeanInfo(Class<?> clazz) {
        try {
            return Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Gibt die Methode mit dem übergebenen name und den übergebenen Parametertypen zurück.
     * 
     * @see Class#getMethod(String, Class...)
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        } catch (SecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Returns the class' declared field with the given name. Declared field means that the field is
     * declared in the given class, any super classes are not searched.
     * 
     * @see Class#getDeclaredField(String)
     */
    public static Field getDeclaredField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the class' field with the given name. In contrast to <tt>getDeclaredField</tt> this
     * method searches the type's hierarchy (if the class extends a super class)
     * 
     * @see Class#getDeclaredField(String)
     */
    public static Field getField(Class<?> clazz, String name) {
        // Note: We can't use Class.getField(name) as the method does not consider private fields.
        Class<?> classToSearch = clazz;
        while (classToSearch != null) {
            try {
                return classToSearch.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                // field not found in class, search super class
                classToSearch = classToSearch.getSuperclass();
            }
        }
        throw new NoSuchFieldError("No field '" + name + "' found in class '" + clazz
                + "' or any of its super classes.");
    }

    /**
     * Returns the object's value for the field with the given name.
     * 
     * @see Class#getDeclaredField(String)
     */
    public static Object getValueFromField(Object object, String name) {
        Field field = getField(object.getClass(), name);
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            field.setAccessible(accessible);
        }
    }

    /**
     * Returns the class' property descriptor with the given name.
     * 
     * @throws IllegalArgumentException if no such property exists.
     */
    public static PropertyDescriptor getProperty(Class<?> clazz, String propertyName) {
        PropertyDescriptor[] descriptors = getBeanInfo(clazz).getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : descriptors) {
            if (propertyDescriptor.getName().equals(propertyName)) {
                return propertyDescriptor;
            }
        }
        throw new IllegalArgumentException("Class '" + clazz + "' has not property'" + propertyName + "'.");
    }

    private BeanUtils() {

    }
}
