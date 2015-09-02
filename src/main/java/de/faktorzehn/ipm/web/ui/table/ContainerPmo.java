/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import java.util.List;

import javax.annotation.Nonnull;

import de.faktorzehn.ipm.web.PresentationModelObject;

/**
 * A container PMOs that has itself the role of a PMO for table controls (and maybe for other
 * controls displaying a set of objects).
 *
 * @author ortmann
 */
public interface ContainerPmo<T extends PresentationModelObject> {

    /**
     * Returns the class of the items / rows in the container.
     */
    @Nonnull
    public Class<T> getItemPmoClass();

    /**
     * Returns <code>true</code> if the items are editable, otherwise <code>false</code>.
     */
    public boolean isEditable();

    /**
     * Returns <code>true</code> if it is possible to add items to the container, otherwise
     * <code>false</code>.
     */
    public boolean isAddItemAvailable();

    /**
     * Returns <code>true</code> if it is possible to delete items to the container, otherwise
     * <code>false</code>.
     */
    public boolean isDeleteItemAvailable();

    /**
     * Returns the items / rows in the container.
     */
    @Nonnull
    public List<T> getItems();

    /**
     * Deletes the given item from the container.
     */
    public void deleteItem(@Nonnull T item);

    /**
     * Creates a new item and adds it to the container,
     */
    public void newItem();

}
