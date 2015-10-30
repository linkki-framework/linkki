/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.faktorzehn.ipm.web.ButtonPmo;
import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;
import de.faktorzehn.ipm.web.ui.section.annotations.UITableColumn;

@RunWith(MockitoJUnitRunner.class)
public class PmoBasedTableFactoryTest {

    @Mock
    private BindingContext ctx;

    @Mock
    private Function<TestColumnPmo, PropertyDispatcher> propertyDispatcherBuilder;

    @Test
    public void testCreateTable_FieldLabelsAreUsedAsColumnHeaders() {
        PmoBasedTableFactory<TestColumnPmo> factory = new PmoBasedTableFactory<>(new TestContainerPmo(), ctx,
                propertyDispatcherBuilder);
        PmoBasedTable<TestColumnPmo> table = factory.createTable();
        assertThat(table, is(notNullValue()));
        assertThat(table.getColumnHeaders(), is(arrayContaining("1", "2", "3")));
    }

    @Test
    public void testCreateTable_WidthAndExpandRatioIsReadFromAnnotation() {
        PmoBasedTableFactory<TestColumnPmo> factory = new PmoBasedTableFactory<>(new TestContainerPmo(), ctx,
                propertyDispatcherBuilder);
        PmoBasedTable<TestColumnPmo> table = factory.createTable();
        assertThat(table, is(notNullValue()));

        assertThat(table.getColumnWidth("value1"), is(100));
        assertThat(table.getColumnExpandRatio("value1"), is(UITableColumn.UNDEFINED_EXPAND_RATIO));

        assertThat(table.getColumnWidth("value2"), is(UITableColumn.UNDEFINED_WIDTH));
        assertThat(table.getColumnExpandRatio("value2"), is(2.0f));

        assertThat(table.getColumnWidth("value3"), is(UITableColumn.UNDEFINED_WIDTH));
        assertThat(table.getColumnExpandRatio("value3"), is(UITableColumn.UNDEFINED_EXPAND_RATIO));
    }

    @Test
    public void testCreateTable_InitialPageLengthIsSetOnTable() {
        TestContainerPmo containerPmo = new TestContainerPmo();
        PmoBasedTableFactory<TestColumnPmo> factory = new PmoBasedTableFactory<>(containerPmo, ctx,
                propertyDispatcherBuilder);
        PmoBasedTable<TestColumnPmo> table = factory.createTable();
        assertThat(table.getPageLength(), is(ContainerPmo.DEFAULT_PAGE_LENGTH));
    }

    @Test
    public void testCreateTable_ItemsAreBound() {
        TestContainerPmo containerPmo = new TestContainerPmo();
        PmoBasedTableFactory<TestColumnPmo> factory = new PmoBasedTableFactory<>(containerPmo, ctx,
                propertyDispatcherBuilder);
        PmoBasedTable<TestColumnPmo> table = factory.createTable();

        assertThat(containerPmo.getItems().size(), is(2));
        assertThat(table.getItemIds().size(), is(2));
    }

    @Test
    public void testAddItemButtonPmoUpdatesTable() {
        TestContainerPmo containerPmo = new TestContainerPmo();
        PmoBasedTableFactory<TestColumnPmo> factory = new PmoBasedTableFactory<>(containerPmo, ctx,
                propertyDispatcherBuilder);
        PmoBasedTable<TestColumnPmo> table = factory.createTable();
        ButtonPmo addItemButtonPmo = table.getNewItemButtonPmo().get();

        assertThat(containerPmo.getItems().size(), is(2));
        assertThat(table.getItemIds().size(), is(2));

        addItemButtonPmo.onClick();

        assertThat(containerPmo.getItems().size(), is(3));
        assertThat(table.getItemIds().size(), is(3));
    }

}
