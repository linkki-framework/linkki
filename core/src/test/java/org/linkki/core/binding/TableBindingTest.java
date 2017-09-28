/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.ui.table.TableFooterPmo;
import org.linkki.core.ui.table.TestRowPmo;
import org.linkki.core.ui.table.TestTablePmo;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("null")
public class TableBindingTest {

    @Mock
    private BindingContext bindingContext;

    private TestTablePmo containerPmo;

    private Table table = new Table();
    private Set<String> columnNames = new HashSet<>(
            Arrays.asList(TestRowPmo.PROPERTY_VALUE_1, TestRowPmo.PROPERTY_VALUE_2, TestRowPmo.PROPERTY_VALUE_3,
                          TestRowPmo.PROPERTY_DELETE));

    @Mock
    private ItemSetChangeListener listener;

    private TableBinding<TestRowPmo> tableBinding;

    @Before
    public void setUp() {
        containerPmo = new TestTablePmo();
        containerPmo.addItem();

        columnNames.forEach(c -> table.addGeneratedColumn(c, (source, itemId, columnId) -> new Label()));

        tableBinding = new TableBinding<TestRowPmo>(bindingContext, table, containerPmo);
        tableBinding.addItemSetChangeListener(listener);
    }

    @Test
    public void testDataSourceSet() {
        assertEquals(tableBinding, table.getContainerDataSource());
    }

    @Test
    public void testUpdateFromPmo_PageLengthIsSet() {
        containerPmo.setPageLength(23);

        tableBinding.updateFromPmo();

        assertEquals(23, table.getPageLength());
        verifyZeroInteractions(listener);
    }

    @Test
    public void testUpdateFromPmo_NewItemsAreAdded() {
        containerPmo.addItem();

        tableBinding.updateFromPmo();

        assertEquals(containerPmo.getItems(), table.getItemIds());
        verify(listener).containerItemSetChange(any());
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testUpdateFromPmo_RemovedItemsAreCleanedUp() {
        TestRowPmo removed = containerPmo.getItems().remove(0);

        tableBinding.updateFromPmo();

        assertEquals(containerPmo.getItems(), table.getItemIds());
        verify(listener).containerItemSetChange(any());
        verifyNoMoreInteractions(listener);
        verify(bindingContext).removeBindingsForPmo(removed);
        verifyNoMoreInteractions(bindingContext);
    }

    @Test
    public void testUpdateFromPmo_FooterIsAlwaysUpdated() {
        TableFooterPmo footerPmo = mock(TableFooterPmo.class);
        containerPmo.setFooterPmo(footerPmo);

        tableBinding.updateFromPmo();
        tableBinding.updateFromPmo();

        // item set did not change
        verifyNoMoreInteractions(listener);

        // each footer property was requested for each call of updateFromPmo
        verify(footerPmo, times(2)).getFooterText(TestRowPmo.PROPERTY_VALUE_1);
        verify(footerPmo, times(2)).getFooterText(TestRowPmo.PROPERTY_VALUE_2);
        verify(footerPmo, times(2)).getFooterText(TestRowPmo.PROPERTY_VALUE_3);
        verify(footerPmo, times(2)).getFooterText(TestRowPmo.PROPERTY_DELETE);
        verifyNoMoreInteractions(footerPmo);

    }

}
