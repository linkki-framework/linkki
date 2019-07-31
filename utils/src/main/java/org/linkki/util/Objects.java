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

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Provides {@code static} utility methods that delegate to {@link java.util.Objects} and are
 * additionally annotated with SpotBugs {@code null} annotations.
 */
public final class Objects {

    private Objects() {
        // prevent instantiation
    }

    /**
     * Checks that the specified object reference is not {@code null} and throws a customized
     * {@link NullPointerException} if it is. This method is designed primarily for doing parameter
     * validation in methods and constructors with multiple parameters, as demonstrated below:
     * 
     * <code>
     * public Foo(Bar bar, Baz baz) {
     *     this.bar = Objects.requireNonNull(bar, "bar must not be null");
     *     this.baz = Objects.requireNonNull(baz, "baz must not be null");
     * }
     * </code>
     *
     * Wrapper for {@link java.util.Objects#requireNonNull(Object, String)} that is annotated with
     * SpotBugs annotations to correctly handle the null case.
     * <p>
     * Will be removed when issue https://github.com/spotbugs/spotbugs/issues/456 is fixed
     *
     * @param obj the object reference to check for nullity
     * @param message detail message to be used in the event that a {@code
     *                NullPointerException} is thrown
     * @param <T> the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    @SuppressFBWarnings(value = "NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE", justification = "that's the trick of this method")
    @NonNull
    public static <T> T requireNonNull(@CheckForNull T obj, @NonNull String message) {
        return java.util.Objects.requireNonNull(obj, message);
    }

    /**
     * Checks that the specified object reference is not {@code null} and throws a customized
     * {@link NullPointerException} if it is.
     *
     * <p>
     * Unlike the method {@link #requireNonNull(Object, String)}, this method allows creation of the
     * message to be deferred until after the null check is made. While this may confer a performance
     * advantage in the non-null case, when deciding to call this method care should be taken that the
     * costs of creating the message supplier are less than the cost of just creating the string message
     * directly.
     *
     * Wrapper for {@link java.util.Objects#requireNonNull(Object, Supplier)} that is annotated with
     * SpotBugs annotations to correctly handle the null case.
     * <p>
     * Will be removed when issue https://github.com/spotbugs/spotbugs/issues/456 is fixed
     *
     * @param obj the object reference to check for nullity
     * @param messageSupplier detail message supplier to be used in the event that a {@code
     *                NullPointerException} is thrown
     * @param <T> the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    @SuppressFBWarnings(value = "NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE", justification = "that's the trick of this method")
    @NonNull
    public static <T> T requireNonNull(@CheckForNull T obj, @NonNull Supplier<String> messageSupplier) {
        return java.util.Objects.requireNonNull(obj, messageSupplier);
    }
}
