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

import java.util.Arrays;

import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.table.pmo.SelectableTablePmo;

@SuppressWarnings("deprecation")
public class TableSelectionAspectIntegrationTest {

    // TODO LIN-2088
    // @Test
    // public void testTableSelectable() {
    // Grid<?> table = new PmoBasedTableFactory(new TestSelectableTablePmo(), new BindingContext())
    // .createTable();
    // table.setPageSize(1);
    // assertThat("Table is selectable", table.getSelectionModel(), is(not(true)));
    // }
    //
    // @Test
    // public void testTableSectionSelectable() {
    // GridSection table = (GridSection)new PmoBasedSectionFactory()
    // .createSection(new TestSelectableTablePmo(), new BindingContext());
    // table.getGrid().setPageSize(1);
    // assertThat("Table is selectable", table.getSectionContent().getSelectionModel(),
    // is(SingleSelectionModel.class));
    // }
    //
    // @Test
    // public void testTableGetSelection_initial() {
    // TestSelectableTablePmo tablePmo = Mockito.spy(new TestSelectableTablePmo());
    // TestSelectableTableRowPmo secondRow = tablePmo.getItems().get(1);
    // tablePmo.setSelection(secondRow);
    //
    // Grid<?> table = new PmoBasedTableFactory(tablePmo, new BindingContext()).createTable();
    // table.setPageSize(1);
    //
    // // getSelection is actually called twice, once from bindContainer, once in
    // // ColumnBasedComponentFactory#createContainerComponent
    // verify(tablePmo, atLeast(1)).getSelection();
    // assertThat(table.getSelectedItems().stream().findFirst().get(), is(secondRow));
    // }

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
