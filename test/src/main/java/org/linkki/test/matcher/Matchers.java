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

import java.util.function.Predicate;

import org.hamcrest.MatcherAssert;

public class Matchers {

    private Matchers() {
        // do not instantiate
    }

    public static OptionalPresentMatcher<Object> absent() {
        return new OptionalPresentMatcher<>(false);
    }

    public static OptionalPresentMatcher<Object> present() {
        return new OptionalPresentMatcher<>(true);
    }

    public static <T> OptionalValueMatcher<T> hasValue(T value) {
        return new OptionalValueMatcher<>(value);
    }

    public static <T> PredicateMatcher<T> matches(Predicate<T> function, String description) {
        return new PredicateMatcher<>(function, description);
    }

    public static <T> PredicateMatcher<T> matches(Predicate<T> function) {
        return new PredicateMatcher<>(function, "function that matches");
    }

    public static <T> StreamMatcher<T> allMatch(Predicate<T> predicate) {
        return StreamMatcher.allMatch(predicate);
    }

    public static <T> StreamMatcher<T> anyMatch(Predicate<T> predicate) {
        return StreamMatcher.anyMatch(predicate);
    }

    /**
     * Shortcut for {@code assertThat(condition, is(true))} for cases where condition clearly
     * indicates what it does, like {@code assertThat("".isEmpty())}
     * 
     * @param condition condition to be checked
     */
    public static void assertThat(boolean condition) {
        MatcherAssert.assertThat(condition, is(true));
    }

}
