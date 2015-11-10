/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.test.matcher;

import java.util.function.Predicate;
import java.util.stream.Stream;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class StreamMatcher<T> extends TypeSafeMatcher<Stream<T>> {

    private final Predicate<T> predicate;
    private final boolean allMatch;

    private StreamMatcher(Predicate<T> predicate, boolean allMatch) {
        super();
        this.predicate = predicate;
        this.allMatch = allMatch;
    }

    @Override
    public void describeTo(Description description) {
        if (allMatch) {
            description.appendText("a Stream whose items all match " + predicate);
        } else {
            description.appendText("a Stream with any item matching " + predicate);
        }

    }

    @Override
    protected boolean matchesSafely(Stream<T> s) {
        if (allMatch) {
            return s.allMatch(predicate);
        } else {
            return s.anyMatch(predicate);
        }
    }

    public static <T> StreamMatcher<T> allMatch(Predicate<T> predicate) {
        return new StreamMatcher<>(predicate, true);
    }

    public static <T> StreamMatcher<T> anyMatch(Predicate<T> predicate) {
        return new StreamMatcher<>(predicate, false);
    }

}
