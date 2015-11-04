/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.ui.table.TestColumnPmo;
import org.linkki.core.ui.table.TestContainerPmo;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.ui.Table;

@RunWith(MockitoJUnitRunner.class)
public class TableBindingTest {

    @Mock
    private BindingContext bindingContext;

    private TestContainerPmo containerPmo;

    private Table table = new Table();

    @Mock
    private ItemSetChangeListener listener;

    private TableBinding<TestColumnPmo> tableBinding;

    @Before
    public void setUp() {
        containerPmo = new TestContainerPmo();
        containerPmo.addItem();
        tableBinding = new TableBinding<TestColumnPmo>(bindingContext, table, containerPmo);
        tableBinding.addItemSetChangeListener(listener);

    }

    @Test
    public void testDataSourceSet() {
        assertEquals(tableBinding, table.getContainerDataSource());
    }

    @Test
    public void testUpdateFromPmo_pageLength() {
        containerPmo.setPageLength(23);

        tableBinding.updateFromPmo();

        assertEquals(23, table.getPageLength());
        verifyZeroInteractions(listener);
    }

    @Test
    public void testUpdateFromPmo_newItem() {
        containerPmo.addItem();

        tableBinding.updateFromPmo();

        assertEquals(containerPmo.getItems(), table.getItemIds());
        verify(listener).containerItemSetChange(any());
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testUpdateFromPmo_delItem() {
        TestColumnPmo removed = containerPmo.getItems().remove(0);

        tableBinding.updateFromPmo();

        assertEquals(containerPmo.getItems(), table.getItemIds());
        verify(listener).containerItemSetChange(any());
        verifyNoMoreInteractions(listener);
        verify(bindingContext).removeBindingsForPmo(removed);
        verifyNoMoreInteractions(bindingContext);
    }

}
