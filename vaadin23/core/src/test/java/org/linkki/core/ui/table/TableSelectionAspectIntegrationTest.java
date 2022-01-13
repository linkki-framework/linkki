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

package org.linkki.core.ui.table;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.ui.creation.table.GridComponentCreator;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.table.pmo.SelectableTablePmo;
import org.linkki.core.vaadin.component.section.GridSection;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;

public class TableSelectionAspectIntegrationTest {

    @Test
    public void testTableSelectable() {
        Grid<?> table = GridComponentCreator.createGrid(new TestSelectableTablePmo(), new BindingContext());
        table.setPageSize(1);
        assertThat("Table is selectable", table.getSelectionModel(), is(instanceOf(GridSingleSelectionModel.class)));
    }

    @Test
    public void testTableSectionSelectable() {
        GridSection table = (GridSection)new PmoBasedSectionFactory()
                .createSection(new TestSelectableTablePmo(), new BindingContext());
        table.getGrid().setPageSize(1);
        assertThat("Table in table section is selectable", table.getSectionContent().getSelectionModel(),
                   is(instanceOf(GridSingleSelectionModel.class)));
    }

    @Test
    public void testTableGetSelection_initial() {
        TestSelectableTablePmo tablePmo = spy(new TestSelectableTablePmo());
        TestSelectableTableRowPmo secondRow = tablePmo.getItems().get(1);
        tablePmo.setSelection(secondRow);

        Grid<?> table = GridComponentCreator.createGrid(tablePmo, new BindingContext());
        table.setPageSize(1);

        // getSelection is actually called twice, once from bindContainer, once in
        // ColumnBasedComponentFactory#createContainerComponent
        verify(tablePmo, atLeast(1)).getSelection();
        assertThat(table.getSelectedItems().stream().findFirst().get(), is(secondRow));
    }

    public static class TestSelectableTablePmo extends SimpleTablePmo<String, TestSelectableTableRowPmo>
            implements SelectableTablePmo<TestSelectableTableRowPmo> {

        private TestSelectableTableRowPmo selectedRow;

        TestSelectableTablePmo() {
            super(Arrays.asList("row1", "row2"));
        }

        @Override
        protected TestSelectableTableRowPmo createRow(String modelObject) {
            return new TestSelectableTableRowPmo(modelObject);
        }

        @Override
        public TestSelectableTableRowPmo getSelection() {
            return selectedRow;
        }

        @Override
        public void setSelection(TestSelectableTableRowPmo selectedRow) {
            this.selectedRow = selectedRow;
        }

        @Override
        public void onDoubleClick() {
            // does nothing
        }

    }

    public static class TestSelectableTableRowPmo {

        private final String value;

        TestSelectableTableRowPmo(String value) {
            this.value = value;
        }

        @UILabel(position = 0)
        public String getValue() {
            return value;
        }
    }
}
