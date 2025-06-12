/*
 * Copyright Faktor Zehn GmbH.
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
package org.linkki.util.handler;

import static java.util.Objects.requireNonNull;

import java.util.function.Consumer;

/**
 * A functional interface to proceed any operation. May be used to provide click handler or any
 * other runnable action.
 * <p>
 * Handlers can be composed using the {@link #andThen(Handler)} method.
 */
@FunctionalInterface
public interface Handler {

    /** A handler that does nothing. */
    Handler NOP_HANDLER = () -> {
        // nothing to do
    };

    /**
     * Called when the handler should be applied.
     */
    void apply();

    /**
     * Returns a composed handler that first executes this handler's {@link #apply()} method and
     * then the {@link #apply()} method of the given handler.
     * 
     * @see #compose(Handler)
     */
    default Handler andThen(Handler after) {
        requireNonNull(after, "after must not be null");
        return () -> {
            apply();
            after.apply();
        };
    }

    /**
     * Returns a composed handler that first applies the {@code before} handler, and then applies
     * this handler. If evaluation of either handler throws an exception, it is relayed to the
     * caller of the composed handler.
     *
     * @param before the handler to apply before this handler is applied
     * @return a composed handler that first applies the {@code before} handler and then applies
     *         this handler
     * @throws NullPointerException if before is null
     *
     * @see #andThen(Handler)
     */
    default Handler compose(Handler before) {
        requireNonNull(before, "before must not be null");
        return () -> {
            before.apply();
            apply();
        };
    }

    /**
     * Returns a consumer that first applies the {@code before} consumer, and then applies this
     * handler.
     *
     * @param before the consumer to execute before this handler is applied
     * @return a consumer that first applies the {@code before} consumer and then applies this
     *         handler
     * @throws NullPointerException if before is null
     *
     * @see #compose(Handler)
     */
    default <T> Consumer<T> compose(Consumer<T> before) {
        requireNonNull(before, "before must not be null");
        return before.andThen(t -> apply());
    }

}