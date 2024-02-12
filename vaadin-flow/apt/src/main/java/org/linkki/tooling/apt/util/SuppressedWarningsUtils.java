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

package org.linkki.tooling.apt.util;

import static java.util.Arrays.asList;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

public final class SuppressedWarningsUtils {

    private static final Set<String> SUPPORTED_SUPPRESSABLE_WARNINGS = new HashSet<>(asList("all", "linkki"));

    private SuppressedWarningsUtils() {
        // util
    }

    public static boolean isSuppressed(Element element) {
        SuppressWarnings annotation = element.getAnnotation(SuppressWarnings.class);
        return annotation != null
                && Stream.of(annotation.value()).anyMatch(SUPPORTED_SUPPRESSABLE_WARNINGS::contains);
    }

    public static boolean isSuppressed(Element element, Kind kind) {
        return kind == Kind.WARNING && isSuppressed(element);
    }

}