/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

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
        this.initializer = initializer;
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
        if (!internalMap.containsKey(key)) {
            init(key);
        }
        return getIfPresent(key);
    }

    private void init(K key) {
        internalMap.put(key, initializer.apply(key));
    }

    /**
     * Returns the value the given key maps to. Returns <code>null</code> if the key does not map to
     * a value.
     */
    public V getIfPresent(K key) {
        return internalMap.get(key);
    }

    /**
     * Removes the key from the map.
     * 
     * @see Map#remove(Object)
     */
    public V remove(K key) {
        return internalMap.remove(key);
    }
}
