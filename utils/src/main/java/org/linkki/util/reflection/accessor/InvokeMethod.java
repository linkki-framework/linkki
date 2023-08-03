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

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Wrapper for a void {@link Method} without parameters. {@link #isPresent()} can safely be accessed
 * even if the method to be invoked does not exist. {@link #invoke(Object)} will access the method via
 * {@link LambdaMetafactory} if possible, falling back to using reflection API.
 *
 * @param <T> the type containing the method
 */
final class InvokeMethod<T> extends AbstractMethod<T, Consumer<T>> {

    InvokeMethod(Class<? extends T> boundClass,
                               String propertyName,
                               Supplier<Optional<Method>> methodSupplier) {
        super(boundClass, propertyName, methodSupplier);
    }

    /**
     * Invokes the method.
     *
     * @throws IllegalStateException if the method to be invoked does not exist
     * @throws RuntimeException if an error occurs while accessing the method
     */
    public void invoke(T target) {
        getMethodAsFunction().accept(target);
    }

    @Override
    protected CallSite getCallSiteForFunction(Lookup lookup, MethodHandle methodHandle)
            throws LambdaConversionException {
        return LambdaMetafactory.metafactory(lookup,
                                             "accept",
                                             MethodType.methodType(Consumer.class),
                                             MethodType.methodType(Void.TYPE, Object.class),
                                             methodHandle,
                                             wrap(methodHandle));
    }

    @Override
    protected Consumer<T> handleExceptionForMethodHandle(Consumer<T> methodAsFunction) {
        return o -> {
            try {
                methodAsFunction.accept(o);
                // CSOFF: IllegalCatch
                // Rewords all exceptions to make sure the bound class and property appear in the message
            } catch (RuntimeException e) {
                throw errorCallingMethod(e).get();
            }
            // CSON: IllegalCatch
        };
    }

    @Override
    protected Consumer<T> fallbackReflectionCall(Method method) {
        return o -> {
            try {
                method.invoke(o);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw errorCallingMethod(e).get();
            }
        };
    }

    @Override
    public String toString() {
        return "invoke method for " + getBoundClass().getName() + "#" + getPropertyName();
    }
}
