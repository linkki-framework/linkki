package de.faktorzehn.ipm.web.binding.dispatcher.accessor;

import java.lang.reflect.Method;

/**
 * Base class for method wrappers. Allows the wrapped {@link Method java.lang.reflect.Method} to be
 * <code>null</code>.
 *
 * @author widmaier
 */
public abstract class AbstractMethod {

    private final Class<?> boundClass;
    private final String propertyName;
    private final Method reflectionMethod;

    /**
     * @param descriptor the descriptor for the property
     * @param reflectionMethod the method. May be <code>null</code>.
     */
    public AbstractMethod(PropertyAccessDescriptor descriptor, Method reflectionMethod) {
        boundClass = descriptor.getBoundClass();
        propertyName = descriptor.getPropertyName();
        this.reflectionMethod = reflectionMethod;
    }

    protected boolean hasMethod() {
        return getReflectionMethod() != null;
    }

    protected Method getReflectionMethod() {
        return reflectionMethod;
    }

    protected Object getBoundClass() {
        return boundClass;
    }

    protected String getPropertyName() {
        return propertyName;
    }

}