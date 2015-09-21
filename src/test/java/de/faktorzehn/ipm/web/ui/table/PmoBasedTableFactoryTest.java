/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.faktorzehn.ipm.web.ButtonPmo;
import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyBehaviorProvider;
import de.faktorzehn.ipm.web.ui.section.annotations.UITableColumn;

@RunWith(MockitoJUnitRunner.class)
public class PmoBasedTableFactoryTest {

    @Mock
    private BindingContext ctx;

    @Mock
    private PropertyBehaviorProvider pbp;

    @Test
    public void testCreateTable_FieldLabelsAreUsedAsColumnHeaders() {
        PmoBasedTableFactory<TestColumnPmo> factory = new PmoBasedTableFactory<>(new TestContainerPmo(), ctx, pbp);
        PmoBasedTable<TestColumnPmo> table = factory.createTable();
        assertThat(table, is(notNullValue()));
        assertThat(table.getColumnHeaders(), is(arrayContaining("1:", "2:", "3:")));
    }

    @Test
    public void testCreateTable_WidthAndExpandRatioIsReadFromAnnotation() {
        PmoBasedTableFactory<TestColumnPmo> factory = new PmoBasedTableFactory<>(new TestContainerPmo(), ctx, pbp);
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
    public void testCreateTable_DeleteColumnHeaderIsReadFromAnnotation() {
        TestContainerPmoWithAnnotation containerPmo = new TestContainerPmoWithAnnotation();
        containerPmo.setDeleteAction(System.out::println);
        PmoBasedTableFactory<TestColumnPmo> factory = new PmoBasedTableFactory<>(containerPmo, ctx, pbp);
        PmoBasedTable<TestColumnPmo> table = factory.createTable();
        assertThat(table, is(notNullValue()));
        assertThat(table.getColumnHeaders(),
                   is(arrayContaining("1:", "2:", "3:", TestContainerPmoWithAnnotation.DELETE_ITEM_COLUMN_HEADER)));
    }

    @Test
    public void testCreateTable_DefaultDeleteColumnHeaderIsUsedIfAnnotationIsMissing() {
        TestContainerPmo containerPmo = new TestContainerPmo();
        containerPmo.setDeleteAction(System.out::println);
        PmoBasedTableFactory<TestColumnPmo> factory = new PmoBasedTableFactory<>(containerPmo, ctx, pbp);
        PmoBasedTable<TestColumnPmo> table = factory.createTable();
        assertThat(table, is(notNullValue()));
        assertThat(table.getColumnHeaders(), is(arrayContaining("1:", "2:", "3:", "Entfernen")));
    }

    @Test
    public void testCreateTable_InitialPageLengthIsSetOnTable() {
        TestContainerPmo containerPmo = new TestContainerPmo();
        PmoBasedTableFactory<TestColumnPmo> factory = new PmoBasedTableFactory<>(containerPmo, ctx, pbp);
        PmoBasedTable<TestColumnPmo> table = factory.createTable();
        assertThat(table.getPageLength(), is(ContainerPmo.DEFAULT_PAGE_LENGTH));
    }

    @Test
    public void testCreateTable_PageLengthListenerIsRegistered() {
        TestContainerPmo containerPmo = new TestContainerPmo();
        PmoBasedTableFactory<TestColumnPmo> factory = new PmoBasedTableFactory<>(containerPmo, ctx, pbp);
        factory.createTable();
        assertThat(containerPmo.pageLengthListeners(), hasSize(1));
    }

    @Test
    public void testCreateTable_PageLengthListenerChangesTablesPageLength() {
        TestContainerPmo containerPmo = new TestContainerPmo();
        PmoBasedTableFactory<TestColumnPmo> factory = new PmoBasedTableFactory<>(containerPmo, ctx, pbp);
        PmoBasedTable<TestColumnPmo> table = factory.createTable();

        containerPmo.setPageLength(5);
        assertThat(table.getPageLength(), is(5));

        containerPmo.setPageLength(0);
        assertThat(table.getPageLength(), is(0));

        // This is easier than removing the listener explicitly...
        containerPmo.cleanPageLengthListeners();

        containerPmo.setPageLength(10);
        assertThat(table.getPageLength(), is(0));
    }

    @Test
    public void testCreateTable_ItemsAreBound() {
        TestContainerPmo containerPmo = new TestContainerPmo();
        PmoBasedTableFactory<TestColumnPmo> factory = new PmoBasedTableFactory<>(containerPmo, ctx, pbp);
        PmoBasedTable<TestColumnPmo> table = factory.createTable();

        assertThat(containerPmo.getItems().size(), is(2));
        assertThat(table.getItemIds().size(), is(2));
    }

    @Test
    public void testAddItemButtonPmoUpdatesTable() {
        TestContainerPmo containerPmo = new TestContainerPmo();
        PmoBasedTableFactory<TestColumnPmo> factory = new PmoBasedTableFactory<>(containerPmo, ctx, pbp);
        PmoBasedTable<TestColumnPmo> table = factory.createTable();
        ButtonPmo addItemButtonPmo = table.addItemButtonPmo(ctx).get();

        assertThat(containerPmo.getItems().size(), is(2));
        assertThat(table.getItemIds().size(), is(2));

        addItemButtonPmo.onClick();

        assertThat(containerPmo.getItems().size(), is(3));
        assertThat(table.getItemIds().size(), is(3));
    }

}
