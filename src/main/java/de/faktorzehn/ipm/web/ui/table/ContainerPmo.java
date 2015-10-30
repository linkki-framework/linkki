/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.vaadin.ui.Table;

import de.faktorzehn.ipm.web.ButtonPmo;
import de.faktorzehn.ipm.web.PresentationModelObject;

/**
 * A container PMOs that has itself the role of a PMO for table controls (and maybe for other
 * controls displaying a set of objects).
 *
 * @author ortmann
 */
public interface ContainerPmo<T extends PresentationModelObject> {

    /** Default page length to use when no other page length is set. */
    public static final int DEFAULT_PAGE_LENGTH = 15;

    /** Returns the class of the items / rows in the container. */
    @Nonnull
    public Class<T> getItemPmoClass();

    /** Returns the items / rows in the container. */
    @Nonnull
    public List<T> getItems();

    /**
     * Returns a {@link ButtonPmo} for the add button in the table section
     * 
     */
    @Nonnull
    public default Optional<ButtonPmo> getAddItemButtonPmo() {
        return Optional.empty();
    }

    /**
     * Returns the current page length.
     * 
     * Default is 15 to deactivate the paging mechanism. Return 0 to deactivate table paging.
     * 
     * @see Table#setPageLength(int)
     */
    default int getPageLength() {
        return DEFAULT_PAGE_LENGTH;
    }

}
