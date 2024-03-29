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

package org.linkki.util.reflection.accessor;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.apache.commons.lang3.reflect.TypeUtils;

/**
 * Utility class to get easy access to the return value of member. In contrast to
 * {@link PropertyAccessor}, values of fields can also be read, in addition to the return value of
 * methods.
 * <p>
 * The implementation may use any performance optimization like caching of access code but never
 * caches the returned value. Every call will retrieve the current value from the method or field.
 */
public class MemberAccessors {

    private static final Map<Member, Function<Object, Object>> ACCESSOR_CACHE = new ConcurrentHashMap<>();

    private MemberAccessors() {
        // prevents instantiation
    }

    /**
     * Retrieve the value from the given member ({@link Field} or {@link Method}) in the given
     * object by calling the method or getting the value from the field. The returned value will
     * directly be cast to the expected type. Note that this is always an unsafe cast!
     *
     * @param <T> the expected type of the return value
     * @param object the object where to read the value from
     * @param fieldOrMethod a {@link Field} or {@link Method} to access the value
     * @return the value retrieved by the {@link Field} or {@link Method}
     * @throws IllegalArgumentException if the field is not accessible or the value cannot be
     *             retrieved. The cause of the original exception is included.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValue(Object object, Member fieldOrMethod) {
        if (fieldOrMethod instanceof Field field) {
            return (T)getFieldValue(object, field);
        } else if (fieldOrMethod instanceof Method method) {
            Function<Object, Object> accessor = ACCESSOR_CACHE
                    .computeIfAbsent(fieldOrMethod, key -> getMethodAsFunction(method));
            try {
                return (T)accessor.apply(object);
            } catch (IllegalStateException e) {
                throw new IllegalArgumentException(e.getCause());
            }
        } else {
            throw new IllegalArgumentException("Only field or method is supported, found "
                    + fieldOrMethod.getClass().getCanonicalName() + " as type of "
                    + getNameOf(fieldOrMethod));
        }
    }

    private static Function<Object, Object> getMethodAsFunction(Method method) {
        if (Void.TYPE.equals(method.getReturnType())) {
            throw new IllegalArgumentException(
                    String.format("Cannot call method %s#%s: void as return type is not allowed",
                                  method.getDeclaringClass().getSimpleName(), method.getName()));
        }
        return new ReadMethod<>(method).getMethodAsFunction();
    }

    /**
     * Field access is simply called by reflection, no cache needed because it already performs best
     * this way.
     */
    private static Object getFieldValue(Object object, Field field) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot access field " + field, e);
        }
    }

    private static String getNameOf(Member member) {
        return member.getDeclaringClass().getName() + "#" + member.getName();
    }

    /**
     * Returns the type of the given member, either the type of the field, or the return type of the
     * method.
     *
     * @param fieldOrMethod a {@link Field} or {@link Method} to access the value
     * @return the type of the member
     * @throws IllegalArgumentException if the given member is not a field or a method
     *
     * @deprecated use {@link #getType(Member, Type)} instead to be able to handle generic types
     *             properly
     */
    @Deprecated(since = "2.5.0")
    public static Class<?> getType(Member fieldOrMethod) {
        if (fieldOrMethod instanceof Field field) {
            return field.getType();
        } else if (fieldOrMethod instanceof Method method) {
            return method.getReturnType();
        } else {
            throw new IllegalArgumentException("Only field or method is supported, found "
                    + fieldOrMethod.getClass().getCanonicalName() + " as type of "
                    + getNameOf(fieldOrMethod));
        }
    }

    /**
     * Returns the type of the given member, either the type of the field, or the return type of the
     * method. The concrete {@link Type class} is required to be able to handle generics types
     * properly.
     *
     * @param fieldOrMethod a {@link Field} or {@link Method} to access the value
     * @param clazz the {@link Type class} that contains the member
     * @return the type of the member
     * @throws IllegalArgumentException if the given member is not a field or a method, or if the
     *             Type can not be resolved
     *
     */
    public static Class<?> getType(Member fieldOrMethod, Type clazz) {
        Type type;
        if (fieldOrMethod instanceof Field field) {
            type = field.getGenericType();
        } else if (fieldOrMethod instanceof Method method) {
            type = method.getGenericReturnType();
        } else {
            throw exceptionUnableToDetermineTypeOf(fieldOrMethod,
                                                   "Only field or method is supported, but found "
                                                           + fieldOrMethod.getClass().getCanonicalName()
                                                           + " as accessor type of the member.");
        }

        Class<?> rawType = TypeUtils.getRawType(type, clazz);
        if (rawType != null) {
            return rawType;
        } else if (type instanceof TypeVariable<?> typeVariable) {
            Type[] implicitBounds = TypeUtils.getImplicitBounds(typeVariable);

            if (implicitBounds.length > 1) {
                throw exceptionUnableToDetermineTypeOf(fieldOrMethod, "The type variable has multiple bounds:"
                        + Arrays.toString(implicitBounds));
            }

            if (implicitBounds[0] instanceof Class<?> boundClass && !Object.class.equals(boundClass)) {
                return boundClass;
            } else {
                throw exceptionUnableToDetermineTypeOf(fieldOrMethod,
                                                       "Cannot determine the bound of the type variable.");
            }
        }
        throw new IllegalArgumentException(
                "Unable to determine the type of the member " + getNameOf(fieldOrMethod));
    }

    private static IllegalArgumentException exceptionUnableToDetermineTypeOf(Member fieldOrMethod,
            String cause) {
        return new IllegalArgumentException("Unable to determine the type of the member " + getNameOf(fieldOrMethod)
                + ". " + cause);
    }

}
