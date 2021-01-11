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
package org.linkki.core.ui.creation.table;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;

import com.vaadin.flow.component.grid.Grid;

/**
 * A factory to create a table based on a {@link ContainerPmo}.
 */
public class PmoBasedTableFactory {

    private final ContainerPmo<?> containerPmo;
    private final BindingContext bindingContext;

    /**
     * Creates a new factory.
     * 
     * @param containerPmo The container providing the contents and column definitions.
     * @param bindingContext The binding context to which the cell bindings are added.
     */
    public PmoBasedTableFactory(ContainerPmo<?> containerPmo, BindingContext bindingContext) {
        this.containerPmo = containerPmo;
        this.bindingContext = bindingContext;
    }

    public Grid<?> createTable() {
        return new GridComponentCreator().createGridComponent(containerPmo,
                                                              bindingContext);
    }

}
