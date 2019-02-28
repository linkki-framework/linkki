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

package org.linkki.core.ui.table;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.linkki.core.container.LinkkiInMemoryContainer;
import org.linkki.core.ui.columnbased.ColumnBasedComponentWrapper;
import org.linkki.core.ui.components.CaptionComponentWrapper;

/**
 * Wraps a vaadin {@link com.vaadin.v7.ui.Table Table}.
 * 
 * @param <ROW> a class annotated with linkki annotations used as PMO for a row in the table
 */
@SuppressWarnings({ "deprecation", "javadoc" })
public class TableComponentWrapper<@NonNull ROW> extends CaptionComponentWrapper
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
    @SuppressWarnings("javadoc")
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
    @SuppressWarnings("javadoc")
    @Override
    public void setPageLength(int pageLength) {
        getComponent().setPageLength(pageLength);
    }

    /**
     * Sets the items displayed in the {@link com.vaadin.v7.ui.Table Table} by updating the
     * {@link com.vaadin.v7.ui.Table#getContainerDataSource() container data source} if the given items
     * are not the ones currently displayed.
     */
    @SuppressWarnings("javadoc")
    @Override
    public void setItems(List<ROW> actualItems) {
        boolean hasChildChanged = hasItemChanged(actualItems);
        if (hasChildChanged || hasItemListChanged(actualItems)) {
            tableContainer.setItems(actualItems);
        }
    }

    private boolean hasItemListChanged(List<ROW> actualItems) {
        return !tableContainer.getItemIds().equals(actualItems);
    }

    private boolean hasItemChanged(List<? extends ROW> items) {
        return items.stream()
                .map(this::hasItemChanged)
                .reduce(false, (anyChanged, thisChanged) -> anyChanged || thisChanged);
    }

    private boolean hasItemChanged(ROW item) {
        if (getComponent() instanceof com.vaadin.v7.ui.TreeTable) {
            boolean collapsed = ((com.vaadin.v7.ui.TreeTable)getComponent()).isCollapsed(item);
            if (collapsed) {
                return tableContainer.removeExistingChildren(item);
            } else {
                Collection<ROW> existingChildren = tableContainer.getExistingChildren(item);
                List<? extends ROW> currentChildren = getCurrentChildren(item);
                boolean itemInsideChildrenHasChanged = hasItemChanged(currentChildren);
                boolean childrenHaveChanged = !currentChildren.equals(existingChildren);
                if (itemInsideChildrenHasChanged || childrenHaveChanged) {
                    return tableContainer.removeExistingChildren(item);
                }
                return false;
            }
        } else {
            return false;
        }
    }

    private List<? extends ROW> getCurrentChildren(ROW item) {
        @SuppressWarnings("unchecked")
        HierarchicalRowPmo<? extends ROW> hierarchicalRowPmo = (HierarchicalRowPmo<? extends ROW>)item;
        return hierarchicalRowPmo.getChildRows();
    }

}
