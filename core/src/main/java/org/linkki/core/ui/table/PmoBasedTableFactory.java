/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.core.ui.table;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.TableBinding;
import org.linkki.core.binding.descriptor.ElementDescriptor;
import org.linkki.core.binding.descriptor.PropertyElementDescriptors;
import org.linkki.core.binding.descriptor.UIAnnotationReader;
import org.linkki.core.nls.pmo.PmoLabelType;
import org.linkki.core.nls.pmo.PmoNlsService;
import org.linkki.core.ui.application.ApplicationStyles;
import org.linkki.core.ui.components.LabelComponentWrapper;
import org.linkki.core.ui.section.annotations.TableColumnDescriptor;
import org.linkki.core.ui.section.annotations.UITableColumn.CollapseMode;

import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TreeTable;

/**
 * A factory to create a table based on a {@link ContainerPmo}.
 */
public class PmoBasedTableFactory<@NonNull T> {

    private PmoNlsService pmoNlsService;

    private final ContainerPmo<T> containerPmo;

    private final UIAnnotationReader annotationReader;

    private final BindingContext bindingContext;

    private final Class<? extends T> rowPmoClass;

    /**
     * Creates a new factory.
     * 
     * @param containerPmo The container providing the contents and column definitions.
     * @param bindingContext The binding context to which the cell bindings are added.
     */
    public PmoBasedTableFactory(ContainerPmo<T> containerPmo, BindingContext bindingContext) {
        this.containerPmo = requireNonNull(containerPmo, "containerPmo must not be null");
        this.bindingContext = requireNonNull(bindingContext, "bindingContext must not be null");
        this.rowPmoClass = containerPmo.getItemPmoClass();
        this.annotationReader = new UIAnnotationReader(rowPmoClass);
        pmoNlsService = PmoNlsService.get();
    }

    /**
     * Create a new table based on the container PMO.
     */
    public Table createTable() {
        Table table = createTableComponent();
        TableBinding<T> tableBinding = bindTable(table);
        createColumns(tableBinding);
        tableBinding.init();
        boolean hasCollapsibleColumn = annotationReader.getUiElements().stream()
                .map(annotationReader::getTableColumnDescriptor)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(TableColumnDescriptor::getCollapseMode)
                .anyMatch(CollapseMode::isCollapsible);
        table.setColumnCollapsingAllowed(hasCollapsibleColumn);
        if (hasCollapsibleColumn) {
            annotationReader.getUiElements().forEach(e -> setCollapsed(table, e));
        }
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
        Table table = getContainerPmo().isHierarchical() ? new TreeTable() : new Table();
        table.addStyleName(ApplicationStyles.TABLE);
        table.setHeightUndefined();
        table.setWidth("100%");
        table.setSortEnabled(false);
        return table;
    }

    private void createColumns(TableBinding<T> tableBinding) {
        annotationReader.getUiElements()
                .forEach(e -> createColumn(tableBinding, e));
    }

    /**
     * Creates a new column for a field of a PMO. Sets the configured width or expand ratio for the
     * field's column if either one is configured. Does nothing if no values are configured.
     * 
     * @param elementDesc the descriptor for the PMO's field
     */
    private void createColumn(TableBinding<T> tableBinding, PropertyElementDescriptors elementDesc) {
        Table table = tableBinding.getBoundComponent();
        FieldColumnGenerator<T> columnGen = new FieldColumnGenerator<>(elementDesc, tableBinding);
        String propertyName = elementDesc.getPmoPropertyName();
        table.addGeneratedColumn(propertyName, columnGen);
        table.setColumnHeader(propertyName,
                              pmoNlsService.getLabel(PmoLabelType.PROPERTY_LABEL, rowPmoClass, propertyName,
                                                     elementDesc.getLabelText()));
        setConfiguredColumnWidthOrExpandRatio(table, elementDesc);
    }

    private void setCollapsed(Table table, PropertyElementDescriptors elementDesc) {
        Optional<TableColumnDescriptor> column = annotationReader.getTableColumnDescriptor(elementDesc);
        CollapseMode collapseMode = column.map(TableColumnDescriptor::getCollapseMode)
                .orElse(CollapseMode.NOT_COLLAPSIBLE);
        table.setColumnCollapsible(elementDesc.getPmoPropertyName(), collapseMode.isCollapsible());
        table.setColumnCollapsed(elementDesc.getPmoPropertyName(), collapseMode.isInitiallyCollapsed());
    }

    private void setConfiguredColumnWidthOrExpandRatio(Table table, PropertyElementDescriptors elementDesc) {
        Optional<TableColumnDescriptor> column = annotationReader.getTableColumnDescriptor(elementDesc);
        column.ifPresent(c -> {
            c.checkValidConfiguration();
            if (c.isCustomWidthDefined()) {
                table.setColumnWidth(elementDesc.getPmoPropertyName(), c.getWidth());
            } else if (c.isCustomExpandRatioDefined()) {
                table.setColumnExpandRatio(elementDesc.getPmoPropertyName(), c.getExpandRatio());
            }
        });
    }

    private TableBinding<T> bindTable(Table table) {
        return TableBinding.create(bindingContext, table, getContainerPmo());
    }

    /** Column generator that generates a column for a field of a PMO. */
    private static class FieldColumnGenerator<@NonNull T> implements ColumnGenerator {

        private static final long serialVersionUID = 1L;

        private final PropertyElementDescriptors elementDescriptors;
        private final TableBinding<T> tableBinding;

        public FieldColumnGenerator(PropertyElementDescriptors elementDescriptors, TableBinding<T> tableBinding) {
            this.elementDescriptors = requireNonNull(elementDescriptors, "elementDescriptors must not be null");
            this.tableBinding = requireNonNull(tableBinding, "tableBinding must not be null");
        }

        @Override
        public Object generateCell(Table source,
                Object itemId,
                Object columnId) {
            requireNonNull(itemId, "itemId must not be null");
            ElementDescriptor elementDescriptor = elementDescriptors.getDescriptor(itemId);
            Component component = elementDescriptor.newComponent();
            component.addStyleName(ApplicationStyles.BORDERLESS);
            component.addStyleName(ApplicationStyles.TABLE_CELL);

            @SuppressWarnings("unchecked")
            T itemPmo = (T)itemId;
            component.addAttachListener($ -> tableBinding.bind(itemPmo, elementDescriptor,
                                                               new LabelComponentWrapper(component)));
            component.addDetachListener($ -> tableBinding.removeBindingsForComponent(component));

            // removed the following line as on the created binding for the cell
            // a updateFromPmo() is called later on by the binding manager, see FIPM-497 for details
            // binding.updateFromPmo()
            return component;
        }
    }

}
