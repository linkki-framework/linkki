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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.linkki.core.defaults.columnbased.ColumnBasedComponentWrapper;
import org.linkki.core.defaults.columnbased.pmo.TableFooterPmo;
import org.linkki.core.ui.wrapper.VaadinComponentWrapper;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.data.provider.ListDataProvider;

/**
 * Wraps a vaadin {@link Grid}.
 * 
 * @param <ROW> a class annotated with linkki annotations used as PMO for a row in the table
 */
public class GridComponentWrapper<ROW> extends VaadinComponentWrapper
        implements ColumnBasedComponentWrapper<ROW> {

    private static final long serialVersionUID = 1L;

    private final List<ROW> items = new ArrayList<>();

    public GridComponentWrapper(String id, Grid<ROW> grid) {
        super(grid, COLUMN_BASED_TYPE);
        grid.setId(id);
        grid.setDataProvider(new ListDataProvider<>(items));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Grid<ROW> getComponent() {
        return (Grid<ROW>)super.getComponent();
    }

    @Override
    public void setLabel(String labelText) {
        // label not supported
    }

    /**
     * Updates the footer in all columns.
     * 
     * If the given {@link Optional}&lt;{@link TableFooterPmo}&gt; is {@link Optional#empty() empty}, no
     * footer will be visible, otherwise the the column footers will be set according to
     * {@link TableFooterPmo#getFooterText(String)}.
     */
    @Override
    public void updateFooter(Optional<TableFooterPmo> footerPmo) {
        Grid<ROW> grid = getComponent();
        if (footerPmo.isPresent()) {
            for (Column<ROW> column : grid.getColumns()) {
                column.setFooter(footerPmo.get().getFooterText(column.getKey()));
            }
        }
        // TODO LIN-2130
        // else {
        // cannot remove footer https://github.com/vaadin/vaadin-grid/issues/2006
        // }
    }

    /**
     * Sets the {@link Grid#setPageSize(int) page size} of the grid.
     */
    @Override
    public void setPageLength(int pageLength) {
        // TODO LIN-2126
        if (pageLength < 1 && !getComponent().isHeightByRows()) {
            getComponent().setHeightByRows(true);
        } else {
            if (getComponent().isHeightByRows()) {
                getComponent().setHeightByRows(false);
            }
            if (getComponent().getPageSize() != pageLength) {
                getComponent().setPageSize(pageLength);
            }
            int headerAndFooter = 1 + getComponent().getFooterRows().size();
            getComponent().setHeight((pageLength + headerAndFooter) * 3 + "em");
        }
    }

    /**
     * Sets the items displayed in the {@link Grid} by updating the items in the data provider if the
     * given items are not the ones currently displayed.
     */
    @Override
    public void setItems(List<ROW> newItems) {
        // TODO LIN-2088
        // boolean hasChildChanged = updateChildren(newItems);
        // if (hasChildChanged || hasItemListChanged(newItems)) {
        if (hasItemListChanged(newItems)) {
            items.clear();
            items.addAll(newItems);
            getComponent().getDataProvider().refreshAll();
        }
    }

    private boolean hasItemListChanged(List<ROW> newItems) {
        // TODO LIN-2088
        // muss für hierarchical noch aktualisiert werden
        return !this.items.equals(newItems);
        // return !tableContainer.rootItemIds().equals(items);
    }

    // TODO LIN-2088
    // /**
    // * @see #updateChildren(ROW)
    // */
    // private boolean updateChildren(List<? extends ROW> newItems) {
    // boolean changed = false;
    // for (ROW item : newItems) {
    // changed |= updateChildren(item);
    // }
    // return changed;
    // }
    //
    // /**
    // * Updates the children stored in the underlying container. Stored children that do not match the
    // * ones present on the given item are updated accordingly. Children of items that are not visible
    // * are ignored.
    // *
    // * A call to {@link LinkkiInMemoryContainer#setItems(Collection)} is required if this method
    // returns
    // * {@code true}.
    // *
    // * @return {@code true} if the underlying container changed as a result of the call
    // */
    // private boolean updateChildren(ROW item) {
    // return false;
    // if (getComponent() instanceof com.vaadin.v7.ui.TreeTable) {
    // boolean expanded = !((com.vaadin.v7.ui.TreeTable)getComponent()).isCollapsed(item);
    // Collection<ROW> storedChildren = tableContainer.getExistingChildren(item);
    // List<? extends ROW> currentChildren = getCurrentChildren(item);
    // boolean childrenHaveChanged = !currentChildren.equals(storedChildren);
    // boolean subChildrenHaveChanged = expanded && updateChildren(currentChildren);
    //
    // if (childrenHaveChanged || subChildrenHaveChanged) {
    // tableContainer.removeExistingChildren(item);
    // tableContainer.getChildren(item);
    // return true;
    // } else {
    // return false;
    // }
    // } else {
    // return false;
    // }
    // }

    // private List<? extends ROW> getCurrentChildren(ROW item) {
    // if (!(item instanceof HierarchicalRowPmo<?>)) {
    // return Collections.emptyList();
    // }
    // @SuppressWarnings("unchecked")
    // HierarchicalRowPmo<? extends ROW> hierarchicalRowPmo = (HierarchicalRowPmo<? extends ROW>)item;
    // return hierarchicalRowPmo.getChildRows();
    // }

}
