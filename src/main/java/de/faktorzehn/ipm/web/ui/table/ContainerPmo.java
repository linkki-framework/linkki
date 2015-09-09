/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import de.faktorzehn.ipm.web.PresentationModelObject;

/**
 * A container PMOs that has itself the role of a PMO for table controls (and maybe for other
 * controls displaying a set of objects).
 *
 * @author ortmann
 */
public interface ContainerPmo<T extends PresentationModelObject> {

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

}
