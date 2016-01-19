/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.test.matcher;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.function.Predicate;

import org.faktorips.runtime.ObjectProperty;
import org.hamcrest.Matcher;

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

    public static EmptyMessageListMatcher emptyMessageList() {
        return new EmptyMessageListMatcher();
    }

    public static MessageListSizeMatcher hasSize(int size) {
        return new MessageListSizeMatcher(equalTo(size));
    }

    public static MessageListSizeMatcher hasSize(Matcher<Integer> intMatcher) {
        return new MessageListSizeMatcher(intMatcher);
    }

    public static MessageListObjectPropertyMatcher hasMessageFor(Object o, String property) {
        return new MessageListObjectPropertyMatcher(new ObjectProperty(o, property));
    }

    public static MessageListObjectPropertyMatcher hasMessagesFor(int count, Object o, String property) {
        return new MessageListObjectPropertyMatcher(new ObjectProperty(o, property), count);
    }

}
