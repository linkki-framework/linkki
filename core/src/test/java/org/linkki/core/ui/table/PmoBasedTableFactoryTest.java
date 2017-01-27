/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.table;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.section.annotations.UITableColumn;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.vaadin.ui.Table;

@RunWith(MockitoJUnitRunner.class)
public class PmoBasedTableFactoryTest {

    @Mock
    private BindingContext ctx;

    @Test
    public void testCreateTable_FieldLabelsAreUsedAsColumnHeaders() {
        PmoBasedTableFactory<TestRowPmo> factory = new PmoBasedTableFactory<>(new TestTablePmo(), ctx);
        Table table = factory.createTable();
        assertThat(table, is(notNullValue()));
        // 1, 2 and 3 are the labels for the fields, the delete button has an no label
        assertThat(table.getColumnHeaders(), is(arrayContaining("1", "2", "3", "")));
    }

    @Test
    public void testCreateTable_WidthAndExpandRatioIsReadFromAnnotation() {
        PmoBasedTableFactory<TestRowPmo> factory = new PmoBasedTableFactory<>(new TestTablePmo(), ctx);
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
        TestTablePmo containerPmo = new TestTablePmo();
        PmoBasedTableFactory<TestRowPmo> factory = new PmoBasedTableFactory<>(containerPmo, ctx);
        Table table = factory.createTable();
        assertThat(table.getPageLength(), is(ContainerPmo.DEFAULT_PAGE_LENGTH));
    }

    @Test
    public void testCreateTable_ItemsAreBound() {
        TestTablePmo containerPmo = new TestTablePmo();
        TestRowPmo columnPmo1 = containerPmo.addItem();
        TestRowPmo columnPmo2 = containerPmo.addItem();
        assertThat(containerPmo.getItems(), contains(columnPmo1, columnPmo2));

        PmoBasedTableFactory<TestRowPmo> factory = new PmoBasedTableFactory<>(containerPmo, ctx);
        Table table = factory.createTable();

        assertThat(table.getItemIds(), contains(columnPmo1, columnPmo2));
    }

    @Test
    public void testGetItemPmoClass() {
        PmoBasedTableFactory<TestRowPmo> pmoBasedTableFactory = new PmoBasedTableFactory<>(new TestTablePmo(), ctx);

        Class<?> itemPmoClass = pmoBasedTableFactory.getItemPmoClass();

        assertThat(itemPmoClass, is(TestRowPmo.class));
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("unchecked")
    public void testGetItemPmoClass_exception() {
        PmoBasedTableFactory<TestRowPmo> pmoBasedTableFactory = new PmoBasedTableFactory<>(new RawContainerPmo(), ctx);

        Class<?> itemPmoClass = pmoBasedTableFactory.getItemPmoClass();

        assertThat(itemPmoClass, is(TestRowPmo.class));
    }

    @SuppressWarnings("rawtypes")
    private static class RawContainerPmo implements ContainerPmo {

        @Override
        public List getItems() {
            return new ArrayList<>();
        }

    }

}
