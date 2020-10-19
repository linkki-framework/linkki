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
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ContainerBinding;
import org.linkki.core.binding.descriptor.PropertyElementDescriptors;
import org.linkki.core.binding.descriptor.UIElementAnnotationReader;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.table.column.annotation.UITableColumn;

@SuppressWarnings("deprecation")
public class TableCreatorTest {

    @Test
    public void testCreateComponent_PmoClassIsUsedAsId() {
        TableCreator creator = new TableCreator();
        com.vaadin.v7.ui.Table table = (com.vaadin.v7.ui.Table)creator.createComponent(new TestTablePmo())
                .getComponent();
        assertThat(table.getId(), is("TestTablePmo_table"));
    }

    @Test
    public void testInitColumn_FieldLabelsAreUsedAsColumnHeaders() {
        com.vaadin.v7.ui.Table table = createTableWithColumns();

        // 1, 2 and 3 are the labels for the fields, the delete button has an no label
        assertThat(table.getColumnHeaders(), is(arrayContaining("1", "2", "3", "")));
    }

    @Test
    public void testInitColumn_WidthAndExpandRatioIsReadFromAnnotation() {
        com.vaadin.v7.ui.Table table = createTableWithColumns();

        assertThat(table.getColumnWidth("value1"), is(100));
        assertThat(table.getColumnExpandRatio("value1"), is(UITableColumn.UNDEFINED_EXPAND_RATIO));

        assertThat(table.getColumnWidth("value2"), is(UITableColumn.UNDEFINED_WIDTH));
        assertThat(table.getColumnExpandRatio("value2"), is(2.0f));

        assertThat(table.getColumnWidth("value3"), is(UITableColumn.UNDEFINED_WIDTH));
        assertThat(table.getColumnExpandRatio("value3"), is(UITableColumn.UNDEFINED_EXPAND_RATIO));
    }

    @Test
    public void testInitColumn_CollapsibleAndCollapsedIsReadFromAnnotation() {
        com.vaadin.v7.ui.Table table = createTableWithColumns();

        assertThat(table.isColumnCollapsible("value1"), is(true));
        assertThat(table.isColumnCollapsed("value1"), is(false));

        assertThat(table.isColumnCollapsible("value2"), is(true));
        assertThat(table.isColumnCollapsed("value2"), is(true));

        assertThat(table.isColumnCollapsible("value3"), is(false));
        assertThat(table.isColumnCollapsed("value3"), is(false));
    }

    private com.vaadin.v7.ui.Table createTableWithColumns() {
        TestTablePmo containerPmo = new TestTablePmo();
        Stream<PropertyElementDescriptors> uiElements = new UIElementAnnotationReader(TestRowPmo.class).getUiElements();
        TableCreator creator = new TableCreator();
        ComponentWrapper componentWrapper = creator.createComponent(containerPmo);
        BindingContext bindingContext = new BindingContext();
        ContainerBinding binding = bindingContext.bindContainer(containerPmo,
                                                                BoundProperty.of(""), Arrays.asList(),
                                                                new TableComponentWrapper<>("",
                                                                        (com.vaadin.v7.ui.Table)componentWrapper
                                                                                .getComponent()));

        uiElements.forEach(elementDesc -> creator.initColumn(containerPmo, componentWrapper, binding, elementDesc));
        return (com.vaadin.v7.ui.Table)componentWrapper.getComponent();
    }

}
