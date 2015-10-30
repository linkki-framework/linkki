package de.faktorzehn.ipm.web.binding.dispatcher.accessor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * This class wraps a {@link PropertyDescriptor} for a bound class and a specified property
 * identified by its name. It could create a {@link WriteMethod} or {@link ReadMethod} object for
 * the bound class and property using the methods {@link #createWriteMethod()} and
 * {@link #createReadMethod()}.
 *
 */
public class PropertyAccessDescriptor {

    private final Class<?> boundClass;
    private final String propertyName;
    private final Optional<PropertyDescriptor> propertyDescriptor;

    public PropertyAccessDescriptor(Class<?> boundClass, String propertyName) {
        this.boundClass = boundClass;
        this.propertyName = propertyName;

        propertyDescriptor = findDescriptor();
    }

    public WriteMethod createWriteMethod() {
        return new WriteMethod(this);
    }

    public ReadMethod createReadMethod() {
        return new ReadMethod(this);
    }

    Optional<Method> getReflectionWriteMethod() {
        return propertyDescriptor.map(pd -> pd.getWriteMethod());
    }

    Optional<Method> getReflectionReadMethod() {
        return propertyDescriptor.map(pd -> pd.getReadMethod());
    }

    private Optional<PropertyDescriptor> findDescriptor() {
        return getDefaultMethodPropertyDescriptor();
    }

    private Optional<PropertyDescriptor> getDefaultMethodPropertyDescriptor() {
        return PropertyDescriptorFactory.createPropertyDescriptor(getBoundClass(), getPropertyName());
    }

    public Class<?> getBoundClass() {
        return boundClass;
    }

    public String getPropertyName() {
        return propertyName;
    }

}
