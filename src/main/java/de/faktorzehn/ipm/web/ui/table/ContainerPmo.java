/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import java.util.EventListener;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.vaadin.ui.Table;

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

    /** Action that deletes items from the container. */
    @FunctionalInterface
    public static interface DeleteItemAction<T> {

        /** Deletes the given item from the container. */
        void deleteItem(T item);

    }

    /** Action that adds items to the container. */
    @FunctionalInterface
    public static interface AddItemAction<T> {

        /** Creates a new item and add it to the container. */
        T newItem();

    }

    /** Listener that is notified when the page length is changed. */
    @FunctionalInterface
    public static interface PageLengthListener extends EventListener {

        /** Method that is called when the page length is changed. */
        void pageLengthChanged(int newPageLength);

    }

    /** Returns the class of the items / rows in the container. */
    @Nonnull
    public Class<T> getItemPmoClass();

    /** Returns {@code true} if the items are editable, otherwise {@code false}. */
    public boolean isEditable();

    /**
     * Returns the {@link AddItemAction} to add new items if it is possible to add items to the
     * container.
     */
    @Nonnull
    public default Optional<AddItemAction<T>> addItemAction() {
        return Optional.empty();
    }

    /**
     * Returns the {@link DeleteItemAction} to delete items if it is possible to delete items from
     * the container.
     */
    @Nonnull
    public default Optional<DeleteItemAction<T>> deleteItemAction() {
        return Optional.empty();
    }

    /** Returns the items / rows in the container. */
    @Nonnull
    public List<T> getItems();

    /**
     * Sets the page length.
     * <p>
     * Setting page length 0 disables paging.
     * <p>
     * All registered {@link PageLengthListener}s are notified of the new value. If the value is not
     * changed, i.e. the value from {@link #getPageLength()} is set, listeners may or may not be
     * notified.
     * 
     * @see Table#setPageLength(int)
     */
    public void setPageLength(int newPageLength);

    /** Returns the current page length. */
    public int getPageLength();

    /** Registers the listener to be notified when the page length changes. */
    public void addPageLengthListener(PageLengthListener listener);

    /** Removes a previously registered page length listener. */
    public void removePageLengthListener(PageLengthListener listener);

}
