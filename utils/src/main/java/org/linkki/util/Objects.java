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

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class Objects {

    private Objects() {
        // do not instantiate
    }

    /**
     * Checks that the specified object reference is not {@code null} and throws a customized
     * {@link NullPointerException} if it is. This method is designed primarily for doing parameter
     * validation in methods and constructors with multiple parameters, as demonstrated below:
     * <blockquote>
     * 
     * <pre>
     * public Foo(Bar bar, Baz baz) {
     *     this.bar = Objects.requireNonNull(bar, "bar must not be null");
     *     this.baz = Objects.requireNonNull(baz, "baz must not be null");
     * }
     * </pre>
     * 
     * </blockquote>
     *
     * Wrapper for {@link java.util.Objects#requireNonNull(Object)} that is annotated with spotbugs
     * annotations to correctly handle the null case.
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
    @SuppressFBWarnings(value = "NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE", justification = "thats the trick of this methos")
    @NonNull
    public static <T> T requireNonNull(@Nullable @CheckForNull T obj, String message) {
        return java.util.Objects.requireNonNull(obj, message);
    }

}
