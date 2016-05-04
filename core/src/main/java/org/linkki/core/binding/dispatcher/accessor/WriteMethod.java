package org.linkki.core.binding.dispatcher.accessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wrapper for a {@link Method}. {@link #canWrite()} can safely be accessed even if no write method
 * exists. {@link #writeValue(Object, Object)} will access the setter via reflection.
 *
 * @author widmaier
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
    public void writeValue(Object target, Object value) {
        if (canWrite()) {
            writeValueWithExceptionHandling(target, value);
        } else {
            throw new IllegalStateException("Cannot write property \"" + getPropertyName() + "\" in object " + target);
        }
    }

    private void writeValueWithExceptionHandling(Object boundObject, Object value) {
        try {
            invokeMethod(boundObject, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    "Cannot access class: " + getBoundClass() + ", property: " + getPropertyName() + " for writing.",
                    e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(
                    "Cannot write value: " + value + " in object: " + boundObject + ", property: " + getPropertyName(),
                    e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(
                    "Cannot invoke write method on class: " + getBoundClass() + ", property: " + getPropertyName(), e);
        }
    }

    private void invokeMethod(Object boundObject, Object value)
            throws IllegalAccessException, InvocationTargetException {
        getMethodWithExceptionHandling().invoke(boundObject, value);
    }

}
