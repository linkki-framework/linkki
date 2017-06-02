/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.table;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.TableBinding;
import org.linkki.core.nls.pmo.PmoLabelType;
import org.linkki.core.nls.pmo.PmoNlsService;
import org.linkki.core.ui.application.ApplicationStyles;
import org.linkki.core.ui.section.annotations.ElementDescriptor;
import org.linkki.core.ui.section.annotations.ElementDescriptors;
import org.linkki.core.ui.section.annotations.TableColumnDescriptor;
import org.linkki.core.ui.section.annotations.UIAnnotationReader;

import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

/**
 * A factory to create a table based on a {@link ContainerPmo}.
 */
public class PmoBasedTableFactory<T> {

    private PmoNlsService pmoNlsService;

    private final ContainerPmo<T> containerPmo;

    private final UIAnnotationReader annotationReader;

    private final BindingContext bindingContext;

    /**
     * Creates a new factory.
     * 
     * @param containerPmo The container providing the contents and column definitions.
     * @param bindingContext The binding context to which the cell bindings are added.
     */
    public PmoBasedTableFactory(ContainerPmo<T> containerPmo, BindingContext bindingContext) {
        this.containerPmo = requireNonNull(containerPmo, "containerPmo must not be null");
        this.bindingContext = requireNonNull(bindingContext, "bindingContext must not be null");
        this.annotationReader = new UIAnnotationReader(getItemPmoClass());
        pmoNlsService = PmoNlsService.get();
    }

    /* private */ final Class<?> getItemPmoClass() {
        Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(containerPmo.getClass(),
                                                                              ContainerPmo.class);
        for (Entry<TypeVariable<?>, Type> typeArgument : typeArguments.entrySet()) {
            if (typeArgument.getKey().getGenericDeclaration().equals(ContainerPmo.class)) {
                return (Class<?>)typeArgument.getValue();
            }
        }
        throw new IllegalArgumentException("Cannot identify row pmo type");
    }

    /**
     * Create a new table based on the container PMO.
     */
    public Table createTable() {
        Table table = createTableComponent();
        createColumns(table);
        bindTable(table);
        table.setPageLength(containerPmo.getPageLength());
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
        annotationReader.getUiElements()
                .forEach(e -> createColumn(table, e));
    }

    /**
     * Creates a new column for a field of a PMO. Sets the configured width or expand ratio for the
     * field's column if either one is configured. Does nothing if no values are configured.
     * 
     * @param elementDesc the descriptor for the PMO's field
     */
    private void createColumn(Table table, ElementDescriptors elementDesc) {
        FieldColumnGenerator<T> columnGen = new FieldColumnGenerator<>(elementDesc, bindingContext);
        String propertyName = elementDesc.getPmoPropertyName();
        table.addGeneratedColumn(propertyName, columnGen);
        table.setColumnHeader(propertyName,
                              pmoNlsService.getLabel(PmoLabelType.PROPERTY_LABEL, getItemPmoClass(), propertyName,
                                                     elementDesc.getLabelText()));
        setConfiguredColumndWidthOrExpandRatio(table, elementDesc);
    }

    private void setConfiguredColumndWidthOrExpandRatio(Table table, ElementDescriptors field) {
        if (!annotationReader.hasTableColumnAnnotation(field)) {
            return;
        }
        TableColumnDescriptor column = annotationReader.getTableColumnDescriptor(field);
        column.checkValidConfiguration();
        if (column.isCustomWidthDefined()) {
            table.setColumnWidth(field.getPmoPropertyName(), column.getWidth());
        } else if (column.isCustomExpandRatioDefined()) {
            table.setColumnExpandRatio(field.getPmoPropertyName(), column.getExpandRatio());
        }
    }

    private void bindTable(Table table) {
        TableBinding.create(bindingContext, table, getContainerPmo());
    }

    /** Column generator that generates a column for a field of a PMO. */
    private static class FieldColumnGenerator<T> implements ColumnGenerator {

        private static final long serialVersionUID = 1L;

        private final ElementDescriptors elementDescriptors;
        private final BindingContext bindingContext;

        public FieldColumnGenerator(ElementDescriptors elementDescriptors, BindingContext bindingContext) {
            this.elementDescriptors = requireNonNull(elementDescriptors, "elementDescriptors must not be null");
            this.bindingContext = requireNonNull(bindingContext, "bindingContext must not be null");
        }

        @Override
        public Object generateCell(@Nullable Table source,
                @SuppressWarnings("null") Object itemId,
                @Nullable Object columnId) {
            requireNonNull(itemId, "itemId must not be null");
            ElementDescriptor elementDescriptor = elementDescriptors.getDescriptor(itemId);
            Component component = elementDescriptor.newComponent();
            component.addStyleName(ApplicationStyles.BORDERLESS);
            component.addStyleName(ApplicationStyles.TABLE_CELL);

            @SuppressWarnings("unchecked")
            T itemPmo = (T)itemId;
            bindingContext.bind(itemPmo, elementDescriptor, component, null);

            // removed the following line as on the created binding for the cell
            // a updateFromPmo() is called later on by the binding manager, see FIPM-497 for details
            // binding.updateFromPmo()
            return component;
        }
    }

}
