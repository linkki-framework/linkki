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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.element.annotation.TestUiUtil;
import org.linkki.core.ui.table.hierarchy.CodeTablePmo;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridNoneSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.treegrid.TreeGrid;

public class GridComponentCreatorTest {

    @Test
    public void testCreateComponent_PmoClassIsUsedAsId() {
        BindingContext bindingContext = new BindingContext();
        Grid<?> table = GridComponentCreator.createGrid(new TestTablePmo(), bindingContext);
        assertThat(table.getId().get(), is("TestTablePmo_table"));
    }

    @Test
    public void testInitColumn_FieldLabelsAreUsedAsColumnHeaders() {
        Grid<?> table = createTableWithColumns();

        assertThat(TestUiUtil.getColumnHeaders(table), contains("1", "2", "3", "", ""));
    }

    @Test
    public void testInitColumn_NoTableColumnAnnotation() {
        Grid<?> table = createTableWithColumns();

        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_3).isAutoWidth(), is(false));
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_3).getWidth(), nullValue());
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_3).getFlexGrow(), is(1));
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

        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_2).isAutoWidth(), is(false));
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_2).getWidth(), is(nullValue()));
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_2).getFlexGrow(), is(20));
    }

    @Test
    public void testInitColumn_WidthAndFlexGrow() {
        Grid<?> table = createTableWithColumns();

        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_WITH_WIDTH_AND_FLEX_GROW).isAutoWidth(), is(false));
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_WITH_WIDTH_AND_FLEX_GROW).getWidth(), is("20px"));
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_WITH_WIDTH_AND_FLEX_GROW).getFlexGrow(), is(10));
    }

    @Test
    public void testInitColumn_DefaultNotSelectable() {
        Grid<?> table = createTableWithColumns();

        assertThat(table.getSelectionModel(), is(instanceOf(GridNoneSelectionModel.class)));
    }

    @Test
    public void testInitColumn_DefaultAutoWidth() {
        Grid<?> table = createTableWithColumns();

        table.getColumns().forEach(c -> assertThat(c.isAutoWidth(), is(false)));
    }

    @Test
    public void testInitGrid_ThemeVariants() {
        Grid<?> table = createTableWithColumns();

        assertThat(table.getThemeNames(), containsInAnyOrder(GridVariant.LUMO_NO_ROW_BORDERS.getVariantName(),
                                                             GridVariant.LUMO_WRAP_CELL_CONTENT.getVariantName(),
                                                             GridVariant.LUMO_ROW_STRIPES.getVariantName(),
                                                             GridVariant.LUMO_NO_BORDER.getVariantName()));
    }

    @Test
    public void testInitTreeGrid_ThemeVariants() {
        TreeGrid<?> table = createTreeTableWithColumns();

        assertThat(table.getThemeNames(), containsInAnyOrder(GridVariant.LUMO_NO_ROW_BORDERS.getVariantName(),
                                                             GridVariant.LUMO_WRAP_CELL_CONTENT.getVariantName(),
                                                             GridVariant.LUMO_ROW_STRIPES.getVariantName(),
                                                             GridVariant.LUMO_NO_BORDER.getVariantName()));
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

    private TreeGrid<?> createTreeTableWithColumns() {
        CodeTablePmo containerPmo = new CodeTablePmo();

        BindingContext bindingContext = new BindingContext();
        TreeGrid<?> componentWrapper = (TreeGrid<?>)GridComponentCreator.createGrid(containerPmo, bindingContext);
        return componentWrapper;
    }
}
