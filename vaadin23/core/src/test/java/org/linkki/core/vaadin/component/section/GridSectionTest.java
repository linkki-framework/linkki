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

package org.linkki.core.vaadin.component.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.ui.creation.table.GridColumnWrapper;
import org.linkki.core.ui.table.column.annotation.UITableColumn.CollapseMode;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.menubar.MenuBar;

class GridSectionTest {

    @Test
    void testSetGrid_noPreviousGrid() {
        GridSection gridSection = new GridSection("caption", true);

        Grid<Object> grid = new Grid<>();
        gridSection.setGrid(grid);

        assertThat(gridSection.getGrid()).isEqualTo(grid);
    }

    @Test
    void testSetGrid_replacePreviousGrid() {
        GridSection gridSection = new GridSection("caption", true);
        gridSection.setGrid(new Grid<>());
        assertThatNoException().isThrownBy(gridSection::getGrid);

        Grid<Object> anotherGrid = new Grid<>();
        gridSection.setGrid(anotherGrid);

        assertThat(gridSection.getGrid()).isEqualTo(anotherGrid);
    }

    @Test
    void testSetGrid_columnToggle() {
        var grid = new Grid<>();
        var collapsibleColumn = grid.addColumn(String::valueOf);
        ComponentUtil.setData(collapsibleColumn, CollapseMode.class, CollapseMode.COLLAPSIBLE);
        ComponentUtil.setData(collapsibleColumn, GridColumnWrapper.KEY_HEADER, "collapsible");
        var notCollapsibleColumn = grid.addColumn(String::valueOf);
        ComponentUtil.setData(notCollapsibleColumn, CollapseMode.class, CollapseMode.NOT_COLLAPSIBLE);
        ComponentUtil.setData(notCollapsibleColumn, GridColumnWrapper.KEY_HEADER, "not collapsible");
        var initiallyCollapsedColumn = grid.addColumn(String::valueOf);
        ComponentUtil.setData(initiallyCollapsedColumn, CollapseMode.class, CollapseMode.INITIALLY_COLLAPSED);
        ComponentUtil.setData(initiallyCollapsedColumn, GridColumnWrapper.KEY_HEADER, "initially collapsed");
        initiallyCollapsedColumn.setVisible(false);

        var gridSection = new GridSection("caption", false);
        gridSection.setGrid(grid);
        var rightHeaderComponents = getRightHeaderComponents(gridSection);

        assertThat(rightHeaderComponents).hasSize(1);
        assertThat(rightHeaderComponents.get(0)).isInstanceOf(MenuBar.class);

        var menu = (MenuBar) rightHeaderComponents.get(0);

        assertThat(menu.isVisible()).as("Column toggle should be visible as there are collapsible columns").isTrue();

        var toggleItem = menu.getItems().get(0);
        var toggleColumns = toggleItem.getSubMenu().getItems();

        assertThat(toggleColumns.stream().map(MenuItem::getText))
                .containsExactly("collapsible", "initially collapsed");
        assertThat(toggleColumns.stream().map(MenuItem::isChecked)).containsExactly(true, false);
    }

    @Test
    void testSetGrid_columnToggle_noCollapsibleColumn() {
        var grid = new Grid<>();
        var column1 = grid.addColumn(String::valueOf);
        ComponentUtil.setData(column1, CollapseMode.class, CollapseMode.NOT_COLLAPSIBLE);
        ComponentUtil.setData(column1, GridColumnWrapper.KEY_HEADER, "column1");
        var column2 = grid.addColumn(String::valueOf);
        ComponentUtil.setData(column2, CollapseMode.class, CollapseMode.NOT_COLLAPSIBLE);
        ComponentUtil.setData(column2, GridColumnWrapper.KEY_HEADER, "column2");

        var gridSection = new GridSection("caption", false);
        gridSection.setGrid(grid);
        var rightHeaderComponents = getRightHeaderComponents(gridSection);

        assertThat(rightHeaderComponents).hasSize(1);
        assertThat(rightHeaderComponents.get(0)).isInstanceOf(MenuBar.class);

        var menu = (MenuBar) rightHeaderComponents.get(0);

        assertThat(menu.isVisible()).as("Column toggle should be not be visible as there are no collapsible columns")
                .isFalse();
    }

    @Test
    void testToggleVisibility() {
        var grid = new Grid<>();
        var column = grid.addColumn(String::valueOf);
        ComponentUtil.setData(column, CollapseMode.class, CollapseMode.COLLAPSIBLE);
        ComponentUtil.setData(column, GridColumnWrapper.KEY_HEADER, "column");
        var gridSection = new GridSection("caption", false);
        gridSection.setGrid(grid);
        var menuItem = ((MenuBar)getRightHeaderComponents(gridSection).get(0)).getItems().get(0).getSubMenu().getItems()
                .get(0);
        assertThat(menuItem.isChecked()).isTrue();
        assertThat(column.isVisible()).isTrue();

        gridSection.toggleVisibility(column, new ClickEvent<>(menuItem));

        assertThat(menuItem.isChecked()).isFalse();
        assertThat(column.isVisible()).isFalse();

    }

    private List<Component> getRightHeaderComponents(LinkkiSection section) {
        return section.getChildren()
                .filter(c -> LinkkiSection.SLOT_RIGHT_HEADER_COMPONENTS
                        .contentEquals(c.getElement().getAttribute("slot")))
                .collect(Collectors.toList());
    }
}
