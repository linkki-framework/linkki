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

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Utility class to get easy access to the return value of a method or the current object of a field.
 * Simply use {@link #getValue(Object, Member)}.
 * <p>
 * The implementation may use any performance optimization like caching of access code but never caches
 * the returned value. Every call will retrieve the current value from the method or field.
 */
public class MemberAccessors {

    private static final Logger LOGGER = Logger.getLogger(MemberAccessors.class.getName());
    private static final Map<Member, Function<Object, Object>> ACCESSOR_CACHE = new ConcurrentHashMap<>();

    private static boolean reflectionWarning = true;

    private final Method method;

    private MemberAccessors(Method method) {
        this.method = method;
    }

    /**
     * Tries to create a function for the method using a {@link MethodHandle}.
     * <p>
     * If Spring devtools is used, the method may be in a class (e.g. PMO) that is in the restart classloader,
     * while the LookupProvider is in the app classloader, i.e. they are in different modules.
     * This leads to the situation that the Lookup that is created does not have MODULE access, which leads to
     * LambdaConversionException in AbstractValidatingLambdaMetafactory.
     * In this case, the implementation falls back to using the classic reflection API.
     */
     private Function<Object, Object> createFunction() {
        if (Void.TYPE.equals(method.getReturnType())) {
            throw new IllegalArgumentException("Cannot call void method using " + this);
        }

        try {
            return createFunctionWithMethodHandle();
        } catch (LambdaConversionException e) {
            logFallbackToReflection(e);
            return createFunctionWithReflection();
        }
    }

    private Function<Object, Object> createFunctionWithReflection() {
        return o -> {
            try {
                return method.invoke(o);
            } catch (IllegalAccessException | InvocationTargetException invokeException) {
                throw new IllegalArgumentException("Cannot retrieve value by using reflection " + this, invokeException);
            }
        };
    }

    @SuppressWarnings("unchecked")
    private Function<Object, Object> createFunctionWithMethodHandle() throws LambdaConversionException {
        Lookup lookup = LookupProvider.lookup(method.getDeclaringClass());
        MethodHandle methodHandle = getMethodHandle(lookup);
        CallSite site = getCallSite(lookup, methodHandle);
        try {
            return (Function<Object, Object>)site.getTarget().invoke();
            // CSOFF: IllegalCatch
        } catch (Throwable e) {
            //NOSONAR An exception should never be thrown as the invocation only creates the function
            throw new IllegalArgumentException("Cannot create getter function using method handle for " + this, e);
        }
        // CSON: IllegalCatch
    }

    private MethodHandle getMethodHandle(Lookup lookup) {
        try {
            return lookup.unreflect(method);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Can't get " + MethodHandle.class.getSimpleName() + " for "
                    + this, e);
        }
    }

    private CallSite getCallSite(Lookup lookup, MethodHandle methodHandle) throws LambdaConversionException {
        return LambdaMetafactory
                .metafactory(lookup,
                             "apply",
                             MethodType.methodType(Function.class),
                             MethodType.methodType(Object.class, Object.class),
                             methodHandle,
                             methodHandle.type());
    }

    private static void logFallbackToReflection(LambdaConversionException exception) {
        if (reflectionWarning) {
            LOGGER.warning(() -> """
                    Error during method invocation using method handle, falling back to reflection API. \
                    This is probably due to different class loaders in play, \
                    e.g. when using Spring Devtools. \
                    This should not happen in production. \
                    Configure a fine logging level for more information.
                    """);
            reflectionWarning = false;
        }
        LOGGER.fine(() -> "Cannot invoke method using method handle: " + exception.getMessage());
    }

    @Override
    public String toString() {
        return "accessor to " + getNameOf(method);
    }

    /**
     * Retrieve the value from the given member ({@link Field} or {@link Method}) in the given object by
     * calling the method or getting the value from the field. The returned value will directly be cast
     * to the expected type. Note that this is always an unsafe cast!
     *
     * @param <T> the expected type of the return value
     * @param object the object where to read the value from
     * @param fieldOrMethod a {@link Field} or {@link Method} to access the value
     * @return the value retrieved by the {@link Field} or {@link Method}
     * @throws IllegalArgumentException if the field is not accessible or the value cannot be retrieved.
     *             The cause of the original exception is included.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValue(Object object, Member fieldOrMethod) {
        if (fieldOrMethod instanceof Field) {
            return (T)getFieldValue(object, (Field)fieldOrMethod);
        } else if (fieldOrMethod instanceof Method) {
            Function<Object, Object> accessor = ACCESSOR_CACHE
                    .computeIfAbsent(fieldOrMethod, key -> new MemberAccessors((Method)key).createFunction());
            return (T)accessor.apply(object);
        } else {
            throw new IllegalArgumentException("Only field or method is supported, found "
                    + fieldOrMethod.getClass().getCanonicalName() + " as type of "
                    + getNameOf(fieldOrMethod));
        }
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

}
