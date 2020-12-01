/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.core.ui.creation.table.container;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.WeakHashMap;

import org.linkki.core.defaults.columnbased.pmo.HierarchicalRowPmo;

import edu.umd.cs.findbugs.annotations.CheckForNull;


/**
 * An in-memory container which doesn't do any reflection magic. This container simply stores the
 * Objects.
 * 
 * @param <T> the type of items contained in this container
 */
@SuppressWarnings("deprecation")
public class LinkkiInMemoryContainer<T>
        extends com.vaadin.v7.data.util.AbstractInMemoryContainer<T, Object, com.vaadin.v7.data.Item>
        implements com.vaadin.v7.data.Container.Hierarchical {

    private static final long serialVersionUID = -1708252890035638419L;

    // Use a weak reference to remove mappings of children that have been removed because they are
    // no longer referenced by their former parent. Their bindings are removed automatically when
    // the corresponding Components are detached after their parent's binding was updated.
    private WeakHashMap<T, T> parents = new WeakHashMap<>();
    private WeakHashMap<T, List<T>> children = new WeakHashMap<>();

    private ArrayList<T> roots = new ArrayList<>();

    @Override
    public Collection<?> getContainerPropertyIds() {
        return Collections.emptyList();
    }

    /**
     * Replaces this container's contents with the given items. Vaadin is notified of the change, and
     * potentially triggers a redraw. Due to the implementation of this container, this should only be
     * called if the items have changed.
     * 
     * @param items a collection of items
     */
    public void setItems(Collection<? extends T> items) {
        requireNonNull(items, "items must not be null");

        this.roots = new ArrayList<>(items);

        getAllItemIds().clear();
        getAllItemIds().addAll(items);

        fireItemSetChange();
    }

    /**
     * @deprecated Since July 26, 2018. Use {@link #setItems(Collection)} with empty list instead. If
     *             {@link #removeAllItems()} was only used in combination with
     *             {@link #addAllItems(Collection)} simply call {@link #setItems(Collection)} with the
     *             new collection.
     */
    @Deprecated
    @Override
    public boolean removeAllItems() {
        getAllItemIds().clear();
        parents.clear();
        children.clear();
        roots.clear();
        return true;
    }

    /**
     * @deprecated Since July 26, 2018. Adding new items to a list of existing items is no longer
     *             supported. Call {@link #setItems(Collection)} to specify the new list of items.
     */
    @Deprecated
    public void addAllItems(Collection<T> items) {
        this.roots.addAll(items);
        getAllItemIds().addAll(items);
        fireItemSetChange();
    }

    /**
     * Returns a {@link DummyItemImplementation}.
     * <p>
     * Note that this method should not be needed. The <code>itemId</code> should already be a row PMO.
     * However, this method cannot return <code>null</code> or throw an exception as otherwise it would
     * not be possible to add any {@link com.vaadin.v7.event.ItemClickEvent.ItemClickListener
     * ItemClickListener} to the table.
     */
    @SuppressWarnings("javadoc")
    @Override
    protected com.vaadin.v7.data.Item getUnfilteredItem(@CheckForNull Object itemId) {
        return new DummyItemImplementation();
    }

    @Override
    public com.vaadin.v7.data.Property<T> getContainerProperty(@CheckForNull Object itemId,
            @CheckForNull Object propertyId) {
        throw new UnsupportedOperationException("getContainerProperty is not supported");
    }

    @Override
    @CheckForNull
    public Class<?> getType(@CheckForNull Object propertyId) {
        throw new UnsupportedOperationException("getType is not supported");
    }

    // methods from Hierarchical start here

    @Override
    @SuppressWarnings({ "unchecked" })
    public Collection<T> getChildren(Object itemId) {
        List<T> newChildren = children.computeIfAbsent((T)itemId, this::getChildrenTypesafe);
        newChildren.forEach(c -> {
            if (!containsId(c)) {
                getAllItemIds().add(c);
            }
        });
        return newChildren;
    }

    public Collection<T> getExistingChildren(T parent) {
        return children.getOrDefault(parent, Collections.emptyList());
    }

    public boolean removeExistingChildren(T item) {
        boolean result = children.remove(item) != null;
        if (result) {
            getAllItemIds().remove(item);
        }
        return result;
    }

    private List<T> getChildrenTypesafe(T parent) {
        @SuppressWarnings("unchecked")
        List<T> childRows = getHierarchicalItem(parent)
                // create new ArrayList to prevent modification
                .map(t -> (List<T>)new ArrayList<>(t.getChildRows()))
                .orElseGet(Collections::emptyList);
        childRows.forEach(child -> parents.put(child, parent));
        return childRows;
    }

    private Optional<HierarchicalRowPmo<?>> getHierarchicalItem(Object itemId) {
        return Optional.ofNullable(itemId)
                .filter(HierarchicalRowPmo.class::isInstance)
                .map(HierarchicalRowPmo.class::cast);
    }

    @CheckForNull
    @Override
    public T getParent(Object itemId) {
        return parents.get(itemId);
    }

    @Override
    public Collection<?> rootItemIds() {
        return roots;
    }

    @Override
    public boolean areChildrenAllowed(Object itemId) {
        return hasChildren(itemId);
    }

    @Override
    public boolean isRoot(Object itemId) {
        return roots.contains(itemId);
    }

    @Override
    public boolean hasChildren(Object itemId) {
        return getHierarchicalItem(itemId)
                .map(HierarchicalRowPmo::hasChildRows)
                .orElse(false);
    }

    @Override
    public boolean setParent(Object itemId, Object newParentId)
            throws UnsupportedOperationException {
        return false;
    }

    @Override
    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed)
            throws UnsupportedOperationException {
        return false;
    }

}
