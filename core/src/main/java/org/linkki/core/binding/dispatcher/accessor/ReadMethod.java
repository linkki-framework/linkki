package org.linkki.core.binding.dispatcher.accessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wrapper for a {@link Method}. {@link #canRead()} can safely be accessed even if no read method
 * exists. {@link #readValue(Object)} will access the getter via reflection.
 *
 * @author widmaier
 */
public class ReadMethod extends AbstractMethod {

    public ReadMethod(PropertyAccessDescriptor descriptor) {
        super(descriptor, descriptor.getReflectionReadMethod());
    }

    public boolean canRead() {
        return hasMethod();
    }

    /**
     * Reads and returns the value by accessing the respective read method.
     *
     * @throws IllegalStateException if no read method exists
     * @throws RuntimeException if an error occurs while accessing the read method
     */
    public Object readValue(Object boundObject) {
        if (canRead()) {
            return readValueWithExceptionHandling(boundObject);
        } else {
            throw new IllegalStateException("Cannot read property \"" + getPropertyName() + "\".");
        }
    }

    private Object readValueWithExceptionHandling(Object boundObject) {
        try {
            return invokeMethod(boundObject);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    "Cannot access class: " + getBoundClass() + ", property: " + getPropertyName() + " for reading.",
                    e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(
                    "Cannot read value from object: " + boundObject + ", property: " + getPropertyName(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(
                    "Cannot invoke read method on class: " + getBoundClass() + ", property: " + getPropertyName(), e);
        }
    }

    private Object invokeMethod(Object boundObject) throws IllegalAccessException, InvocationTargetException {
        return getMethodWithExceptionHandling().invoke(boundObject);
    }

    public Class<?> getReturnType() {
        return getMethodWithExceptionHandling().getReturnType();
    }

}
