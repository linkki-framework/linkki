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
package org.linkki.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;

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
     * Returns a new {@link Sequence} concatenated with the given elements. This {@link Sequence} is not
     * affected.
     * 
     * @param newElements the new elements that should be concatenated
     * @return a new sequence with all elements of this {@link Sequence} concatenated with the new
     *         elements
     */
    @CheckReturnValue
    public Sequence<T> with(Collection<T> newElements) {
        ArrayList<T> collection = new ArrayList<>(list);
        collection.addAll(newElements);
        return new Sequence<>(collection);
    }

    /**
     * Returns a new {@link Sequence} concatenated with the given elements. This {@link Sequence} is not
     * affected.
     * 
     * @param newElements the new elements that should be concatenated
     * @return a new sequence with all elements of this {@link Sequence} concatenated with the new
     *         elements
     */
    @CheckReturnValue
    @SafeVarargs
    public final Sequence<T> with(T... newElements) {
        return with(Arrays.asList(newElements));
    }


    /**
     * Returns a new {@link Sequence} concatenated with the elements produced by the given
     * {@link Supplier Suppliers} if the condition ist {@code true}. This {@link Sequence} is not
     * affected.
     * 
     * @param suppliers the suppliers for new elements that should be concatenated
     * @return a new sequence with all elements of this {@link Sequence} concatenated with the new
     *         elements or this {@link Sequence} if the condition is {@code false}
     */
    @SafeVarargs
    @CheckReturnValue
    public final Sequence<T> withIf(boolean condition, Supplier<T>... suppliers) {
        if (condition) {
            return with(Arrays.stream(suppliers).map(Supplier::get).collect(Collectors.toList()));
        } else {
            return this;
        }
    }

    /**
     * Returns a new {@link Sequence} concatenated with the element produced by the given
     * {@link Supplier} if the condition ist {@code true}. This {@link Sequence} is not affected.
     * 
     * @param supplier the supplier for a new element that should be concatenated
     * @return a new sequence with all elements of this {@link Sequence} concatenated with the new
     *         elements or this {@link Sequence} if the condition is {@code false}
     */
    @CheckReturnValue
    public final Sequence<T> withIf(boolean condition, Supplier<T> supplier) {
        if (condition) {
            return with(supplier.get());
        } else {
            return this;
        }
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

    /**
     * Creates a {@link Collector} that collects a {@link Stream}'s elements into a {@link Sequence}.
     */
    public static <T> Collector<T, ?, Sequence<T>> collect() {
        return new SequenceCollector<T>();
    }

    private static final class SequenceCollector<T> implements Collector<T, List<T>, Sequence<T>> {

        SequenceCollector() {
        }

        @Override
        public Supplier<List<T>> supplier() {
            return LinkedList::new;
        }

        @Override
        public BiConsumer<List<T>, T> accumulator() {
            return List::add;
        }

        @Override
        public BinaryOperator<List<T>> combiner() {
            return (l1, l2) -> {
                l1.addAll(l2);
                return l1;
            };
        }

        @Override
        public Function<List<T>, Sequence<T>> finisher() {
            return Sequence::of;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.emptySet();
        }
    }

}
