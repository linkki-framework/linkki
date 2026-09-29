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
package org.linkki.core.defaults.columnbased.pmo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.Test;
import org.linkki.core.pmo.ModelObject;

class SimpleItemSupplierTest {

    private final List<Integer> modelObjects = new LinkedList<>();

    private final SimpleItemSupplier<SimplePmo, Integer> itemSupplier =
            new SimpleItemSupplier<>(this::getModelObjects, SimplePmo::new);

    /**
     * The purpose of this test is not really that the itemSupplier has the specified value but that
     * the item supplier could be created using a Supplier without <code>? extends</code>
     */
    @Test
    void testCreateItemSupplierWithoutUnknownType() {
        Supplier<List<Integer>> supplier = () -> Arrays.asList(1);

        var itemSupplier2 = new SimpleItemSupplier<>(supplier, UnaryOperator.<Integer> identity());

        assertThat(itemSupplier2.get()).contains(1);
    }

    @Test
    void testGet_shouldReturnEmptyList_ifUnderlyingListIsEmpty() {
        assertThat(itemSupplier.get()).isEmpty();
    }

    @Test
    void testGet_shouldReturnNewList_ifUnderlyingListHasCanged() {
        itemSupplier.get();

        modelObjects.add(42);
        assertThat(itemSupplier.get()).hasSize(1);
        var pmo = itemSupplier.get().get(0);
        assertThat(pmo.getModelObject()).isEqualTo(42);

        modelObjects.add(43);
        assertThat(itemSupplier.get()).hasSize(2);
        assertThat(itemSupplier.get().get(0)).isEqualTo(pmo);
        assertThat(itemSupplier.get().get(1).getModelObject()).isEqualTo(43);
    }

    @Test
    void testGet_shouldReturnNewList_ifUnderlyingListHasCangedOrder() {
        modelObjects.add(0);
        modelObjects.add(1);
        assertThat(itemSupplier.get()).extracting(SimplePmo::getModelObject).containsExactly(0, 1);

        var pmo2 = itemSupplier.get().get(1);

        modelObjects.set(0, 99);
        assertThat(itemSupplier.get()).extracting(SimplePmo::getModelObject).containsExactly(99, 1);
        assertThat(pmo2).isEqualTo(itemSupplier.get().get(1));
    }

    @Test
    void testGet_ShouldRemoveUnusedModelObjectMappings() {
        modelObjects.add(0);
        var pmo1 = itemSupplier.get().get(0);

        modelObjects.set(0, 99);
        itemSupplier.get();
        modelObjects.set(0, 0);

        assertThat(pmo1).isNotEqualTo(itemSupplier.get().get(0));
    }

    @Test
    void testGet_shouldReturnSameList_ifUnderlyingListHasNotChanged() {
        modelObjects.add(42);
        var list = itemSupplier.get();
        var list2 = itemSupplier.get();
        assertThat(list).isSameAs(list2);
    }

    @Test
    void testGet_shouldThrowNullPointerException_ifSupplierReturnsNull() {
        var nullSupplier = new SimpleItemSupplier<>(this::getModelObjects, mo -> null);
        modelObjects.add(42);
        assertThatNullPointerException().isThrownBy(nullSupplier::get);
    }

    private List<Integer> getModelObjects() {
        return modelObjects;
    }

    static class SimplePmo {

        private final Integer mo;

        SimplePmo(Integer mo) {
            this.mo = mo;
        }

        @ModelObject
        public Integer getModelObject() {
            return mo;
        }

    }

}
