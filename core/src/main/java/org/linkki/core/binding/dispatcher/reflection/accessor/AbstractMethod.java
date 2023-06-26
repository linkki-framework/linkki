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
package org.linkki.core.binding.dispatcher.reflection.accessor;

import static java.util.Objects.requireNonNull;
import static org.linkki.util.ExceptionSupplier.illegalArgumentException;

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

import org.linkki.util.LookupProvider;

/**
 * Base class for method wrappers. Allows the wrapped {@link Method java.lang.reflect.Method} to be
 * <code>null</code>.
 *
 * @param <T> the type containing the property
 * @param <I> type of the method as a functional interface
 */
public abstract class AbstractMethod<T, I> {

    private static final Logger LOGGER = Logger.getLogger(AbstractMethod.class.getName());
    private static boolean reflectionWarning = true;

    private final Class<? extends T> boundClass;
    private final String propertyName;
    private final Supplier<Optional<Method>> methodSupplier;

    /**
     * @param descriptor the descriptor for the property
     * @param methodSupplier the {@link Supplier} for the {@link Method}. May return
     *            {@link Optional#empty()}.
     */
    protected AbstractMethod(PropertyAccessDescriptor<T, ?> descriptor, Supplier<Optional<Method>> methodSupplier) {
        this(requireNonNull(descriptor, "descriptor must not be null").getBoundClass(),
                descriptor.getPropertyName(), methodSupplier);
    }

    /**
     * Constructor for testing
     */
    /* private */ AbstractMethod(Class<? extends T> boundClass,
                             String propertyName,
                             Supplier<Optional<Method>> methodSupplier) {
        this.boundClass = requireNonNull(boundClass, "boundClass must not be null");
        this.propertyName = requireNonNull(propertyName, "propertyName must not be null");
        this.methodSupplier = requireNonNull(methodSupplier, "methodSupplier must not be null");
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

    protected Supplier<IllegalArgumentException> noMethodFound(String accessMethodName) {
        return illegalArgumentException("Found no " + accessMethodName + "for " + getBoundClass() + "#"
                + getPropertyName());
    }

    /**
     * Performance optimization. Avoid creating exception supplier to save memory.
     *
     * @return the read method if existent. Otherwise, throws an IllegalArgumentException.
     */
    protected Method getMethodWithExceptionHandling() {
        return getReflectionMethod().orElseThrow(() -> noMethodFound(this.getClass().getSimpleName()).get());
    }

    /**
     * Uses {@link LambdaMetafactory} to create an implementation of the functional interface that
     * references the {@link #getReflectionMethod() method found via reflection}.
     * <p>
     * If the method handle cannot be used due to multiple classloaders in play, {@link #fallbackReflectionCall(Method)}
     * is called instead.
     */
    protected I getMethodAsFunction() {
        Method method = getMethodWithExceptionHandling();
        Lookup lookup = LookupProvider.lookup(method.getDeclaringClass());
        MethodHandle methodHandle = getMethodHandle(method, lookup);
        MethodType func = methodHandle.type();
        try {
            var callSite = getCallSite(lookup, methodHandle, func);
            return invokeCallSite(callSite, method);
            // CSON: IllegalCatch
        } catch (LambdaConversionException e) {
            logFallbackToReflection(e);
            return fallbackReflectionCall(method);
        }
    }

    @SuppressWarnings("unchecked")
    private I invokeCallSite(CallSite callSite, Method method) {
        try {
            return (I)callSite.getTarget().invoke();
            // CSOFF: IllegalCatch
        } catch (Throwable e) {
            throw new IllegalStateException("Can't create function for " + method, e);
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

    /**
     * Wraps the method type to avoid problems with primitive parameters in Java 11
     */
    protected MethodType wrap(MethodHandle methodHandle) {
        return methodHandle.type().wrap().changeReturnType(Void.TYPE);
    }

    protected abstract CallSite getCallSite(Lookup lookup, MethodHandle methodHandle, MethodType func)
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
        LOGGER.fine(() -> "Cannot invoke method using method handle: " + exception.getMessage());
    }

    protected abstract I fallbackReflectionCall(Method method);
}