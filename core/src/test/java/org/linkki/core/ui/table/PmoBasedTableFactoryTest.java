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
package org.linkki.core.ui.table;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ContainerBinding;
import org.linkki.core.container.LinkkiInMemoryContainer;
import org.mockito.junit.MockitoJUnitRunner;

import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.ui.Table;

@RunWith(MockitoJUnitRunner.class)
public class PmoBasedTableFactoryTest {

    private BindingContext bindingContext = spy(new BindingContext());

    private static LinkkiInMemoryContainer<?> getTableContainer(BindingContext bindingContext) {
        return (LinkkiInMemoryContainer<?>)getFirstBinding(bindingContext).getBoundComponent().getContainerDataSource();
    }

    @SuppressWarnings("unchecked")
    private static ContainerBinding<Table> getFirstBinding(BindingContext bindingContext) {
        return (ContainerBinding<Table>)bindingContext.getBindings().stream().findFirst().get();
    }

    private static ItemSetChangeListener addItemSetChangeListener(BindingContext bindingContext) {
        ItemSetChangeListener listener = mock(ItemSetChangeListener.class);
        getTableContainer(bindingContext).addItemSetChangeListener(listener);
        return listener;
    }

    @Test
    public void testPageLength() {
        TestTablePmo tablePmo = new TestTablePmo(15);

        Table table = new PmoBasedTableFactory<>(tablePmo, bindingContext).createTable();
        ItemSetChangeListener itemSetChangedListener = addItemSetChangeListener(bindingContext);

        assertThat(table.getPageLength(), is(15));

        tablePmo.setPageLength(25);
        bindingContext.modelChanged();

        assertThat(table.getPageLength(), is(25));
        verifyZeroInteractions(itemSetChangedListener);
    }

    @Test
    public void testItems_AfterConstruction() {
        TestRowPmo rowPmo1 = new TestRowPmo();
        TestRowPmo rowPmo2 = new TestRowPmo();

        TestTablePmo tablePmo = new TestTablePmo(rowPmo1, rowPmo2);
        Table table = new PmoBasedTableFactory<>(tablePmo, bindingContext).createTable();

        assertThat(tablePmo.getItems(), contains(rowPmo1, rowPmo2));
        assertThat(table.getItemIds(), contains(rowPmo1, rowPmo2));
    }

    @Test
    public void testItems_UponUpdate_AddItems() {
        TestTablePmo tablePmo = new TestTablePmo();

        Table table = new PmoBasedTableFactory<>(tablePmo, bindingContext).createTable();
        ItemSetChangeListener itemSetChangedListener = addItemSetChangeListener(bindingContext);

        assertThat(table.getItemIds(), is(empty()));

        TestRowPmo newRow = new TestRowPmo();
        tablePmo.getItems().add(newRow);

        bindingContext.modelChanged();

        assertThat(tablePmo.getItems(), contains(newRow));
        assertThat(table.getItemIds(), contains(newRow));
        verify(itemSetChangedListener).containerItemSetChange(any());
        verifyNoMoreInteractions(itemSetChangedListener);
    }

    @Test
    public void testItems_UponUpdate_RemoveItems() {
        TestRowPmo row1 = new TestRowPmo();
        TestRowPmo row2 = new TestRowPmo();
        TestTablePmo tablePmo = new TestTablePmo(row1, row2);

        Table table = new PmoBasedTableFactory<>(tablePmo, bindingContext).createTable();
        ItemSetChangeListener itemSetChangedListener = addItemSetChangeListener(bindingContext);

        assertThat(table.getItemIds().size(), is(2));

        tablePmo.getItems().remove(row1);
        bindingContext.modelChanged();

        assertThat(tablePmo.getItems(), contains(row2));
        assertThat(table.getItemIds(), contains(row2));
        verify(itemSetChangedListener).containerItemSetChange(any());
        verifyNoMoreInteractions(itemSetChangedListener);
    }

    @Test
    public void testDataSourceSet() {
        TestTablePmo tablePmo = new TestTablePmo();
        Table table = new PmoBasedTableFactory<>(tablePmo, bindingContext).createTable();
        assertThat(getTableContainer(bindingContext), is(table.getContainerDataSource()));
    }

    @Test
    public void testFooterUpdateAfterConstruction() {
        TableFooterPmo footerPmo = property -> property;
        TestTablePmo tablePmo = new TestTablePmo(footerPmo);

        Table table = new PmoBasedTableFactory<>(tablePmo, bindingContext).createTable();
        ItemSetChangeListener itemSetChangedListener = addItemSetChangeListener(bindingContext);

        assertTrue(table.isFooterVisible());
        assertThat(table.getColumnFooter(TestRowPmo.PROPERTY_VALUE_1), is(TestRowPmo.PROPERTY_VALUE_1));
        assertThat(table.getColumnFooter(TestRowPmo.PROPERTY_VALUE_2), is(TestRowPmo.PROPERTY_VALUE_2));

        tablePmo.setFooterPmo(null);
        bindingContext.modelChanged();
        assertFalse(table.isFooterVisible());
        verifyNoMoreInteractions(itemSetChangedListener);

        tablePmo.setFooterPmo(property -> "test");
        bindingContext.modelChanged();
        assertTrue(table.isFooterVisible());
        assertThat(table.getColumnFooter(TestRowPmo.PROPERTY_VALUE_1), is("test"));
        verifyNoMoreInteractions(itemSetChangedListener);
    }

}
