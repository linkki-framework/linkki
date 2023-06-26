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
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Wrapper for a setter {@link Method}. {@link #isPresent()} can safely be accessed even if no write
 * method exists. {@link #writeValue(Object, Object)} will access the setter via {@link LambdaMetafactory} if possible,
 * falling back to using reflection API.
 * 
 * @param <T> the type containing the property
 * @param <V> the property's type
 */
final class WriteMethod<T, V> extends AbstractMethod<T, BiConsumer<T, V>> {

    WriteMethod(Class<? extends T> boundClass, String propertyName, Supplier<Optional<Method>> methodSupplier) {
        super(boundClass, propertyName, methodSupplier);
    }

    /**
     * Writes a value by accessing the respective write method.
     * 
     * @param value the value to be written
     * @throws IllegalArgumentException if the method to be invoked does not exist
     * @throws RuntimeException if an error occurs while accessing the write method
     */
    public void writeValue(T target, @CheckForNull V value) {
       getMethodAsFunction().accept(target, value);
    }

    protected BiConsumer<T, V> fallbackReflectionCall(Method method) {
        return (o, v) -> {
            try {
                method.invoke(o, v);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw errorCallingMethod(e).get();
            }
        };
    }

    @Override
    protected CallSite getCallSiteForFunction(Lookup lookup, MethodHandle methodHandle)
            throws LambdaConversionException {
        return LambdaMetafactory.metafactory(lookup,
                                             "accept",
                                             MethodType.methodType(BiConsumer.class),
                                             MethodType.methodType(Void.TYPE, Object.class, Object.class),
                                             methodHandle,
                                             wrap(methodHandle));
    }

    @Override
    protected BiConsumer<T, V> handleExceptionForMethodHandle(BiConsumer<T, V> methodAsFunction) {
        return (o, v) -> {
            try {
                methodAsFunction.accept(o, v);
                // CSOFF: IllegalCatch
                // Rewords all exceptions to make sure the bound class and property appear in the message
            } catch (RuntimeException e) {
                throw errorCallingMethod(e).get();
            }
            // CSON: IllegalCatch
        };
    }

    @Override
    public String toString() {
        return "write method for " + getBoundClass().getName() + "#" + getPropertyName();
    }
}
