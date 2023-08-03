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

import org.linkki.util.BeanUtils;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Wrapper for a getter {@link Method}. {@link #isPresent()} can safely be accessed even if no read method
 * exists. {@link #readValue(Object)} will access the getter via {@link LambdaMetafactory} if possible,
 * falling back to using reflection API.
 * 
 * @param <T> the type containing the property
 * @param <V> the property's type
 */
final class ReadMethod<T, V> extends AbstractMethod<T, Function<T, V>> {

    ReadMethod(Class<? extends T> boundClass,
                             String propertyName,
                             Supplier<Optional<Method>> methodSupplier) {
        super(boundClass, propertyName, methodSupplier);
    }

    @SuppressWarnings("unchecked")
    public ReadMethod(Method method) {
        super((Class<? extends T>)method.getDeclaringClass(),
              BeanUtils.getPropertyName(method), () -> Optional.of(method));
    }

    /**
     * Reads and returns the value by accessing the respective read method.
     *
     * @throws IllegalStateException if no read method exists
     * @throws RuntimeException if an error occurs while accessing the read method
     */
    public V readValue(T boundObject) {
        return getMethodAsFunction().apply(boundObject);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Function<T, V> fallbackReflectionCall(Method method) {
        return o -> {
            try {
                return (V)method.invoke(o);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw errorCallingMethod(e).get();
            }
        };
    }

    @Override
    protected Function<T, V> handleExceptionForMethodHandle(Function<T, V> methodAsFunction) {
        return o -> {
            try {
                return methodAsFunction.apply(o);
                // CSOFF: IllegalCatch
                // Rewords all exceptions to make sure the bound class and property appear in the message
            } catch (RuntimeException e) {
                throw errorCallingMethod(e).get();
            }
            // CSON: IllegalCatch
        };
    }

    @SuppressWarnings("unchecked")
    public Class<V> getReturnType() {
        return (Class<V>)getMethodWithExceptionHandling().getReturnType();
    }

    @Override
    protected CallSite getCallSiteForFunction(Lookup lookup, MethodHandle methodHandle)
            throws LambdaConversionException {
        return LambdaMetafactory
                .metafactory(lookup,
                             "apply",
                             MethodType.methodType(Function.class),
                             MethodType.methodType(Object.class, Object.class),
                             methodHandle,
                             methodHandle.type());
    }

    @Override
    public String toString() {
        return "read method for " + getBoundClass().getName() + "#" + getPropertyName();
    }
}
