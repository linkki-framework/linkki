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


package org.linkki.tooling.apt.compiler;

import static java.util.stream.Collectors.joining;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class ErrorMessageMatcher<T> extends BaseMatcher<T> {


    private final Collection<String> stringSnippets;
    private Function<String, Boolean> matches;

    public ErrorMessageMatcher(Collection<String> stringSnippets, Function<String, Boolean> matches) {
        this.stringSnippets = stringSnippets;
        this.matches = matches;
    }

    public static <T> Matcher<T> containsAny(Collection<String> stringSnippets) {
        return new ErrorMessageMatcher<T>(stringSnippets, string -> stringSnippets.stream().anyMatch(string::contains));
    }

    public static <T> Matcher<T> containsAll(Collection<String> stringSnippets) {
        return new ErrorMessageMatcher<T>(stringSnippets, string -> stringSnippets.stream().allMatch(string::contains));
    }

    public static <T> Matcher<T> containsNone(Collection<String> stringSnippets) {
        return new ErrorMessageMatcher<T>(stringSnippets,
                string -> stringSnippets.stream().noneMatch(string::contains));
    }


    @Override
    public boolean matches(Object obj) {
        return Objects.nonNull(obj) ? matches.apply(obj.toString()) : false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(stringSnippets.stream().collect(joining(" | ")));
    }

}
