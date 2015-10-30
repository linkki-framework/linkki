package de.faktorzehn.ipm.web.binding;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.viritin.ListContainer;

import com.vaadin.ui.Table;

import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.ui.table.ContainerPmo;

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
        this.itemCopy = new ArrayList<>(getContainerPmo().getItems());
    }

    @Override
    public void updateFromPmo() {
        if (itemCopy.size() != getContainerPmo().getItems().size()) {
            itemCopy.removeAll(getContainerPmo().getItems());
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

    public static <T extends PresentationModelObject> TableBinding<T> create(BindingContext bindingContext,
            Table table,
            ContainerPmo<T> containerPmo) {
        return new TableBinding<T>(bindingContext, table, containerPmo);
    }

}