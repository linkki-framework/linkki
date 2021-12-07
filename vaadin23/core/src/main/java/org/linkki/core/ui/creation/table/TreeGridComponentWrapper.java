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

import java.util.Arrays;
import java.util.Collection;
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
     * Sets the items displayed in the {@link Grid} by updating the items in the data provider if the
     * given items are not the ones currently displayed.
     */
    @Override
    public void setItems(List<ROW> rootItems) {
        boolean hasChildChanged = updateChildren(rootItems);
        boolean rootItemsChanged = hasItemListChanged(rootItems);
        if (hasChildChanged || rootItemsChanged) {
            treeData.clear();
            treeData.addItems(rootItems, this::getCurrentChildren);
            if (rootItemsChanged) {
                getComponent().getDataProvider().refreshAll();
            }
        }
    }

    private boolean hasItemListChanged(List<ROW> rootItems) {
        return !treeData.getRootItems().equals(rootItems);
    }

    /**
     * @see #updateChildren(ROW)
     */
    private boolean updateChildren(List<? extends ROW> newItems) {
        boolean changed = false;
        for (ROW item : newItems) {
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
        if (!treeData.contains(item)) {
            return false;
        }
        Collection<ROW> storedChildren = treeData.getChildren(item);
        List<? extends ROW> currentChildren = getCurrentChildren(item);
        boolean childrenHaveChanged = !currentChildren.equals(storedChildren);
        boolean subChildrenHaveChanged = updateChildren(currentChildren);

        if (childrenHaveChanged || subChildrenHaveChanged) {
            getComponent().getDataProvider().refreshItem(item, true);
            if (currentChildren.isEmpty()) {
                getComponent().collapse(Arrays.asList(item));
            }
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private List<ROW> getCurrentChildren(ROW item) {
        if (!(item instanceof HierarchicalRowPmo<?>)) {
            return Collections.emptyList();
        } else {
            HierarchicalRowPmo<? extends ROW> hierarchicalRowPmo = (HierarchicalRowPmo<? extends ROW>)item;
            return (List<ROW>)hierarchicalRowPmo.getChildRows();
        }
    }

}
