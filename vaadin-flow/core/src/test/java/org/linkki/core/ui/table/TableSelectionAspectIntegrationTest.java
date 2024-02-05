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
import static org.hamcrest.Matchers.empty;
import static org.linkki.test.matcher.Matchers.hasValue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.creation.table.GridComponentCreator;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.table.aspects.annotation.BindTableSelection;
import org.linkki.core.ui.table.pmo.MultiSelectableTablePmo;
import org.linkki.core.ui.table.pmo.SelectableTablePmo;
import org.linkki.core.vaadin.component.section.GridSection;

import com.github.mvysny.kaributesting.v10.GridKt;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;

class TableSelectionAspectIntegrationTest {

    @BeforeEach
    void mockVaadin() {
        MockVaadin.setup();
    }

    @AfterEach
    void tearDownVaadin() {
        MockVaadin.tearDown();
    }

    @Test
    void testTableSelectable() {
        var table = GridComponentCreator.createGrid(new TestSelectableTablePmo(), new BindingContext());

        assertThat("Table is selectable", table.getSelectionModel(), is(instanceOf(GridSingleSelectionModel.class)));
    }

    @Test
    void testTableSectionSelectable() {
        var tableSection = (GridSection)VaadinUiCreator.createComponent(new TestSelectableTablePmo(),
                                                                        new BindingContext());

        assertThat("Table in table section is selectable", tableSection.getGrid().getSelectionModel(),
                   is(instanceOf(GridSingleSelectionModel.class)));
    }

    @Test
    void testGetSelection_Initial() {
        var tablePmo = spy(new TestSelectableTablePmo("row1", "row2"));
        tablePmo.setSelection(tablePmo.getItems().get(1));

        var table = GridComponentCreator.createGrid(tablePmo, new BindingContext());

        // getSelection is actually called twice, once from bindContainer, once in
        // ColumnBasedComponentFactory#createContainerComponent
        verify(tablePmo, atLeast(1)).getSelection();
        assertThat(table.getSelectedItems().stream().findFirst(), hasValue(tablePmo.getItems().get(1)));
    }

    @Test
    void testGetSelection_InitialNullSelection() {
        var tablePmo = spy(new TestSelectableTablePmo("row1", "row2"));

        var table = GridComponentCreator.createGrid(tablePmo, new BindingContext());

        assertThat(table.getSelectedItems(), is(empty()));
    }

    @Test
    void testSetSelection_ReselectSelectedItem() {
        var tablePmo = spy(new TestSelectableTablePmo("row1", "row2"));
        tablePmo.setSelection(tablePmo.getItems().get(1));
        var table = GridComponentCreator.createGrid(tablePmo, new BindingContext());
        assertThat(table.getSelectedItems().stream().findFirst(), hasValue(tablePmo.getItems().get(1)));

        GridKt._clickItem(table, 1);

        assertThat(tablePmo.getSelection(), is(tablePmo.getItems().get(1)));
        assertThat(table.getSelectedItems().stream().findFirst(), hasValue(tablePmo.getItems().get(1)));
    }

    @Test
    void testGetSelection_ChangeSelection() {
        var tablePmo = new TestSelectableTablePmo("row1", "row2");
        tablePmo.setSelection(tablePmo.getItems().get(1));
        var table = GridComponentCreator.createGrid(tablePmo, new BindingContext());
        assertThat(table.getSelectedItems().stream().findFirst(), hasValue(tablePmo.getItems().get(1)));

        GridKt._clickItem(table, 0);

        assertThat(table.getSelectedItems().stream().findFirst(), hasValue(tablePmo.getItems().get(0)));
    }

    @Test
    void testDoubleClick_OnNewItem_ChangeSelection() {
        var tablePmo = new TestSelectableTablePmo("row1", "row2");
        var table = GridComponentCreator.createGrid(tablePmo, new BindingContext());
        assertThat(table.getSelectedItems(), is(empty()));

        GridKt._doubleClickItem(table, 0);

        var row1 = tablePmo.getItems().get(0);
        assertThat(tablePmo.getSelection(), is(row1));
        assertThat(table.getSelectedItems().stream().findFirst(), hasValue(row1));
    }

    @Test
    void testDoubleClick_OnSelectedItem_NotChangeSelection() {
        var tablePmo = new TestSelectableTablePmo("row1", "row2");
        var row2 = tablePmo.getItems().get(1);
        tablePmo.setSelection(row2);
        var table = GridComponentCreator.createGrid(tablePmo, new BindingContext());
        assertThat(table.getSelectedItems().stream().findFirst(), hasValue(row2));

        GridKt._doubleClickItem(table, 1);

        assertThat(tablePmo.getSelection(), is(row2));
        assertThat(table.getSelectedItems().stream().findFirst(), hasValue(row2));
    }

    @Test
    void testDoubleClick_CallPmoMethod() {
        var tablePmo = spy(new TestSelectableTablePmo("row1", "row2"));
        tablePmo.setSelection(tablePmo.getItems().get(1));
        var table = GridComponentCreator.createGrid(tablePmo, new BindingContext());
        assertThat(table.getSelectedItems().stream().findFirst(), hasValue(tablePmo.getItems().get(1)));

        GridKt._doubleClickItem(table, 0);

        verify(tablePmo).onDoubleClick();
    }

    @Test
    void testVisualOnlyTable_VisualOnlySetTrue() {
        var tablePmo = new TestVisualOnlyselectableTablePmo("row1", "row2");
        var table = GridComponentCreator.createGrid(tablePmo, new BindingContext());

        assertThat("Table is selectable", table.getSelectionModel(), is(instanceOf(GridSingleSelectionModel.class)));
    }

    @Test
    void testMultiTableSectionSelectable() {
        var tableSection = (GridSection)VaadinUiCreator.createComponent(new TestMultiSelectableTablePmo(),
                                                                        new BindingContext());

        assertThat("Table in table section is selectable", tableSection.getGrid().getSelectionModel(),
                   is(instanceOf(GridMultiSelectionModel.class)));
    }

    @Test
    void testMultiSelectableTable_InitialNullSelection() {
        var tablePmo = spy(new TestMultiSelectableTablePmo("row1", "row2"));

        var table = GridComponentCreator.createGrid(tablePmo, new BindingContext());

        assertThat(table.getSelectedItems(), is(empty()));
    }

    @Test
    void testMultiGetSelection_Initial() {
        var tablePmo = new TestMultiSelectableTablePmo("row1", "row2");
        tablePmo.setSelection(new HashSet<>(tablePmo.getItems()));
        var table = GridComponentCreator.createGrid(tablePmo, new BindingContext());
        assertThat(table.getSelectedItems().size(), is(2));
    }

    @UISection
    public static class TestSelectableTablePmo extends SimpleTablePmo<String, TestSelectableTableRowPmo>
            implements SelectableTablePmo<TestSelectableTableRowPmo> {

        private TestSelectableTableRowPmo selectedRow;

        TestSelectableTablePmo(String... rows) {
            super(Arrays.asList(rows));
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

        @Override
        public String toString() {
            return "TestSelectableTableRowPmo{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }

    @UISection
    @BindTableSelection(visualOnly = true)
    public static class TestVisualOnlyselectableTablePmo extends SimpleTablePmo<String, TestSelectableTableRowPmo> {

        protected TestVisualOnlyselectableTablePmo(String... rows) {
            super(Arrays.asList(rows));
        }

        @Override
        protected TestSelectableTableRowPmo createRow(String modelObject) {
            return new TestSelectableTableRowPmo(modelObject);
        }
    }

    @UISection
    public static class TestMultiSelectableTablePmo extends SimpleTablePmo<String, TestSelectableTableRowPmo>
            implements MultiSelectableTablePmo<TestSelectableTableRowPmo> {

        private Set<TestSelectableTableRowPmo> selectedRows;

        protected TestMultiSelectableTablePmo(String... rows) {
            super(Arrays.asList(rows));
            this.selectedRows = new HashSet<>();
        }

        @Override
        protected TestSelectableTableRowPmo createRow(String modelObject) {
            return new TestSelectableTableRowPmo(modelObject);
        }

        @Override
        public Set<TestSelectableTableRowPmo> getSelection() {
            return selectedRows;
        }

        @Override
        public void setSelection(Set<TestSelectableTableRowPmo> selectedRows) {
            this.selectedRows = selectedRows;
        }
    }
}
