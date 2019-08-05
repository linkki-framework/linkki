/*******************************************************************************
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 * 
 * All Rights Reserved - Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.util;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Utility class for {@link Optional}.
 */
public final class Optionals {

    private Optionals() {
        // prevent instantiation
    }

    /**
     * In anticipation of Java 9 Optional#ifPresentOrElse.
     * <p>
     * If a value is present in the given optional, performs the given action with the value, otherwise
     * performs the given empty-based action.
     * 
     * @param optional an {@link Optional}
     * @param action the action to be performed, if a value is present
     * @param emptyAction the empty-based action to be performed, if no value is present
     * @throws NullPointerException if a value is present and the given action is null, or no value is
     *             present and the given empty-based action is null.
     */
    public static <T> void ifPresentOrElse(Optional<T> optional,
            Consumer<? super T> action,
            Runnable emptyAction) {
        if (optional.isPresent()) {
            action.accept(optional.get());
        } else {
            emptyAction.run();
        }
    }

    /**
     * In anticipation of Java 9 Optional#stream.
     * <p>
     * If a value is present, returns a sequential Stream containing only that value, otherwise returns
     * an empty Stream.
     * <p>
     * <b>API Note:</b> This method can be used to transform a Stream of optional elements to a Stream
     * of present value elements:
     * 
     * <pre>
     *  
     * Stream&lt;Optional&lt;T&gt;&gt; os = .. 
     * Stream&lt;T&gt; s = os.flatMap(Optional::stream)
     * </pre>
     * 
     * 
     * @return the optional value as a Stream
     */
    public static <T> Stream<T> stream(Optional<T> o) {
        return o.map(Stream::of).orElseGet(Stream::empty);
    }


    /**
     * In anticipation of Java 9 Optional#or.
     * 
     * Creates an {@link Either} wrapper around the given {@link Optional} on which
     * {@link Either#or(Supplier)} can be called.
     */
    public static <T> Either<T> either(Optional<T> o) {
        return new Either<>(o);
    }

    /**
     * Wrapper around an {@link Optional} that offers Java 9's Optional#or method.
     */
    public static class Either<T> {
        private final Optional<T> thiz;

        Either(Optional<T> o) {
            this.thiz = requireNonNull(o, "o must not be null");
        }

        /**
         * If a value is present, returns an {@code Optional} describing the value, otherwise returns an
         * {@code Optional} produced by the supplying function.
         *
         * @param supplier the supplying function that produces an {@code Optional} to be returned
         * @return returns an {@code Optional} describing the value of this {@code Optional}, if a value
         *         is present, otherwise an {@code Optional} produced by the supplying function.
         * @throws NullPointerException if the supplying function is {@code null} or produces a
         *             {@code null} result
         */
        public Optional<T> or(Supplier<? extends Optional<? extends T>> supplier) {
            Objects.requireNonNull(supplier);
            if (thiz.isPresent()) {
                return thiz;
            } else {
                @SuppressWarnings("unchecked")
                Optional<T> r = (Optional<T>)supplier.get();
                return Objects.requireNonNull(r);
            }
        }
    }
}