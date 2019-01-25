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

import org.eclipse.jdt.annotation.NonNull;
import org.linkki.core.binding.Binding;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.SimpleBindingDescriptor;
import org.linkki.core.binding.descriptor.UIAnnotationReader;
import org.linkki.core.ui.table.aspect.TableAspectDefinitions;

import com.vaadin.ui.Table;

/**
 * A factory to create a table based on a {@link ContainerPmo}.
 * 
 * @param <ROW> a class annotated with linkki annotations used as PMO for a row in the table
 */
public class PmoBasedTableFactory<@NonNull ROW> {

    private final ContainerPmo<ROW> containerPmo;

    private final UIAnnotationReader annotationReader;

    private final BindingContext bindingContext;

    private final Class<? extends ROW> rowPmoClass;

    private final ContainerComponentCreator<ROW> containerComponentCreator;

    /**
     * Creates a new factory.
     * 
     * @param containerPmo The container providing the contents and column definitions.
     * @param bindingContext The binding context to which the cell bindings are added.
     */
    public PmoBasedTableFactory(ContainerPmo<ROW> containerPmo, BindingContext bindingContext) {
        this.containerPmo = requireNonNull(containerPmo, "containerPmo must not be null");
        this.bindingContext = requireNonNull(bindingContext, "bindingContext must not be null");
        this.rowPmoClass = containerPmo.getItemPmoClass();
        this.annotationReader = new UIAnnotationReader(rowPmoClass);
        this.containerComponentCreator = new ContainerComponentCreator<>(this.containerPmo);
    }

    /**
     * Create a new table based on the container PMO.
     */
    public Table createTable() {
        Table table = containerComponentCreator.createTableComponent();
        TableComponentWrapper<ROW> tableComponentWrapper = new TableComponentWrapper<>(
                containerPmo.getClass().getName(), table);
        SimpleBindingDescriptor bindingDescriptor = new SimpleBindingDescriptor("", TableAspectDefinitions.createAll());
        Binding<Table> binding = bindingContext.bind(containerPmo, bindingDescriptor, tableComponentWrapper);
        createColumns(binding);
        table.setPageLength(containerPmo.getPageLength());
        return table;
    }

    private void createColumns(Binding<Table> binding) {
        annotationReader.getUiElements().forEach(e -> containerComponentCreator.createColumn(binding, e));
    }

}
