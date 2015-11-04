/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.table;

import java.util.Optional;

import org.linkki.core.PresentationModelObject;
import org.linkki.core.ui.section.AbstractSection;

import com.vaadin.ui.Table;
import com.vaadin.ui.Button;

public class TableSection<T extends PresentationModelObject> extends AbstractSection {

    private static final long serialVersionUID = 1L;

    private Table table;

    public TableSection(String caption, Optional<Button> addItemButton) {
        super(caption, false, addItemButton);
    }

    void setTable(Table table) {
        if (this.table != null) {
            throw new IllegalStateException("Table already set.");
        }
        this.table = table;
        addComponent(table);
    }

    @Override
    public String toString() {
        return "TableSection based on " + table.getContainerDataSource();
    }
}
