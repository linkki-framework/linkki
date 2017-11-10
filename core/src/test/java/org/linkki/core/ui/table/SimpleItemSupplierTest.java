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
package org.linkki.core.ui.table;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.junit.Test;
import org.linkki.core.ui.section.annotations.ModelObject;

public class SimpleItemSupplierTest {

    private List<Integer> modelObjects = new LinkedList<>();

    private SimpleItemSupplier<SimplePmo, Integer> itemSupplier = new SimpleItemSupplier<>(this::getModelObjects,
            mo -> new SimplePmo(mo));

    /**
     * The purpose of this test is not really that the itemSupplier has the specified value but that
     * the item supplier could be created using a Supplier without <code>? extends</code>
     */
    @Test
    public void testCreateItemSupplierWithoutUnknownType() {
        Supplier<List<Integer>> supplier = () -> Arrays.asList(1);

        SimpleItemSupplier<Integer, Integer> itemSupplier2 = new SimpleItemSupplier<>(supplier,
                UnaryOperator.identity());

        assertThat(itemSupplier2.get(), hasItem(1));
    }

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
