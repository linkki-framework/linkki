/*
 * Copyright Faktor Zehn AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.core.container;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.linkki.core.ui.table.HierarchicalRowPmo;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractInMemoryContainer;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

/**
 * An in-memory container which doesn't do any reflection magic. This container simply stores the
 * Objects.
 */
public class LinkkiInMemoryContainer<@NonNull T> extends AbstractInMemoryContainer<T, Object, Item>
        implements Container.Hierarchical {

    private static final long serialVersionUID = -1708252890035638419L;

    // Use a weak reference to remove mappings of children that have been removed because they are
    // no longer referenced by their former parent. Their bindings are removed automatically when
    // the corresponding Components are detached after their parent's binding was updated.
    private Map<T, T> parents = new WeakHashMap<>();
    private Map<T, List<T>> children = new WeakHashMap<>();

    @Override
    public Collection<?> getContainerPropertyIds() {
        return Collections.emptyList();
    }

    /**
     * Replaces this container's contents with the given items.
     * 
     * @param items a collection of items
     */
    public void setItems(Collection<? extends T> items) {
        requireNonNull(items, "items must not be null");

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
        return true;
    }

    /**
     * @deprecated Since July 26, 2018. Adding new items to a list of existing items is no longer
     *             supported. Call {@link #setItems(Collection)} to specify the new list of items.
     */
    @Deprecated
    public void addAllItems(Collection<T> items) {
        getAllItemIds().addAll(items);
        fireItemSetChange();
    }

    /**
     * Returns a {@link DummyItemImplementation}.
     * <p>
     * Note that this method should not be needed. The <code>itemId</code> should be already a row PMO.
     * However, this method cannot return <code>null</code> or throw an exception as otherwise it would
     * not be possible to add any {@link ItemClickListener} to the table.
     */
    @Override
    protected Item getUnfilteredItem(@Nullable Object itemId) {
        return new DummyItemImplementation();
    }

    @Override
    @Nullable
    public Property<T> getContainerProperty(@Nullable Object itemId, @Nullable Object propertyId) {
        throw new UnsupportedOperationException("getContainerProperty is not supported");
    }

    @Override
    @Nullable
    public Class<?> getType(@Nullable Object propertyId) {
        throw new UnsupportedOperationException("getType is not supported");
    }

    // methods from Hierarchical start here

    @Override
    @SuppressWarnings({ "unchecked" })
    public Collection<T> getChildren(Object itemId) {
        return children.computeIfAbsent((T)itemId, this::getChildrenTypesafe);
    }

    public Collection<T> getExistingChildren(T parent) {
        return children.getOrDefault(parent, Collections.emptyList());
    }

    public boolean removeExistingChildren(T item) {
        return children.remove(item) != null;
    }

    private List<T> getChildrenTypesafe(T parent) {
        @SuppressWarnings("unchecked")
        List<T> childRows = getHierarchicalItem(parent)
                .map(t -> (List<T>)t.getChildRows())
                .orElseGet(Collections::emptyList);
        childRows.forEach(child -> parents.put(child, parent));
        return childRows;
    }

    private Optional<HierarchicalRowPmo<?>> getHierarchicalItem(Object itemId) {
        return Optional.ofNullable(itemId)
                .filter(HierarchicalRowPmo.class::isInstance)
                .map(HierarchicalRowPmo.class::cast);
    }

    @Nullable
    @Override
    public T getParent(Object itemId) {
        return parents.get(itemId);
    }

    @Override
    public Collection<?> rootItemIds() {
        return getItemIds();
    }

    @Override
    public boolean areChildrenAllowed(Object itemId) {
        return hasChildren(itemId);
    }

    @Override
    public boolean isRoot(Object itemId) {
        return containsId(itemId);
    }

    @Override
    public boolean hasChildren(Object itemId) {
        return getHierarchicalItem(itemId)
                .map(HierarchicalRowPmo::hasChildRows)
                .orElse(false);
    }

    @Override
    public boolean setParent(Object itemId, @Nullable Object newParentId)
            throws UnsupportedOperationException {
        return false;
    }

    @Override
    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed)
            throws UnsupportedOperationException {
        return false;
    }

}
