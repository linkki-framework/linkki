package org.linkki.core.binding;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.linkki.core.PresentationModelObject;
import org.linkki.core.binding.annotations.BindContext;
import org.linkki.core.ui.table.ContainerPmo;
import org.vaadin.viritin.ListContainer;

import com.vaadin.ui.Table;

/**
 * A binding for a single Vaadin field to properties of a presentation model object. The binding
 * binds the value shown in the field to a property providing the value. It also binds other field
 * properties like enabled required.
 */
public class TableBinding<T extends PresentationModelObject> extends ListContainer<T> implements Binding {

    private static final long serialVersionUID = 1L;

    private final BindingContext bindingContext;

    private final Table table;

    private final ContainerPmo<T> containerPmo;

    private List<T> itemCopy;

    public TableBinding(BindingContext bindingContext, Table table, ContainerPmo<T> containerPmo) {
        super(containerPmo.getItemPmoClass());
        checkNotNull(bindingContext);
        checkNotNull(table);
        checkNotNull(containerPmo);
        this.bindingContext = bindingContext;
        this.table = table;
        this.containerPmo = containerPmo;
        updateItemCopy();
        table.setContainerDataSource(this);
    }

    private void updateItemCopy() {
        itemCopy = new ArrayList<>(getBackingList());
    }

    @Override
    protected List<T> getBackingList() {
        return containerPmo.getItems();
    }

    /**
     * Need to return an empty list for container parameter ids because every column should be
     * generated using the column generator.
     */
    @Override
    public Collection<String> getContainerPropertyIds() {
        return Collections.emptyList();
    }

    @Override
    public void updateFromPmo() {
        if (!itemCopy.equals(getBackingList())) {
            itemCopy.removeAll(getBackingList());
            itemCopy.forEach(bindingContext::removeBindingsForPmo);
            fireItemSetChange();
            updateItemCopy();
        }
        table.setPageLength(getContainerPmo().getPageLength());
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
     * Creates a new {@link TableBinding} and add the new binding to the given {@link BindContext}
     * 
     * @param bindingContext The binding context used to bind the given {@link ContainerPmo} to the
     *            given {@link Table}
     * @param table The table that should be updated by this binding
     * @param containerPmo The {@link ContainerPmo} that holds the item that should be displayed in
     *            the table
     * @return The newly created {@link TableBinding}
     */
    public static <T extends PresentationModelObject> TableBinding<T> create(BindingContext bindingContext,
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

}