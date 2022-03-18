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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.columnbased.pmo.HierarchicalRowPmo;

import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;

public class TreeGridComponentWrapperTest {

    private int eventCount = 0;

    @Test
    void testSetItems() {
        TreeGridComponentWrapper<Row> componentWrapper = new TreeGridComponentWrapper<>(new TreeGrid<>());
        List<Row> items = Arrays.asList(new Row(), new Row());

        componentWrapper.setItems(items);

        TreeDataProvider<Row> treeDataProvider = (TreeDataProvider<Row>)componentWrapper.getComponent()
                .getDataProvider();
        assertThat(treeDataProvider.getTreeData().getRootItems(), is(items));
    }

    @Test
    void testSetItems_CheckUpdateEvent() {
        TreeGridComponentWrapper<Row> componentWrapper = new TreeGridComponentWrapper<>(new TreeGrid<>());
        List<Row> items = Arrays.asList(new Row(), new Row());
        componentWrapper.getComponent().getDataProvider().addDataProviderListener($ -> eventCount++);

        componentWrapper.setItems(items);

        assertThat(eventCount, is(1));
    }

    @Test
    void testSetItems_CheckUpdateEventOnChange() {
        TreeGridComponentWrapper<Row> componentWrapper = new TreeGridComponentWrapper<>(new TreeGrid<>());
        List<Row> items = Arrays.asList(new Row(), new Row());
        componentWrapper.getComponent().getDataProvider().addDataProviderListener($ -> eventCount++);
        componentWrapper.setItems(items);
        eventCount = 0;

        items.set(0, new Row());
        componentWrapper.setItems(items);

        assertThat(eventCount, is(1));
    }

    @Test
    void testSetItems_WithChildren() {
        TreeGridComponentWrapper<Row> componentWrapper = new TreeGridComponentWrapper<>(new TreeGrid<>());
        List<Row> items = Arrays.asList(new Row(new Row(), new Row()), new Row(new Row(new Row())));

        componentWrapper.setItems(items);

        TreeDataProvider<Row> treeDataProvider = (TreeDataProvider<Row>)componentWrapper.getComponent()
                .getDataProvider();
        assertThat(treeDataProvider.getTreeData().getRootItems(), is(items));
        assertThat(treeDataProvider.getTreeData().getChildren(items.get(0)), is(items.get(0).children));
        assertThat(treeDataProvider.getTreeData().getChildren(items.get(1)), is(items.get(1).children));
        assertThat(treeDataProvider.getTreeData().getChildren(items.get(1).children.get(0)),
                   is(items.get(1).children.get(0).children));
    }

    @Test
    void testSetItems_WithChildren_CheckUpdateEventOnChange() {
        TreeGridComponentWrapper<Row> componentWrapper = new TreeGridComponentWrapper<>(new TreeGrid<>());
        List<Row> items = Arrays.asList(new Row(new Row(), new Row()), new Row(new Row(new Row())));
        componentWrapper.getComponent().getDataProvider().addDataProviderListener($ -> eventCount++);
        componentWrapper.setItems(items);
        eventCount = 0;

        items.get(0).children.set(0, new Row());
        componentWrapper.setItems(items);

        assertThat(eventCount, is(1));
    }

    @Test
    void testSetItems_DoNotUpdateForUnchangedList() {
        TreeGridComponentWrapper<Row> componentWrapper = new TreeGridComponentWrapper<>(new TreeGrid<>());
        List<Row> items = Arrays.asList(new Row(), new Row());
        componentWrapper.getComponent().getDataProvider().addDataProviderListener($ -> eventCount++);

        // call setItems twice
        componentWrapper.setItems(items);
        componentWrapper.setItems(items);

        assertThat(eventCount, is(1));
    }

    @Test
    void testCollapseItem_WhenNoChildren_ThenCollapsed() {
        eventCount = 0;
        TreeGridComponentWrapper<Row> componentWrapper = new TreeGridComponentWrapper<>(new TreeGrid<>());
        componentWrapper.getComponent().addCollapseListener($ -> eventCount++);
        List<Row> items = Arrays.asList(new Row(Arrays.asList(new Row(), new Row())));
        componentWrapper.setItems(items);
        componentWrapper.getComponent().expand(items);

        // remove all children
        items.get(0).getChildRows().clear();
        componentWrapper.setItems(items);

        assertThat(eventCount, is(1));
    }

    @Test
    void testHasItemsAttribute() {
        TreeGridComponentWrapper<Integer> componentWrapper = new TreeGridComponentWrapper<>(new TreeGrid<>());

        assertThat(componentWrapper.getComponent().getElement().hasAttribute("has-items")).isFalse();

        componentWrapper.setItems(Arrays.asList(1, 2, 3));

        assertThat(componentWrapper.getComponent().getElement().hasAttribute("has-items")).isTrue();

        componentWrapper.setItems(Collections.emptyList());

        assertThat(componentWrapper.getComponent().getElement().hasAttribute("has-items")).isFalse();
    }

    private static class Row implements HierarchicalRowPmo<Row> {

        private final List<Row> children;

        public Row(Collection<Row> children) {
            this.children = new ArrayList<>(children);
        }

        public Row(Row... children) {
            this.children = Arrays.asList(children);
        }

        @Override
        public List<? extends Row> getChildRows() {
            return children;
        }

    }

}
