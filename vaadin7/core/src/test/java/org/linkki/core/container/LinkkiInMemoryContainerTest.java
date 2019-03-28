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
package org.linkki.core.container;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.linkki.core.ui.table.HierarchicalRowPmo;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class LinkkiInMemoryContainerTest {


    @Test
    public void testBackupListShouldBeClearedOnRemoveAllItems() {

        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        container.setItems(Collections.singletonList(new TestItem()));

        assertThat(container.getItemIds(), hasSize(1));

        container.setItems(Collections.emptyList());
        assertThat(container.getItemIds(), hasSize(0));
    }


    @Test
    public void testEqualsOfWrapperShouldOnlyCheckReference() {

        TestItem testItem = new TestItem(42);

        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        container.setItems(Collections.singletonList(testItem));

        TestItem equalItem = new TestItem(42);

        // items are equal if TestItem#equals is used
        assertThat(testItem, is(equalItem));

        // wrapped items are equal because the 'testItem's are equal
        assertThat(container.getItemIds().get(0), is(equalItem));

        // wrapped items are equal because same reference of 'testItem'
        assertThat(container.getItemIds().get(0), is(testItem));
    }

    @Test
    public void testGetChildren_ofItemNotContained() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();

        assertThat(container.getChildren(new TestItem(42)), is(empty()));
    }

    @Test
    public void testGetChildren_ofItemWithoutChildren() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        TestItem testItem = new TestItem(42);
        container.setItems(Collections.singletonList(testItem));

        assertThat(container.getChildren(testItem), is(empty()));
    }

    @Test
    public void testGetChildren_ofHierarchicalItemWithoutChildren() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        HierarchicalTestItem testItem = new HierarchicalTestItem(42);
        container.setItems(Collections.singletonList(testItem));

        assertThat(container.getChildren(testItem), is(empty()));
    }

    @Test
    public void testGetChildren_ofHierarchicalItemWithChildren() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        HierarchicalTestItem child1 = new HierarchicalTestItem(6);
        HierarchicalTestItem child2 = new HierarchicalTestItem(9);
        HierarchicalTestItem testItem = new HierarchicalTestItem(42, child1, child2);
        container.setItems(Collections.singletonList(testItem));

        assertThat(container.getChildren(testItem), contains(child1, child2));
    }

    @Test
    public void testGetParent_ofItemNotContained() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();

        assertThat(container.getParent(new TestItem(42)), is(nullValue()));
    }

    @Test
    public void testGetParent_ofItemWithoutParent() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        TestItem testItem = new TestItem(42);
        container.setItems(Collections.singletonList(testItem));

        assertThat(container.getParent(testItem), is(nullValue()));
    }

    @Test
    public void testGetParent_ofHierarchicalItemWithoutParent() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        HierarchicalTestItem testItem = new HierarchicalTestItem(42);
        container.setItems(Collections.singletonList(testItem));

        assertThat(container.getParent(testItem), is(nullValue()));
    }

    @Test
    public void testGetParent_ofHierarchicalItemWithParent() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        HierarchicalTestItem child1 = new HierarchicalTestItem(6);
        HierarchicalTestItem child2 = new HierarchicalTestItem(9);
        HierarchicalTestItem testItem = new HierarchicalTestItem(42, child1, child2);
        container.setItems(Collections.singletonList(testItem));

        assertThat(container.getParent(testItem), is(nullValue()));

        // will always be called before getParent(because if it isn't, the child item is not known).
        // Needed to lazy initialize the parent map.
        container.getChildren(testItem);

        assertThat(container.getParent(child1), is(testItem));
        assertThat(container.getParent(child2), is(testItem));
    }

    @Test
    public void testRootItemIds_ofStandardItems() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        TestItem testItem1 = new TestItem(23);
        TestItem testItem2 = new TestItem(42);
        container.setItems(Arrays.asList(testItem1, testItem2));

        assertThat(container.rootItemIds(), contains(testItem1, testItem2));
    }

    @Test
    public void testRootItemIds_ofHierarchicalItems() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        HierarchicalTestItem child1 = new HierarchicalTestItem(6);
        HierarchicalTestItem child2 = new HierarchicalTestItem(9);
        HierarchicalTestItem testItem1 = new HierarchicalTestItem(42, child1, child2);
        HierarchicalTestItem testItem2 = new HierarchicalTestItem(23);
        container.setItems(Arrays.asList(testItem1, testItem2));

        assertThat(container.rootItemIds(), contains(testItem1, testItem2));
    }

    @Test
    public void testSetParent() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        HierarchicalTestItem testItem1 = new HierarchicalTestItem(23);
        HierarchicalTestItem testItem2 = new HierarchicalTestItem(42);

        assertThat(container.setParent(testItem1, testItem2), is(false));
    }

    @Test
    public void testAreChildrenAllowed_ofItemNotContained() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();

        assertThat(container.areChildrenAllowed(new TestItem(42)), is(false));
    }

    @Test
    public void testAreChildrenAllowed_ofItemWithoutChildren() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        TestItem testItem = new TestItem(42);
        container.setItems(Collections.singletonList(testItem));

        assertThat(container.areChildrenAllowed(testItem), is(false));
    }

    @Test
    public void testAreChildrenAllowed_ofHierarchicalItemWithoutChildren() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        HierarchicalTestItem testItem = new HierarchicalTestItem(42);
        container.setItems(Collections.singletonList(testItem));

        assertThat(container.areChildrenAllowed(testItem), is(false));
    }

    @Test
    public void testAreChildrenAllowed_ofHierarchicalItemWithChildren() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        HierarchicalTestItem child1 = new HierarchicalTestItem(6);
        HierarchicalTestItem child2 = new HierarchicalTestItem(9);
        HierarchicalTestItem testItem = new HierarchicalTestItem(42, child1, child2);
        container.setItems(Collections.singletonList(testItem));

        assertThat(container.areChildrenAllowed(testItem), is(true));
    }

    @Test
    public void testSetChildrenAllowed() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        HierarchicalTestItem testItem = new HierarchicalTestItem(23);

        assertThat(container.setChildrenAllowed(testItem, true), is(false));
    }

    @Test
    public void testIsRoot_ofItemNotContained() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();

        assertThat(container.isRoot(new TestItem(42)), is(false));
    }

    @Test
    public void testIsRoot_ofItemWithoutChildren() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        TestItem testItem = new TestItem(42);
        container.setItems(Collections.singletonList(testItem));

        assertThat(container.isRoot(testItem), is(true));
    }

    @Test
    public void testIsRoot_ofHierarchicalItemWithoutChildren() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        HierarchicalTestItem testItem = new HierarchicalTestItem(42);
        container.setItems(Collections.singletonList(testItem));

        assertThat(container.isRoot(testItem), is(true));
    }

    @Test
    public void testIsRoot_ofHierarchicalItemWithChildren() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        HierarchicalTestItem child1 = new HierarchicalTestItem(6);
        HierarchicalTestItem child2 = new HierarchicalTestItem(9);
        HierarchicalTestItem testItem = new HierarchicalTestItem(42, child1, child2);
        container.setItems(Collections.singletonList(testItem));

        assertThat(container.isRoot(testItem), is(true));
        assertThat(container.isRoot(child1), is(false));
        assertThat(container.isRoot(child2), is(false));
    }

    @Test
    public void testHasChildren_ofItemNotContained() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();

        assertThat(container.hasChildren(new TestItem(42)), is(false));
    }

    @Test
    public void testHasChildren_ofItemWithoutChildren() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        TestItem testItem = new TestItem(42);
        container.setItems(Collections.singletonList(testItem));

        assertThat(container.hasChildren(testItem), is(false));
    }

    @Test
    public void testHasChildren_ofHierarchicalItemWithoutChildren() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        HierarchicalTestItem testItem = new HierarchicalTestItem(42);
        container.setItems(Collections.singletonList(testItem));

        assertThat(container.hasChildren(testItem), is(false));
    }

    @Test
    public void testHasChildren_ofHierarchicalItemWithChildren() {
        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        HierarchicalTestItem child1 = new HierarchicalTestItem(6);
        HierarchicalTestItem child2 = new HierarchicalTestItem(9);
        HierarchicalTestItem testItem = new HierarchicalTestItem(42, child1, child2);
        container.setItems(Collections.singletonList(testItem));

        assertThat(container.hasChildren(testItem), is(true));
    }


    private static class TestItem {

        private int meaningOfLife;

        public TestItem() {

        }

        public TestItem(int meaningOfLife) {
            this.meaningOfLife = meaningOfLife;
        }

        @Override
        public boolean equals(@CheckForNull Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            TestItem testItem = (TestItem)o;

            return meaningOfLife == testItem.meaningOfLife;
        }

        @Override
        public int hashCode() {
            return meaningOfLife;
        }
    }

    private static class HierarchicalTestItem extends TestItem implements HierarchicalRowPmo<HierarchicalTestItem> {

        private final List<HierarchicalTestItem> children;

        public HierarchicalTestItem(int meaningOfLife, HierarchicalTestItem... children) {
            super(meaningOfLife);
            this.children = Arrays.asList(children);
        }

        @Override
        public List<? extends HierarchicalTestItem> getChildRows() {
            return children;
        }

    }


}