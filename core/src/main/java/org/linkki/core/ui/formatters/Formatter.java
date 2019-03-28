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

package org.linkki.core.ui.formatters;

import java.util.Locale;

import org.linkki.core.ui.UiFramework;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * A {@link Formatter} is used to convert a Value of type {@code <T>} to a {@link String}.
 * 
 * @param <T> any class with a {@link Object#toString() toString()} method not suitable for the intended
 *            situation
 */
@FunctionalInterface
public interface Formatter<T> {

    /**
     * Formats the given value with the given locale.
     * 
     * @param value value that should be formatted
     * @param locale locale that determines the format
     * @return given value as formatted String
     */
    @CheckForNull
    String format(@CheckForNull T value, Locale locale);

    /**
     * Formats the given value with the locale derived from {@link UiFramework#getLocale()}.
     * 
     * @param value value that should be formatted
     * @return given value as formatted String
     */
    @CheckForNull
    public default String format(@CheckForNull T value) {
        return format(value, UiFramework.getLocale());
    }
}
