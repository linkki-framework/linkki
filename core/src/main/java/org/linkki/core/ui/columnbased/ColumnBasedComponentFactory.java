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

package org.linkki.core.ui.columnbased;

import static java.util.Objects.requireNonNull;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ContainerBinding;
import org.linkki.core.binding.descriptor.UIAnnotationReader;
import org.linkki.core.binding.property.BoundProperty;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.table.ContainerPmo;

/**
 * Factory to create a column based component like table or grid from a {@link ContainerPmo}.
 * <p>
 * This implementation is independent of any specific UI framework but uses a
 * {@link ColumnBasedComponentCreator} to create the UI framework specific components.
 */
public class ColumnBasedComponentFactory {

    private final ColumnBasedComponentCreator containerComponentCreator;

    public ColumnBasedComponentFactory(ColumnBasedComponentCreator containerComponentCreator) {
        this.containerComponentCreator = requireNonNull(containerComponentCreator,
                                                        "containerComponentCreator must not be null");
    }

    /**
     * Create a new table based on the container PMO.
     */
    public Object createContainerComponent(ContainerPmo<?> containerPmo, BindingContext bindingContext) {
        ComponentWrapper tableWrapper = containerComponentCreator
                .createComponent(requireNonNull(containerPmo, "containerPmo must not be null"));
        ContainerBinding binding = requireNonNull(bindingContext, "bindingContext must not be null")
                .bindContainer(containerPmo, BoundProperty.of(""),
                               containerComponentCreator.getContainerAspects(),
                               tableWrapper);
        createColumns(containerPmo, tableWrapper, binding);
        // need to update binding after columns are created because the footer content cannot be updated
        // without columns
        binding.updateFromPmo();
        return tableWrapper.getComponent();
    }

    private void createColumns(ContainerPmo<?> containerPmo, ComponentWrapper tableWrapper, ContainerBinding binding) {
        Class<?> rowPmoClass = containerPmo.getItemPmoClass();
        UIAnnotationReader annotationReader = new UIAnnotationReader(rowPmoClass);
        annotationReader.getUiElements()
                .forEach(e -> containerComponentCreator.initColumn(containerPmo, tableWrapper, binding, e));
    }

}