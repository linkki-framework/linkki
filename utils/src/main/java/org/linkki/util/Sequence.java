/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * This sequence is a wrapper for a list to create immutable lists easily. It could be instantiated
 * using the static of-methods or the {@link #empty()} method.
 * <p>
 * If you already have such a {@link Sequence} object you could create a new {@link Sequence}
 * concatenated with additional objects using the with-methods.
 * <p>
 * To allow access to the elements of this sequence it implements the {@link Iterable} interface and
 * provides methods like {@link #list()} or {@link #stream()} to access the internal list.
 */
public class Sequence<T> implements Iterable<T> {

    private final List<T> list;

    private Sequence() {
        this.list = Collections.emptyList();
    }

    private Sequence(Collection<T> collection) {
        this.list = Collections.unmodifiableList(new ArrayList<>(collection));
    }

    /**
     * Creates a new {@link Sequence} with the elements of the given {@link Collection}.
     * 
     * @param elements the elements that should be part of this sequence
     * 
     * @return the new {@link Sequence} with the given elements
     */
    public static <T> Sequence<T> of(Collection<T> elements) {
        return new Sequence<T>(elements);
    }

    /**
     * Creates a new {@link Sequence} with the given elements.
     * 
     * @param elements the elements that should be part of this sequence
     * 
     * @return the new {@link Sequence} with the given elements
     */
    @SafeVarargs
    public static <T> Sequence<T> of(T... elements) {
        return new Sequence<T>(Arrays.asList(elements));
    }

    /**
     * Creates an empty {@link Sequence}.
     * 
     * @return an empty {@link Sequence}
     */
    public static <T> Sequence<T> empty() {
        return new Sequence<T>();
    }

    /**
     * Returns a new {@link Sequence} concatenated with the given elements. This {@link Sequence} is
     * not affected.
     * 
     * @param newElements the new elements that should be concatenated
     * @return a new sequence with all elements of this {@link Sequence} concatenated with the new
     *         elements
     */
    public Sequence<T> with(Collection<T> newElements) {
        ArrayList<T> collection = new ArrayList<>(list);
        collection.addAll(newElements);
        return new Sequence<>(collection);
    }

    /**
     * Returns a new {@link Sequence} concatenated with the given elements. This {@link Sequence} is
     * not affected.
     * 
     * @param newElements the new elements that should be concatenated
     * @return a new sequence with all elements of this {@link Sequence} concatenated with the new
     *         elements
     */
    @SafeVarargs
    public final Sequence<T> with(T... newElements) {
        return with(Arrays.asList(newElements));
    }

    /**
     * Returns the internal list of this {@link Sequence}.
     * 
     * @return a list containing all elements of this sequence
     */
    public List<T> list() {
        return list;
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    /**
     * Directly access the stream of the list that contains all the elements of this sequence.
     * 
     * @return the {@link Stream} of {@link #list()}
     */
    public Stream<T> stream() {
        return list.stream();
    }

}
