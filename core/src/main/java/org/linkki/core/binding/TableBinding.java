/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import org.linkki.core.container.LinkkiInMemoryContainer;
import org.linkki.core.message.MessageList;
import org.linkki.core.ui.table.ContainerPmo;
import org.linkki.core.ui.table.TableFooterPmo;

import com.vaadin.ui.Table;

/**
 * A binding for a Vaadin table to a container PMO and the items provided by it.
 *
 * @see ContainerPmo
 */
public class TableBinding<T> implements Binding {

    private final BindingContext bindingContext;

    private final Table table;

    private final LinkkiInMemoryContainer<T> tableContainer = new LinkkiInMemoryContainer<>();

    private final ContainerPmo<T> containerPmo;

    public TableBinding(BindingContext bindingContext,
            Table table,
            ContainerPmo<T> containerPmo) {
        this.bindingContext = requireNonNull(bindingContext, "bindingContext must not be null");
        this.table = requireNonNull(table, "table must not be null");
        this.containerPmo = requireNonNull(containerPmo, "containerPmo must not be null");
        table.setContainerDataSource(getTableContainer());
        getTableContainer().setItems(containerPmo.getItems());
        updateFooter();
    }

    public LinkkiInMemoryContainer<T> getTableContainer() {
        return tableContainer;
    }

    /**
     * If the list of items to display in the table has changed, the bindings for the old items are
     * removed from the binding context, and new bindings are created. As the binding context first
     * updates the table bindings and then the field bindings, the cells are updated from the
     * corresponding field bindings afterwards.
     */
    @Override
    public void updateFromPmo() {

        List<T> actualItems = containerPmo.getItems();

        if (hasItemListChanged(actualItems)) {
            removeBindingsForOldItems();
            getTableContainer().setItems(actualItems);
        }
        // Update the footer even if the same rows are displayed. Selecting a displayed row might
        // change the footer, e.g. when the footer sums up the selected rows
        updateFooter();
        table.setPageLength(getContainerPmo().getPageLength());
    }

    private void updateFooter() {
        Optional<TableFooterPmo> footerPmo = containerPmo.getFooterPmo();
        table.setFooterVisible(footerPmo.isPresent());
        if (footerPmo.isPresent()) {
            for (Object column : table.getVisibleColumns()) {
                String text = footerPmo.get().getFooterText((String)column);
                table.setColumnFooter(column, text);
            }
        }
    }

    private void removeBindingsForOldItems() {
        getTableContainer().getItemIds().stream()
                .filter(i -> i != null)
                .forEach(bindingContext::removeBindingsForPmo);
    }

    private boolean hasItemListChanged(List<T> actualItems) {
        return !getTableContainer().getItemIds().equals(actualItems);
    }

    @Override
    public Table getBoundComponent() {
        return table;
    }

    public ContainerPmo<T> getContainerPmo() {
        return containerPmo;
    }

    @Override
    public String toString() {
        return "TableBinding [bindingContext=" + bindingContext + ", table=" + table + ", containerPmo="
                + getContainerPmo() + "]";
    }

    /**
     * Creates a new {@link TableBinding} and add the new binding to the given {@link BindingContext}.
     *
     * @param bindingContext The binding context used to bind the given {@link ContainerPmo} to the
     *            given {@link Table}
     * @param table The table that should be updated by this binding
     * @param containerPmo The {@link ContainerPmo} that holds the item that should be displayed in the
     *            table
     * @return The newly created {@link TableBinding}
     */
    public static <T> TableBinding<T> create(BindingContext bindingContext,
            Table table,
            ContainerPmo<T> containerPmo) {
        TableBinding<T> tableBinding = new TableBinding<T>(bindingContext, table, containerPmo);
        bindingContext.add(tableBinding);
        return tableBinding;
    }

    @Override
    public Object getPmo() {
        return containerPmo;
    }

    /**
     * We do not support messages on tables at the moment.
     */
    @Override
    public MessageList displayMessages(@Nullable MessageList messages) {
        return new MessageList();
    }

}