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

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Base class for all formatters which format {@link TemporalAccessor}-subclasses as {@link String}
 * according to the given locale.
 *
 * @param <T> type of the subclass to format
 */
public abstract class TemporalAccessorFormatter<T extends TemporalAccessor> implements Formatter<T> {

    /**
     * @param locale the {@link Locale} used for the {@link DateTimeFormatter}
     *
     * @return {@link DateTimeFormatter} used to convert {@code T} to {@link String}
     */
    protected abstract DateTimeFormatter getFormatter(Locale locale);

    /**
     * Formats the given {@link TemporalAccessor} value with the given locale.
     * 
     * @param value temporal accessor that should be formatted
     * @param locale locale that determines the format
     * @return given value as formatted String
     */
    @Override
    @CheckForNull
    public String format(@CheckForNull T value, Locale locale) {
        if (value == null) {
            return "";
        }

        return getFormatter(locale).format(value);
    }

}
