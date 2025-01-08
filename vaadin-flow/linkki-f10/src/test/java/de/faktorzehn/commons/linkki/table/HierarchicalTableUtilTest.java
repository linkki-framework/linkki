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

package de.faktorzehn.commons.linkki.table;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.defaults.columnbased.pmo.HierarchicalRowPmo;
import org.linkki.core.ui.creation.table.GridComponentCreator;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.table.column.annotation.UITableColumn;

import com.vaadin.flow.component.treegrid.TreeGrid;

import de.faktorzehn.commons.linkki.ui.table.HierarchicalTableUtil;

class HierarchicalTableUtilTest {

    @Test
    void testExpandRootNodes() {
        var grid = createTreeGrid(3);
        var rootItem = getRootItem(grid);
        assertThat(grid.isExpanded(rootItem)).isFalse();

        HierarchicalTableUtil.expandRootNodes(grid);

        assertThat(grid.isExpanded(rootItem)).isTrue();
        var firstLevelItem = getRootItem(grid).getChildRows().get(0);
        assertThat(grid.isExpanded(firstLevelItem)).isFalse();
        var secondLevelItem = firstLevelItem.getChildRows().get(0);
        assertThat(grid.isExpanded(secondLevelItem)).isFalse();
    }

    @Test
    void testExpandNodes() {
        var grid = createTreeGrid(3);
        var rootItem = getRootItem(grid);
        assertThat(grid.isExpanded(rootItem)).isFalse();

        HierarchicalTableUtil.expandNodes(grid, 2);

        assertThat(grid.isExpanded(rootItem)).isTrue();
        var firstLevelItem = getRootItem(grid).getChildRows().get(0);
        assertThat(grid.isExpanded(firstLevelItem)).isTrue();
        var secondLevelItem = firstLevelItem.getChildRows().get(0);
        assertThat(grid.isExpanded(secondLevelItem)).isFalse();
    }

    @Test
    void testExpandNodesIf() {
        var grid = createTreeGrid(3);
        var rootItem = getRootItem(grid);
        assertThat(grid.isExpanded(rootItem)).isFalse();

        HierarchicalTableUtil.expandNodesIf(grid, 3, this::createTestFilter);

        assertThat(grid.isExpanded(rootItem)).isTrue();
        var firstLevelItem = getRootItem(grid).getChildRows().get(0);
        assertThat(grid.isExpanded(firstLevelItem)).isTrue();
        var secondLevelItem = firstLevelItem.getChildRows().get(0);
        assertThat(grid.isExpanded(secondLevelItem)).isTrue();
        var thirdLevel = secondLevelItem.getChildRows().get(0);
        assertThat(grid.isExpanded(thirdLevel)).isFalse();
    }

    @Test
    void testCollapseRootNodes() {
        var grid = createTreeGrid(3);
        var rootItem = getRootItem(grid);
        HierarchicalTableUtil.expandNodes(grid, Integer.MAX_VALUE);
        assertThat(grid.isExpanded(rootItem)).isTrue();

        HierarchicalTableUtil.collapseRootNodes(grid);

        assertThat(grid.isExpanded(rootItem)).isFalse();
    }

    @Test
    void testCollapseNodes() {
        var grid = createTreeGrid(3);
        var rootItem = getRootItem(grid);
        HierarchicalTableUtil.expandNodes(grid, Integer.MAX_VALUE);
        assertThat(grid.isExpanded(rootItem)).isTrue();

        HierarchicalTableUtil.collapseRootNodes(grid);

        assertThat(grid.isExpanded(rootItem)).isFalse();
    }

    @Test
    void testCollapseNodesIf() {
        var grid = createTreeGrid(3);
        var rootItem = getRootItem(grid);
        assertThat(grid.isExpanded(rootItem)).isFalse();

        HierarchicalTableUtil.expandNodesIf(grid, 3, this::createTestFilter);

        assertThat(grid.isExpanded(rootItem)).isTrue();
        var firstLevelItem = getRootItem(grid).getChildRows().get(0);
        assertThat(grid.isExpanded(firstLevelItem)).isTrue();
        var secondLevelItem = firstLevelItem.getChildRows().get(0);
        assertThat(grid.isExpanded(secondLevelItem)).isTrue();
        var thirdLevel = secondLevelItem.getChildRows().get(0);
        assertThat(grid.isExpanded(thirdLevel)).isFalse();
    }

    private TestTreeTableRowPmo getRootItem(TreeGrid<TestTreeTableRowPmo> grid) {
        return grid.getTreeData().getRootItems().get(0);
    }

    /**
     * Creates a filter that matches with all {@link TestTreeTableRowPmo rows} with children.
     */
    private Boolean createTestFilter(TestTreeTableRowPmo rowPmo) {
        return !rowPmo.getChildRows().isEmpty();
    }

    /**
     * Creates a {@link TreeGrid} with the given amount of levels.
     */
    private TreeGrid<TestTreeTableRowPmo> createTreeGrid(int level) {
        var table = new TestTreeTablePmo(level);
        return (TreeGrid<TestTreeTableRowPmo>)GridComponentCreator.createGrid(table, new BindingContext());
    }

    private record TestTreeTableRowPmo(int remainingLevels) implements HierarchicalRowPmo<TestTreeTableRowPmo> {

        @Override
        public List<? extends TestTreeTableRowPmo> getChildRows() {
            return remainingLevels > 0 ? List.of(new TestTreeTableRowPmo(remainingLevels - 1)) : List.of();
        }

        @UITableColumn
        @UITextField(position = 10, label = "Upper")
        public String getContent() {
            return "Test";
        }
    }

    private record TestTreeTablePmo(int remainingLevels) implements ContainerPmo<TestTreeTableRowPmo> {

        @Override
        public List<TestTreeTableRowPmo> getItems() {
            return List.of(new TestTreeTableRowPmo(remainingLevels));
        }
    }
}
