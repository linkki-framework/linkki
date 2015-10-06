/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

import de.faktorzehn.ipm.utils.LazyInitializingMap;
import de.faktorzehn.ipm.web.ButtonPmo;
import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.FieldBinding;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;
import de.faktorzehn.ipm.web.ui.application.ApplicationStyles;
import de.faktorzehn.ipm.web.ui.section.annotations.ElementDescriptor;
import de.faktorzehn.ipm.web.ui.section.annotations.UITable;
import de.faktorzehn.ipm.web.ui.util.ComponentFactory;

/**
 * A table that is driven by a {@link ContainerPmo}. The table gets its content from the container
 * PMO, the columns are defined by the annotations of the according item PMO.
 */
public class PmoBasedTable<T extends PresentationModelObject> extends Table {

    private static final long serialVersionUID = 1L;

    private final ContainerPmo<T> containerPmo;

    /** Cache for property dispatchers used by the column generators. */
    private final LazyInitializingMap<T, PropertyDispatcher> dispatcherCache;

    /** PMO for the button to delete items in this table. */
    private class DeleteItemButtonPmo implements ButtonPmo {

        private final T item;
        private final BindingContext bindingContext;

        DeleteItemButtonPmo(T item, BindingContext bindingContext) {
            this.item = item;
            this.bindingContext = bindingContext;
        }

        @Override
        public void onClick() {
            getPmo().deleteItemAction()
                    .orElseThrow(illegalStateException(getPmo().getClass() + " does not allow to delete items."))//
                    .accept(item);
            updateFromPmo();
            dispatcherCache().remove(item);
            bindingContext.removeBindingsForPmo(item);
            bindingContext.updateUI();
        }

        @Override
        public Resource buttonIcon() {
            return FontAwesome.TRASH_O;
        }

        @Override
        public Collection<String> styleNames() {
            return Lists.newArrayList(ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_LARGE);
        }

    }

    /** PMO for the button to add items in this table. */
    private class AddItemButtonPmo implements ButtonPmo {

        private final BindingContext bindingContext;

        AddItemButtonPmo(BindingContext bindingContext) {
            this.bindingContext = bindingContext;
        }

        @Override
        public void onClick() {
            getPmo().addItemAction()//
                    .orElseThrow(illegalStateException(getPmo().getClass() + "does not allow to add items."))//
                    .get();
            updateFromPmo();
            bindingContext.updateUI();
        }

        @Override
        public Resource buttonIcon() {
            UITable tableAnnotation = getPmo().getClass().getAnnotation(UITable.class);
            if (tableAnnotation != null) {
                return tableAnnotation.addItemIcon();
            } else {
                return UITable.DEFAULT_ADD_ITEM_ICON;
            }
        }

    }

    /** Column generator that generates a column with a button to delete items in this table. */
    private class DeleteItemColumnGenerator implements ColumnGenerator {

        private static final long serialVersionUID = 1L;

        private final BindingContext bindingContext;

        DeleteItemColumnGenerator(BindingContext bindingContext) {
            super();
            this.bindingContext = bindingContext;
        }

        @Override
        public Object generateCell(Table source, Object itemId, Object columnId) {
            T itemPmo = getPmo().getItemPmoClass().cast(itemId);
            return ComponentFactory.newButton(new DeleteItemButtonPmo(itemPmo, bindingContext));
        }

    }

    /** Column generator that generates a column for a field of a PMO. */
    private class FieldColumnGenerator implements ColumnGenerator {

        private static final long serialVersionUID = 1L;

        private final ElementDescriptor elementDescriptor;
        private final boolean receiveFocusOnNew;
        private final BindingContext bindingContext;

        public FieldColumnGenerator(ElementDescriptor elementDescriptor, boolean receiveFocusOnNew,
                BindingContext bindingContext) {
            this.elementDescriptor = elementDescriptor;
            this.receiveFocusOnNew = receiveFocusOnNew;
            this.bindingContext = bindingContext;
        }

        @Override
        public Object generateCell(Table source, Object itemId, Object columnId) {
            Component component = elementDescriptor.newComponent();
            component.addStyleName(ApplicationStyles.BORDERLESS);
            component.addStyleName(ApplicationStyles.TABLE_CELL);
            component.setEnabled(getPmo().isEditable());
            Field<?> field = (Field<?>)component;
            T itemPmo = getPmo().getItemPmoClass().cast(itemId);

            FieldBinding<?> binding = FieldBinding.create(bindingContext, (String)columnId, null, field,
                                                          dispatcherCache().get(itemPmo));
            bindingContext.add(binding);
            binding.updateFromPmo();
            if (receiveFocusOnNew) {
                field.focus();
            }
            return component;
        }
    }

    /**
     * Constructor for a PmoBasedTable.
     * 
     * @param tablePmo the container PMO
     * @param dispatcherBuilder the function that creates a new {@link PropertyDispatcher} for an
     *            item from the container PMO
     */
    public PmoBasedTable(ContainerPmo<T> tablePmo, Function<T, PropertyDispatcher> dispatcherBuilder) {
        super();
        this.containerPmo = tablePmo;
        this.dispatcherCache = new LazyInitializingMap<>(dispatcherBuilder);
    }

    public ContainerPmo<T> getPmo() {
        return containerPmo;
    }

    /**
     * Public for updating the table from "outside" e.g. via other buttons than the add item action.
     */
    public void updateFromPmo() {
        removeAllItems();
        createItems();
        refreshRowCache();
    }

    private void createItems() {
        List<T> rows = containerPmo.getItems();
        // don't add values directly, let data binding take care of it
        rows.forEach(row -> addItem(new Object[0], row));
    }

    @Override
    public String toString() {
        return "Table based on PMO=" + getPmo();
    }

    LazyInitializingMap<T, PropertyDispatcher> dispatcherCache() {
        return dispatcherCache;
    }

    /**
     * Creates a new column generator that generates a column with a button to delete items in this
     * table.
     * 
     * @param ctx the context in which the items in this table are bound
     * 
     * @return a new column generator that generates a column with a button to delete items in this
     *         table
     */
    ColumnGenerator deleteItemColumnGenerator(BindingContext ctx) {
        return new DeleteItemColumnGenerator(ctx);
    }

    /**
     * Creates a new column generator for a field of a PMO.
     * 
     * @param elementDesc the descriptor for the PMO's field
     * @param receiveFocusOnNew whether or not the generated field should receive the focus when a
     *            new row is generated
     * @param bindingContext the context in which the field is bound
     * 
     * @return a new column generator for a field of a PMO
     */
    ColumnGenerator fieldColumnGenerator(ElementDescriptor elementDesc,
            boolean receiveFocusOnNew,
            BindingContext bindingContext) {
        return new FieldColumnGenerator(elementDesc, receiveFocusOnNew, bindingContext);
    }

    /**
     * Creates a new {@link ButtonPmo} for the button to add items to the table if it is possible to
     * add items.
     * 
     * @param ctx the context in which the item are bound
     * @return a new {@link ButtonPmo} for the button to add items to the table if it is possible to
     *         add items
     */
    Optional<ButtonPmo> addItemButtonPmo(BindingContext ctx) {
        return containerPmo.addItemAction().map(a -> new AddItemButtonPmo(ctx));
    }

    /** Helper function that supplies an {@link IllegalStateException}. */
    static Supplier<IllegalStateException> illegalStateException(String message) {
        return () -> new IllegalStateException(message);
    }
}
