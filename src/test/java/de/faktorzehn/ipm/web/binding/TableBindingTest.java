/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.ui.Table;

import de.faktorzehn.ipm.web.ui.table.TestColumnPmo;
import de.faktorzehn.ipm.web.ui.table.TestContainerPmo;

@RunWith(MockitoJUnitRunner.class)
public class TableBindingTest {

    @Mock
    private BindingContext bindingContext;

    private TestContainerPmo containerPmo = new TestContainerPmo();

    @Mock
    private Table table;

    @Mock
    private ItemSetChangeListener listener;

    private TableBinding<TestColumnPmo> tableBinding;

    @Before
    public void setUp() {
        tableBinding = new TableBinding<TestColumnPmo>(bindingContext, table, containerPmo);
        tableBinding.addItemSetChangeListener(listener);
        verify(table).setContainerDataSource(tableBinding);
    }

    @Test
    public void testUpdateFromPmo_noChanged() {
        tableBinding.updateFromPmo();

        verify(table).setPageLength(15);
        verifyNoMoreInteractions(table);
        verifyZeroInteractions(listener);
        verifyZeroInteractions(bindingContext);
    }

    @Test
    public void testUpdateFromPmo_newItem() {
        containerPmo.addItem();

        tableBinding.updateFromPmo();

        verify(table).setPageLength(15);
        verifyNoMoreInteractions(table);
        verify(listener).containerItemSetChange(any());
        verifyZeroInteractions(bindingContext);
    }

    @Test
    public void testUpdateFromPmo_delItem() {
        TestColumnPmo removed = containerPmo.getItems().remove(0);

        tableBinding.updateFromPmo();

        verify(table).setPageLength(15);
        verifyNoMoreInteractions(table);
        verify(listener).containerItemSetChange(any());
        verifyNoMoreInteractions(listener);
        verify(bindingContext).removeBindingsForPmo(removed);
        verifyNoMoreInteractions(bindingContext);
    }

}
