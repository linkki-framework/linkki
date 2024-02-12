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
package org.linkki.core.defaults.columnbased.pmo;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.columnbased.aspects.annotation.BindTableFooter;
import org.linkki.core.defaults.columnbased.aspects.annotation.BindTableItems;
import org.linkki.core.defaults.columnbased.aspects.annotation.BindTablePageLength;
import org.linkki.core.pmo.ButtonPmo;

/**
 * A container of PMOs that has itself the role of a PMO for table controls (and maybe for other
 * controls displaying a set of objects).
 * 
 * @param <ROW> a PMO class annotated with linkki annotations for fields and/or table columns. May
 *            implement {@link HierarchicalRowPmo} to create a hierarchical table.
 * 
 * @implSpec if you want to create a hierarchical table, either your PMO class must implement
 *           {@link HierarchicalRowPmo} or you must override {@link #isHierarchical()} to return
 *           {@code true} and have only the non-leaf nodes implement {@link HierarchicalRowPmo}
 */
@BindTableFooter
@BindTableItems
@BindTablePageLength
public interface ContainerPmo<ROW> {

    /** Default page length to use when no other page length is set. */
    public static final int DEFAULT_PAGE_LENGTH = 15;

    /**
     * Returns the class of the items / rows in the container.
     * 
     * @implNote The default implementation reads the generic type {@literal<ROW>} from the class
     *           definition.
     */
    @SuppressWarnings({ "unchecked" })
    default Class<? extends ROW> getItemPmoClass() {
        Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(getClass(), ContainerPmo.class);
        for (Entry<TypeVariable<?>, Type> typeArgument : typeArguments.entrySet()) {
            TypeVariable<?> key = typeArgument.getKey();
            if (key.getGenericDeclaration().equals(ContainerPmo.class)) {
                return (Class<ROW>)typeArgument.getValue();
            }
        }
        throw new IllegalArgumentException("Cannot identify row pmo type for " + getClass().getName());
    }

    /**
     * Returns the items / rows in the container.
     * 
     * @implSpec !!! Important Note !!!
     *           <p>
     *           The container MUST return a list with identical items if the rows to be displayed
     *           haven't changed. This is not given if you create a new list with new item PMOs on
     *           each call of the {@link #getItems()} method. If you don't adhere to this rule, the
     *           contents of the table will be replaces each time you leave a cell after you have
     *           changed a value. This results in poor performance and the focus gets lost each time
     *           you leave a cell (after changing the value).
     *           <p>
     *           If you create the item PMOs based on a list of model objects, the easiest way is to
     *           use the {@link SimpleItemSupplier}.
     */
    List<ROW> getItems();

    /**
     * Returns a {@link TableFooterPmo} that provides the data for the table footer.
     * 
     * @return The PMO that provides the data for the footer or an empty optional if no footer
     *         should be shown (default).
     */
    default Optional<TableFooterPmo> getFooterPmo() {
        return Optional.empty();
    }

    /**
     * Returns a {@link ButtonPmo} for the add button in the table section.
     * 
     * @implSpec If you plan to {@link BindingContext#removeBindingsForPmo(Object) remove bindings
     *           from a binding context} later, make sure to not create a new instance on every call
     *           to this method.
     */
    default Optional<ButtonPmo> getAddItemButtonPmo() {
        return Optional.empty();
    }

    /**
     * Returns the current page length.
     * 
     * @implNote Default is 15 to activate the paging mechanism.
     * @implSpec Return 0 to deactivate table paging.
     */
    default int getPageLength() {
        return DEFAULT_PAGE_LENGTH;
    }

    /**
     * Returns whether the data contained in this container is hierarchical.
     * 
     * @implNote The default implementation checks whether the generic PMO class implements
     *           {@link HierarchicalRowPmo}.
     * 
     * @return whether the data contained in this container is hierarchical
     */
    default boolean isHierarchical() {
        return HierarchicalRowPmo.class.isAssignableFrom(getItemPmoClass());
    }
}
