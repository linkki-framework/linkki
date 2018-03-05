/*
 * Copyright Faktor Zehn AG.
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

import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;
import org.linkki.core.ui.section.AbstractSection;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

/**
 * A section containing a single table. This kind of section is created by the
 * {@link PmoBasedTableSectionFactory}.
 */
public class TableSection<T> extends AbstractSection {

    private static final long serialVersionUID = 1L;

    @Nullable
    private Table table;

    /* package private, used by the PmoBaseTableFactory */
    TableSection(String caption, boolean closeable, Optional<Button> addItemButton) {
        super(caption, closeable, addItemButton);
    }

    /**
     * Sets the table shown in the section.
     */
    /* package private, used by the PmoBaseTableFactory */
    void setTable(Table table) {
        Validate.isTrue(this.table == null, "Table already set.");
        this.table = requireNonNull(table, "table must not be null");
        addComponent(table);
        setExpandRatio(table, 1f);
        table.setSizeFull();
    }

    /**
     * Returns the table shown in the section.
     */
    public Table getTable() {
        Validate.isTrue(this.table != null, "Table must be already set.");
        return table;
    }

    @Override
    public String toString() {
        return "TableSection based on " + (table != null ? table.getContainerDataSource() : null);
    }

    @Override
    public Component getSectionContent() {
        return table;
    }
}
