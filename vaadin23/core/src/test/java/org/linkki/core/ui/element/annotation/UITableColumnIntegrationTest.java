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

package org.linkki.core.ui.element.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.creation.table.GridComponentCreator;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.table.column.annotation.UITableColumn;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridSortOrder;

class UITableColumnIntegrationTest {

    @Test
    void testSortable_ColumnIsSortable() {
        Grid<SampleRowPmo> grid = GridComponentCreator.createGrid(new SampleTablePmo(), new BindingContext());
        Column<SampleRowPmo> column = grid.getColumns().get(0);

        assertThat(column.isSortable(), is(true));
    }

    @Test
    void testSortable_ColumnSortOrder() {
        Grid<SampleRowPmo> grid = GridComponentCreator.createGrid(new SampleTablePmo(), new BindingContext());
        Column<SampleRowPmo> column = grid.getColumns().get(0);

        grid.sort(GridSortOrder.asc(column).build());

        List<SampleRowPmo> items = grid.getListDataView().getItems().collect(Collectors.toList());
        assertThat(items.get(0).getString(), is("abc"));
        assertThat(items.get(1).getString(), is("xyz"));
    }

    @Test
    void testSortable_ModelBindingThrowsException() {
        LinkkiBindingException exception = assertThrows(LinkkiBindingException.class, () -> {
            GridComponentCreator.createGrid(new ModelBindingTablePmo(), new BindingContext());
        });
        assertThat(exception.getMessage(), containsString("Could not read ModelBindingRowPmo#getString"));
    }

    @Test
    void testSortable_NonComparableThrowsException() {
        LinkkiBindingException exception = assertThrows(LinkkiBindingException.class, () -> {
            GridComponentCreator.createGrid(new NonComparableTablePmo(), new BindingContext());
        });
        assertThat(exception.getMessage(),
                   containsString("Cannot sort by NonComparableRowPmo#object as java.lang.Object does not implement Comparable"));
    }

    @UISection
    private static class SampleTablePmo implements ContainerPmo<SampleRowPmo> {

        @Override
        public List<SampleRowPmo> getItems() {
            return Arrays.asList(new SampleRowPmo("xyz"), new SampleRowPmo("abc"));
        }

    }

    private static class SampleRowPmo {

        private final String string;

        public SampleRowPmo(String string) {
            this.string = string;
        }

        @UITableColumn(sortable = true)
        @UILabel(position = 0)
        public String getString() {
            return string;
        }

    }

    private static class TestModelObject {

        private final String string;

        public TestModelObject(String string) {
            this.string = string;
        }

        @SuppressWarnings("unused")
        public String getString() {
            return this.string;
        }

    }

    @UISection
    private static class ModelBindingTablePmo implements ContainerPmo<ModelBindingRowPmo> {

        @Override
        public List<ModelBindingRowPmo> getItems() {
            return Arrays.asList(new ModelBindingRowPmo(new TestModelObject("xyz")));
        }

    }

    private static class ModelBindingRowPmo {

        private final TestModelObject modelObject;

        public ModelBindingRowPmo(TestModelObject modelObject) {
            this.modelObject = modelObject;
        }

        @ModelObject
        public TestModelObject getModelObject() {
            return modelObject;
        }

        @UITableColumn(sortable = true)
        @UILabel(position = 0, modelAttribute = "string")
        public void getString() {
            // model binding
        }

    }

    @UISection
    private static class NonComparableTablePmo implements ContainerPmo<NonComparableRowPmo> {

        @Override
        public List<NonComparableRowPmo> getItems() {
            return Arrays.asList(new NonComparableRowPmo(new TestModelObject("xyz")));
        }

    }

    private static class NonComparableRowPmo {

        private final Object object;

        public NonComparableRowPmo(TestModelObject modelObject) {
            this.object = modelObject;
        }

        @UITableColumn(sortable = true)
        @UILabel(position = 0)
        public Object getObject() {
            return object;
        }

    }

}
