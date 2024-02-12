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
package org.linkki.core.defaults.nls;

import static java.util.Objects.requireNonNull;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

import org.linkki.core.nls.NlsService;

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
     *         {@link Locale#ROOT} if none is found for the given locale, it's language or the
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
