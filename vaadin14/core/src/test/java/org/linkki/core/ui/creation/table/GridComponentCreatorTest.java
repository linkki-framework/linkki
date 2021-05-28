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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;

import com.vaadin.flow.component.grid.Grid;

public class GridComponentCreatorTest {

    @Test
    public void testCreateComponent_PmoClassIsUsedAsId() {
        BindingContext bindingContext = new BindingContext();
        Grid<?> table = GridComponentCreator.createGrid(new TestTablePmo(), bindingContext);
        assertThat(table.getId().get(), is("TestTablePmo_table"));
    }

    @Test
    public void testInitColumn_NoTableColumnAnnotation() {
        Grid<?> table = createTableWithColumns();

        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_3).isAutoWidth(), is(true));
        assertThat("Properties without @UITableColumn annotation should not have a defined width",
                   table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_3).getWidth(), nullValue());
        assertThat("Properties without @UITableColumn annotation should not take up more space than necessary",
                   table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_3).getFlexGrow(), is(0));
    }

    @Test
    public void testInitColumn_OnlyWidth() {
        Grid<?> table = createTableWithColumns();

        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_1).isAutoWidth(), is(false));
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_1).getWidth(), is("100px"));
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_1).getFlexGrow(), is(0));
    }

    @Test
    public void testInitColumn_OnlyFlexGrow() {
        Grid<?> table = createTableWithColumns();

        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_2).isAutoWidth(), is(true));
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_2).getWidth(), nullValue());
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_2).getFlexGrow(), is(20));
    }

    @Test
    public void testInitColumn_WidthAndFlexGrow() {
        Grid<?> table = createTableWithColumns();

        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_WITH_WIDTH_AND_FLEX_GROW).isAutoWidth(), is(false));
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_WITH_WIDTH_AND_FLEX_GROW).getWidth(), is("20px"));
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_WITH_WIDTH_AND_FLEX_GROW).getFlexGrow(), is(10));
    }

    // TODO LIN-2138
    // @Test
    // public void testInitColumn_CollapsibleAndCollapsedIsReadFromAnnotation() {
    // Grid<?> table = createTableWithColumns();
    //
    // assertThat(table.isColumnCollapsible("value1"), is(true));
    // assertThat(table.isColumnCollapsed("value1"), is(false));
    //
    // assertThat(table.isColumnCollapsible("value2"), is(true));
    // assertThat(table.isColumnCollapsed("value2"), is(true));
    //
    // assertThat(table.isColumnCollapsible("value3"), is(false));
    // assertThat(table.isColumnCollapsed("value3"), is(false));
    // }

    private Grid<?> createTableWithColumns() {
        TestTablePmo containerPmo = new TestTablePmo();
        BindingContext bindingContext = new BindingContext();
        Grid<?> componentWrapper = GridComponentCreator.createGrid(containerPmo, bindingContext);
        return componentWrapper;
    }

}
