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
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A registry for date format patterns that changes some rather unsuitable default patterns provided by
 * {@link DateFormat#getDateInstance(int, Locale)} such as the German "dd.MM.yyyy" instead of
 * "dd.MM.yy".
 * <p>
 * The date pattern for a given locale is determined using the following order:
 * <ul>
 * <li>Registered patterns for the locale</li>
 * <li>Registered patterns for the language</li>
 * <li>The {@link DateFormat#SHORT short date pattern} obtained from the JDK</li>
 * <li>The ISO date pattern</li>
 * </ul>
 */
public final class DateFormats {

    public static final String PATTERN_ISO = "yyyy-MM-dd";
    public static final String PATTERN_EN = "MM/dd/yyyy";
    public static final String PATTERN_DE = "dd.MM.yyyy";

    public static final String PATTERN_EN_US = "MM/dd/yyyy";
    public static final String PATTERN_EN_GB = "dd/MM/yyyy";

    private static final Map<String, String> LANGUAGE_PATTERNS = new HashMap<>();
    private static final Map<Locale, String> LOCALE_PATTERNS = new HashMap<>();

    static {
        LANGUAGE_PATTERNS.put(Locale.ENGLISH.getLanguage(), PATTERN_EN);
        LANGUAGE_PATTERNS.put(Locale.GERMAN.getLanguage(), PATTERN_DE);

        LOCALE_PATTERNS.put(Locale.US, PATTERN_EN_US);
        LOCALE_PATTERNS.put(Locale.UK, PATTERN_EN_GB);
    }

    private DateFormats() {
        // prevent instantiation
    }

    /**
     * Registers the given pattern for the given language. The language pattern is used as a fallback if
     * no pattern has been explicitly defined for a locale.
     * <p>
     * To register a pattern for a specific locale use {@link #register(Locale, String)}.
     * 
     * @param languageCode a ISO 639 language code, as obtainable from {@link Locale#getLanguage()}
     * @param pattern a pattern for {@link DateTimeFormatter}
     */
    public static void register(String languageCode, String pattern) {
        LANGUAGE_PATTERNS.put(languageCode, pattern);
    }

    /**
     * Registers the given pattern for the given locale.
     * <p>
     * To register a pattern for a language use {@link #register(String, String)}.
     * 
     * @param locale the locale
     * @param pattern a pattern for {@link DateTimeFormatter}
     */
    public static void register(Locale locale, String pattern) {
        LOCALE_PATTERNS.put(locale, pattern);
    }

    /**
     * Returns the date pattern for the given locale. It is determined using the following order:
     * <ul>
     * <li>Registered patterns for the locale</li>
     * <li>Registered patterns for the language</li>
     * <li>The {@link DateFormat#SHORT short date pattern} obtained from the JDK</li>
     * <li>The ISO date pattern</li>
     * </ul>
     */
    public static String getPattern(Locale locale) {
        requireNonNull(locale, "locale must not be null");

        if (LOCALE_PATTERNS.containsKey(locale)) {
            return LOCALE_PATTERNS.get(locale);
        } else if (LANGUAGE_PATTERNS.containsKey(locale.getLanguage())) {
            return LANGUAGE_PATTERNS.get(locale.getLanguage());
        } else {
            return defaultLocalePattern(locale);
        }
    }

    private static String defaultLocalePattern(Locale locale) {
        requireNonNull(locale, "locale must not be null");

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        if (dateFormat instanceof SimpleDateFormat) {
            String pattern = ((SimpleDateFormat)dateFormat).toPattern();
            // for unknown locales, a "short ISO" is returned - we want the long version
            return "y-MM-dd".equals(pattern) ? DateFormats.PATTERN_ISO : pattern;
        } else {
            // Use some sensible fallback in case the JDK implementation changes. Ahem.
            return PATTERN_ISO;
        }
    }
}
