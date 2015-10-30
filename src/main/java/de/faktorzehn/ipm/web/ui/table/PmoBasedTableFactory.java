/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;
import de.faktorzehn.ipm.web.ui.application.ApplicationStyles;
import de.faktorzehn.ipm.web.ui.section.annotations.ElementDescriptor;
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

    private ContainerPmo<T> containerPmo;
    private UIAnnotationReader annotationReader;
    private BindingContext bindingContext;
    private Function<T, PropertyDispatcher> propertyDispatcherBuilder;

    /**
     * Creates a new factory.
     * 
     * @param containerPmo The container providing the contents and column definitions.
     * @param bindingContext The binding context to which the cell bindings are added.
     * @param propertyDispatcherBuilder A function that provides property dispatcher for a given
     *            {@link PresentationModelObject}
     */
    public PmoBasedTableFactory(ContainerPmo<T> containerPmo, BindingContext bindingContext,
            Function<T, PropertyDispatcher> propertyDispatcherBuilder) {
        this.containerPmo = containerPmo;
        this.propertyDispatcherBuilder = propertyDispatcherBuilder;
        this.annotationReader = new UIAnnotationReader(containerPmo.getItemPmoClass());
        this.bindingContext = bindingContext;
    }

    /**
     * Create a new table based on the container PMO.
     */
    public PmoBasedTable<T> createTable() {
        PmoBasedTable<T> table = createTableComponent();
        createColumns(table);
        table.setPageLength(containerPmo.getPageLength());
        table.updateFromPmo();
        return table;
    }

    public ContainerPmo<T> getContainerPmo() {
        return containerPmo;
    }

    protected BindingContext getBindingContext() {
        return bindingContext;
    }

    private PmoBasedTable<T> createTableComponent() {
        PmoBasedTable<T> table = new PmoBasedTable<>(containerPmo, propertyDispatcherBuilder);
        table.addStyleName(ApplicationStyles.TABLE);
        table.setHeightUndefined();
        table.setWidth("100%");
        table.setSortEnabled(false);
        return table;
    }

    private void createColumns(PmoBasedTable<T> table) {
        Set<ElementDescriptor> uiElements = annotationReader.getUiElements();
        boolean receiveFocusOnNew = true;
        for (ElementDescriptor uiElement : uiElements) {
            createColumn(table, uiElement, receiveFocusOnNew);
            receiveFocusOnNew = false;
        }
        Optional<Consumer<T>> deleteItemConsumer = containerPmo.getDeleteItemConsumer();
        if (deleteItemConsumer.isPresent()) {
            table.createDeleteItemColumn(DELETE_ITEM_COLUMN_ID, getDeleteItemColumnHeader(), deleteItemConsumer.get(),
                                         bindingContext);
            table.setColumnWidth(DELETE_ITEM_COLUMN_ID, -1);
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

    private void createColumn(PmoBasedTable<T> table, ElementDescriptor elementDesc, boolean receiveFocusOnNew) {
        table.createColumn(elementDesc, receiveFocusOnNew, bindingContext);
        setConfiguredColumndWidthOrExpandRatio(table, elementDesc);
    }

    /**
     * Sets the configured width or expand ratio for the field's column if either one is configured.
     * Does nothing if no values are configured.
     */
    private void setConfiguredColumndWidthOrExpandRatio(PmoBasedTable<T> table, ElementDescriptor field) {
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
