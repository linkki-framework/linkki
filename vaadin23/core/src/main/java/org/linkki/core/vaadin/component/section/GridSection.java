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

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.ui.creation.table.GridColumnWrapper;
import org.linkki.core.ui.table.column.annotation.UITableColumn;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * A section containing a single table. This kind of section is created by the
 * {@link PmoBasedSectionFactory} if the presentation model object is a {@link ContainerPmo}.
 */
public class GridSection extends LinkkiSection {

    private static final long serialVersionUID = 1L;

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
        var menuBar = new MenuBar();
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
        columnItem.setCheckable(true);
        columnItem.setChecked(column.isVisible());
        columnItem.addClickListener(event -> toggleVisibility(column, event));
    }

    /* private */ void toggleVisibility(Grid.Column<?> c, ClickEvent<MenuItem> event) {
        c.setVisible(!c.isVisible());
        event.getSource().setChecked(c.isVisible());
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
