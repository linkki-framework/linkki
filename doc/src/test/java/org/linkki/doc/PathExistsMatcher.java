package org.linkki.doc;

import java.nio.file.Path;

import org.eclipse.jdt.annotation.NonNull;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

class PathExistsMatcher extends TypeSafeMatcher<Path> {

    public static Matcher<Path> exists() {
        return new PathExistsMatcher();
    }

    @Override
    public void describeTo(@NonNull Description description) {
        description.appendText("an existing file");
    }

    @Override
    protected boolean matchesSafely(Path item) {
        return item.toFile().exists();
    }
}