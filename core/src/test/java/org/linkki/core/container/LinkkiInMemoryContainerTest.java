/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.container;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;

import javax.annotation.Nullable;

import org.junit.Test;

public class LinkkiInMemoryContainerTest {


    @SuppressWarnings("deprecation")
    @Test
    public void testBackupListShouldBeClearedOnRemoveAllItems() {

        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        container.setItems(Collections.singletonList(new TestItem()));

        assertThat(container.getItemIds(), hasSize(1));

        container.removeAllItems();
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


    private static class TestItem {

        private int meaningOfLife;

        public TestItem() {

        }

        public TestItem(int meaningOfLife) {
            this.meaningOfLife = meaningOfLife;
        }

        @Override
        public boolean equals(@Nullable Object o) {
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


}