/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import java.util.Optional;

import com.vaadin.server.Resource;

import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.ui.section.AbstractSection;
import de.faktorzehn.ipm.web.ui.table.ContainerPmo.AddItemAction;

public class TableSection<T extends PresentationModelObject> extends AbstractSection {

    private static final long serialVersionUID = 1L;

    private PmoBasedTable<? extends PresentationModelObject> table;

    public TableSection(String caption, Optional<AddItemAction<T>> addItemAction, Resource addItemIcon) {
        super(caption, false, addItemAction.map(AddItemAction.toEditAction(addItemIcon)));
    }

    void setTable(PmoBasedTable<? extends PresentationModelObject> theTable) {
        if (this.table != null) {
            throw new IllegalStateException("Table already set.");
        }
        this.table = theTable;
        addComponent(table);
    }

    public ContainerPmo<?> getPmo() {
        if (table == null) {
            throw new IllegalStateException("Table with PMO hasn't been set, yet!");
        }
        return table.getPmo();
    }

    public void update() {
        table.updateFromPmo();
    }

    @Override
    public String toString() {
        return "TableSection based on PMO=" + getPmo();
    }
}
