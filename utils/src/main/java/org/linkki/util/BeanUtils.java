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
package org.linkki.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class BeanUtils {

    public static final String GET_PREFIX = "get";
    public static final String SET_PREFIX = "set";
    public static final String IS_PREFIX = "is";

    private BeanUtils() {
        // prevent instantiation
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
     * Returns an optional containing the class' method matching the given predicate (empty optional if
     * none matches). If you expect more than one match, {@link #getMethods(Class, Predicate)} might be
     * what you're looking for.
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
    @CheckForNull
    public static Object getValueFromField(Object object, String name) {
        Field field = getField(object.getClass(), name);
        return getValueFromField(object, field);
    }

    /**
     * Returns the object's value for the field with the given name.
     * 
     * @see Class#getDeclaredField(String)
     */
    @CheckForNull
    public static Object getValueFromField(Object object, Field field) {
        return AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            @CheckForNull
            public Object run() {
                boolean accessible = field.isAccessible();
                if (!accessible) {
                    field.setAccessible(true);
                }
                try {
                    return field.get(object);
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException(e);
                } finally {
                    field.setAccessible(accessible);
                }
            }
        });
    }

    /**
     * Returns the property name from the given method:
     * <ul>
     * <li>{@code String getFoo()} -&gt; "foo"</li>
     * <li>{@code boolean isBar()} -&gt; "bar"</li>
     * <li>{@code void fooBar()} -&gt; "fooBar"</li>
     * </ul>
     */
    public static String getPropertyName(Method method) {
        return getPropertyName(method.getReturnType(), method.getName());
    }


    /**
     * Returns the property name from the given method name:
     * <ul>
     * <li>{@code getFoo} -&gt; "foo"</li>
     * <li>{@code isBar} -&gt; "bar"</li>
     * <li>{@code fooBar} -&gt; "fooBar"</li>
     * </ul>
     */
    public static String getPropertyName(Type returnType, String methodName) {
        if (returnType.equals(Void.TYPE)) {
            return methodName;
        } else if (methodName.startsWith(IS_PREFIX)) {
            return StringUtils.uncapitalize(methodName.substring(2));
        } else if (methodName.startsWith(GET_PREFIX)) {
            return StringUtils.uncapitalize(methodName.substring(3));
        } else {
            return methodName;
        }
    }
}
