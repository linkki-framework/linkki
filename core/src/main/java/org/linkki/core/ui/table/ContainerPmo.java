/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.table;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.linkki.core.ButtonPmo;
import org.linkki.core.TableFooterPmo;

import com.vaadin.ui.Table;

/**
 * A container PMOs that has itself the role of a PMO for table controls (and maybe for other
 * controls displaying a set of objects).
 *
 * @author ortmann
 */
public interface ContainerPmo<T> {

    /** Default page length to use when no other page length is set. */
    public static final int DEFAULT_PAGE_LENGTH = 15;

    /**
     * Returns the class of the items / rows in the container.
     * 
     * @deprecated This method is not used anymore because the type could be derived from the
     *             generic interface.
     */
    @Deprecated
    default Class<T> getItemPmoClass() {
        return null;
    }

    /**
     * Returns the items / rows in the container.
     * <p>
     * !!! Important Note !!!
     * <p>
     * The container MUST return a list with identical items if the rows to be displayed haven't
     * changed. This is not given if you create a new list with new item PMOs on each call of the
     * <code>getItems()</code> method. If you don't adhere to this rule, the contents of the table
     * will be replaces each time you leave a cell after you have changed a value. This results in
     * poor performance and the focus gets lost each time you leave a cell (after changing the
     * value).
     * <p>
     * If you create the item PMOs based on a list of model objects, the easiest way is to use the
     * {@link SimpleItemSupplier}.
     */
    @Nonnull
    List<T> getItems();

    /**
     * Returns s {@link TableFooterPmo} that provides the data for the table footer.
     * 
     * @return The PMO that provides the data for the footer or an empty optional if no footer
     *         should be shown (default).
     */
    @Nonnull
    default Optional<TableFooterPmo> getFooterPmo() {
        return Optional.empty();
    }

    /**
     * Returns a {@link ButtonPmo} for the add button in the table section
     */
    @Nonnull
    default Optional<ButtonPmo> getAddItemButtonPmo() {
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
