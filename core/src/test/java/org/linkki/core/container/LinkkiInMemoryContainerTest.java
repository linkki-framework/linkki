/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.container;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Collections;

import org.junit.Test;

public class LinkkiInMemoryContainerTest {


    @Test
    public void testBackupListShouldBeClearedOnRemoveAllItems() {

        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        container.addAllItems(Collections.singletonList(new TestItem()));

        assertThat(container.getBackupList(), hasSize(1));

        container.removeAllItems();
        assertThat(container.getBackupList(), hasSize(0));
    }


    @Test
    public void testEqualsOfWrapperShouldOnlyCheckReference() {

        TestItem testItem = new TestItem();
        testItem.meaningOfLife = 42;

        LinkkiInMemoryContainer<TestItem> container = new LinkkiInMemoryContainer<>();
        container.addAllItems(Collections.singletonList(testItem));

        TestItem equalItem = new TestItem();
        equalItem.meaningOfLife = 42;

        // items are equal if TestItem#equals is used
        assertThat(testItem, is(equalItem));

        // wrapped items are not equal because not the same 'testItem' reference
        assertThat(container.getBackupList().get(0), is(not(new LinkkiInMemoryContainer.LinkkiItemWrapper<>(equalItem))));

        // wrapped items are equal because same reference of 'testItem'
        assertThat(container.getBackupList().get(0), is(new LinkkiInMemoryContainer.LinkkiItemWrapper<>(testItem)));
    }



    private static class TestItem {

        private int meaningOfLife;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestItem testItem = (TestItem) o;

            return meaningOfLife == testItem.meaningOfLife;
        }

        @Override
        public int hashCode() {
            return meaningOfLife;
        }
    }


}