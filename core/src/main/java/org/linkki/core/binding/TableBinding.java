package org.linkki.core.binding;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.faktorips.runtime.MessageList;
import org.linkki.core.TableFooterPmo;
import org.linkki.core.ui.section.PmoBasedSectionFactory;
import org.linkki.core.ui.table.ContainerPmo;
import org.vaadin.viritin.ListContainer;

import com.vaadin.ui.Table;

/**
 * A binding for a Vaadin table to a container PMO and the items provided by it.
 * 
 * @see ContainerPmo
 */
public class TableBinding<T> extends ListContainer<T> implements Binding {

    private static final long serialVersionUID = 1L;

    private final BindingContext bindingContext;

    private final Table table;
    private final Set<String> columnNames;
    private final ContainerPmo<T> containerPmo;

    private List<T> itemCopy;

    public TableBinding(BindingContext bindingContext, Table table, Set<String> columnNames,
            ContainerPmo<T> containerPmo) {
        super(containerPmo.getItemPmoClass());
        checkNotNull(bindingContext);
        checkNotNull(table);
        checkNotNull(containerPmo);
        this.bindingContext = bindingContext;
        this.table = table;
        this.columnNames = columnNames;
        this.containerPmo = containerPmo;
        saveItemCopy(containerPmo.getItems());
        table.setContainerDataSource(this);
    }

    private void saveItemCopy(List<T> items) {
        itemCopy = new ArrayList<>(items);
    }

    @Override
    protected List<T> getBackingList() {
        return itemCopy;
    }

    /**
     * Need to return an empty list for container parameter ids because every column should be
     * generated using the column generator.
     */
    @Override
    public Collection<String> getContainerPropertyIds() {
        return Collections.emptyList();
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
            saveItemCopy(actualItems);
            createCellsAndBindings();
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
        itemCopy.forEach(bindingContext::removeBindingsForPmo);
    }

    /**
     * Creates new bindings and updates the table control. This is done by firing an item set
     * changed event which triggers the recreation of all cells and bindings in the generateCell()
     * method in PmoBasedSectionFactory.
     * 
     * @see PmoBasedSectionFactory
     */
    private void createCellsAndBindings() {
        fireItemSetChange();
    }

    private boolean hasItemListChanged(List<T> actualItems) {
        return !itemCopy.equals(actualItems);
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
    public MessageList displayMessages(MessageList messages) {
        return new MessageList();
    }

}