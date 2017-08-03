/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.container;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNullableByDefault;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractInMemoryContainer;

/**
 * A in-memory container which doesn't do any reflection magic. This container simply stores the
 * Objects.
 */
public class LinkkiInMemoryContainer<T>
        extends AbstractInMemoryContainer<Object, Object, LinkkiInMemoryContainer.LinkkiItemWrapper<T>> {

    private static final long serialVersionUID = -1708252890035638419L;

    private List<LinkkiItemWrapper<T>> backupList = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Override
    protected LinkkiItemWrapper<T> getUnfilteredItem(@Nullable Object itemId) {
        return new LinkkiItemWrapper<>((T)itemId);
    }

    @Override
    public Collection<?> getContainerPropertyIds() {
        return Collections.emptyList();
    }

    @Override
    @CheckForNull
    public Property<T> getContainerProperty(@Nullable Object itemId, @Nullable Object propertyId) {
        return null;
    }

    @Override
    @CheckForNull
    public Class<?> getType(@Nullable Object propertyId) {
        throw new UnsupportedOperationException("getType is not supported");
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        getAllItemIds().clear();
        backupList.clear();
        return true;
    }

    public void addAllItems(Collection<T> items) {
        addAllItems(asLinkkiItemWrapper(items, new ArrayList<>(items.size())));
    }

    protected void addAllItems(List<LinkkiItemWrapper<T>> items) {

        requireNonNull(items, "items must not be null");

        List<Object> allItemIds = getAllItemIds();

        for (LinkkiItemWrapper<T> item : items) {
            backupList.add(item);
            allItemIds.add(item.getItem());
        }

        fireItemSetChange();
    }

    protected List<LinkkiItemWrapper<T>> asLinkkiItemWrapper(Collection<T> items,
            List<LinkkiItemWrapper<T>> target) {
        requireNonNull(items, "items must not be null");
        requireNonNull(target, "target must not be null");

        for (T item : items) {
            target.add(new LinkkiItemWrapper<T>(item));
        }

        return target;
    }

    protected List<LinkkiItemWrapper<T>> getBackupList() {
        return backupList;
    }

    /**
     * A simple Wrapper class for the Object in {@link LinkkiInMemoryContainer}. This class needs to
     * be public to ensure that it can be accessed from all JVM versions.
     * <p>
     * This wrapper is needed to 'override' the {@link #equals(Object)} and {@link #hashCode()} of
     * the containing objects for our 'need to reload the container' check.
     */
    @ParametersAreNullableByDefault
    public static class LinkkiItemWrapper<T> implements Item {

        private static final long serialVersionUID = -8239631444860890275L;

        @Nullable
        private final T item;

        public LinkkiItemWrapper(@Nullable T item) {
            this.item = item;
        }

        @Override
        @CheckForNull
        public Property<T> getItemProperty(@Nullable Object id) {
            // probably we should throw an UnsupportedOperationException
            // this method shall never be called but without an exception we'll never
            // notice it...
            return null;
        }

        @Override
        public Collection<?> getItemPropertyIds() {
            return Collections.emptyList();
        }

        @Override
        public boolean addItemProperty(@Nullable Object id, @SuppressWarnings("rawtypes") @Nullable Property property)
                throws UnsupportedOperationException {
            throw new UnsupportedOperationException("addItemProperty is not supported");
        }

        @Override
        public boolean removeItemProperty(@Nullable Object id) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("removeItemProperty is not supported");
        }

        @CheckForNull
        public T getItem() {
            return item;
        }

        @SuppressWarnings("null")
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            LinkkiItemWrapper<?> that = (LinkkiItemWrapper<?>)o;

            // only reference check because we only want to reload
            // the container if references has changed!
            return item == that.item;
        }

        @Override
        public int hashCode() {
            if (item != null) {
                return item.hashCode();
            } else {
                return 0;
            }
        }
    }

}
