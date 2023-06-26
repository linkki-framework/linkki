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
package org.linkki.util.reflection.accessor;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import org.linkki.util.ExceptionSupplier;
import org.linkki.util.reflection.LookupProvider;


import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static org.linkki.util.Objects.requireNonNull;

/**
 * Base class for method wrappers. Allows the wrapped {@link Method java.lang.reflect.Method} to be
 * <code>null</code>.
 *
 * @param <T> the type containing the property
 * @param <I> type of the method as a functional interface
 */
abstract sealed class AbstractMethod<T, I> permits ReadMethod, WriteMethod, InvokeMethod {

    private static final Logger LOGGER = Logger.getLogger(AbstractMethod.class.getName());
    private static boolean reflectionWarning = true;

    private final Class<? extends T> boundClass;
    private final String propertyName;
    private final Supplier<Optional<Method>> methodSupplier;
    @CheckForNull
    private I methodAsFunction;

    AbstractMethod(Class<? extends T> boundClass,
                             String propertyName,
                             Supplier<Optional<Method>> methodSupplier) {
        this.boundClass = requireNonNull(boundClass, "boundClass must not be null");
        this.propertyName = requireNonNull(propertyName, "propertyName must not be null");
        this.methodSupplier = requireNonNull(methodSupplier, "methodSupplier must not be null");
    }

    public boolean isPresent() {
        return getReflectionMethod().isPresent();
    }

    private Optional<Method> getReflectionMethod() {
        return methodSupplier.get();
    }

    protected Class<? extends T> getBoundClass() {
        return boundClass;
    }

    protected String getPropertyName() {
        return propertyName;
    }

    /**
     * Performance optimization. Avoid creating exception supplier to save memory.
     *
     * @return the read method if existent. Otherwise, throws an IllegalArgumentException.
     */
    protected Method getMethodWithExceptionHandling() {
        return getReflectionMethod()
                .orElseThrow(ExceptionSupplier.illegalStateException("Found no " + this));
    }

    /**
     * Exception that should be thrown if the method could not be called.
     */
    protected Supplier<IllegalStateException> errorCallingMethod(Throwable cause) {
        return ExceptionSupplier.illegalStateException("Error calling " + this, cause);
    }

    /**
     * Uses {@link LambdaMetafactory} to create an implementation of the functional interface that
     * references the method found via reflection.
     * <p>
     * If the method handle cannot be used due to multiple classloaders in play, {@link #fallbackReflectionCall(Method)}
     * is called instead.
     * <p>
     * The created function throws an {@link IllegalStateException} if any error occurs during invocation.
     * This makes sure that the behavior is consistent with {@link #fallbackReflectionCall(Method)}.
     *
     * @throws IllegalArgumentException if the method cannot be found
     */
    public I getMethodAsFunction() {
        if (methodAsFunction == null) {
            var method = getMethodWithExceptionHandling();
            var lookup = LookupProvider.lookup(method.getDeclaringClass());
            var methodHandle = getMethodHandle(method, lookup);
            try {
                var callSite = getCallSiteForFunction(lookup, methodHandle);
                var function = invokeCallSiteForFunction(callSite, method);
                methodAsFunction = handleExceptionForMethodHandle(function);
            } catch (LambdaConversionException e) {
                logFallbackToReflection(e);
                methodAsFunction = fallbackReflectionCall(method);
            }
        }
        return methodAsFunction;
    }

    /**
     * Handles the exception that may be thrown when calling the method using method handle.
     * Any exceptions that occur should be wrapped in {@link #errorCallingMethod(Throwable)}.
     */
    protected abstract I handleExceptionForMethodHandle(I methodAsFunction);

    @SuppressWarnings("unchecked")
    private I invokeCallSiteForFunction(CallSite callSite, Method method) {
        try {
            return (I)callSite.getTarget().invoke();
            // CSOFF: IllegalCatch
            // In the invoke method throws a throwable, so we cannot catch a more specific exception
        } catch (Throwable e) {
            throw new IllegalStateException("Can't create function for " + method, e);
        }
        // CSON: IllegalCatch
    }

    private static MethodHandle getMethodHandle(Method method, Lookup lookup) {
        try {
            return lookup.unreflect(method);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Can't get " + MethodHandle.class.getSimpleName() + " for "
                    + method, e);
        }
    }

    /**
     * Wraps the method type to avoid problems with primitive parameters in Java 11
     */
    protected MethodType wrap(MethodHandle methodHandle) {
        return methodHandle.type().wrap().changeReturnType(Void.TYPE);
    }

    protected abstract CallSite getCallSiteForFunction(Lookup lookup, MethodHandle methodHandle)
            throws LambdaConversionException;

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
        LOGGER.fine(() -> "Cannot call method using method handle: " + exception.getMessage());
    }


    /**
     * Creates a function that should call the method by using reflection API.
     * Any exceptions that occur should be wrapped in {@link #errorCallingMethod(Throwable)}.
     */
    protected abstract I fallbackReflectionCall(Method method);
}