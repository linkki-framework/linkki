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

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Method;
import java.util.List;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ContainerBinding;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectAnnotationReader;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyAnnotationReader;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.columnbased.ColumnBasedComponentCreator;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.defaults.style.LinkkiTheme;
import org.linkki.core.uicreation.ComponentAnnotationReader;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.treegrid.TreeGrid;

/**
 * A {@link ColumnBasedComponentCreator} that creates a {@link Grid} component.
 */
public class GridComponentCreator {

    private static final String TABLE_ID_SUFFIX = "_table";

    private GridComponentCreator() {
        // utility class
    }

    /**
     * Create a new {@link Grid} component based on the container PMO.
     */
    public static <ROW> Grid<ROW> createGrid(ContainerPmo<ROW> containerPmo, BindingContext bindingContext) {
        requireNonNull(containerPmo, "containerPmo must not be null");
        requireNonNull(bindingContext, "bindingContext must not be null");
        AbstractGridComponentWrapper<ROW> gridWrapper = createComponent(containerPmo);
        List<LinkkiAspectDefinition> tableAspects = AspectAnnotationReader
                .createAspectDefinitionsFor(containerPmo.getClass());
        ContainerBinding binding = bindingContext
                .bindContainer(containerPmo, BoundProperty.empty(), tableAspects, gridWrapper);
        createColumns(containerPmo, gridWrapper, binding);
        // need to update binding after columns are created because the footer content cannot be updated
        // without columns
        binding.updateFromPmo();
        return gridWrapper.getComponent();
    }

    /**
     * Creates a new table based on the container PMO.
     */
    private static <ROW> AbstractGridComponentWrapper<ROW> createComponent(ContainerPmo<ROW> containerPmo) {
        if (containerPmo.isHierarchical()) {
            TreeGrid<ROW> grid = new TreeGrid<>();
            grid.addClassName("tree-table");
            grid.setWidth("100%");
            return new TreeGridComponentWrapper<>(containerPmo.getClass().getSimpleName() + TABLE_ID_SUFFIX, grid);
        } else {
            Grid<ROW> grid = new Grid<>();
            applyStyle(grid);
            return new GridComponentWrapper<>(containerPmo.getClass().getSimpleName() + TABLE_ID_SUFFIX, grid);
        }
    }

    private static <ROW> void applyStyle(Grid<ROW> grid) {
        grid.addClassName(LinkkiTheme.TABLE);
        grid.setWidth("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS,
                              GridVariant.LUMO_ROW_STRIPES);
    }


    private static <ROW> void createColumns(ContainerPmo<ROW> containerPmo,
            ComponentWrapper tableWrapper,
            BindingContext bindingContext) {
        Class<?> rowPmoClass = containerPmo.getItemPmoClass();

        ComponentAnnotationReader.getComponentDefinitionMethods(rowPmoClass)
                .forEach(m -> initColumn(containerPmo, tableWrapper, bindingContext, m));
    }

    /**
     * Creates a new column for a field of a PMO and applies all
     * {@link LinkkiAspectDefinition#supports(org.linkki.core.binding.wrapper.WrapperType) supported}
     * {@link LinkkiAspectDefinition LinkkiAspectDefinitions}.
     * 
     * @param elementDesc the descriptor for the PMO's field
     * @param tableWrapper the {@link ComponentWrapper} that wraps the table
     */
    private static <ROW> void initColumn(ContainerPmo<ROW> containerPmo,
            ComponentWrapper tableWrapper,
            BindingContext bindingContext,
            Method m) {
        @SuppressWarnings("unchecked")
        Grid<ROW> grid = (Grid<ROW>)tableWrapper.getComponent();
        BoundProperty boundProperty = BoundPropertyAnnotationReader.getBoundProperty(m);
        Column<ROW> column = createComponentColumn(m, grid, bindingContext);
        column.setKey(boundProperty.getPmoProperty());
        column.setResizable(true);
        List<LinkkiAspectDefinition> aspectDefs = AspectAnnotationReader.createAspectDefinitionsFor(m);
        bindingContext.bind(containerPmo.getItemPmoClass(), boundProperty,
                            aspectDefs,
                            new GridColumnWrapper(column));
    }

    private static <ROW> Column<ROW> createComponentColumn(Method m, Grid<ROW> grid, BindingContext bindingContext) {
        ComponentColumnProvider<ROW> columnGen = new ComponentColumnProvider<>(m, bindingContext);
        if (grid instanceof TreeGrid && grid.getColumns().size() == 0) {
            return ((TreeGrid<ROW>)grid).addComponentHierarchyColumn(columnGen);
        } else {
            return grid.addComponentColumn(columnGen);
        }
    }

}