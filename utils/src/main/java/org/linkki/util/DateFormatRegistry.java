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
package org.linkki.util;

import static java.util.Objects.requireNonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;

/**
 * A registry for date format patterns to use instead of the rather unsuitable default patterns provided
 * by {@link DateFormat#getDateInstance(int, Locale)}.
 * 
 * @deprecated since 2019-02-26. Use the static {@link DateFormats} instead.
 */
@Deprecated
public class DateFormatRegistry {

    /**
     * @deprecated use {@link DateFormats#PATTERN_ISO} instead
     */
    @Deprecated
    public static final String PATTERN_ISO = "yyyy-MM-dd";
    /**
     * @deprecated use {@link DateFormats#PATTERN_DE} instead
     */
    @Deprecated
    public static final String PATTERN_DE = "dd.MM.yyyy";

    private final Map<String, @Nullable String> languagePatterns = new HashMap<>();

    {
        languagePatterns.put(Locale.GERMAN.getLanguage(), PATTERN_DE);
    }

    /**
     * Returns the registered pattern if existent or a default pattern obtained from
     * {@link DateFormat#getDateInstance(int, Locale)} with style {@link DateFormat#SHORT}.
     * 
     * @deprecated use the static {@link DateFormats#getPattern(Locale)} instead
     */
    @Deprecated
    public String getPattern(Locale locale) {
        requireNonNull(locale, "locale must not be null");

        String registeredPattern = languagePatterns.get(locale.getLanguage());
        if (registeredPattern != null) {
            return registeredPattern;
        } else {
            return defaultLocalePattern(locale);
        }
    }

    private String defaultLocalePattern(Locale locale) {
        requireNonNull(locale, "locale must not be null");

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        if (dateFormat instanceof SimpleDateFormat) {
            return ((SimpleDateFormat)dateFormat).toPattern();
        } else {
            // Use some sensible fallback in case the JDK implementation changes. Ahem.
            return PATTERN_ISO;
        }
    }
}
