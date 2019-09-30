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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.linkki.test.matcher.Matchers.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ContainerBinding;
import org.linkki.core.defaults.columnbased.pmo.TableFooterPmo;
import org.linkki.core.ui.creation.table.container.LinkkiInMemoryContainer;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("deprecation")
@ExtendWith(MockitoExtension.class)
public class PmoBasedTableFactoryTest {

    private BindingContext bindingContext = spy(new BindingContext());

    private static LinkkiInMemoryContainer<?> getTableContainer(BindingContext bindingContext) {
        return (LinkkiInMemoryContainer<?>)((com.vaadin.v7.ui.Table)getFirstBinding(bindingContext).getBoundComponent())
                .getContainerDataSource();
    }

    private static ContainerBinding getFirstBinding(BindingContext bindingContext) {
        return (ContainerBinding)bindingContext.getBindings().stream().findFirst().get();
    }

    private static com.vaadin.v7.data.Container.ItemSetChangeListener addItemSetChangeListener(
            BindingContext bindingContext) {
        com.vaadin.v7.data.Container.ItemSetChangeListener listener = mock(com.vaadin.v7.data.Container.ItemSetChangeListener.class);
        getTableContainer(bindingContext).addItemSetChangeListener(listener);
        return listener;
    }

    @Test
    public void testPageLength() {
        TestTablePmo tablePmo = new TestTablePmo(15);

        com.vaadin.v7.ui.Table table = new PmoBasedTableFactory(tablePmo, bindingContext).createTable();
        com.vaadin.v7.data.Container.ItemSetChangeListener itemSetChangedListener = addItemSetChangeListener(bindingContext);

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
        com.vaadin.v7.ui.Table table = new PmoBasedTableFactory(tablePmo, bindingContext).createTable();

        assertThat(tablePmo.getItems(), contains(rowPmo1, rowPmo2));
        assertThat(table.getItemIds(), contains(rowPmo1, rowPmo2));
    }

    @Test
    public void testItems_UponUpdate_AddItems() {
        TestTablePmo tablePmo = new TestTablePmo();

        com.vaadin.v7.ui.Table table = new PmoBasedTableFactory(tablePmo, bindingContext).createTable();
        com.vaadin.v7.data.Container.ItemSetChangeListener itemSetChangedListener = addItemSetChangeListener(bindingContext);

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

        com.vaadin.v7.ui.Table table = new PmoBasedTableFactory(tablePmo, bindingContext).createTable();
        com.vaadin.v7.data.Container.ItemSetChangeListener itemSetChangedListener = addItemSetChangeListener(bindingContext);

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
        com.vaadin.v7.ui.Table table = new PmoBasedTableFactory(tablePmo, bindingContext).createTable();
        assertThat(getTableContainer(bindingContext), is(table.getContainerDataSource()));
    }

    @Test
    public void testFooterUpdateAfterConstruction() {
        TableFooterPmo footerPmo = property -> property;
        TestTablePmo tablePmo = new TestTablePmo(footerPmo);

        com.vaadin.v7.ui.Table table = new PmoBasedTableFactory(tablePmo, bindingContext).createTable();
        com.vaadin.v7.data.Container.ItemSetChangeListener itemSetChangedListener = addItemSetChangeListener(bindingContext);

        assertThat(table.isFooterVisible());
        assertThat(table.getColumnFooter(TestRowPmo.PROPERTY_VALUE_1), is(TestRowPmo.PROPERTY_VALUE_1));
        assertThat(table.getColumnFooter(TestRowPmo.PROPERTY_VALUE_2), is(TestRowPmo.PROPERTY_VALUE_2));

        tablePmo.setFooterPmo(null);
        bindingContext.modelChanged();
        assertThat(table.isFooterVisible(), is(false));
        verifyNoMoreInteractions(itemSetChangedListener);

        tablePmo.setFooterPmo(property -> "test");
        bindingContext.modelChanged();
        assertThat(table.isFooterVisible());
        assertThat(table.getColumnFooter(TestRowPmo.PROPERTY_VALUE_1), is("test"));
        verifyNoMoreInteractions(itemSetChangedListener);
    }

}
