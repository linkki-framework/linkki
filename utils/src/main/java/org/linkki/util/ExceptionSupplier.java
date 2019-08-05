/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.util;

import java.util.function.Supplier;

/**
 * Helper class that for exception suppliers to simplify usage of
 * {@link java.util.Optional#orElseThrow(Supplier)}.
 */
public final class ExceptionSupplier {

    private ExceptionSupplier() {
        // prevent instantiation
    }

    /** Returns a supplier for an {@link IllegalArgumentException} with the given message. */
    public static Supplier<IllegalArgumentException> illegalArgumentException(String message) {
        return () -> new IllegalArgumentException(message);
    }

    /**
     * Returns a supplier for an {@link IllegalArgumentException} with the given message and cause.
     */
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
