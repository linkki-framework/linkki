/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.message;

import java.io.Serializable;


/**
 * An instance of this class identifies a property in an object, e.g. the name property of a
 * specific person.
 * <p>
 * To add custom information that additionally qualifies the object property, it is possible to
 * implement and use an {@link IPropertyQualifier}.
 */

public class ObjectProperty implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -3407760096164658253L;

    private final Object object;

    private final String property;

    private final int index;

    private final int hashCode;

    private final IPropertyQualifier qualifier;

    /**
     * Creates a new ObjectProperty. If the property is a list or an array the index can specify the
     * position within the property. An index smaller than 0 indicates that it is not an indexed
     * property.
     * <p>
     * It is possible to provide additional information using the qualifier and implementing the
     * interface {@link IPropertyQualifier}.
     */
    public ObjectProperty(Object object, String property, int index, IPropertyQualifier qualifier) {
        this.object = object;
        this.property = property;
        this.index = index;
        this.qualifier = qualifier;
        hashCode = createHashCode();
    }

    /**
     * Creates a new ObjectProperty.
     * <p>
     * It is possible to provide additional information using the qualifier and implementing the
     * interface {@link IPropertyQualifier}.
     */
    public ObjectProperty(Object object, String property, IPropertyQualifier qualifier) {
        this(object, property, -1, qualifier);
    }

    /**
     * Creates a new ObjectProperty. If the property is a list or an array the index can specify the
     * position within the property. An index smaller than 0 indicates that it is not an indexed
     * property.
     */
    public ObjectProperty(Object object, String property, int index) {
        this(object, property, index, null);
    }

    /**
     * Creates an ObjectProperty that characterizes the object and the name of the property.
     */
    public ObjectProperty(Object object, String property) {
        this(object, property, -1);
    }

    /**
     * Creates an ObjectProperty that characterizes only the object but not a specific property of
     * it.
     */
    public ObjectProperty(Object object) {
        this(object, null, -1);
    }

    private int createHashCode() {
        int hash = object.hashCode() + index;
        hash = property == null ? hash : 31 * hash + property.hashCode();
        hash = qualifier == null ? hash : 31 * hash + qualifier.hashCode();
        return hash;
    }

    /**
     * The object that is identified by this {@link ObjectProperty}.
     * 
     */
    public Object getObject() {
        return object;
    }

    /**
     * The name of the property that is identified by this {@link ObjectProperty}. The property name
     * should be available as bean property in the given object.
     * 
     */
    public String getProperty() {
        return property;
    }

    /**
     * In case of {@link #getObject()} is an array or list this index defines which object of the
     * index is referenced.
     * 
     * @return The index of the referenced object in the array/list that is referenced by
     *         {@link #getObject()}. Returns -1 if there is no index available.
     * 
     * @see #hasIndex()
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the {@link IPropertyQualifier} defined at the instantiation of this
     * {@link ObjectProperty}.
     * 
     * @return an {@link IPropertyQualifier} containing additional information or <code>null</code>
     *         if no qualifier exists.
     */
    public IPropertyQualifier getQualifier() {
        return qualifier;
    }

    /**
     * Returns whether this {@link ObjectProperty} has an index that identifies an object in an
     * array or list.
     * 
     * @return <code>true</code> if this {@link ObjectProperty} references an index, false if there
     *         is no index available.
     */
    public boolean hasIndex() {
        return index >= 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ObjectProperty) {
            ObjectProperty other = (ObjectProperty)obj;
            return ObjectUtil.equals(object, other.object) && index == other.index
                    && ObjectUtil.equals(property, other.property) && ObjectUtil.equals(qualifier, other.qualifier);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return String.valueOf(object) + "." + property;
    }

}
