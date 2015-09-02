/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.ui.section.AbstractSection;

public class TableSection extends AbstractSection {

    private static final long serialVersionUID = 1L;

    private PmoBasedTable<? extends PresentationModelObject> table;

    public TableSection(String caption, boolean canAdd) {
        super(caption, false);
        // FIXME anders lösen
        // setEditAction(() -> editClicked());
        if (canAdd) {
            // FIXME anders lösen
            // setEditButtonIcon(FontAwesome.PLUS);
        }
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

    protected void editClicked() {
        getPmo().newItem();
        update();
    }

    @Override
    public String toString() {
        return "TableSection based on PMO=" + getPmo();
    }
}
