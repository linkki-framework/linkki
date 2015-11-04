/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.vaadin.ui.Table;

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
        Table table = factory.createTable();
        assertThat(table, is(notNullValue()));
        // 1, 2 and 3 are the labels for the fields, the delete button has an no label
        assertThat(table.getColumnHeaders(), is(arrayContaining("1", "2", "3", "")));
    }

    @Test
    public void testCreateTable_WidthAndExpandRatioIsReadFromAnnotation() {
        PmoBasedTableFactory<TestColumnPmo> factory = new PmoBasedTableFactory<>(new TestContainerPmo(), ctx,
                propertyDispatcherBuilder);
        Table table = factory.createTable();
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
        Table table = factory.createTable();
        assertThat(table.getPageLength(), is(ContainerPmo.DEFAULT_PAGE_LENGTH));
    }

    @Test
    public void testCreateTable_ItemsAreBound() {
        TestContainerPmo containerPmo = new TestContainerPmo();
        TestColumnPmo columnPmo1 = containerPmo.addItem();
        TestColumnPmo columnPmo2 = containerPmo.addItem();
        assertThat(containerPmo.getItems(), contains(columnPmo1, columnPmo2));

        PmoBasedTableFactory<TestColumnPmo> factory = new PmoBasedTableFactory<>(containerPmo, ctx,
                propertyDispatcherBuilder);
        Table table = factory.createTable();

        assertThat(table.getItemIds(), contains(columnPmo1, columnPmo2));
    }

    @Test
    public void testAddItemButtonPmoUpdatesTable() {
        TestContainerPmo containerPmo = new TestContainerPmo();
        PmoBasedTableFactory<TestColumnPmo> factory = new PmoBasedTableFactory<>(containerPmo, ctx,
                propertyDispatcherBuilder);
        Table table = factory.createTable();
        ButtonPmo addItemButtonPmo = containerPmo.getAddItemButtonPmo().get();

        assertThat(containerPmo.getItems(), is(empty()));
        assertThat(table.getItemIds(), is(empty()));

        addItemButtonPmo.onClick();

        assertThat(containerPmo.getItems(), hasSize(1));
        assertThat(table.getItemIds(), hasSize(1));
    }

    @Test
    public void testDeleteItemInColumnPmoUpdatesTable() {
        TestContainerPmo containerPmo = new TestContainerPmo();
        TestColumnPmo columnPmo1 = containerPmo.addItem();
        TestColumnPmo columnPmo2 = containerPmo.addItem();
        PmoBasedTableFactory<TestColumnPmo> factory = new PmoBasedTableFactory<>(containerPmo, ctx,
                propertyDispatcherBuilder);
        Table table = factory.createTable();

        assertThat(containerPmo.getItems(), contains(columnPmo1, columnPmo2));
        assertThat(table.getItemIds(), contains(columnPmo1, columnPmo2));

        columnPmo2.delete();

        assertThat(containerPmo.getItems(), contains(columnPmo1));
        assertThat(table.getItemIds(), contains(columnPmo1));

        columnPmo1.delete();

        assertThat(containerPmo.getItems(), is(empty()));
        assertThat(table.getItemIds(), is(empty()));
    }

}
