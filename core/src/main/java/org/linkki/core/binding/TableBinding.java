package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;

import org.faktorips.runtime.MessageList;
import org.linkki.core.TableFooterPmo;
import org.linkki.core.container.LinkkiInMemoryContainer;
import org.linkki.core.ui.table.ContainerPmo;

import com.vaadin.ui.Table;

/**
 * A binding for a Vaadin table to a container PMO and the items provided by it.
 *
 * @see ContainerPmo
 */
public class TableBinding<T> extends LinkkiInMemoryContainer<T> implements Binding {

    private static final long serialVersionUID = 1L;

    private final BindingContext bindingContext;

    private final Table table;
    private final Set<String> columnNames;
    private final ContainerPmo<T> containerPmo;

    public TableBinding(BindingContext bindingContext, Table table, Set<String> columnNames,
            ContainerPmo<T> containerPmo) {
        requireNonNull(bindingContext, "bindingContext must not be null");
        this.bindingContext = bindingContext;
        requireNonNull(table, "table must not be null");
        this.table = table;
        this.columnNames = columnNames;
        requireNonNull(containerPmo, "containerPmo must not be null");
        this.containerPmo = containerPmo;
        table.setContainerDataSource(this);
        addAllItems(containerPmo.getItems());
    }

    /**
     * If the list of items to display in the table has changed, the bindings for the old items are
     * removed from the binding context, and new bindings are created. As the binding context first
     * updates the table bindings and then the field bindings, the cells are updated from the
     * corresponding field bindings afterwards.
     */
    @Override
    public void updateFromPmo() {

        List<T> pmoItems = containerPmo.getItems();
        List<LinkkiItemWrapper<T>> actualItems = asLinkkiItemWrapper(pmoItems, new ArrayList<>(pmoItems.size()));

        if (hasItemListChanged(actualItems)) {
            removeBindingsForOldItems();
            addAllItems(actualItems);
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
            for (String column : columnNames) {
                String text = footerPmo.get().getFooterText(column);
                table.setColumnFooter(column, text);
            }
        }
    }

    private void removeBindingsForOldItems() {
        getBackupList().stream()
                .map(LinkkiItemWrapper<T>::getItem)
                .filter(i -> i != null)
                .forEach(bindingContext::removeBindingsForPmo);
        removeAllItems();
    }

    private boolean hasItemListChanged(List<LinkkiItemWrapper<T>> actualItems) {
        return !getBackupList().equals(actualItems);
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
     * Creates a new {@link TableBinding} and add the new binding to the given
     * {@link BindingContext}.
     *
     * @param bindingContext The binding context used to bind the given {@link ContainerPmo} to the
     *            given {@link Table}
     * @param table The table that should be updated by this binding
     * @param columnNames The table's column names (propertyIds)
     * @param containerPmo The {@link ContainerPmo} that holds the item that should be displayed in
     *            the table
     * @return The newly created {@link TableBinding}
     */
    public static <T> TableBinding<T> create(BindingContext bindingContext,
            Table table,
            Set<String> columnNames,
            ContainerPmo<T> containerPmo) {
        TableBinding<T> tableBinding = new TableBinding<T>(bindingContext, table, columnNames, containerPmo);
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