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
import static org.linkki.util.ExceptionSupplier.illegalArgumentException;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Base class for method wrappers. Allows the wrapped {@link Method java.lang.reflect.Method} to be
 * <code>null</code>.
 * 
 * @param <T> the type containing the property
 */
public abstract class AbstractMethod<@NonNull T> {

    private final Class<? extends T> boundClass;
    private final String propertyName;
    private final Supplier<Optional<Method>> methodSupplier;

    /**
     * @param descriptor the descriptor for the property
     * @param methodSupplier the {@link Supplier} for the {@link Method}. May return
     *            {@link Optional#empty()}.
     */
    public AbstractMethod(PropertyAccessDescriptor<T, ?> descriptor, Supplier<Optional<Method>> methodSupplier) {
        this.methodSupplier = requireNonNull(methodSupplier, "methodSupplier must not be null");
        requireNonNull(descriptor, "descriptor must not be null");

        boundClass = descriptor.getBoundClass();
        propertyName = descriptor.getPropertyName();
    }

    protected boolean hasMethod() {
        return getReflectionMethod().isPresent();
    }

    protected Optional<Method> getReflectionMethod() {
        return methodSupplier.get();
    }

    protected Class<? extends T> getBoundClass() {
        return boundClass;
    }

    protected String getPropertyName() {
        return propertyName;
    }

    protected Supplier<@NonNull IllegalArgumentException> noMethodFound(String accessMethodName) {
        return illegalArgumentException("Found no " + accessMethodName + "for " + getBoundClass() + "#"
                + getPropertyName());
    }

    /**
     * Performance optimization. Avoid creating exception supplier to save memory.
     * 
     * @return the read method if existent. Otherwise throws an IllegalArgumentException.
     */
    protected Method getMethodWithExceptionHandling() {
        if (hasMethod()) {
            return getReflectionMethod().get();
        } else {
            throw noMethodFound(this.getClass().getSimpleName()).get();
        }
    }

    /**
     * Uses {@link LambdaMetafactory} to create an implementation of the given functional interface
     * that references the {@link #getReflectionMethod() method found via reflection}.
     */
    protected <X> X getMethodAs(Class<X> functionalInterface) {
        Method method = getMethodWithExceptionHandling();
        Lookup lookup = getLookup(method.getDeclaringClass());
        MethodHandle methodHandle = getMethodHandle(method, lookup);
        MethodType func = methodHandle.type();
        CallSite site = getCallSite(lookup, methodHandle, func, functionalInterface);
        try {
            return (X)site.getTarget().invoke();
            // CSOFF: IllegalCatch
        } catch (Throwable e) {
            throw new IllegalStateException("Can't create " + functionalInterface.getSimpleName() + " for "
                    + method);
        }
        // CSON: IllegalCatch
    }

    private static Lookup getLookup(Class<?> clazz) {
        Constructor<Lookup> constructor = getPackagePrivateLookupConstructor();
        try {
            return constructor.newInstance(clazz);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new IllegalStateException("Can't create " + Lookup.class.getSimpleName() + " for "
                    + clazz.getName());
        }
    }

    private static Constructor<Lookup> getPackagePrivateLookupConstructor() {
        try {
            Constructor<Lookup> constructor = Lookup.class.getDeclaredConstructor(Class.class);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException | SecurityException e1) {
            throw new IllegalStateException("Can't access constructor " + Lookup.class.getSimpleName() + '('
                    + Class.class.getSimpleName() + ')');
        }
    }

    private static MethodHandle getMethodHandle(Method method, Lookup lookup) {
        try {
            return lookup.unreflect(method);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Can't get " + MethodHandle.class.getSimpleName() + " for "
                    + method, e);
        }
    }

    private static CallSite getCallSite(Lookup lookup, MethodHandle methodHandle, MethodType func, Class<?> type) {
        try {
            Method method = Arrays.stream(type.getDeclaredMethods())
                    .filter(m -> Modifier.isAbstract(m.getModifiers()))
                    .filter(m -> !Modifier.isStatic(m.getModifiers()))
                    .reduce((a, b) -> {
                        throw new IllegalArgumentException(
                                type + " is no @" + FunctionalInterface.class.getSimpleName());
                    }).orElseGet(() -> {
                        throw new IllegalArgumentException(
                                type + " is no @" + FunctionalInterface.class.getSimpleName());
                    });
            return LambdaMetafactory
                    .metafactory(lookup, method.getName(), MethodType.methodType(type),
                                 MethodType.methodType(method.getReturnType(), method.getParameterTypes()),
                                 methodHandle, func);
        } catch (LambdaConversionException e) {
            throw new IllegalStateException("Can't create " + CallSite.class.getSimpleName() + " for "
                    + methodHandle);
        }
    }

}