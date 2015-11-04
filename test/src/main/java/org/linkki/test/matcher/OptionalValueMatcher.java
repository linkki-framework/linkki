/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.test.matcher;

import java.util.Objects;
import java.util.Optional;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class OptionalValueMatcher<T> extends TypeSafeMatcher<Optional<? extends T>> {

    private T expectedValue;

    public OptionalValueMatcher(T expectedValue) {
        this.expectedValue = expectedValue;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expectedValue);
    }

    @Override
    protected boolean matchesSafely(Optional<? extends T> item) {
        return Objects.equals(item.get(), expectedValue);
    }

}
