/*
 * Copyright Faktor Zehn AG.
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
