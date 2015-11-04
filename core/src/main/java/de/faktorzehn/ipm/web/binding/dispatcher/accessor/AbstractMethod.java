package de.faktorzehn.ipm.web.binding.dispatcher.accessor;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Base class for method wrappers. Allows the wrapped {@link Method java.lang.reflect.Method} to be
 * <code>null</code>.
 *
 * @author widmaier
 */
public abstract class AbstractMethod {

    private final Class<?> boundClass;
    private final String propertyName;
    private final Optional<Method> reflectionMethod;

    /**
     * @param descriptor the descriptor for the property
     * @param reflectionMethod the method. May be <code>null</code>.
     */
    public AbstractMethod(PropertyAccessDescriptor descriptor, Optional<Method> reflectionMethod) {
        boundClass = descriptor.getBoundClass();
        propertyName = descriptor.getPropertyName();
        this.reflectionMethod = reflectionMethod;
    }

    protected boolean hasMethod() {
        return getReflectionMethod().isPresent();
    }

    protected Optional<Method> getReflectionMethod() {
        return reflectionMethod;
    }

    protected Object getBoundClass() {
        return boundClass;
    }

    protected String getPropertyName() {
        return propertyName;
    }

    protected Supplier<RuntimeException> noMethodFound(String accessMethodName) {
        return () -> new IllegalArgumentException(
                "Found no " + accessMethodName + "in class: " + getBoundClass() + ", property: " + getPropertyName());
    }

}