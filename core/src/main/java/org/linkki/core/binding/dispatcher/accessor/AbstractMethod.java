package org.linkki.core.binding.dispatcher.accessor;

import static java.util.Objects.requireNonNull;
import static org.linkki.util.ExceptionSupplier.illegalArgumentException;

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
     * @param reflectionMethod the method. May be {@link Optional#empty()}.
     */
    public AbstractMethod(PropertyAccessDescriptor descriptor, Optional<Method> reflectionMethod) {
        this.reflectionMethod = requireNonNull(reflectionMethod, "reflectionMethod must not be null");
        requireNonNull(descriptor, "descriptor must not be null");
        boundClass = descriptor.getBoundClass();
        propertyName = descriptor.getPropertyName();
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

    protected Supplier<IllegalArgumentException> noMethodFound(String accessMethodName) {
        return illegalArgumentException("Found no " + accessMethodName + "in class: " + getBoundClass() + ", property: "
                + getPropertyName());
    }

    /**
     * Performance optimization. Avoid creating exception supplier to save memory.
     * 
     * @return the read method if existent. Otherwise throws an IllegalArgumentException.
     */
    protected Method getMethodWithExceptionHandling() {
        if (hasMethod()) {
            return getReflectionMethod().get();
        } else {
            throw noMethodFound(this.getClass().getSimpleName()).get();
        }
    }

}