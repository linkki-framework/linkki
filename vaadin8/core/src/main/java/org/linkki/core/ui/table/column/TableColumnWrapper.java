/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.core.ui.table.column;

import static java.util.Objects.requireNonNull;

import org.linkki.core.binding.Binding;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.PropertyElementDescriptors;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.ui.table.column.annotation.UITableColumn.CollapseMode;

/**
 * Wrapper for the column header. The column and column header is no real component in vaadin7, hence
 * this wrapper has no real component to wrap. It is more useful to use only methods directly provided
 * by this wrapper than using the return value of {@link #getComponent()}.
 */
@SuppressWarnings("deprecation")
public class TableColumnWrapper implements ComponentWrapper {

    public static final WrapperType COLUMN_TYPE = WrapperType.of("column-header");

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    private final com.vaadin.v7.ui.Table table;
    private final String propertyName;

    public TableColumnWrapper(com.vaadin.v7.ui.Table table, String propertyName) {
        this.table = requireNonNull(table, "table must not be null");

        this.propertyName = requireNonNull(propertyName, "propertyName must not be null");
        // vaadin default is true, our default is false
        table.setColumnCollapsible(propertyName, false);
    }

    @Override
    public void setId(String id) {
        // do nothing
    }

    @Override
    public void setLabel(String labelText) {
        table.setColumnHeader(propertyName, labelText);
    }

    @Override
    public void setEnabled(boolean enabled) {
        // do nothing
    }

    @Override
    public void setVisible(boolean visible) {
        // do nothing
    }

    @Override
    public void setTooltip(String text) {
        // do nothing
    }

    /**
     * {@inheritDoc}
     * 
     * @implNote this method returns the column generator because in vaadin7 it comes most closest to
     *           any kind of column component.
     */
    @Override
    public com.vaadin.v7.ui.Table.ColumnGenerator getComponent() {
        return table.getColumnGenerator(propertyName);
    }

    @Override
    public void setValidationMessages(MessageList messagesForProperty) {
        // do nothing
    }

    @Override
    public WrapperType getType() {
        return COLUMN_TYPE;
    }

    public void setCollapseMode(CollapseMode collapseMode) {
        if (collapseMode.isCollapsible() && !table.isColumnCollapsingAllowed()) {
            table.setColumnCollapsingAllowed(true);
        }
        if (table.isColumnCollapsingAllowed()) {
            table.setColumnCollapsible(propertyName, collapseMode.isCollapsible());
            table.setColumnCollapsed(propertyName, collapseMode.isInitiallyCollapsed());
        }
    }

    public void setWidth(int width) {
        if (width != UITableColumn.UNDEFINED_WIDTH) {
            table.setColumnWidth(propertyName, width);
        }
    }

    public void setExpandRatio(float expandRatio) {
        if (expandRatio != UITableColumn.UNDEFINED_EXPAND_RATIO) {
            table.setColumnExpandRatio(propertyName, expandRatio);
        }
    }

    @Override
    public void registerBinding(Binding binding) {
        ((FieldColumnGenerator<?>)getComponent()).setBinding(binding);
    }

    @Override
    public String toString() {
        return "ColumnHeaderWrapper [" + table.getId() + "#" + propertyName + "]";
    }

    public static com.vaadin.v7.ui.Table.ColumnGenerator createComponent(PropertyElementDescriptors elementDesc,
            BindingContext bindingContext) {
        return new FieldColumnGenerator<>(elementDesc, bindingContext);
    }

}
