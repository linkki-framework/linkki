/*
 * Copyright Faktor Zehn GmbH.
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
package org.linkki.core.binding.validation.message;

import java.io.Serializable;
import java.util.Objects;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * An instance of this class identifies a property in an object, e.g. the name property of a
 * specific person.
 */

public class ObjectProperty implements Serializable {

    private static final long serialVersionUID = -6116979544231081015L;

    private final Object object;
    @CheckForNull
    private final String property;

    private final int index;

    private final int hashCode;

    /**
     * Creates a new ObjectProperty. If the property is a list or an array the index can specify the
     * position within the property. An index smaller than 0 indicates that it is not an indexed
     * property.
     */
    public ObjectProperty(Object object, @CheckForNull String property, int index) {
        this.object = Objects.requireNonNull(object, "object must not be null");
        this.property = property;
        this.index = index;
        hashCode = createHashCode();
    }

    /**
     * Creates a new ObjectProperty.
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
        hash = property != null ? property.hashCode() + 31 * hash : hash;
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
    @CheckForNull
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
            return Objects.equals(object, other.object) && index == other.index
                    && Objects.equals(property, other.property);
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
