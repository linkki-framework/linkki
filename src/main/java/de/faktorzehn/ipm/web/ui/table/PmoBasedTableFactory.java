/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.themes.ValoTheme;

import de.faktorzehn.ipm.utils.LazyInitializingMap;
import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.FieldBinding;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyBehaviorProvider;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcherFactory;
import de.faktorzehn.ipm.web.ui.application.ApplicationStyles;
import de.faktorzehn.ipm.web.ui.section.annotations.FieldDescriptor;
import de.faktorzehn.ipm.web.ui.section.annotations.TableColumnDescriptor;
import de.faktorzehn.ipm.web.ui.section.annotations.UIAnnotationReader;
import de.faktorzehn.ipm.web.ui.section.annotations.UITable;
import de.faktorzehn.ipm.web.ui.table.ContainerPmo.DeleteItemAction;

/**
 * A factory to create a table based on a {@link ContainerPmo}.
 *
 * @author ortmann
 */
public class PmoBasedTableFactory<T extends PresentationModelObject> {

    private static final String REMOVE_COLUMN = "remove";

    private static final PropertyDispatcherFactory DISPATCHER_FACTORY = new PropertyDispatcherFactory();

    private static final String DEFAULT_DELETE_ITEM_COLUMN_HEADER = "Entfernen";

    private ContainerPmo<T> containerPmo;
    private UIAnnotationReader annotationReader;
    private BindingContext bindingContext;
    private PropertyBehaviorProvider propertyBehaviorProvider;

    /**
     * Creates a new factory.
     * 
     * @param containerPmo The container providing the contents and column definitions.
     * @param bindingContext The binding context to which the cell bindings are added.
     * @param propertyBehaviorProvider The property behavior provider ...
     */
    public PmoBasedTableFactory(ContainerPmo<T> containerPmo, BindingContext bindingContext,
            PropertyBehaviorProvider propertyBehaviorProvider) {
        this.containerPmo = containerPmo;
        this.annotationReader = new UIAnnotationReader(containerPmo.getItemPmoClass());
        this.bindingContext = bindingContext;
        this.propertyBehaviorProvider = propertyBehaviorProvider;
    }

    /**
     * Create a new table based on the container PMO.
     */
    public PmoBasedTable<T> createTable() {
        PmoBasedTable<T> table = createTableComponent();
        createColumns(table);
        return table;
    }

    public ContainerPmo<T> getContainerPmo() {
        return containerPmo;
    }

    protected BindingContext getBindingContext() {
        return bindingContext;
    }

    protected PropertyDispatcher createPropertyDispatcher(final PresentationModelObject pmo) {
        return DISPATCHER_FACTORY.defaultDispatcherChain(pmo, propertyBehaviorProvider);
    }

    private PmoBasedTable<T> createTableComponent() {
        PmoBasedTable<T> table = new PmoBasedTable<>(containerPmo);
        table.addStyleName(ApplicationStyles.TABLE);
        table.setHeightUndefined();
        table.setPageLength(0);
        table.setSortEnabled(false);
        return table;
    }

    private void createColumns(Table table) {
        Set<FieldDescriptor> fields = annotationReader.getFields();
        boolean receiveFocusOnNew = true;
        for (FieldDescriptor field : fields) {
            createColumn(table, field, receiveFocusOnNew);
            receiveFocusOnNew = false;
        }
        if (containerPmo.deleteItemAction().isPresent()) {
            table.addGeneratedColumn(REMOVE_COLUMN,
                                     new RemoveIconColumnGenerator(containerPmo.deleteItemAction().get()));
            table.setColumnHeader(REMOVE_COLUMN, getDeleteItemColumnHeader());
            table.setColumnExpandRatio(REMOVE_COLUMN, 0.0f);
        }
    }

    private String getDeleteItemColumnHeader() {
        if (containerPmo.getClass().isAnnotationPresent(UITable.class)) {
            UITable tableAnnotation = containerPmo.getClass().getAnnotation(UITable.class);
            return StringUtils.defaultIfEmpty(tableAnnotation.deleteItemColumnHeader(), DEFAULT_DELETE_ITEM_COLUMN_HEADER);
        }
        return DEFAULT_DELETE_ITEM_COLUMN_HEADER;
    }

    private void createColumn(Table table, FieldDescriptor field, boolean receiveFocusOnNew) {
        table.addGeneratedColumn(field.getPropertyName(), new FieldColumnGenerator(field, receiveFocusOnNew));
        table.setColumnHeader(field.getPropertyName(), field.getLabelText());
        setConfiguredColumndWidthOrExpandRatio(table, field);
    }

    /**
     * Sets the configured width or expand ration for the field's column if either one is
     * configured. Does nothing if no values are configured.
     */
    private void setConfiguredColumndWidthOrExpandRatio(Table table, FieldDescriptor field) {
        if (!annotationReader.hasTableColumnAnnotation(field)) {
            return;
        }
        TableColumnDescriptor column = annotationReader.getTableColumnDescriptor(field);
        column.checkValidConfiguration();
        if (column.isCustomWidthDefined()) {
            table.setColumnWidth(field.getPropertyName(), column.getWidth());
        } else if (column.isCustomExpandRatioDefined()) {
            table.setColumnExpandRatio(field.getPropertyName(), column.getExpandRatio());
        }
    }

    class FieldColumnGenerator implements ColumnGenerator {

        private static final long serialVersionUID = 1L;

        private final LazyInitializingMap<PresentationModelObject, PropertyDispatcher> dispatchers = new LazyInitializingMap<>(
                PmoBasedTableFactory.this::createPropertyDispatcher);
        private final FieldDescriptor fieldDescriptor;
        private final boolean receiveFocusOnNew;

        public FieldColumnGenerator(FieldDescriptor fieldDescriptor, boolean receiveFocusOnNew) {
            this.fieldDescriptor = fieldDescriptor;
            this.receiveFocusOnNew = receiveFocusOnNew;
        }

        @Override
        public Object generateCell(Table source, Object itemId, Object columnId) {
            Component component = fieldDescriptor.newComponent();
            component.addStyleName(ApplicationStyles.BORDERLESS);
            component.addStyleName(ApplicationStyles.TABLE_CELL);
            component.setEnabled(getContainerPmo().isEditable());
            Field<?> field = (Field<?>)component;
            T pmo = getContainerPmo().getItemPmoClass().cast(itemId);

            FieldBinding<?> binding = FieldBinding.create(getBindingContext(), (String)columnId, null, field,
                                                          dispatchers.get(pmo));
            getBindingContext().add(binding);
            binding.updateFieldFromPmo();
            if (receiveFocusOnNew) {
                field.focus();
            }
            return component;
        }
    }

    class RemoveIconColumnGenerator implements ColumnGenerator {

        private static final long serialVersionUID = 1L;

        private final DeleteItemAction<T> deleteAction;

        public RemoveIconColumnGenerator(DeleteItemAction<T> deleteAction) {
            super();
            this.deleteAction = deleteAction;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object generateCell(Table source, Object itemId, Object columnId) {
            Button button = new Button(FontAwesome.TRASH_O);
            button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
            button.addStyleName(ValoTheme.BUTTON_LARGE);
            T row = (T)itemId;
            button.addClickListener(e -> {
                deleteAction.deleteItem(row);
                ((PmoBasedTable<T>)source).updateFromPmo();
                getBindingContext().updateUI();
            });
            return button;
        }

    }

}
