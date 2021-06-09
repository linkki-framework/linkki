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

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.ui.table.column.annotation.UITableColumn.CollapseMode;
import org.linkki.core.ui.wrapper.VaadinComponentWrapper;

import com.vaadin.flow.component.grid.Grid.Column;

/**
 * Wrapper for the grid column. The label of the column wrapper is defined as the column header.
 */
public class GridColumnWrapper extends VaadinComponentWrapper {

    public static final WrapperType COLUMN_TYPE = WrapperType.of("column-header");

    private static final long serialVersionUID = 1L;

    private int flexGrow = UITableColumn.UNDEFINED_FLEX_GROW;

    public GridColumnWrapper(Column<?> column) {
        super(column, COLUMN_TYPE);
    }

    @Override
    public Column<?> getComponent() {
        return (Column<?>)super.getComponent();
    }

    @Override
    public void setLabel(String labelText) {
        if (!StringUtils.isEmpty(labelText)) {
            getComponent().setHeader(labelText);
        }
    }

    /**
     * Updates the collapse mode of the wrapped column
     * 
     * @param collapseMode The {@link CollapseMode} that should be set
     */
    public void setCollapseMode(CollapseMode collapseMode) {
        // TODO LIN-2138
        // if (collapseMode.isCollapsible() && !grid.isColumnCollapsingAllowed()) {
        // grid.setColumnCollapsingAllowed(true);
        // }
        // if (grid.isColumnCollapsingAllowed()) {
        // grid.setColumnCollapsible(propertyName, collapseMode.isCollapsible());
        // grid.setColumnCollapsed(propertyName, collapseMode.isInitiallyCollapsed());
        // }
    }

    /**
     * Sets the width of the grid to the given value and set flexGrow to 0 if the flexGrow is undefined.
     * 
     * @param width of the grid in pixel
     */
    public void setWidth(int width) {
        if (width != UITableColumn.UNDEFINED_WIDTH) {
            getComponent().setWidth(width + "px");
            if (flexGrow == UITableColumn.UNDEFINED_FLEX_GROW) {
                getComponent().setFlexGrow(0);
            }
        }
    }

    /**
     * Sets the flexGrow of the grid to the given value if the flexGrow is not
     * {@link UITableColumn#UNDEFINED_FLEX_GROW}. If flexGrow is
     * {@link UITableColumn#UNDEFINED_FLEX_GROW}, the flexGrow is set to 0
     * 
     * @param flexGrow of the grid to set
     */
    public void setFlexGrow(int flexGrow) {
        this.flexGrow = flexGrow;
        if (flexGrow != UITableColumn.UNDEFINED_FLEX_GROW) {
            getComponent().setFlexGrow(flexGrow);
        } else {
            getComponent().setFlexGrow(0);
        }
    }

    @Override
    public String toString() {
        return "ColumnHeaderWrapper [" + getComponent().getGrid().getId() + "#" + getComponent().getId() + "]";
    }

}
