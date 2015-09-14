/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyBehaviorProvider;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcherFactory;
import de.faktorzehn.ipm.web.ui.application.ApplicationStyles;
import de.faktorzehn.ipm.web.ui.section.annotations.FieldDescriptor;
import de.faktorzehn.ipm.web.ui.section.annotations.TableColumnDescriptor;
import de.faktorzehn.ipm.web.ui.section.annotations.UIAnnotationReader;
import de.faktorzehn.ipm.web.ui.section.annotations.UITable;

/**
 * A factory to create a table based on a {@link ContainerPmo}.
 *
 * @author ortmann
 */
public class PmoBasedTableFactory<T extends PresentationModelObject> {

    private static final String DELETE_ITEM_COLUMN_ID = "remove";
    private static final String DELETE_ITEM_COLUMN_DEFAULT_HEADER = "Entfernen";

    private static final PropertyDispatcherFactory DISPATCHER_FACTORY = new PropertyDispatcherFactory();

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
     * Creates a new {@link PropertyDispatcher} chain for the given PMO.
     */
    private PropertyDispatcher createPropertyDispatcher(final T pmo) {
        return DISPATCHER_FACTORY.defaultDispatcherChain(pmo, propertyBehaviorProvider);
    }

    /**
     * Create a new table based on the container PMO.
     */
    public PmoBasedTable<T> createTable() {
        PmoBasedTable<T> table = createTableComponent();
        createColumns(table);
        table.setPageLength(containerPmo.getPageLength());
        containerPmo.addPageLengthListener(table::setPageLength);
        return table;
    }

    private PmoBasedTable<T> createTableComponent() {
        PmoBasedTable<T> table = new PmoBasedTable<>(containerPmo, this::createPropertyDispatcher);
        table.addStyleName(ApplicationStyles.TABLE);
        table.setHeightUndefined();
        table.setPageLength(0);
        table.setSortEnabled(false);
        return table;
    }

    private void createColumns(PmoBasedTable<T> table) {
        Set<FieldDescriptor> fields = annotationReader.getFields();
        boolean receiveFocusOnNew = true;
        for (FieldDescriptor field : fields) {
            createColumn(table, field, receiveFocusOnNew);
            receiveFocusOnNew = false;
        }
        if (containerPmo.deleteItemAction().isPresent()) {
            table.addGeneratedColumn(DELETE_ITEM_COLUMN_ID, table.deleteItemColumnGenerator(bindingContext));
            table.setColumnHeader(DELETE_ITEM_COLUMN_ID, getDeleteItemColumnHeader());
            table.setColumnExpandRatio(DELETE_ITEM_COLUMN_ID, 0.0f);
        }
    }

    private String getDeleteItemColumnHeader() {
        if (containerPmo.getClass().isAnnotationPresent(UITable.class)) {
            UITable tableAnnotation = containerPmo.getClass().getAnnotation(UITable.class);
            return StringUtils.defaultIfEmpty(tableAnnotation.deleteItemColumnHeader(),
                                              DELETE_ITEM_COLUMN_DEFAULT_HEADER);
        }
        return DELETE_ITEM_COLUMN_DEFAULT_HEADER;
    }

    private void createColumn(PmoBasedTable<T> table, FieldDescriptor field, boolean receiveFocusOnNew) {
        table.addGeneratedColumn(field.getPropertyName(),
                                 table.fieldColumnGenerator(field, receiveFocusOnNew, bindingContext));
        table.setColumnHeader(field.getPropertyName(), field.getLabelText());
        setConfiguredColumndWidthOrExpandRatio(table, field);
    }

    /**
     * Sets the configured width or expand ratio for the field's column if either one is configured.
     * Does nothing if no values are configured.
     */
    private void setConfiguredColumndWidthOrExpandRatio(PmoBasedTable<T> table, FieldDescriptor field) {
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

}
