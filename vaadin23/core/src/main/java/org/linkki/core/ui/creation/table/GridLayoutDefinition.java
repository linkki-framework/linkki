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

import java.lang.reflect.Method;
import java.util.List;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectAnnotationReader;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyAnnotationReader;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.uicreation.ComponentAnnotationReader;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.treegrid.TreeGrid;

public class GridLayoutDefinition implements LinkkiLayoutDefinition {

    @Override
    public void createChildren(Object parentComponent, Object pmo, BindingContext bindingContext) {
        createColumns((ContainerPmo<?>)pmo, (Grid<?>)parentComponent, bindingContext);
        // need to update binding after columns are created because the footer content
        // cannot be updated without columns
        bindingContext.uiUpdated();
    }

    private void createColumns(ContainerPmo<?> containerPmo,
            Grid<?> grid,
            BindingContext bindingContext) {
        Class<?> rowPmoClass = containerPmo.getItemPmoClass();
        ComponentAnnotationReader.getComponentDefinitionMethods(rowPmoClass)
                .forEach(m -> initColumn(containerPmo, grid, bindingContext, m));
    }


    /**
     * Creates a new column for a field of a PMO and applies all
     * {@link LinkkiAspectDefinition#supports(org.linkki.core.binding.wrapper.WrapperType) supported}
     * {@link LinkkiAspectDefinition LinkkiAspectDefinitions}.
     * 
     * @param elementDesc the descriptor for the PMO's field
     * @param tableWrapper the {@link ComponentWrapper} that wraps the table
     */
    private void initColumn(ContainerPmo<?> containerPmo,
            Grid<?> grid,
            BindingContext bindingContext,
            Method m) {
        BoundProperty boundProperty = BoundPropertyAnnotationReader.getBoundProperty(m);
        Column<?> column = createComponentColumn(m, grid, bindingContext);
        column.setKey(boundProperty.getPmoProperty());
        column.setResizable(true);
        List<LinkkiAspectDefinition> aspectDefs = AspectAnnotationReader.createAspectDefinitionsFor(m);
        bindingContext.bind(containerPmo.getItemPmoClass(), boundProperty,
                            aspectDefs,
                            new GridColumnWrapper(column));
    }

    private <ROW> Column<ROW> createComponentColumn(Method m, Grid<ROW> grid, BindingContext bindingContext) {
        ComponentColumnProvider<ROW> columnGen = new ComponentColumnProvider<>(m, bindingContext);
        if (grid instanceof TreeGrid && grid.getColumns().size() == 0) {
            return ((TreeGrid<ROW>)grid).addComponentHierarchyColumn(columnGen);
        } else {
            return grid.addComponentColumn(columnGen);
        }
    }
}
