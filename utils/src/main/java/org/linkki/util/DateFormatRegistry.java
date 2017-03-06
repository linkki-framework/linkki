/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.util;

import static java.util.Objects.requireNonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A registry for date format patterns to use instead of the rather unsuitable default patterns
 * provided by {@link DateFormat#getDateInstance(int, Locale)}.
 */
public class DateFormatRegistry {

    public static final String PATTERN_ISO = "yyyy-MM-dd";
    public static final String PATTERN_DE = "dd.MM.yyyy";

    private final Map<String, String> languagePatterns = new HashMap<>();

    {
        languagePatterns.put(Locale.GERMAN.getLanguage(), PATTERN_DE);
    }

    /**
     * Returns the registered pattern if existent or a default pattern obtained from
     * {@link DateFormat#getDateInstance(int, Locale)} with style {@link DateFormat#SHORT}.
     */
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
