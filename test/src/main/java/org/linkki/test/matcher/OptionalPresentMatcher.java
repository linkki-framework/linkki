/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.test.matcher;

import java.util.Optional;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class OptionalPresentMatcher<T> extends TypeSafeMatcher<Optional<? extends T>> {

    private final boolean expectedPresent;

    OptionalPresentMatcher(boolean expected) {
        this.expectedPresent = expected;
    }

    @Override
    public void describeTo(Description description) {
        if (expectedPresent) {
            description.appendText("<Present>");
        } else {
            description.appendText("<Absent>");
        }
    }

    @Override
    protected boolean matchesSafely(Optional<? extends T> item) {
        return item.isPresent() == expectedPresent;
    }

}
