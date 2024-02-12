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
     * @apiNote <em>As for {@link ContainerPmo#getItems()}, the child items and row PMOs should not
     *          be recreated on each call!</em>
     *          <p>
     *          You can use a {@link SimpleItemSupplier} to create the rows on demand.
     */
    List<? extends R> getChildRows();

    /**
     * Returns whether this row PMO has child rows.
     * 
     * @implSpec The default implementation checks whether {@link #getChildRows()} is empty.
     * 
     * @deprecated This method is unused and will be removed.
     */
    @Deprecated(since = "2.4.0")
    default boolean hasChildRows() {
        return !getChildRows().isEmpty();
    }

}
