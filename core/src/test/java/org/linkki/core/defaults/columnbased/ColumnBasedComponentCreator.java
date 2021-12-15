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

package org.linkki.core.defaults.columnbased;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.PropertyElementDescriptors;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;

/**
 * Used to create UI framework specific column based components like table or grid.
 * <p>
 * 
 * @implSpec This interface is intended to be implemented by UI framework specific implementation. It is
 *           called by a {@link ColumnBasedComponentFactory} to create the framework specific
 *           implementation.
 */
public interface ColumnBasedComponentCreator {

    /**
     * Creates a new {@link ComponentWrapper}.
     * 
     * @param containerPmo the presentation model object for the container
     * @return the wrapper for the component that contains the columns, like a table or a grid component
     */
    ComponentWrapper createComponent(ContainerPmo<?> containerPmo);

    /**
     * Initializes a single column for the previously created column based component.
     * 
     * @implSpec The implementation is responsible to dynamically create the needed UI elements for
     *           every row in this column, that means the field in the cell. It is also responsible to
     *           register and to remove the bindings for these UI elements.
     * @param containerPmo the presentation model object for the whole container
     * @param parentWrapper the component wrapper that contains the column based component (table or
     *            grid)
     * @param bindingContext the {@link BindingContext} used to bind the column and field properties
     * @param elementDesc the {@link PropertyElementDescriptors} that describes the content of the
     *            column and UI element in the cell
     */
    void initColumn(ContainerPmo<?> containerPmo,
            ComponentWrapper parentWrapper,
            BindingContext bindingContext,
            PropertyElementDescriptors elementDesc);
}