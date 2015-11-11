/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.table;

import java.util.Set;
import java.util.function.Function;

import org.linkki.core.PresentationModelObject;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.TableBinding;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.application.ApplicationStyles;
import org.linkki.core.ui.section.annotations.ElementDescriptor;
import org.linkki.core.ui.section.annotations.TableColumnDescriptor;
import org.linkki.core.ui.section.annotations.UIAnnotationReader;
import org.linkki.util.LazyInitializingMap;

import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

/**
 * A factory to create a table based on a {@link ContainerPmo}.
 *
 * @author ortmann
 */
public class PmoBasedTableFactory<T extends PresentationModelObject> {

    private ContainerPmo<T> containerPmo;

    private UIAnnotationReader annotationReader;

    private BindingContext bindingContext;

    /** Cache for property dispatchers used by the column generators. */
    private final LazyInitializingMap<T, PropertyDispatcher> dispatcherCache;

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
        this.annotationReader = new UIAnnotationReader(containerPmo.getItemPmoClass());
        this.bindingContext = bindingContext;
        dispatcherCache = new LazyInitializingMap<>(propertyDispatcherBuilder);
    }

    /**
     * Create a new table based on the container PMO.
     */
    public Table createTable() {
        Table table = createTableComponent();
        createColumns(table);
        bindTable(table);
        table.setPageLength(containerPmo.getPageLength());
        bindingContext.updateUI();
        return table;
    }

    public ContainerPmo<T> getContainerPmo() {
        return containerPmo;
    }

    protected BindingContext getBindingContext() {
        return bindingContext;
    }

    private Table createTableComponent() {
        Table table = new Table();
        table.addStyleName(ApplicationStyles.TABLE);
        table.setHeightUndefined();
        table.setWidth("100%");
        table.setSortEnabled(false);
        return table;
    }

    private void createColumns(Table table) {
        Set<ElementDescriptor> uiElements = annotationReader.getUiElements();
        for (ElementDescriptor uiElement : uiElements) {
            createColumn(table, uiElement);
        }
    }

    /**
     * Sets the configured width or expand ratio for the field's column if either one is configured.
     * Does nothing if no values are configured.
     * 
     * /** Creates a new column for a field of a PMO.
     * 
     * @param elementDesc the descriptor for the PMO's field
     * @param receiveFocusOnNew whether or not the generated field should receive the focus when a
     *            new row is generated
     * @param bindingContext the context in which the field is bound
     */
    private void createColumn(Table table, ElementDescriptor elementDesc) {
        FieldColumnGenerator<T> columnGen = new FieldColumnGenerator<T>(elementDesc, bindingContext, dispatcherCache);
        String propertyName = elementDesc.getPropertyName();
        table.addGeneratedColumn(propertyName, columnGen);
        table.setColumnHeader(propertyName, elementDesc.getLabelText());
        setConfiguredColumndWidthOrExpandRatio(table, elementDesc);
    }

    private void setConfiguredColumndWidthOrExpandRatio(Table table, ElementDescriptor field) {
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

    private void bindTable(Table table) {
        TableBinding.create(bindingContext, table, getContainerPmo());
    }

    /** Column generator that generates a column for a field of a PMO. */
    private static class FieldColumnGenerator<T> implements ColumnGenerator {

        private static final long serialVersionUID = 1L;

        private final ElementDescriptor elementDescriptor;
        private final BindingContext bindingContext;

        private final LazyInitializingMap<T, PropertyDispatcher> dispatcherCache;

        public FieldColumnGenerator(ElementDescriptor elementDescriptor, BindingContext bindingContext,
                LazyInitializingMap<T, PropertyDispatcher> dispatcherCache) {
            this.elementDescriptor = elementDescriptor;
            this.bindingContext = bindingContext;
            this.dispatcherCache = dispatcherCache;
        }

        @Override
        public Object generateCell(Table source, Object itemId, Object columnId) {

            Component component = elementDescriptor.newComponent();
            component.addStyleName(ApplicationStyles.BORDERLESS);
            component.addStyleName(ApplicationStyles.TABLE_CELL);

            @SuppressWarnings("unchecked")
            T itemPmo = (T)itemId;
            ElementBinding binding = elementDescriptor.createBinding(bindingContext, itemPmo, null, component,
                                                                     dispatcherCache.get(itemPmo));

            binding.updateFromPmo();
            return component;
        }
    }

}
