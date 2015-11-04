package org.linkki.test.matcher;

import java.util.function.Predicate;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class PredicateMatcher<T> extends TypeSafeMatcher<T> {

    private final Predicate<T> predicate;

    private final String description;

    PredicateMatcher(Predicate<T> predicate, String description) {
        this.predicate = predicate;
        this.description = description;
    }

    @Override
    protected boolean matchesSafely(T item) {
        return predicate.test(item);
    }

    @Override
    public void describeTo(Description descriptionResult) {
        descriptionResult.appendText(this.description);
    }

}