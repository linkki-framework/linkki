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

import java.util.Optional;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class OptionalPresentMatcher<T> extends TypeSafeMatcher<Optional<? extends T>> {

    private final boolean expectedPresent;

    public OptionalPresentMatcher(boolean expected) {
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
