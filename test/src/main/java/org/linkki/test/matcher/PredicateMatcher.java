/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.test.matcher;

import java.util.function.Predicate;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class PredicateMatcher<T> extends TypeSafeMatcher<T> {

    private final Predicate<T> predicate;

    private final String description;

    public PredicateMatcher(Predicate<T> predicate, String description) {
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