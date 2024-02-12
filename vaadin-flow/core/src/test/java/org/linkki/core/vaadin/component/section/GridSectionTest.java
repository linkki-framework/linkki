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

package org.linkki.core.vaadin.component.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.creation.table.GridColumnWrapper;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.ui.table.column.annotation.UITableColumn.CollapseMode;
import org.linkki.core.vaadin.component.section.GridSectionTest.TestCollapsibleColumnTablePmo.TestCollapsibleColumnRowPmo;

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

        var menu = (MenuBar)rightHeaderComponents.get(0);

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

        var menu = (MenuBar)rightHeaderComponents.get(0);

        assertThat(menu.isVisible()).as("Column toggle should be not be visible as there are no collapsible columns")
                .isFalse();
    }

    @Test
    void testToggleVisibility() {
        GridSection section = (GridSection)VaadinUiCreator
                .createComponent(new TestCollapsibleColumnTablePmo(),
                                 new BindingContext());

        MenuItem collapsibleColumnMenuItem = getColumnMenu(section).stream()
                .filter(e -> e.getId().get().contentEquals("colmenu-collapsible")).findFirst().get();

        assertThat(collapsibleColumnMenuItem.isChecked()).isTrue();
        assertThat(section.getGrid().getColumnByKey("collapsible").isVisible()).isTrue();

        ComponentUtil.fireEvent(collapsibleColumnMenuItem, new ClickEvent<>(collapsibleColumnMenuItem));

        assertThat(collapsibleColumnMenuItem.isChecked()).isFalse();
        assertThat(section.getGrid().getColumnByKey("collapsible").isVisible()).isFalse();
    }

    @Test
    void testColumnMenuItemVisibilityChecked_Collapsible_ProgrammaticallyCollapsed() {
        GridSection section = (GridSection)VaadinUiCreator
                .createComponent(new TestCollapsibleColumnTablePmo(),
                                 new BindingContext());
        section.setColumnVisible("programaticallyCollapsed", false);

        List<MenuItem> columnMenuItems = getColumnMenu(section);
        assertThat(columnMenuItems.get(0).isChecked()).isTrue();
        assertThat(columnMenuItems.get(1).isChecked()).isFalse();
        assertThat(columnMenuItems.get(2).isChecked()).isFalse();
    }

    private List<MenuItem> getColumnMenu(GridSection section) {
        return section.getChildren().filter(MenuBar.class::isInstance)
                .map(MenuBar.class::cast).findFirst().get().getItems().get(0).getSubMenu().getItems();
    }

    @UISection(caption = "Test table with collapsible columns")
    public static class TestCollapsibleColumnTablePmo implements ContainerPmo<TestCollapsibleColumnRowPmo> {

        @Override
        public List<TestCollapsibleColumnRowPmo> getItems() {
            return List.of(new TestCollapsibleColumnRowPmo(), new TestCollapsibleColumnRowPmo());
        }

        @Override
        public int getPageLength() {
            return 0;
        }

        public static class TestCollapsibleColumnRowPmo {

            @UITableColumn(collapsible = CollapseMode.COLLAPSIBLE, flexGrow = 1)
            @UILabel(position = 10, label = "Collapsible")
            public String getCollapsible() {
                return "cell";
            }

            @UITableColumn(collapsible = CollapseMode.INITIALLY_COLLAPSED, flexGrow = 1)
            @UILabel(position = 20, label = "Initially collapsed")
            public String getInitiallyCollapsed() {
                return "cell";
            }

            @UITableColumn(collapsible = CollapseMode.NOT_COLLAPSIBLE, flexGrow = 1)
            @UILabel(position = 30, label = "Not collapsible")
            public String getNotCollapsible() {
                return "cell";
            }

            @UITableColumn(collapsible = CollapseMode.COLLAPSIBLE, flexGrow = 1)
            @UILabel(position = 40, label = "Programatically collapsed")
            public String getProgramaticallyCollapsed() {
                return "Programatically collapsed";
            }
        }
    }

    private List<Component> getRightHeaderComponents(LinkkiSection section) {
        return section.getChildren()
                .filter(c -> LinkkiSection.SLOT_RIGHT_HEADER_COMPONENTS
                        .contentEquals(c.getElement().getAttribute("slot")))
                .collect(Collectors.toList());
    }
}
