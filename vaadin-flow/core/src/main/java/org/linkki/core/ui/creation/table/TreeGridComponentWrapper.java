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

import java.util.Collections;
import java.util.List;

import org.linkki.core.defaults.columnbased.pmo.HierarchicalRowPmo;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;

/**
 * Wraps a vaadin {@link Grid}.
 *
 * @param <ROW> a class annotated with linkki annotations used as PMO for a row in the table
 */
public class TreeGridComponentWrapper<ROW> extends AbstractGridComponentWrapper<ROW> {

    private static final long serialVersionUID = 1L;

    private final TreeData<ROW> treeData = new TreeData<>();

    public TreeGridComponentWrapper(TreeGrid<ROW> grid) {
        super(grid);
        grid.setDataProvider(new TreeDataProvider<>(treeData));
    }

    @Override
    public TreeGrid<ROW> getComponent() {
        return (TreeGrid<ROW>)super.getComponent();
    }

    /**
     * Sets the items displayed in the {@link Grid} by updating the items in the data provider if
     * the given items are not the ones currently displayed.
     */
    @Override
    public void setItems(List<ROW> rootItems) {
        if (hasItemListChanged(rootItems) || hasChildChanged(rootItems)) {
            treeData.clear();
            treeData.addItems(rootItems, this::getCurrentChildren);
            getComponent().getDataProvider().refreshAll();
            getComponent().getElement().setAttribute("has-items", !rootItems.isEmpty());
        }
    }

    private boolean hasItemListChanged(List<ROW> rootItems) {
        return !treeData.getRootItems().equals(rootItems);
    }

    /**
     * @see #hasChildChanged(ROW)
     */
    private boolean hasChildChanged(List<? extends ROW> newItems) {
        return newItems.stream().anyMatch(this::hasChildChanged);
    }

    /**
     * Checks if a child of the specified ROW item has changed.
     *
     * @param item The ROW item to check for child changes.
     *
     * @return {@code true} if a child of the item has changed, {@code false} otherwise.
     */
    private boolean hasChildChanged(ROW item) {
        if (!treeData.contains(item)) {
            return false;
        }
        var storedChildren = treeData.getChildren(item);
        var currentChildren = getCurrentChildren(item);
        return !currentChildren.equals(storedChildren) || hasChildChanged(currentChildren);
    }

    @SuppressWarnings("unchecked")
    private List<ROW> getCurrentChildren(ROW item) {
        if (item instanceof HierarchicalRowPmo<?> hierarchicalRowPmo) {
            return (List<ROW>)hierarchicalRowPmo.getChildRows();
        } else {
            return Collections.emptyList();
        }
    }

}
