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

import org.linkki.core.binding.LinkkiBindingException;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Wrapper for a getter {@link Method}. {@link #canRead()} can safely be accessed even if no read method
 * exists. {@link #readValue(Object)} will access the getter via {@link LambdaMetafactory}.
 * 
 * @param <T> the type containing the property
 * @param <V> the property's type
 */
public class ReadMethod<T, V> extends AbstractMethod<T, Function<T, V>> {

    @CheckForNull
    private Function<T, V> getter;

    ReadMethod(PropertyAccessDescriptor<T, V> descriptor) {
        super(descriptor, descriptor.getReflectionReadMethod());
    }

    /* private */ ReadMethod(Class<? extends T> boundClass,
                             String propertyName,
                             Supplier<Optional<Method>> methodSupplier) {
        super(boundClass, propertyName, methodSupplier);
    }

    /**
     * Checks whether a read method exists.
     */
    public boolean canRead() {
        return hasMethod();
    }

    /**
     * Reads and returns the value by accessing the respective read method.
     *
     * @throws IllegalStateException if no read method exists
     * @throws RuntimeException if an error occurs while accessing the read method
     */
    public V readValue(T boundObject) {
        if (canRead()) {
            return readValueWithExceptionHandling(boundObject);
        } else {
            throw new IllegalStateException(
                    "Cannot find getter method for "
                            + boundObject.getClass().getName()
                            + "#" + getPropertyName());
        }
    }

    private V readValueWithExceptionHandling(T boundObject) {
        try {
            return getter().apply(boundObject);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new LinkkiBindingException(
                    "Cannot read value from object: " + boundObject + ", property: " + getPropertyName(), e);
        }
    }

    private Function<T, V> getter() {
        if (getter == null) {
            getter = getMethodAsFunction();
        }
        return getter;
    }

    @SuppressWarnings("unchecked")
    protected Function<T, V> fallbackReflectionCall(Method method) {
        return o -> {
            try {
                return (V)method.invoke(o);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new LinkkiBindingException(
                        "Cannot read value from object: " + o + ", property: " + getPropertyName(), e);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public Class<V> getReturnType() {
        return (Class<V>)getMethodWithExceptionHandling().getReturnType();
    }

    @Override
    protected CallSite getCallSite(Lookup lookup, MethodHandle methodHandle, MethodType func)
            throws LambdaConversionException {
        return LambdaMetafactory
                .metafactory(lookup,
                             "apply",
                             MethodType.methodType(Function.class),
                             MethodType.methodType(Object.class, Object.class),
                             methodHandle,
                             func);
    }

}
