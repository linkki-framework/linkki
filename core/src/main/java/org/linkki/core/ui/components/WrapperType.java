/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.core.ui.components;

import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * A wrapper type helps to distinguish different kinds of component wrappers. It is used to describe
 * aspects that only support specific kinds of components.
 * <p>
 * The {@link WrapperType} is hierarchical, which means every {@link WrapperType} (expect of the root)
 * has a parent {@link WrapperType}. When a {@link ComponentWrapper} is responsible for a specific
 * wrapper type it is also responsible for every sub-component-type.
 * 
 * @see ComponentWrapper#getType()
 * @see LinkkiAspectDefinition#supports(WrapperType)
 */
public class WrapperType {

    /**
     * The root for all other {@link WrapperType types}.
     */
    public static final WrapperType ROOT = new WrapperType("", null);

    /**
     * A wrapper type for all common components, parent type for other concrete components.
     */
    public static final WrapperType COMPONENT = new WrapperType("component", ROOT);

    /**
     * A wrapper type for all kinds of layout components like sections or panels.
     */
    public static final WrapperType LAYOUT = new WrapperType("layout", COMPONENT);

    /**
     * A wrapper type for all kinds of fields, that means input fields as well as buttons etc.
     */
    public static final WrapperType FIELD = new WrapperType("field", COMPONENT);

    private final String name;

    @CheckForNull
    private final WrapperType parent;

    private WrapperType(String name, WrapperType parent) {
        this.name = name;
        this.parent = parent;
    }

    /**
     * Creates a new {@link WrapperType} with {@link #ROOT} as parent.
     * 
     * @param name the name of the new {@link WrapperType}
     * @return the new {@link WrapperType}
     */
    public static WrapperType of(String name) {
        return new WrapperType(name, ROOT);
    }

    /**
     * Creates a new {@link WrapperType} with the specified parent.
     * 
     * @param name the name of the new {@link WrapperType}
     * @return the new {@link WrapperType}
     */
    public static WrapperType of(String name, WrapperType parent) {
        return new WrapperType(name, parent);
    }

    /**
     * Returns the parent of this {@link WrapperType}. It is only <code>null</code> if this is the root
     * type
     * 
     * @return the parent {@link WrapperType} of this type
     * @see #isRoot()
     */
    @CheckForNull
    public WrapperType getParent() {
        return parent;
    }

    /**
     * Returns {@code true} if this {@link WrapperType} is exactly the {@link #ROOT} type.
     * 
     * @return {@code true} if this is the root, {@code false} if not
     */
    public boolean isRoot() {
        return this == ROOT;
    }

    /**
     * Returns whether this type is either equal to the given type or this is equal to one of the
     * parents of type. In other words: the given type is the same or a subtype of this.
     * 
     * @param type another {@link WrapperType} that should be the same or a subtype of this
     * @return {@code true} if the given type is the same or a subtype of this, otherwise {@code false}
     */
    public boolean isAssignableFrom(@CheckForNull WrapperType type) {
        return this.equals(type) || (type != null && isAssignableFrom(type.getParent()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        if (parent != null) {
            result = parent.hashCode() + result * prime;
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        WrapperType other = (WrapperType)obj;
        if (!name.equals(other.name)) {
            return false;
        }
        if (parent == null) {
            if (other.parent != null) {
                return false;
            }
        } else if (!parent.equals(other.parent)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WrapperType [" + name + ", parent=" + parent + "]";
    }


}
