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
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.linkki.core.binding.LinkkiBindingException;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Wrapper for a void {@link Method} without parameters. {@link #canInvoke()} can safely be accessed
 * even if the method to be invoked does not exist. {@link #invoke(Object)} will access the method via
 * {@link LambdaMetafactory}.
 * 
 * @param <T> the type containing the method
 */
public class InvokeMethod<T> extends AbstractMethod<T, Consumer<T>> {

    @CheckForNull
    private Consumer<T> invoker;

    public InvokeMethod(PropertyAccessDescriptor<T, ?> descriptor) {
        super(descriptor, descriptor.getReflectionInvokeMethod());
    }

    /* private */ InvokeMethod(Class<? extends T> boundClass,
                               String propertyName,
                               Supplier<Optional<Method>> methodSupplier) {
        super(boundClass, propertyName, methodSupplier);
    }

    /**
     * Checks whether the method to be invoked exists.
     */
    public boolean canInvoke() {
        return hasMethod();
    }

    /**
     * Invokes the method.
     *
     * @throws IllegalStateException if the method to be invoked does not exist
     * @throws RuntimeException if an error occurs while accessing the method
     */
    public void invoke(T target) {
        try {
            invoker().accept(target);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new LinkkiBindingException(
                    String.format("Error invoking method %s#%s", getBoundClass(), getPropertyName()), e);
        }
    }

    @Override
    protected CallSite getCallSite(Lookup lookup, MethodHandle methodHandle, MethodType func)
            throws LambdaConversionException {
        return LambdaMetafactory.metafactory(lookup,
                                             "accept",
                                             MethodType.methodType(Consumer.class),
                                             MethodType.methodType(Void.TYPE, Object.class),
                                             methodHandle,
                                             wrap(methodHandle));
    }

    private Consumer<T> invoker() {
        if (invoker == null) {
            invoker = getMethodAsFunction();
        }
        return invoker;
    }

    protected Consumer<T> fallbackReflectionCall(Method method) {
        return o -> {
            try {
                method.invoke(o);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new LinkkiBindingException(
                        String.format("Error invoking method %s#%s", getBoundClass(), getPropertyName()), e);
            }
        };
    }

}
