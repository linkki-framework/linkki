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

package de.faktorzehn.commons.linkki.ui.table;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.linkki.core.defaults.columnbased.pmo.HierarchicalRowPmo;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.DataProviderListener;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.shared.Registration;

/**
 * Provides common utility functions for {{@link TreeGrid}s whose rows implement
 * {@link HierarchicalRowPmo}.
 *
 * @deprecated moved to linkki-core. Use {@link org.linkki.core.ui.table.util.HierarchicalTableUtil}
 *             instead
 */
@Deprecated(since = "2.8.0")
public class HierarchicalTableUtil {

    private HierarchicalTableUtil() {
        // utility class
    }

    /**
     * Expands the root nodes of the given {@link TreeGrid}.
     */
    public static <T> void expandRootNodes(TreeGrid<T> grid) {
        expandNodes(grid, 1);
    }

    /**
     * Expands the nodes of the given {@link TreeGrid} up to the given level.
     */
    public static <T> void expandNodes(TreeGrid<T> grid, int maxLevel) {
        expandNodesIf(grid, maxLevel, HierarchicalTableUtil::allNodesAccepted);
    }

    /**
     * Collapses the nodes of the given {@link TreeGrid} up to the given level. Only nodes that
     * match the given filter are included.
     */
    public static <T> void expandNodesIf(TreeGrid<T> grid,
            int maxLevel,
            Predicate<? super T> filter) {
        updateTreeRepresentation(grid, maxLevel, grid::expand, filter);
    }

    /**
     * Collapses the root nodes of the given {@link TreeGrid}.
     */
    public static <T> void collapseRootNodes(TreeGrid<T> grid) {
        updateTreeRepresentation(grid, 1, grid::collapse, HierarchicalTableUtil::allNodesAccepted);
    }

    /**
     * Updates the hierarchical representation of the given {@link TreeGrid} and collapses / expands
     * the nodes based on the given recursive update function.
     * 
     * @param grid the table to be updated
     * @param maxLevel the max level of a node to be included
     * @param updateFunction defines how the hierarchical representation should be updated
     * @param filter defines the items to be included in the update process
     */
    private static <T> void updateTreeRepresentation(TreeGrid<T> grid,
            int maxLevel,
            Consumer<List<T>> updateFunction,
            Predicate<? super T> filter) {
        updateDataProviderListener(grid, maxLevel, updateFunction, filter);

        var affectedNodes = new ArrayList<T>();
        var treeData = grid.getTreeData();
        treeData.getRootItems()
                .forEach(item -> traverseTree(treeData, item, affectedNodes, 0, maxLevel, filter));
        updateFunction.accept(affectedNodes);
    }

    /**
     * Traverses the tree to find all nodes up to the given max level that matches the given filter.
     */
    private static <T> void traverseTree(TreeData<T> treeData,
            T currentItem,
            List<T> allNodes,
            int currentLevel,
            int maxLevel,
            Predicate<? super T> filter) {
        if (currentLevel < maxLevel && filter.test(currentItem)) {
            allNodes.add(currentItem);
            int newLevel = currentLevel + 1;
            treeData.getChildren(currentItem)
                    .forEach(child -> traverseTree(treeData, child, allNodes, newLevel, maxLevel, filter));
        }
    }

    /**
     * Removes an already registered {@link DataProviderListener} from the {@link TreeGrid} that is
     * used for updating the expanded rows on data change and registers a new one with the given
     * parameters.
     */
    private static <T> void updateDataProviderListener(TreeGrid<T> grid,
            int maxLevel,
            Consumer<List<T>> updateFunction,
            Predicate<? super T> filter) {
        var registrationHolder = ComponentUtil.getData(grid, UpdateGridRegistrationHolder.class);
        if (registrationHolder != null) {
            registrationHolder.remove();
        }
        // Register a new DataProviderListener to update the tree representation when the data
        // changes
        var registration = grid.getDataProvider()
                .addDataProviderListener($ -> updateTreeRepresentation(grid, maxLevel, updateFunction, filter));
        ComponentUtil.setData(grid, UpdateGridRegistrationHolder.class, new UpdateGridRegistrationHolder(registration));
    }

    /**
     * Dummy function to include all nodes for expanding or collapsing.
     */
    private static <T> Boolean allNodesAccepted(T row) {
        return true;
    }

    /**
     * Holds the {@link Registration} that is used to update the {@link TreeGrid} representation and
     * provides a method to delete it if necessary.
     */
    private record UpdateGridRegistrationHolder(Registration registration) {
        private void remove() {
            registration.remove();
        }
    }
}
