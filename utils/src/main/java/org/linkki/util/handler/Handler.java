/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.util.handler;

import static java.util.Objects.requireNonNull;

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
     */
    default Handler andThen(Handler after) {
        requireNonNull(after, "after must not be null");
        return () -> {
            apply();
            after.apply();
        };
    }

}