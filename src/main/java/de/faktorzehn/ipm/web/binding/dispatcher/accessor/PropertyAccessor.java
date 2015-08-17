package de.faktorzehn.ipm.web.binding.dispatcher.accessor;

/**
 * Allows reading and writing a value from/to an object's property. Also provides the value class of
 * the property.
 * <p>
 * For accessing an object's property, create a {@link PropertyAccessor} for the class to be
 * accessed and the name of the property. The accessor can determine whether the property can be
 * read or written.
 *
 * @author widmaier
 */
public class PropertyAccessor {

    private final String propertyName;
    private final ReadMethod readMethod;
    private final WriteMethod writeMethod;

    public PropertyAccessor(Class<?> boundClass, String propertyName) {
        this.propertyName = propertyName;

        PropertyAccessDescriptor propertyAccessDescriptor = new PropertyAccessDescriptor(boundClass, propertyName);
        readMethod = propertyAccessDescriptor.createReadMethod();
        writeMethod = propertyAccessDescriptor.createWriteMethod();
    }

    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Reads the property's value.
     *
     * @throws IllegalStateException if no getter can be found
     */
    public Object getPropertyValue(Object boundObject) {
        return readMethod.readValue(boundObject);
    }

    /**
     * Only writes the value, if necessary. That is if the property's current value is different
     * from the value to be written. This avoids infinite feedback loops in data binding, as most
     * fields fire events if their value changes.
     * <p>
     * However, if the property is write only the value will always be written (as the current value
     * cannot be retrieved in this case).
     *
     * @throws IllegalStateException if no setter can be found
     */
    public void setPropertyValue(Object boundObject, Object value) {
        if (requiresWrite(boundObject, value)) {
            writeMethod.writeValue(boundObject, value);
        }
    }

    private boolean requiresWrite(Object boundObject, Object value) {
        return !canRead() || !valuesEqual(value, readMethod.readValue(boundObject));
    }

    private boolean valuesEqual(Object value, Object readValue) {
        if (value == null || readValue == null) {
            return value == null && readValue == null ? true : false;
        } else {
            return value.equals(readValue);
        }
    }

    /**
     * @return <code>true</code> if there is a read method (getter) for the given object and
     *         property
     */
    public boolean canWrite() {
        return writeMethod.canWrite();
    }

    /**
     * @return <code>true</code> if there is a write method (setter) for the given object and
     *         property
     */
    public boolean canRead() {
        return readMethod.canRead();
    }

    /**
     * @return the return type of the getter, i.e. class of the value the read method returns.
     * @throws IllegalStateException if there is no read method for this property
     */
    public Class<?> getValueClass() {
        return readMethod.getReturnType();
    }
}
