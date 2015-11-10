/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.test.matcher;

import java.util.function.Predicate;

public class Matchers {

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

}
