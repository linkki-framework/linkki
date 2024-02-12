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

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.ui.creation.table.GridColumnWrapper;
import org.linkki.core.ui.table.column.annotation.UITableColumn;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;

/**
 * A section containing a single table. This kind of section is created by the
 * {@link PmoBasedSectionFactory} if the presentation model object is a {@link ContainerPmo}.
 */
public class GridSection extends LinkkiSection {

    private static final long serialVersionUID = 1L;

    private static final String COLMENU_ID_PREFIX = "colmenu";

    private final MenuBar menuBar = new MenuBar();

    public GridSection(String caption, boolean closeable) {
        super(caption, closeable, 1);
    }

    /**
     * Returns the table shown in the section.
     */
    public Grid<?> getGrid() {
        return (Grid<?>)getContentWrapper().getComponentAt(0);
    }

    public void setGrid(Grid<?> grid) {
        requireNonNull(grid, "grid must not be null");
        getContentWrapper()
                .replace(getContentWrapper().getComponentCount() > 0 ? getContentWrapper().getComponentAt(0) : null,
                         grid);

        addRightHeaderComponent(createColumnCollapseToggleMenu(grid));
    }

    private MenuBar createColumnCollapseToggleMenu(Grid<?> grid) {
        menuBar.removeAll();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_ICON, MenuBarVariant.LUMO_TERTIARY_INLINE);
        var toggleItem = menuBar.addItem(VaadinIcon.MENU.create());

        grid.getColumns().stream()
                .filter(this::isColumnCollapsible)
                .forEach(column -> addSubMenuItem(toggleItem, column));

        if (toggleItem.getSubMenu().getItems().isEmpty()) {
            menuBar.setVisible(false);
        }
        return menuBar;
    }

    private Boolean isColumnCollapsible(Grid.Column<?> column) {
        return Optional.ofNullable(ComponentUtil.getData(column, UITableColumn.CollapseMode.class))
                .map(UITableColumn.CollapseMode::isCollapsible).orElse(false);
    }

    private void addSubMenuItem(MenuItem toggleItem, Grid.Column<?> column) {
        var header = Optional
                .ofNullable(ComponentUtil.getData(column, GridColumnWrapper.KEY_HEADER))
                .map(String::valueOf).orElse("");
        var columnItem = toggleItem.getSubMenu().addItem(header);
        columnItem.setId(getMenuItemKey(column));
        columnItem.setCheckable(true);
        columnItem.setChecked(column.isVisible());
        columnItem.addClickListener(event -> setColumnVisible(column.getKey(), !column.isVisible()));
    }

    /**
     * Use this method to set {@link Column} visibility after {@link GridSection} has already been
     * created. This method will also update the checked state of the {@link Column columns}
     * {@link MenuItem} correctly.
     * 
     * @param columnKey The {@link Column#getKey() key} of the {@link Column} to set the visible
     *            state
     * @param visible <code>true</code> sets the {@link Column} and the it's {@link MenuItem} to
     *            checked, otherwise <code>false</code> and unchecked
     */
    public void setColumnVisible(String columnKey, boolean visible) {
        Optional.ofNullable(getGrid().getColumnByKey(columnKey)).ifPresent(column -> {
            column.setVisible(visible);
            menuBar.getItems().get(0).getSubMenu().getItems().stream()
                    .filter(menuItem -> getMenuItemKey(column).contentEquals(menuItem.getId().orElse(""))).findFirst()
                    .ifPresent(i -> i.setChecked(visible));
        });
    }

    private String getMenuItemKey(Grid.Column<?> column) {
        return COLMENU_ID_PREFIX + "-" + column.getKey();
    }

    /**
     * @deprecated Use {@link #getGrid()} instead.
     */
    @Deprecated(since = "2.0.0")
    @Override
    public Grid<?> getSectionContent() {
        return getGrid();
    }

    @Override
    public String toString() {
        return "GridSection based on " + (getGrid() == null ? null : getGrid().getId());
    }
}
