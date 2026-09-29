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
package org.linkki.test.matcher;

import static org.hamcrest.Matchers.is;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.hamcrest.MatcherAssert;

/**
 * A utility class providing a series of matchers.
 * 
 * @deprecated Use other already defined alternatives in f10-commons-test or custom matchers
 *             instead.
 */
@Deprecated(since = "2.11.0")
public class Matchers {

    private Matchers() {
        // do not instantiate
    }

    /**
     * Creates a matcher that checks for {@link Optional#isEmpty()}.
     * 
     * @return a matcher that checks for {@link Optional#isEmpty()}.
     * @deprecated Use de.faktorzehn.commons.test.matcher.Matchers.absent() instead.
     */
    @Deprecated(since = "2.11.0")
    public static OptionalPresentMatcher<Object> absent() {
        return new OptionalPresentMatcher<>(false);
    }

    /**
     * Creates a matcher that checks for {@link Optional#isPresent()}.
     *
     * @return a matcher that checks for {@link Optional#isPresent()}.
     * @deprecated Use de.faktorzehn.commons.test.matcher.Matchers.present() instead.
     */
    @Deprecated(since = "2.11.0")
    public static OptionalPresentMatcher<Object> present() {
        return new OptionalPresentMatcher<>(true);
    }

    /**
     * Creates a matcher that checks for {@link Optional} to have a certain value.
     *
     * @return a matcher that checks for the {@link Optional} to have a certain value.
     * @deprecated Use de.faktorzehn.commons.test.matcher.Matchers.hasValue(T) instead.
     */
    @Deprecated(since = "2.11.0")
    public static <T> OptionalValueMatcher<T> hasValue(T value) {
        return new OptionalValueMatcher<>(value);
    }

    /**
     * A matcher that uses a predicate.
     *
     * @param function the predicate.
     * @param description the description.
     * @return a matcher that uses a predicate.
     * @param <T> the type of value
     * @deprecated Use de.faktorzehn.commons.test.matcher.Matchers.matches(Predicate) instead.
     */
    @Deprecated(since = "2.11.0")
    public static <T> PredicateMatcher<T> matches(Predicate<T> function, String description) {
        return new PredicateMatcher<>(function, description);
    }

    /**
     * A matcher that uses a predicate.
     * 
     * @param function the predicate.
     * @return a matcher that uses a predicate.
     * @param <T> the type of value
     * @deprecated Use de.faktorzehn.commons.test.matcher.Matchers.matches(Predicate) instead.
     */
    @Deprecated(since = "2.11.0")
    public static <T> PredicateMatcher<T> matches(Predicate<T> function) {
        return new PredicateMatcher<>(function, "function that matches");
    }

    /**
     * Creates a matcher which is testing a predicate using {@link Stream#allMatch(Predicate)}.
     *
     * @param predicate the predicate.
     * @return a matcher for streams.
     * @param <T> item
     * @deprecated Use de.faktorzehn.commons.test.matcher.Matchers.allMatch(Predicate) instead.
     */
    @Deprecated(since = "2.11.0")
    public static <T> StreamMatcher<T> allMatch(Predicate<T> predicate) {
        return StreamMatcher.allMatch(predicate);
    }

    /**
     * Creates a matcher which is testing a predicate using {@link Stream#anyMatch(Predicate)}.
     * 
     * @param predicate the predicate.
     * @return a matcher for streams.
     * @param <T> item
     * @deprecated Use de.faktorzehn.commons.test.matcher.Matchers.anyMatch(Predicate) instead.
     */
    @Deprecated(since = "2.11.0")
    public static <T> StreamMatcher<T> anyMatch(Predicate<T> predicate) {
        return StreamMatcher.anyMatch(predicate);
    }

    /**
     * Shortcut for {@code assertThat(condition, is(true))} for cases where condition clearly
     * indicates what it does, like {@code assertThat("".isEmpty())}
     * 
     * @param condition condition to be checked
     * @deprecated Use {@code MatcherAssert.assertThat(condition, is(true))} instead.
     */
    @Deprecated(since = "2.11.0")
    public static void assertThat(boolean condition) {
        MatcherAssert.assertThat(condition, is(true));
    }

}
