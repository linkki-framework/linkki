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
package org.linkki.core.ui.bind;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ContainerBinding;
import org.linkki.core.ui.creation.table.PmoBasedTableFactory;
import org.linkki.core.ui.creation.table.container.LinkkiInMemoryContainer;
import org.linkki.core.ui.table.hierarchy.AbstractCodeRow;
import org.linkki.core.ui.table.hierarchy.CodeTablePmo;
import org.linkki.core.ui.table.hierarchy.LowerCaseRowPmo;
import org.linkki.core.ui.table.hierarchy.NumberRowPmo;
import org.linkki.core.ui.table.hierarchy.UpperCaseRowPmo;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("deprecation")
public class HierarchicalTableBindingTest {

    private BindingContext bindingContext = new BindingContext();

    private CodeTablePmo containerPmo;

    private com.vaadin.v7.ui.TreeTable table;

    @Mock
    private com.vaadin.v7.data.Container.ItemSetChangeListener listener;

    private ContainerBinding binding;

    @BeforeEach
    public void setUp() {
        containerPmo = new CodeTablePmo();

        table = (com.vaadin.v7.ui.TreeTable)new PmoBasedTableFactory(containerPmo, bindingContext).createTable();
        binding = bindingContext.getBindings().stream()
                .filter(ContainerBinding.class::isInstance)
                .map(ContainerBinding.class::cast)
                .findFirst()
                .get();
        getTableContainer().addItemSetChangeListener(listener);
    }

    protected LinkkiInMemoryContainer<?> getTableContainer() {
        return (LinkkiInMemoryContainer<?>)((com.vaadin.v7.ui.Table)binding.getBoundComponent())
                .getContainerDataSource();
    }

    @Test
    public void testDataSourceSet() {
        assertEquals(getTableContainer(), table.getContainerDataSource());
    }

    @Test
    public void testUncollapsingRows() {
        UpperCaseRowPmo rowA = rootRow(0);
        UpperCaseRowPmo rowB = rootRow(1);
        table.setCollapsed(rowA, false);
        LowerCaseRowPmo rowAa = childRow(rowA, 0);
        table.setCollapsed(rowAa, false);
        LowerCaseRowPmo rowAb = childRow(rowA, 1);
        NumberRowPmo rowAa1 = childRow(rowAa, 0);
        NumberRowPmo rowAa2 = childRow(rowAa, 1);

        binding.updateFromPmo();

        @SuppressWarnings("unchecked")
        Collection<AbstractCodeRow> itemIds = (Collection<AbstractCodeRow>)table.getItemIds();
        assertThat(itemIds, contains(rowA, rowAa, rowAa1, rowAa2, rowAb, rowB));
        assertThat(itemIds, contains(pmosFor("A", "a", 1, 2, "b", "B")));
    }

    @Test
    public void testUpdateFromPmo_ItemValueChanged() {
        UpperCaseRowPmo rowA = rootRow(0);
        UpperCaseRowPmo rowB = rootRow(1);
        table.setCollapsed(rowA, false);
        LowerCaseRowPmo rowAa = childRow(rowA, 0);
        table.setCollapsed(rowAa, false);
        LowerCaseRowPmo rowAb = childRow(rowA, 1);
        NumberRowPmo rowAa1 = childRow(rowAa, 0);
        NumberRowPmo rowAa2 = childRow(rowAa, 1);
        rowAa1.getCode().setNumber(42);

        binding.updateFromPmo();

        // sort order of numberRows is by number and so Aa1(now Aa42) and Aa2 must switch places
        // we don't use pmosFor(...) here because we want to make sure it's the same PMOs
        assertThat(table.getItemIds(), contains(rowA, rowAa, rowAa2, rowAa1, rowAb, rowB));
        verify(listener).containerItemSetChange(any());
    }

    @Test
    public void testUpdateFromPmo_ItemValueChangedAffectingRootRows() {
        UpperCaseRowPmo rowA = rootRow(0);
        table.setCollapsed(rowA, false);
        LowerCaseRowPmo rowAa = childRow(rowA, 0);
        table.setCollapsed(rowAa, false);
        NumberRowPmo rowAa1 = childRow(rowAa, 0);
        rowAa1.getCode().setUpperCaseLetter("C");

        binding.updateFromPmo();

        @SuppressWarnings("unchecked")
        Collection<AbstractCodeRow> itemIds = (Collection<AbstractCodeRow>)table.getItemIds();
        assertThat(itemIds, contains(pmosFor("A", "a", 2, "b", "B", "C")));
        verify(listener).containerItemSetChange(any());
    }

    @Test
    public void testUpdateFromPmo_ItemValueChangedAffectingOpenRootRows() {
        UpperCaseRowPmo rowA = rootRow(0);
        table.setCollapsed(rowA, false);
        UpperCaseRowPmo rowB = rootRow(1);
        table.setCollapsed(rowB, false);
        LowerCaseRowPmo rowAa = childRow(rowA, 0);
        table.setCollapsed(rowAa, false);
        NumberRowPmo rowAa1 = childRow(rowAa, 0);
        rowAa1.getCode().setUpperCaseLetter("B");
        rowAa1.getCode().setLowerCaseLetter("c");
        rowAa1.getCode().setNumber(23);

        binding.updateFromPmo();

        @SuppressWarnings("unchecked")
        Collection<AbstractCodeRow> itemIds = (Collection<AbstractCodeRow>)table.getItemIds();
        assertThat(itemIds, contains(pmosFor("A", "a", 2, "b", "B", "a", "b", "c")));
        verify(listener).containerItemSetChange(any());
    }

    @Test
    public void testUpdateFromPmo_ItemValueChangedAffectingIntermediateRows() {
        UpperCaseRowPmo rowA = rootRow(0);
        table.setCollapsed(rowA, false);
        LowerCaseRowPmo rowAa = childRow(rowA, 0);
        table.setCollapsed(rowAa, false);
        NumberRowPmo rowAa1 = childRow(rowAa, 0);
        rowAa1.getCode().setLowerCaseLetter("c");

        binding.updateFromPmo();

        @SuppressWarnings("unchecked")
        Collection<AbstractCodeRow> itemIds = (Collection<AbstractCodeRow>)table.getItemIds();
        assertThat(itemIds, contains(pmosFor("A", "a", 2, "b", "c", "B")));
        verify(listener).containerItemSetChange(any());
    }

    @SuppressWarnings("unchecked")
    private <P, C> C childRow(P parentRow, int i) {
        return (C)children(parentRow)[i];
    }

    @SuppressWarnings("unchecked")
    private <T> T rootRow(int i) {
        return (T)containerPmo.getItems().get(i);
    }

    @SuppressWarnings("unchecked")
    private <P, C> C[] children(P parent) {
        return (C[])all(getTableContainer().getChildren(parent));
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] all(Collection<T> collection) {
        return (T[])collection.toArray();
    }

    @SuppressWarnings("unchecked")
    private static Matcher<AbstractCodeRow>[] pmosFor(Object... items) {
        return Arrays.stream(items)
                .map(i -> i instanceof String
                        ? Character.isUpperCase(((String)i).charAt(0))
                                ? upperCaseRowPmo((String)i)
                                : lowerCaseRowPmo((String)i)
                        : numberRowPmo((Integer)i))
                .toArray(Matcher[]::new);
    }

    private static AbstractCodeRowMatcher<UpperCaseRowPmo, String> upperCaseRowPmo(String upperCaseLetter) {
        return new AbstractCodeRowMatcher<>(UpperCaseRowPmo.class, upperCaseLetter,
                AbstractCodeRow::getUpperCaseLetter);
    }

    private static AbstractCodeRowMatcher<LowerCaseRowPmo, String> lowerCaseRowPmo(String lowerCaseLetter) {
        return new AbstractCodeRowMatcher<>(LowerCaseRowPmo.class, lowerCaseLetter,
                AbstractCodeRow::getLowerCaseLetter);
    }

    private static AbstractCodeRowMatcher<NumberRowPmo, Integer> numberRowPmo(int number) {
        return new AbstractCodeRowMatcher<>(NumberRowPmo.class, number, AbstractCodeRow::getNumber);
    }

    private static class AbstractCodeRowMatcher<R extends AbstractCodeRow, V>
            extends TypeSafeMatcher<AbstractCodeRow> {

        private final Class<R> clazz;
        private final V expectedValue;
        private final Function<AbstractCodeRow, V> valueExtractor;

        private AbstractCodeRowMatcher(Class<R> clazz, V expectedValue, Function<AbstractCodeRow, V> valueExtractor) {
            this.clazz = clazz;
            this.expectedValue = expectedValue;
            this.valueExtractor = valueExtractor;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a ");
            description.appendValue(clazz.getSimpleName());
            description.appendText(" for ");
            description.appendValue(expectedValue);
        }

        @Override
        protected boolean matchesSafely(AbstractCodeRow item) {
            return clazz.isInstance(item) && expectedValue.equals(valueExtractor.apply(item));
        }

        @Override
        protected void describeMismatchSafely(AbstractCodeRow item, Description mismatchDescription) {
            mismatchDescription.appendText("was a ");
            mismatchDescription.appendText(item.toString());
        }
    }

}
