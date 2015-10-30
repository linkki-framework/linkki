/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
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

    /** Cache for property dispatchers used by the column generators. */
    private final LazyInitializingMap<T, PropertyDispatcher> dispatcherCache;

    private final PmoListContainer<T> container;

    /**
     * Constructor for a PmoBasedTable.
     * 
     * @param tablePmo the container PMO
     * @param dispatcherBuilder the function that creates a new {@link PropertyDispatcher} for an
     *            item from the container PMO
     */
    public PmoBasedTable(ContainerPmo<T> tablePmo, Function<T, PropertyDispatcher> dispatcherBuilder) {
        super();
        container = new PmoListContainer<T>(tablePmo);
        setContainerDataSource(container);
        this.dispatcherCache = new LazyInitializingMap<>(dispatcherBuilder);
    }

    /**
     * Public for updating the table from "outside" e.g. via other buttons than the add item action.
     */
    public void updateFromPmo() {
        refreshRowCache();
    }

    @Override
    public String toString() {
        return "Table based on PMO=" + container.getContainerPmo();
    }

    /**
     * Creates a new column for a field of a PMO.
     * 
     * @param elementDesc the descriptor for the PMO's field
     * @param receiveFocusOnNew whether or not the generated field should receive the focus when a
     *            new row is generated
     * @param bindingContext the context in which the field is bound
     */
    void createColumn(ElementDescriptor elementDesc, boolean receiveFocusOnNew, BindingContext bindingContext) {
        FieldColumnGenerator<T> columnGen = new FieldColumnGenerator<T>(elementDesc, receiveFocusOnNew, bindingContext,
                dispatcherCache);
        String propertyName = elementDesc.getPropertyName();
        addGeneratedColumn(propertyName, columnGen);
        setColumnHeader(propertyName, elementDesc.getLabelText());
        container.addColumn(propertyName);
    }

    /**
     * Creates a new {@link ButtonPmo} for the button to add items to the table if it is possible to
     * add items.
     * 
     * @return a new {@link ButtonPmo} for the button to add items to the table if it is possible to
     *         add items
     */
    Optional<ButtonPmo> getNewItemButtonPmo() {
        return container.getNewItemAction().map(a -> new AddItemButtonPmo(a, getAddButtonIcon()));
    }

    private Resource getAddButtonIcon() {
        UITable tableAnnotation = container.getContainerPmo().getClass().getAnnotation(UITable.class);
        if (tableAnnotation != null) {
            return tableAnnotation.addItemIcon();
        } else {
            return UITable.DEFAULT_ADD_ITEM_ICON;
        }
    }

    /**
     * Creates a new column generator that generates a column with a button to delete items in this
     * table.
     * 
     * @param ctx the context in which the items in this table are bound
     * 
     */
    public void createDeleteItemColumn(String deleteColId, String header, Consumer<T> deleteConsumer, BindingContext ctx) {
        DeleteItemColumnGenerator<T> colGen = new DeleteItemColumnGenerator<T>(deleteConsumer
                .andThen(item -> container.updateItems()).andThen(item -> dispatcherCache.remove(item))
                .andThen(item -> ctx.removeBindingsForPmo(item)));
        addGeneratedColumn(deleteColId, colGen);
        setColumnHeader(deleteColId, header);
    }

    /** Helper function that supplies an {@link IllegalStateException}. */
    static Supplier<IllegalStateException> illegalStateException(String message) {
        return () -> new IllegalStateException(message);
    }

    /** PMO for the button to delete items in this table. */
    private static class DeleteItemButtonPmo<T> implements ButtonPmo {

        private Runnable deleteAction;

        DeleteItemButtonPmo(Runnable deleteAction) {
            this.deleteAction = deleteAction;
        }

        @Override
        public void onClick() {
            deleteAction.run();
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
    private static class AddItemButtonPmo implements ButtonPmo {

        private Runnable addAction;

        private Resource addButtonIcon;

        AddItemButtonPmo(Runnable addAction, Resource addButtonIcon) {
            this.addAction = addAction;
            this.addButtonIcon = addButtonIcon;
        }

        @Override
        public void onClick() {
            addAction.run();
        }

        @Override
        public Resource buttonIcon() {
            return addButtonIcon;
        }

    }

    /** Column generator that generates a column with a button to delete items in this table. */
    private static class DeleteItemColumnGenerator<T extends PresentationModelObject> implements ColumnGenerator {

        private static final long serialVersionUID = 1L;

        private Consumer<T> deleteConsumer;

        DeleteItemColumnGenerator(Consumer<T> deleteConsumer) {
            super();
            this.deleteConsumer = deleteConsumer;
        }

        @Override
        public Object generateCell(Table source, Object itemId, Object columnId) {
            @SuppressWarnings("unchecked")
            T itemPmo = (T)itemId;
            return ComponentFactory.newButton(new DeleteItemButtonPmo<T>(() -> delete(itemPmo)));
        }

        private void delete(T itemPmo) {
            deleteConsumer.accept(itemPmo);
        }

    }

    /** Column generator that generates a column for a field of a PMO. */
    private static class FieldColumnGenerator<T> implements ColumnGenerator {

        private static final long serialVersionUID = 1L;

        private final ElementDescriptor elementDescriptor;
        private final boolean receiveFocusOnNew;
        private final BindingContext bindingContext;

        private final LazyInitializingMap<T, PropertyDispatcher> dispatcherCache;

        public FieldColumnGenerator(ElementDescriptor elementDescriptor, boolean receiveFocusOnNew,
                BindingContext bindingContext, LazyInitializingMap<T, PropertyDispatcher> dispatcherCache) {
            this.elementDescriptor = elementDescriptor;
            this.receiveFocusOnNew = receiveFocusOnNew;
            this.bindingContext = bindingContext;
            this.dispatcherCache = dispatcherCache;
        }

        @Override
        public Object generateCell(Table source, Object itemId, Object columnId) {
            Component component = elementDescriptor.newComponent();
            component.addStyleName(ApplicationStyles.BORDERLESS);
            component.addStyleName(ApplicationStyles.TABLE_CELL);
            Field<?> field = (Field<?>)component;
            @SuppressWarnings("unchecked")
            T itemPmo = (T)itemId;

            FieldBinding<?> binding = FieldBinding.create(bindingContext, (String)columnId, null, field,
                                                          dispatcherCache.get(itemPmo));
            bindingContext.add(binding);
            binding.updateFromPmo();
            if (receiveFocusOnNew) {
                field.focus();
            }
            return component;
        }
    }

}
