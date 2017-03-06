/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.util;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import javax.annotation.CheckForNull;

/**
 * Key-value store that initializes its values lazily. Values will be initialized using the
 * initializer function the {@link LazyInitializingMap map} is created with.
 *
 * @param <K> the type of key stored in this map
 * @param <V> the type of values stored in this map
 *
 */
public class LazyInitializingMap<K, V> {

    private final Map<K, V> internalMap = new ConcurrentHashMap<>();
    private final Function<K, V> initializer;

    /**
     *
     * @param initializer the initializer function for lazily creating values. This function throws
     *            a {@link NullPointerException} when given <code>null</code>. Returns non-
     *            <code>null</code> values otherwise.
     */
    public LazyInitializingMap(Function<K, V> initializer) {
        this.initializer = requireNonNull(initializer, "initializer must not be null");
    }

    /**
     * Returns the value the given key maps to. Creates a new value if none is present using this
     * map's initializer. Never returns <code>null</code>.
     * <p>
     * Use as replacement for the usual "get or initialize and get" code:
     *
     * <pre>
     * if (!contains(key)) {
     *     initValue(key);
     * }
     * return get(key);
     * </pre>
     *
     * @throws NullPointerException if either the key is <code>null</code>, or if the initializer
     *             function violates its contract and returns <code>null</code>.
     */
    public V get(K key) {

        V value = internalMap.computeIfAbsent(key, initializer);
        if (value == null) {
            throw new NullPointerException("Initializer must not create a null value");
        }
        return value;
    }

    /**
     * Returns the value the given key maps to. Returns <code>null</code> if the key does not map to
     * a value.
     */
    @CheckForNull
    public V getIfPresent(K key) {
        return internalMap.get(key);
    }

    /**
     * Clears this map.
     */
    public void clear() {
        internalMap.clear();
    }

    /**
     * Removes the key from the map. Returns the removed value or <code>null</code> if the key does
     * not map to a value.
     * 
     * @see Map#remove(Object)
     */
    @CheckForNull
    public V remove(K key) {
        return internalMap.remove(key);
    }
}
