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

import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.theme.LinkkiTheme;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.treegrid.TreeGrid;

public class GridComponentDefinition implements LinkkiComponentDefinition {

    private final GridVariant[] variants;

    public GridComponentDefinition(GridVariant... variants) {
        this.variants = variants;
    }

    @Override
    public Object createComponent(Object pmo) {
        ContainerPmo<?> containerPmo = (ContainerPmo<?>)pmo;
        if (containerPmo.isHierarchical()) {
            TreeGrid<?> grid = new TreeGrid<>();
            grid.setSelectionMode(SelectionMode.NONE);
            grid.addClassName("tree-table");
            grid.setWidth("100%");
            grid.addThemeVariants(variants);
            return grid;
        } else {
            Grid<?> grid = new Grid<>();
            grid.setSelectionMode(SelectionMode.NONE);
            grid.addClassName(LinkkiTheme.TABLE);
            grid.setWidth("100%");
            grid.addThemeVariants(variants);
            return grid;
        }
    }
}