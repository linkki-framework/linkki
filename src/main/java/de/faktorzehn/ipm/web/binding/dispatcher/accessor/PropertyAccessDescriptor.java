package de.faktorzehn.ipm.web.binding.dispatcher.accessor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

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
    private final PropertyDescriptor propertyDescriptor;

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

    Method getReflectionWriteMethod() {
        return propertyDescriptor == null ? null : propertyDescriptor.getWriteMethod();
    }

    Method getReflectionReadMethod() {
        return propertyDescriptor == null ? null : propertyDescriptor.getReadMethod();
    }

    private PropertyDescriptor findDescriptor() {
        BeanInfo beanInfo = getBeanInfo();
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            if (getPropertyName().equals(descriptor.getName())) {
                return descriptor;
            }
        }
        return null;
    }

    private BeanInfo getBeanInfo() {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(getBoundClass());
            return beanInfo;
        } catch (IntrospectionException e) {
            throw new RuntimeException("Cannot find bean info for class " + getBoundClass(), e);
        }
    }

    public Class<?> getBoundClass() {
        return boundClass;
    }

    public String getPropertyName() {
        return propertyName;
    }

}
