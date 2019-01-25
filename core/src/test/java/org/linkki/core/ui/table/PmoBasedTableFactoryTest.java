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
package org.linkki.core.ui.table;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.Binding;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.container.LinkkiInMemoryContainer;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.ui.Table;

@RunWith(MockitoJUnitRunner.class)
public class PmoBasedTableFactoryTest {

    private BindingContext bindingContext = spy(new BindingContext());

    private TestTablePmo containerPmo = new TestTablePmo();

    private Table table = new Table();

    @SuppressWarnings("null")
    private Binding<Table> binding;

    @SuppressWarnings("null")
    @Mock
    private ItemSetChangeListener listener;


    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        PmoBasedTableFactory<TestRowPmo> factory = new PmoBasedTableFactory<>(containerPmo, bindingContext);
        table = factory.createTable();

        assertThat(bindingContext.getBindings().size(), is(1));
        binding = (Binding<Table>)bindingContext.getBindings().stream().findFirst().get();

        getTableContainer().addItemSetChangeListener(listener);
    }

    @Test
    public void testCreateTable_InitialPageLengthIsSetOnTable() {
        assertThat(table.getPageLength(), is(ContainerPmo.DEFAULT_PAGE_LENGTH));
    }

    @Test
    public void testCreateTable_ItemsAreBound() {
        TestRowPmo rowPmo1 = containerPmo.addItem();
        TestRowPmo rowPmo2 = containerPmo.addItem();

        bindingContext.modelChanged();

        assertThat(containerPmo.getItems(), contains(rowPmo1, rowPmo2));
        assertThat(table.getItemIds(), contains(rowPmo1, rowPmo2));
    }

    protected LinkkiInMemoryContainer<?> getTableContainer() {
        return (LinkkiInMemoryContainer<?>)binding.getBoundComponent().getContainerDataSource();
    }

    @Test
    public void testDataSourceSet() {
        assertEquals(getTableContainer(), table.getContainerDataSource());
    }

    @Test
    public void testUpdateFromPmo_PageLengthIsSet() {
        containerPmo.setPageLength(23);

        binding.updateFromPmo();

        assertEquals(23, table.getPageLength());
        verifyZeroInteractions(listener);
    }

    @Test
    public void testUpdateFromPmo_NewItemsAreAdded() {
        containerPmo.addItem();

        binding.updateFromPmo();

        assertEquals(containerPmo.getItems(), table.getItemIds());
        verify(listener).containerItemSetChange(any());
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testUpdateFromPmo_RemovedItemsAreCleanedUp() {
        containerPmo.addItem();
        bindingContext.modelChanged();
        reset(listener);
        containerPmo.getItems().clear();

        bindingContext.modelChanged();

        assertEquals(containerPmo.getItems(), table.getItemIds());
        verify(listener).containerItemSetChange(any());
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testUpdateFromPmo_FooterIsAlwaysUpdated() {
        TableFooterPmo footerPmo = mock(TableFooterPmo.class);
        containerPmo.setFooterPmo(footerPmo);

        binding.updateFromPmo();
        binding.updateFromPmo();

        // item set did not change
        verifyNoMoreInteractions(listener);

        // each footer property was requested for each call of updateFromPmo
        verify(footerPmo, times(2)).getFooterText(TestRowPmo.PROPERTY_VALUE_1);
        verify(footerPmo, times(2)).getFooterText(TestRowPmo.PROPERTY_VALUE_2);
        verify(footerPmo, times(2)).getFooterText(TestRowPmo.PROPERTY_VALUE_3);
        verify(footerPmo, times(2)).getFooterText(TestRowPmo.PROPERTY_DELETE);
        verifyNoMoreInteractions(footerPmo);

    }

    @Test
    public void testFooterUpdateAfterConstruction() {
        TableFooterPmo footerPmo = mock(TableFooterPmo.class);
        containerPmo.setFooterPmo(footerPmo);

        bindingContext.modelChanged();

        assertTrue(table.isFooterVisible());

        containerPmo.setFooterPmo(null);

        bindingContext.modelChanged();

        assertFalse(table.isFooterVisible());
    }

}
