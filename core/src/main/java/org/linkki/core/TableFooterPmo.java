/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core;

/**
 * A presentation model for footer cells in a table.
 */
public interface TableFooterPmo {

    /**
     * Returns the text shown in the given column.
     * 
     * @param column The name of the column also called property ID in VAADIN.
     */
    public String getFooterText(String column);

}
