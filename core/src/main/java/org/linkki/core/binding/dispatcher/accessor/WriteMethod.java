/*
 * Copyright Faktor Zehn AG.
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.jdt.annotation.Nullable;
import org.linkki.core.binding.LinkkiBindingException;

/**
 * Wrapper for a {@link Method}. {@link #canWrite()} can safely be accessed even if no write method
 * exists. {@link #writeValue(Object, Object)} will access the setter via reflection.
 */
public class WriteMethod extends AbstractMethod {

    public WriteMethod(PropertyAccessDescriptor descriptor) {
        super(descriptor, descriptor.getReflectionWriteMethod());
    }

    public boolean canWrite() {
        return hasMethod();
    }

    /**
     * Writes a value by accessing the respective write method.
     *
     * @param value the value to be written
     * @throws IllegalStateException if no write method exists
     * @throws RuntimeException if an error occurs while accessing the write method
     */
    public void writeValue(Object target, @Nullable Object value) {
        if (canWrite()) {
            writeValueWithExceptionHandling(target, value);
        } else {
            throw new IllegalStateException(
                    "Cannot write property " + target.getClass().getSimpleName() + "#" + getPropertyName() + " in "
                            + target);
        }
    }

    private void writeValueWithExceptionHandling(Object boundObject, @Nullable Object value) {
        try {
            invokeMethod(boundObject, value);
        } catch (IllegalAccessException e) {
            throw new LinkkiBindingException(
                    "Cannot access " + getBoundClass() + "#" + getPropertyName() + " for writing.",
                    e);
        } catch (IllegalArgumentException e) {
            throw new LinkkiBindingException(
                    "Cannot write value: " + value + " in " + getBoundClass() + "#" + getPropertyName(),
                    e);
        } catch (InvocationTargetException e) {
            throw new LinkkiBindingException(
                    "Cannot invoke write method on " + getBoundClass() + "#" + getPropertyName(), e);
        }
    }

    private void invokeMethod(Object boundObject, @Nullable Object value)
            throws IllegalAccessException, InvocationTargetException {
        getMethodWithExceptionHandling().invoke(boundObject, value);
    }

}
