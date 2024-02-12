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

package org.linkki.core.ui.creation.table;

import java.util.Optional;

import org.linkki.core.defaults.columnbased.ColumnBasedComponentWrapper;
import org.linkki.core.defaults.columnbased.pmo.TableFooterPmo;
import org.linkki.core.ui.wrapper.VaadinComponentWrapper;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;

/**
 * Wraps a vaadin {@link Grid}.
 * 
 * @param <ROW> a class annotated with linkki annotations used as PMO for a row in the table
 */
public abstract class AbstractGridComponentWrapper<ROW> extends VaadinComponentWrapper
        implements ColumnBasedComponentWrapper<ROW> {

    private static final long serialVersionUID = 1L;

    public AbstractGridComponentWrapper(Grid<ROW> grid) {
        super(grid, COLUMN_BASED_TYPE);
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
     * If the given {@link Optional}&lt;{@link TableFooterPmo}&gt; is {@link Optional#empty()
     * empty}, no footer will be visible, otherwise the the column footers will be set according to
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
        if (pageLength < 1) {
            if (!getComponent().isAllRowsVisible()) {
                getComponent().setAllRowsVisible(true);
            }
        } else {
            if (getComponent().isAllRowsVisible()) {
                getComponent().setAllRowsVisible(false);
            }
            if (getComponent().getPageSize() != pageLength) {
                getComponent().setPageSize(pageLength);
            }
            int headerAndFooter = 1 + getComponent().getFooterRows().size();
            getComponent().setHeight((pageLength + headerAndFooter) * 3 + 1 + "em");
        }
    }

}
