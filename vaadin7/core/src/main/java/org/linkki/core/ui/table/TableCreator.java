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

import static java.util.Objects.requireNonNull;

import java.util.List;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.ElementDescriptor;
import org.linkki.core.binding.descriptor.PropertyElementDescriptors;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.columnbased.ColumnBasedComponentCreator;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.defaults.style.LinkkiTheme;
import org.linkki.core.ui.components.LabelComponentWrapper;
import org.linkki.core.ui.table.column.TableColumnWrapper;

import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TreeTable;

/**
 * A {@link ColumnBasedComponentCreator} that creates a Vaadin {@link Table}.
 */
class TableCreator implements ColumnBasedComponentCreator {

    /**
     * Creates a new table based on the container PMO.
     */
    @Override
    public ComponentWrapper createComponent(ContainerPmo<?> containerPmo) {
        Table table = containerPmo.isHierarchical() ? new TreeTable() : new Table();
        table.addStyleName(LinkkiTheme.TABLE);
        table.setHeightUndefined();
        table.setWidth("100%");
        table.setSortEnabled(false);
        return new TableComponentWrapper<>(containerPmo.getClass().getSimpleName(), table);
    }

    /**
     * Creates a new column for a field of a PMO and applies all
     * {@link LinkkiAspectDefinition#supports(org.linkki.core.binding.wrapper.WrapperType) supported}
     * {@link LinkkiAspectDefinition LinkkiAspectDefinitions}.
     * 
     * @param elementDesc the descriptor for the PMO's field
     * @param tableWrapper the {@link ComponentWrapper} that wraps the table
     */
    @Override
    public void initColumn(ContainerPmo<?> containerPmo,
            ComponentWrapper tableWrapper,
            BindingContext bindingContext,
            PropertyElementDescriptors elementDesc) {
        TableCreator.FieldColumnGenerator<?> columnGen = new TableCreator.FieldColumnGenerator<>(
                elementDesc, bindingContext);
        String propertyName = elementDesc.getPmoPropertyName();
        Table table = (Table)tableWrapper.getComponent();
        table.addGeneratedColumn(propertyName, columnGen);
        List<LinkkiAspectDefinition> aspectDefs = elementDesc.getAllAspects();
        bindingContext.bind(containerPmo.getItemPmoClass(), BoundProperty.of(propertyName), aspectDefs,
                            new TableColumnWrapper(table, propertyName));
    }

    /** Column generator that generates a column for a field of a PMO. */
    private static class FieldColumnGenerator<T> implements ColumnGenerator {

        private static final long serialVersionUID = 1L;

        private final PropertyElementDescriptors elementDescriptors;
        private final BindingContext bindingContext;

        public FieldColumnGenerator(PropertyElementDescriptors elementDescriptors,
                BindingContext bindingContext) {
            this.elementDescriptors = requireNonNull(elementDescriptors, "elementDescriptors must not be null");
            this.bindingContext = requireNonNull(bindingContext, "bindingContext must not be null");
        }

        @Override
        public Object generateCell(Table source,
                Object itemId,
                Object columnId) {
            requireNonNull(itemId, "itemId must not be null");
            ElementDescriptor elementDescriptor = elementDescriptors.getDescriptor(itemId);
            Component component = (Component)elementDescriptor.newComponent(itemId);
            component.addStyleName(LinkkiTheme.BORDERLESS);
            component.addStyleName(LinkkiTheme.TABLE_CELL);

            @SuppressWarnings("unchecked")
            T itemPmo = (T)itemId;
            component.addAttachListener($ -> bindingContext.bind(itemPmo, elementDescriptor,
                                                                 new LabelComponentWrapper(component)));
            component.addDetachListener($ -> bindingContext.removeBindingsForComponent(component));

            return component;
        }
    }

}