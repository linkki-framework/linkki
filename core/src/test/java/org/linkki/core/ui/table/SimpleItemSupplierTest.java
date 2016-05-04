/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.table;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.linkki.core.ui.section.annotations.ModelObject;

public class SimpleItemSupplierTest {

    private List<Integer> modelObjects = new LinkedList<>();

    private SimpleItemSupplier<SimplePmo, Integer> itemSupplier = new SimpleItemSupplier<>(this::getModelObjects,
            mo -> new SimplePmo(mo));

    @Test
    public void testGet_shouldReturnEmptyList_ifUnderlyingListIsEmpty() {
        assertThat(itemSupplier.get(), hasSize(0));
    }

    @Test
    public void testGet_shouldReturnNewList_ifUnderlyingListHasCanged() {
        itemSupplier.get();

        modelObjects.add(42);
        assertThat(itemSupplier.get(), hasSize(1));
        assertThat(itemSupplier.get().get(0).getModelObject(), is(42));

        modelObjects.add(43);
        assertThat(itemSupplier.get(), hasSize(2));
        assertThat(itemSupplier.get().get(0).getModelObject(), is(42));
        assertThat(itemSupplier.get().get(1).getModelObject(), is(43));
    }

    @Test
    public void testGet_shouldReturnSameList_ifUnderlyingListHasNotChanged() {
        modelObjects.add(42);
        List<SimplePmo> list = itemSupplier.get();
        List<SimplePmo> list2 = itemSupplier.get();
        assertTrue(list == list2);
    }

    private List<Integer> getModelObjects() {
        return modelObjects;
    }

    class SimplePmo {

        private Integer mo;

        public SimplePmo(Integer mo) {
            this.mo = mo;
        }

        @ModelObject
        public Integer getModelObject() {
            return mo;
        }

    }

}
