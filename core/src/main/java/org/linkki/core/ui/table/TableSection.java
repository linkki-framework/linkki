/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.table;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;
import org.linkki.core.ui.section.AbstractSection;

import com.vaadin.ui.Button;
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
        requireNonNull(table, "table must not be null");
        this.table = table;
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
}
