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
package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.linkki.core.binding.aspect.AspectAnnotationReader;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.SimpleBindingDescriptor;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.ui.table.ContainerPmo;
import org.linkki.core.ui.table.TableComponentWrapper;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Table;

/**
 * A binding for a Vaadin table to a container PMO and the items provided by it.
 *
 * @see ContainerPmo
 */
public class TableBinding<@NonNull T> extends BindingContext implements Binding {

    private final Handler modelChanged;

    private final Table table;

    private final ContainerPmo<T> containerPmo;

    private TableComponentWrapper<T> tableComponentWrapper;


    TableBinding(Handler modelChanged, PropertyBehaviorProvider behaviorProvider,
            Table table,
            ContainerPmo<T> containerPmo) {
        super(containerPmo.getClass().getName(), behaviorProvider, Handler.NOP_HANDLER);
        this.modelChanged = requireNonNull(modelChanged, "modelChanged must not be null");
        this.table = requireNonNull(table, "table must not be null");
        this.containerPmo = requireNonNull(containerPmo, "containerPmo must not be null");
        List<LinkkiAspectDefinition> aspectDefinitions = AspectAnnotationReader
                .createAspectDefinitionsFor(containerPmo.getClass());
        tableComponentWrapper = new TableComponentWrapper<>(containerPmo.getClass().getName(), table);
        bind(containerPmo, new SimpleBindingDescriptor("", aspectDefinitions), tableComponentWrapper);

        // TODO LIN-1190 remove this
        updateFromPmo();
    }

    @Override
    public void modelChanged() {
        modelChanged.apply();
    }

    /**
     * If the list of items to display in the table has changed, the bindings for the old items are
     * removed from the binding context, and new bindings are created. As the binding context first
     * updates the table bindings and then the field bindings, the cells are updated from the
     * corresponding field bindings afterwards.
     */
    @Override
    // TODO LIN-1190 remove this
    public void updateFromPmo() {
        List<T> actualItems = containerPmo.getItems();

        tableComponentWrapper.setItems(actualItems);

        // Update the footer even if the same rows are displayed. Selecting a displayed row might
        // change the footer, e.g. when the footer sums up the selected rows
        tableComponentWrapper.updateFooter(containerPmo.getFooterPmo());
        tableComponentWrapper.setPageLength(containerPmo.getPageLength());
        super.updateFromPmo();
    }


    @Override
    public Table getBoundComponent() {
        return table;
    }

    @Override
    public ContainerPmo<T> getPmo() {
        return containerPmo;
    }

    @Override
    public String toString() {
        return "TableBinding [table=" + table + ", containerPmo="
                + getPmo() + "]";
    }

    /**
     * Creates a new {@link TableBinding} and add the new binding to the given {@link BindingContext}.
     *
     * @param parentBindingContext The binding context used to bind the given {@link ContainerPmo} to
     *            the given {@link Table}
     * @param table The table that should be updated by this binding
     * @param containerPmo The {@link ContainerPmo} that holds the item that should be displayed in the
     *            table
     * @return The newly created {@link TableBinding}
     */
    public static <@NonNull T> TableBinding<T> create(BindingContext parentBindingContext,
            Table table,
            ContainerPmo<T> containerPmo) {
        TableBinding<T> tableBinding = new TableBinding<T>(parentBindingContext::modelChanged,
                parentBindingContext.getBehaviorProvider(), table, containerPmo);
        parentBindingContext.add(tableBinding);
        return tableBinding;
    }

}