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

import javax.annotation.Nonnull;

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

    private List<T> backupList = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Override
    protected LinkkiItemWrapper<T> getUnfilteredItem(Object itemId) {
        return new LinkkiItemWrapper<>((T)itemId);
    }

    @Override
    public Collection<?> getContainerPropertyIds() {
        return Collections.emptyList();
    }

    @Override
    public Property<T> getContainerProperty(Object itemId, Object propertyId) {
        return null;
    }

    @Override
    public Class<?> getType(Object propertyId) {
        throw new UnsupportedOperationException("getType is not supported");
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        getAllItemIds().clear();
        return true;
    }

    public void addAllItems(@Nonnull Collection<T> items) {
        getAllItemIds().addAll(requireNonNull(items));
        backupList.addAll(items);
        fireItemSetChange();
    }

    protected List<T> getBackupList() {
        return backupList;
    }

    /**
     * A simple Wrapper class for the Object in {@link LinkkiInMemoryContainer}.
     */
    static class LinkkiItemWrapper<T> implements Item {

        private static final long serialVersionUID = -8239631444860890275L;

        private final T item;

        public LinkkiItemWrapper(T item) {
            this.item = item;
        }

        @Override
        public Property<T> getItemProperty(Object id) {
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
        public boolean addItemProperty(Object id, @SuppressWarnings("rawtypes") Property property)
                throws UnsupportedOperationException {
            throw new UnsupportedOperationException("addItemProperty is not supported");
        }

        @Override
        public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("removeItemProperty is not supported");
        }

        public T getItem() {
            return item;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            LinkkiItemWrapper<?> that = (LinkkiItemWrapper<?>)o;

            return item.equals(that.item);
        }

        @Override
        public int hashCode() {
            return item.hashCode();
        }
    }

}
