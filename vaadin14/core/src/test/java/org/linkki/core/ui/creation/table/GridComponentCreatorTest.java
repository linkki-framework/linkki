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

    // @Test
    // public void testInitColumn_FieldLabelsAreUsedAsColumnHeaders() {
    // Grid<?> table = createTableWithColumns();
    //
    // TODO LIN-2088
    // Möglich an den Text der HeaderCell zu kommen?
    // 1, 2 and 3 are the labels for the fields, the delete button has an no label
    // assertThat(table.getHeaderRows().get(0).getCells().stream().filter(c -> c instanceof
    // HeaderCell).map(HeaderCell.class::cast).map(c -> c.getColumn()), is(arrayContaining("1", "2",
    // "3", "")));
    // }

    @Test
    public void testInitColumn_WidthAndExpandRatioIsReadFromAnnotation() {
        Grid<?> table = createTableWithColumns();

        assertThat(table.getColumnByKey("value1").getWidth(), is("100px"));
        // If fixed width, then FlexGrow == 0
        assertThat(table.getColumnByKey("value1").getFlexGrow(), is(0));

        // If UITableColumn.UNDEFINED_WIDTH, then getWidth() == null
        assertThat(table.getColumnByKey("value2").getWidth(), nullValue());
        assertThat(table.getColumnByKey("value2").getFlexGrow(), is(20));

        // If UITableColumn.UNDEFINED_WIDTH, then getWidth() == null
        assertThat(table.getColumnByKey("value3").getWidth(), nullValue());
        assertThat(table.getColumnByKey("value3").getFlexGrow(), is(1));
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
