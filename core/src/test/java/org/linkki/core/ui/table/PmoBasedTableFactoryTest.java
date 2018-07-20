/*
 * Copyright Faktor Zehn AG.
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

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.section.annotations.UITableColumn;

import com.vaadin.ui.Table;

public class PmoBasedTableFactoryTest {

    private BindingContext ctx = new BindingContext();

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
    public void testCreateTable_CollapsibleAndCollapsedIsReadFromAnnotation() {
        PmoBasedTableFactory<TestRowPmo> factory = new PmoBasedTableFactory<>(new TestTablePmo(), ctx);
        Table table = factory.createTable();
        assertThat(table, is(notNullValue()));

        assertThat(table.isColumnCollapsible("value1"), is(true));
        assertThat(table.isColumnCollapsed("value1"), is(UITableColumn.UNDEFINED_COLLAPSED));

        assertThat(table.isColumnCollapsible("value2"), is(true));
        assertThat(table.isColumnCollapsed("value2"), is(true));

        assertThat(table.isColumnCollapsible("value3"), is(UITableColumn.UNDEFINED_COLLAPSIBLE));
        assertThat(table.isColumnCollapsed("value3"), is(UITableColumn.UNDEFINED_COLLAPSED));
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


}
