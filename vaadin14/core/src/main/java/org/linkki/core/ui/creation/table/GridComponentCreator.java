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

import java.util.Optional;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.columnbased.ColumnBasedComponentCreator;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.ComponentStyles;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.grid.Grid;
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
    @SuppressWarnings("unchecked")
    public static <ROW> Grid<ROW> createGrid(ContainerPmo<ROW> containerPmo, BindingContext bindingContext) {
        requireNonNull(containerPmo, "containerPmo must not be null");
        requireNonNull(bindingContext, "bindingContext must not be null");
        Grid<ROW> grid = (Grid<ROW>)UiCreator
                .createComponent(containerPmo, bindingContext,
                                 new GridComponentDefinition(GridVariant.LUMO_NO_ROW_BORDERS,
                                         GridVariant.LUMO_ROW_STRIPES,
                                         GridVariant.LUMO_NO_BORDER),
                                 c -> createComponentWrapper(containerPmo, c),
                                 Optional.of(new GridLayoutDefinition()))
                .getComponent();
        // cannot set in component definition as it would be overwritten in UiCreator
        grid.setId(containerPmo.getClass().getSimpleName() + TABLE_ID_SUFFIX);
        ComponentStyles.setOverflowAuto(grid);
        return grid;
    }

    /**
     * Creates a new table based on the container PMO.
     */
    public static AbstractGridComponentWrapper<?> createComponentWrapper(ContainerPmo<?> containerPmo,
            Object grid) {
        if (containerPmo.isHierarchical()) {
            return new TreeGridComponentWrapper<>((TreeGrid<?>)grid);
        } else {
            return new GridComponentWrapper<>((Grid<?>)grid);
        }
    }
}