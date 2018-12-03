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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.Nullable;

public class BeanUtils {

    private BeanUtils() {

    }

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
     * Returns the method with the given name and parameter types.
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
     * Returns an optional containing the class' method matching the given predicate (empty optional
     * if none matches). If you expect more than one match, {@link #getMethods(Class, Predicate)}
     * might be what you're looking for.
     * 
     * @throws IllegalStateException If more than one method matches the predicate.
     */
    public static Optional<Method> getMethod(Class<?> clazz, Predicate<Method> predicate) {
        return getMethods(clazz, predicate).reduce((m1, m2) -> {
            throw new IllegalStateException(
                    MessageFormat.format("Ambiguous methods ({0} and {1}) found in {2}", m1, m2, clazz));
        });
    }

    /**
     * Returns the class' methods matching the given predicate.
     */
    public static Stream<Method> getMethods(Class<?> clazz, Predicate<Method> predicate) {
        // javac creates synthetic bridge methods
        return Arrays.stream(clazz.getMethods())
                .filter(m -> !m.isSynthetic())
                .filter(predicate);
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
     * @throws RuntimeException wrapping a NoSuchFieldException if no such field exists.
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
        throw new RuntimeException(
                new NoSuchFieldException(
                        "No field '" + name + "' found in class '" + clazz + "' or any of its super classes."));
    }

    /**
     * Returns the object's value for the field with the given name.
     * 
     * @see Class#getDeclaredField(String)
     */
    @Nullable
    public static Object getValueFromField(Object object, String name) {
        Field field = getField(object.getClass(), name);
        return AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            @Nullable
            public Object run() {
                boolean accessible = field.isAccessible();
                if (!accessible) {
                    field.setAccessible(true);
                }
                try {
                    return field.get(object);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                } finally {
                    field.setAccessible(accessible);
                }
            }
        });
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
}
