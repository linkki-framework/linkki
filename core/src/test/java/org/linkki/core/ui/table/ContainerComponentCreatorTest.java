/*
 * Copyright Faktor Zehn GmbH.
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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.stream.Stream;

import org.junit.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.TableBinding;
import org.linkki.core.binding.descriptor.PropertyElementDescriptors;
import org.linkki.core.binding.descriptor.UIAnnotationReader;
import org.linkki.core.ui.section.annotations.UITableColumn;

import com.vaadin.ui.Table;

public class ContainerComponentCreatorTest {

    @Test
    public void testCreateTableComponent_PmoClassIsUsedAsId() {
        ContainerComponentCreator<TestRowPmo> creator = new ContainerComponentCreator<>(new TestTablePmo());
        Table table = creator.createTableComponent();
        assertThat(table.getId(), is("TestTablePmo"));
    }

    @Test
    public void testCreateColumn_FieldLabelsAreUsedAsColumnHeaders() {
        Table table = createTableWithColumns();

        // 1, 2 and 3 are the labels for the fields, the delete button has an no label
        assertThat(table.getColumnHeaders(), is(arrayContaining("1", "2", "3", "")));
    }

    @Test
    public void testCreateColumn_WidthAndExpandRatioIsReadFromAnnotation() {
        Table table = createTableWithColumns();

        assertThat(table.getColumnWidth("value1"), is(100));
        assertThat(table.getColumnExpandRatio("value1"), is(UITableColumn.UNDEFINED_EXPAND_RATIO));

        assertThat(table.getColumnWidth("value2"), is(UITableColumn.UNDEFINED_WIDTH));
        assertThat(table.getColumnExpandRatio("value2"), is(2.0f));

        assertThat(table.getColumnWidth("value3"), is(UITableColumn.UNDEFINED_WIDTH));
        assertThat(table.getColumnExpandRatio("value3"), is(UITableColumn.UNDEFINED_EXPAND_RATIO));
    }

    @Test
    public void testCreateColumn_CollapsibleAndCollapsedIsReadFromAnnotation() {
        Table table = createTableWithColumns();

        assertThat(table.isColumnCollapsible("value1"), is(true));
        assertThat(table.isColumnCollapsed("value1"), is(false));

        assertThat(table.isColumnCollapsible("value2"), is(true));
        assertThat(table.isColumnCollapsed("value2"), is(true));

        assertThat(table.isColumnCollapsible("value3"), is(false));
        assertThat(table.isColumnCollapsed("value3"), is(false));
    }

    private Table createTableWithColumns() {
        TestTablePmo containerPmo = new TestTablePmo();
        Stream<PropertyElementDescriptors> uiElements = new UIAnnotationReader(TestRowPmo.class).getUiElements();
        ContainerComponentCreator<TestRowPmo> creator = new ContainerComponentCreator<>(containerPmo);
        Table table = creator.createTableComponent();
        TableBinding<TestRowPmo> tableBinding = TableBinding.create(new BindingContext(),
                                                                    table,
                                                                    containerPmo);

        uiElements.forEach(elementDesc -> creator.createColumn(tableBinding, elementDesc));
        return table;
    }

}
