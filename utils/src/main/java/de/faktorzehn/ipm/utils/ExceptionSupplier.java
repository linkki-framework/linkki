/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.utils;

import java.util.function.Supplier;

/**
 * Helper class that for exception suppliers to simplify usage of
 * {@link java.util.Optional#orElseThrow(Supplier)}.
 */
public final class ExceptionSupplier {

    // Helper class that should not be initialized
    private ExceptionSupplier() {
        super();
    }

    /** Returns a supplier for an {@link IllegalArgumentException} with the given message. */
    public static Supplier<IllegalArgumentException> illegalArgumentException(String message) {
        return () -> new IllegalArgumentException(message);
    }

    /** Returns a supplier for an {@link IllegalArgumentException} with the given message and cause. */
    public static Supplier<IllegalArgumentException> illegalArgumentException(String message, Throwable cause) {
        return () -> new IllegalArgumentException(message, cause);
    }

    /** Returns a supplier for an {@link IllegalStateException} with the given message. */
    public static Supplier<IllegalStateException> illegalStateException(String message) {
        return () -> new IllegalStateException(message);
    }

    /** Returns a supplier for an {@link IllegalStateException} with the given message and cause. */
    public static Supplier<IllegalStateException> illegalStateException(String message, Throwable cause) {
        return () -> new IllegalStateException(message, cause);
    }
}
