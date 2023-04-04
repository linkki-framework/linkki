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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.element.annotation.TestUiUtil;
import org.linkki.core.ui.table.hierarchy.CodeTablePmo;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridNoneSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.treegrid.TreeGrid;

class GridComponentCreatorTest {

    @Test
    void testCreateComponent_PmoClassIsUsedAsId() {
        BindingContext bindingContext = new BindingContext();
        Grid<?> table = GridComponentCreator.createGrid(new TestTablePmo(), bindingContext);
        assertThat(table.getId().get(), is("TestTablePmo_table"));
    }

    @Test
    void testInitColumn_FieldLabelsAreUsedAsColumnHeaders() {
        Grid<?> table = createTableWithColumns();

        assertThat(TestUiUtil.getColumnHeaders(table), contains("1", "2", "3", "", ""));
    }

    @Test
    void testInitColumn_NoTableColumnAnnotation() {
        Grid<?> table = createTableWithColumns();

        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_3).isAutoWidth(), is(false));
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_3).getWidth(), nullValue());
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_3).getFlexGrow(), is(1));
    }

    @Test
    void testInitColumn_OnlyWidth() {
        Grid<?> table = createTableWithColumns();

        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_1).isAutoWidth(), is(false));
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_1).getWidth(), is("100px"));
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_1).getFlexGrow(), is(0));
    }

    @Test
    void testInitColumn_OnlyFlexGrow() {
        Grid<?> table = createTableWithColumns();

        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_2).isAutoWidth(), is(false));
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_2).getWidth(), is(nullValue()));
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_VALUE_2).getFlexGrow(), is(20));
    }

    @Test
    void testInitColumn_WidthAndFlexGrow() {
        Grid<?> table = createTableWithColumns();

        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_WITH_WIDTH_AND_FLEX_GROW).isAutoWidth(), is(false));
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_WITH_WIDTH_AND_FLEX_GROW).getWidth(), is("20px"));
        assertThat(table.getColumnByKey(TestRowPmo.PROPERTY_WITH_WIDTH_AND_FLEX_GROW).getFlexGrow(), is(10));
    }

    @Test
    void testInitColumn_DefaultNotSelectable() {
        Grid<?> table = createTableWithColumns();

        assertThat(table.getSelectionModel(), is(instanceOf(GridNoneSelectionModel.class)));
    }

    @Test
    void testInitColumn_DefaultAutoWidth() {
        Grid<?> table = createTableWithColumns();

        table.getColumns().forEach(c -> assertThat(c.isAutoWidth(), is(false)));
    }

    @Test
    void testInitGrid_ThemeVariants() {
        Grid<?> table = createTableWithColumns();

        assertThat(table.getThemeNames(), containsInAnyOrder(GridVariant.LUMO_WRAP_CELL_CONTENT.getVariantName(),
                                                             GridVariant.LUMO_COMPACT.getVariantName(),
                                                             GridVariant.LUMO_NO_BORDER.getVariantName()));
    }

    @Test
    void testInitGrid_SelectionDisabled() {
        Grid<?> table = createTableWithColumns();

        assertThat(table.getSelectionModel(), is(instanceOf(GridNoneSelectionModel.class)));
    }

    @Test
    void testInitTreeGrid_ThemeVariants() {
        TreeGrid<?> table = createTreeTableWithColumns();

        assertThat(table.getThemeNames(), containsInAnyOrder(GridVariant.LUMO_WRAP_CELL_CONTENT.getVariantName(),
                                                             GridVariant.LUMO_COMPACT.getVariantName(),
                                                             GridVariant.LUMO_NO_BORDER.getVariantName()));
    }

    @Test
    void testInitTreeGrid_SelectionDisabled() {
        TreeGrid<?> table = createTreeTableWithColumns();

        assertThat(table.getSelectionModel(), is(instanceOf(GridNoneSelectionModel.class)));
    }

    @Test
    void testInitTreeGrid_ShouldNotCallModelChanged_AndFooterMustExists() {
        BindingContext bindingContext = new BindingContext.BindingContextBuilder()
                .afterModelChangedHandler(() -> fail("modelChanged should not be called during table creation"))
                .build();

        Grid<?> grid = createTableWithColumns(bindingContext);

        assertThat(grid.getFooterRows()).hasSize(1);
    }

    private Grid<?> createTableWithColumns() {
        BindingContext bindingContext = new BindingContext();
        return createTableWithColumns(bindingContext);
    }

    private Grid<?> createTableWithColumns(BindingContext bindingContext) {
        TestTablePmo containerPmo = new TestTablePmo(s -> "footer");
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
