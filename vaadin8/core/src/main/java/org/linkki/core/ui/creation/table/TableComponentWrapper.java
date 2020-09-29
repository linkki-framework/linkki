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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.linkki.core.defaults.columnbased.ColumnBasedComponentWrapper;
import org.linkki.core.defaults.columnbased.pmo.HierarchicalRowPmo;
import org.linkki.core.defaults.columnbased.pmo.TableFooterPmo;
import org.linkki.core.ui.creation.table.container.LinkkiInMemoryContainer;
import org.linkki.core.ui.wrapper.CaptionComponentWrapper;

/**
 * Wraps a vaadin {@link com.vaadin.v7.ui.Table Table}.
 * 
 * @param <ROW> a class annotated with linkki annotations used as PMO for a row in the table
 */
@SuppressWarnings({ "deprecation", "javadoc" })
public class TableComponentWrapper<ROW> extends CaptionComponentWrapper
        implements ColumnBasedComponentWrapper<ROW> {

    private static final long serialVersionUID = 1L;

    private final LinkkiInMemoryContainer<ROW> tableContainer = new LinkkiInMemoryContainer<>();

    public TableComponentWrapper(String id, com.vaadin.v7.ui.Table table) {
        super(id, table, COLUMN_BASED_TYPE);
        table.setContainerDataSource(tableContainer);
    }

    @Override
    public com.vaadin.v7.ui.Table getComponent() {
        return (com.vaadin.v7.ui.Table)super.getComponent();
    }

    /**
     * Updates the tables {@link com.vaadin.v7.ui.Table#setColumnFooter(Object, String) column footers}.
     * If the given {@link Optional}&lt;{@link TableFooterPmo}&gt; is {@link Optional#empty() empty}, no
     * footer will be visible, otherwise the the column footers will be set according to
     * {@link TableFooterPmo#getFooterText(String)}.
     */
    @Override
    public void updateFooter(Optional<TableFooterPmo> footerPmo) {
        com.vaadin.v7.ui.Table table = getComponent();
        table.setFooterVisible(footerPmo.isPresent());
        if (footerPmo.isPresent()) {
            for (Object column : table.getVisibleColumns()) {
                if (column instanceof String) {
                    String text = footerPmo.get().getFooterText((String)column);
                    table.setColumnFooter(column, text);
                }
            }
        }
    }

    /**
     * Sets the {@link com.vaadin.v7.ui.Table#setPageLength(int) page length}.
     */
    @Override
    public void setPageLength(int pageLength) {
        getComponent().setPageLength(pageLength);
    }

    /**
     * Sets the items displayed in the {@link com.vaadin.v7.ui.Table Table} by updating the
     * {@link com.vaadin.v7.ui.Table#getContainerDataSource() container data source} if the given items
     * are not the ones currently displayed.
     */
    @Override
    public void setItems(List<ROW> items) {
        boolean hasChildChanged = updateChildren(items);
        if (hasChildChanged || hasItemListChanged(items)) {
            tableContainer.setItems(items);
        }
    }

    private boolean hasItemListChanged(List<ROW> items) {
        return !tableContainer.rootItemIds().equals(items);
    }

    /**
     * @see #updateChildren(ROW)
     */
    private boolean updateChildren(List<? extends ROW> items) {
        boolean changed = false;
        for (ROW item : items) {
            changed |= updateChildren(item);
        }
        return changed;
    }

    /**
     * Updates the children stored in the underlying container. Stored children that do not match the
     * ones present on the given item are updated accordingly. Children of items that are not visible
     * are ignored.
     * 
     * A call to {@link LinkkiInMemoryContainer#setItems(Collection)} is required if this method returns
     * {@code true}.
     * 
     * @return {@code true} if the underlying container changed as a result of the call
     */
    private boolean updateChildren(ROW item) {
        if (getComponent() instanceof com.vaadin.v7.ui.TreeTable) {
            boolean expanded = !((com.vaadin.v7.ui.TreeTable)getComponent()).isCollapsed(item);
            Collection<ROW> storedChildren = tableContainer.getExistingChildren(item);
            List<? extends ROW> currentChildren = getCurrentChildren(item);
            boolean childrenHaveChanged = !currentChildren.equals(storedChildren);
            boolean subChildrenHaveChanged = expanded && updateChildren(currentChildren);

            if (childrenHaveChanged || subChildrenHaveChanged) {
                tableContainer.removeExistingChildren(item);
                tableContainer.getChildren(item);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private List<? extends ROW> getCurrentChildren(ROW item) {
        if (!(item instanceof HierarchicalRowPmo<?>)) {
            return Collections.emptyList();
        }
        @SuppressWarnings("unchecked")
        HierarchicalRowPmo<? extends ROW> hierarchicalRowPmo = (HierarchicalRowPmo<? extends ROW>)item;
        return hierarchicalRowPmo.getChildRows();
    }

}
