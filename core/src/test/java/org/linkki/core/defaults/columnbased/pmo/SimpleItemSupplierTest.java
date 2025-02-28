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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.linkki.test.matcher.Matchers.assertThat;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.core.pmo.ModelObject;

public class SimpleItemSupplierTest {

    private List<Integer> modelObjects = new LinkedList<>();

    private SimpleItemSupplier<SimplePmo, Integer> itemSupplier = new SimpleItemSupplier<>(this::getModelObjects,
            mo -> new SimplePmo(mo));

    private SimpleItemSupplier<SimplePmo, Integer> nullSupplier = new SimpleItemSupplier<>(this::getModelObjects,
            mo -> null);

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
        SimplePmo pmo = itemSupplier.get().get(0);
        assertThat(pmo.getModelObject(), is(42));

        modelObjects.add(43);
        assertThat(itemSupplier.get(), hasSize(2));
        assertThat(itemSupplier.get().get(0), is(pmo));
        assertThat(itemSupplier.get().get(1).getModelObject(), is(43));
    }

    @Test
    public void testGet_shouldReturnNewList_ifUnderlyingListHasCangedOrder() {
        modelObjects.add(0);
        modelObjects.add(1);
        assertThat(itemSupplier.get(), hasSize(2));
        assertThat(itemSupplier, hasPmosForModelobjects(0, 1));

        SimplePmo pmo2 = itemSupplier.get().get(1);

        modelObjects.set(0, 99);
        assertThat(itemSupplier.get(), hasSize(2));
        assertThat(itemSupplier, hasPmosForModelobjects(99, 1));
        assertThat(pmo2, is(itemSupplier.get().get(1)));
    }

    @Test
    public void testGet_ShouldRemoveUnusedModelObjectMappings() {
        modelObjects.add(0);
        SimplePmo pmo1 = itemSupplier.get().get(0);

        modelObjects.set(0, 99);
        itemSupplier.get();
        modelObjects.set(0, 0);

        assertThat(pmo1, is(not(itemSupplier.get().get(0))));
    }

    @Test
    public void testGet_shouldReturnSameList_ifUnderlyingListHasNotChanged() {
        modelObjects.add(42);
        List<SimplePmo> list = itemSupplier.get();
        List<SimplePmo> list2 = itemSupplier.get();
        assertThat(list == list2);
    }

    @Test
    public void testGet_shouldThrowNullPointerException_ifSupplierReturnsNull() {
        modelObjects.add(42);
        Assertions.assertThrows(NullPointerException.class, () -> nullSupplier.get());
    }

    private List<Integer> getModelObjects() {
        return modelObjects;
    }

    private Matcher<SimpleItemSupplier<SimplePmo, Integer>> hasPmosForModelobjects(Object... expectedModelObjects) {
        return new TypeSafeMatcher<>() {

            private Matcher<Iterable<? extends SimplePmo>> pmosMatcher = contains(Arrays.stream(expectedModelObjects)
                    .map(ModelObjectValueMatcher::new).toArray(ModelObjectValueMatcher[]::new));

            @Override
            public void describeTo(Description description) {
                description.appendText("a " + SimpleItemSupplier.class.getSimpleName() + " that supplies ");
                pmosMatcher.describeTo(description);
            }

            @Override
            protected void describeMismatchSafely(SimpleItemSupplier<SimplePmo, Integer> item,
                    Description mismatchDescription) {
                mismatchDescription.appendText("a " + SimpleItemSupplier.class.getSimpleName() + " where ");
                pmosMatcher.describeMismatch(item.get(), mismatchDescription);
            }

            @Override
            protected boolean matchesSafely(SimpleItemSupplier<SimplePmo, Integer> item) {
                return pmosMatcher.matches(item.get());
            }
        };
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

    private static final class ModelObjectValueMatcher extends TypeSafeMatcher<SimplePmo> {

        private Matcher<Object> equalsMatcher;

        private ModelObjectValueMatcher(Object mo) {
            equalsMatcher = equalTo(mo);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a " + SimplePmo.class.getSimpleName() + " for ");
            equalsMatcher.describeTo(description);
        }

        @Override
        protected void describeMismatchSafely(SimplePmo item, Description mismatchDescription) {
            mismatchDescription
                    .appendText("a " + SimplePmo.class.getSimpleName() + " which's model object ");
            equalsMatcher.describeMismatch(item.getModelObject(), mismatchDescription);
        }

        @Override
        protected boolean matchesSafely(SimplePmo item) {
            return equalsMatcher.matches(item.getModelObject());
        }
    }

}
