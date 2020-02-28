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

import java.util.List;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.PropertyElementDescriptors;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.columnbased.ColumnBasedComponentCreator;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.defaults.style.LinkkiTheme;
import org.linkki.core.ui.table.column.TableColumnWrapper;

/**
 * A {@link ColumnBasedComponentCreator} that creates a Vaadin {@link com.vaadin.v7.ui.Table Table}.
 */
@SuppressWarnings("javadoc")
class TableCreator implements ColumnBasedComponentCreator {

    /**
     * Creates a new table based on the container PMO.
     */
    @SuppressWarnings("deprecation")
    @Override
    public ComponentWrapper createComponent(ContainerPmo<?> containerPmo) {
        com.vaadin.v7.ui.Table table = containerPmo.isHierarchical() ? new com.vaadin.v7.ui.TreeTable()
                : new com.vaadin.v7.ui.Table();
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
    @SuppressWarnings("deprecation")
    @Override
    public void initColumn(ContainerPmo<?> containerPmo,
            ComponentWrapper tableWrapper,
            BindingContext bindingContext,
            PropertyElementDescriptors elementDesc) {
        com.vaadin.v7.ui.Table.ColumnGenerator columnGen = TableColumnWrapper
                .createComponent(elementDesc, bindingContext);
        String propertyName = elementDesc.getPmoPropertyName();
        com.vaadin.v7.ui.Table table = (com.vaadin.v7.ui.Table)tableWrapper.getComponent();
        table.addGeneratedColumn(propertyName, columnGen);
        List<LinkkiAspectDefinition> aspectDefs = elementDesc.getAllAspects();
        bindingContext.bind(containerPmo.getItemPmoClass(), BoundProperty.of(propertyName), aspectDefs,
                            new TableColumnWrapper(table, propertyName));
    }

}