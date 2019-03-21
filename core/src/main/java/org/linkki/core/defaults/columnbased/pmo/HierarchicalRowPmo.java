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

package org.linkki.core.defaults.columnbased.pmo;

import java.util.List;

/**
 * Marks a row PMO used in a {@link ContainerPmo} as hierarchical. This may lead to a UI that
 * shows/hides child rows below their parents.
 * 
 * @param <R> the type of the child row presentation model objects of this row PMO
 */
public interface HierarchicalRowPmo<R> {

    /**
     * Returns the list of this row's child row PMOs. May be empty if this is a leaf node.
     * 
     * @implSpec <em>As for {@link ContainerPmo#getItems()}, the child items and row PMOs should not be
     *           recreated on each call!</em>
     *           <p>
     *           You can use a {@link SimpleItemSupplier} to create the rows on demand.
     *           <p>
     *           If {@link #getChildRows()} is an expensive calculation, overwrite
     *           {@link #hasChildRows()} to return the number of child rows without actually creating
     *           them.
     * 
     * @return a list of child row PMOs
     */
    List<? extends R> getChildRows();

    /**
     * Returns whether this row PMO has child rows.
     * 
     * @implNote the default implementation checks whether {@link #getChildRows()} is empty.
     * @implSpec if {@link #getChildRows()} is an expensive calculation, overwrite
     *           {@link #hasChildRows()} to return the number of child rows without actually creating
     *           them.
     * @return whether this row PMO has child rows
     */
    default boolean hasChildRows() {
        return !getChildRows().isEmpty();
    }

}
