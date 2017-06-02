/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.nls;

import static java.util.Objects.requireNonNull;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Default {@link NlsService} implementation using {@link ResourceBundle ResourceBundles}.
 */
public class DefaultNlsService implements NlsService {

    @Override
    public Optional<String> getString(String bundleName, String key, Locale locale) {
        try {
            return Optional.of(getResourceBundle(requireNonNull(bundleName, "bundleName must be not null"),
                                                 requireNonNull(locale, "locale must be not null"))
                                                         .getString(requireNonNull(key, "key, must be not null")));
        } catch (MissingResourceException ex) {
            return Optional.empty();
        }
    }

    /**
     * @return the {@link ResourceBundle} for the given {@link Locale} or that for the
     *         {@link Locale.ROOT} if none is found for the given locale, it's language or the
     *         {@link Locale#getDefault() default Locale}
     */
    private ResourceBundle getResourceBundle(String resource, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle(resource, locale);
        Locale bundleLocale = bundle.getLocale();
        if (!bundleLocale.equals(locale) && !locale.equals(Locale.getDefault())
                && !bundleLocale.getLanguage().equals(locale.getLanguage())) {
            bundle = ResourceBundle.getBundle(resource, Locale.ROOT);
        }
        return bundle;
    }

}
