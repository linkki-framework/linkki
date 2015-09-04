/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import java.util.List;

import com.vaadin.ui.Table;

import de.faktorzehn.ipm.web.PresentationModelObject;

/**
 * A table that is driven by a {@link ContainerPmo}. The table gets its content from the container
 * PMO, the columns are defined by the annotations of the according item PMO.
 *
 * @author ortmann
 */
public class PmoBasedTable<T extends PresentationModelObject> extends Table {

    private static final long serialVersionUID = 1L;

    private ContainerPmo<T> containerPmo;

    public PmoBasedTable(ContainerPmo<T> tablePmo) {
        super();
        this.containerPmo = tablePmo;
    }

    public ContainerPmo<T> getPmo() {
        return containerPmo;
    }

    public void updateFromPmo() {
        removeAllItems();
        createItems();
        refreshRowCache();

    }

    private void createItems() {
        List<T> rows = containerPmo.getItems();
        // don't add values directly, let data binding take care of it
        rows.forEach(row -> addItem(new Object[0], row));
    }

    @Override
    public String toString() {
        return "Table based on PMO=" + getPmo();
    }
}
