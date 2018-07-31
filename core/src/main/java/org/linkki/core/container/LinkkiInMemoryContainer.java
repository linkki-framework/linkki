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

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.linkki.core.ui.table.HierarchicalRowPmo;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractInMemoryContainer;

/**
 * An in-memory container which doesn't do any reflection magic. This container simply stores the
 * Objects.
 */
public class LinkkiInMemoryContainer<T> extends AbstractInMemoryContainer<T, Object, Item>
        implements Container.Hierarchical {

    private static final long serialVersionUID = -1708252890035638419L;

    // Use a weak reference to remove mappings of children that have been removed because they are no
    // longer referenced by their former parent. Their bindings are removed automatically when the
    // corresponding Components are detached after their parent's binding was updated.
    private Map<T, T> parents = new WeakHashMap<>();
    private Map<T, List<T>> children = new WeakHashMap<>();

    @Override
    public Collection<?> getContainerPropertyIds() {
        return Collections.emptyList();
    }

    public void setItems(Collection<T> items) {
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

    @Override
    protected Item getUnfilteredItem(@Nullable Object itemId) {
        throw new UnsupportedOperationException("getUnfilteredItem is not supported");
    }

    @Override
    @CheckForNull
    public Property<T> getContainerProperty(@Nullable Object itemId, @Nullable Object propertyId) {
        throw new UnsupportedOperationException("getContainerProperty is not supported");
    }

    @Override
    @CheckForNull
    public Class<?> getType(@Nullable Object propertyId) {
        throw new UnsupportedOperationException("getType is not supported");
    }

    // methods from Hierarchical start here

    @Override
    @SuppressWarnings("unchecked")
    public Collection<T> getChildren(@SuppressWarnings("null") Object itemId) {
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

    @CheckForNull
    @Override
    public T getParent(@SuppressWarnings("null") Object itemId) {
        return parents.get(itemId);
    }

    @Override
    public Collection<?> rootItemIds() {
        return getItemIds();
    }

    @Override
    public boolean areChildrenAllowed(@SuppressWarnings("null") Object itemId) {
        return hasChildren(itemId);
    }

    @Override
    public boolean isRoot(@SuppressWarnings("null") Object itemId) {
        return containsId(itemId);
    }

    @Override
    public boolean hasChildren(@SuppressWarnings("null") Object itemId) {
        return getHierarchicalItem(itemId)
                .map(HierarchicalRowPmo::hasChildRows)
                .orElse(false);
    }

    @Override
    public boolean setParent(@SuppressWarnings("null") Object itemId, @Nullable Object newParentId)
            throws UnsupportedOperationException {
        return false;
    }

    @Override
    public boolean setChildrenAllowed(@SuppressWarnings("null") Object itemId, boolean areChildrenAllowed)
            throws UnsupportedOperationException {
        return false;
    }

}
