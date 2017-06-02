/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.nls;

import static java.util.Objects.requireNonNull;

import java.util.Locale;
import java.util.Optional;

import org.linkki.core.ui.util.UiUtil;
import org.linkki.util.cdi.BeanInstantiator;

/**
 * Native Language Support Service for the translation of texts identified by a bundle name and key.
 * <p>
 * If you want to implement your own service, {@link NlsService#get()} uses CDI to load the
 * implementation, so you might want to replace the {@link DefaultNlsService}.
 */
@FunctionalInterface
public interface NlsService {

    /**
     * Gets a {@link Optional} String for the given key in the given locale's language.
     * {@link Optional#empty()} will be returned if the resource is missing or the key was not
     * found.
     * 
     * @param bundleName required,
     * @param key required, the key for the desired String
     * @param locale required, the locale for which we search a String
     * @return {@link Optional} localized String
     * 
     * @throws NullPointerException if {@code bundleName}, {@code key} or {@code locale} is
     *             {@code null}
     * 
     */
    Optional<String> getString(String bundleName, String key, Locale locale);

    /**
     * Gets a {@link Optional} String for the given key for the locale retrieved from
     * <code> UiUtil.getUiLocale()</code> {@link Optional#empty()} will be returned if the resource
     * is missing or the key was not found.
     * 
     * @param bundleName required, bundle name
     * @param key required, the key for the desired String
     * @return {@link Optional} localized String
     * 
     * @throws NullPointerException if {@code bundleName} or {@code key} is {@code null}
     */
    default Optional<String> getString(String bundleName, String key) {
        return getString(bundleName, key, UiUtil.getUiLocale());
    }

    /**
     * Gets a String for the given key for for the locale retrieved from
     * <code> UiUtil.getUiLocale()</code>. The given default will be returned if the resource is
     * missing or the key was not found.
     * 
     * @param bundleName required, bundle name
     * @param key required, the key for the desired String
     * @param defaultString required, default value which will be returned if the key was not found
     * @return localized String
     * 
     * @throws NullPointerException if {@code bundleName} or {@code key} is {@code null} if no
     *             String could be found and the {@code defaultString} is {@code null}
     */
    default String getString(String bundleName, String key, String defaultString) {
        return getString(bundleName, key, UiUtil.getUiLocale())
                .orElseGet(() -> requireNonNull(defaultString, "defaultString must not be null"));
    }

    /**
     * Gets a String for the given key. The given default will be returned if the resource is
     * missing or the key was not found.
     * 
     * @param bundleName required,
     * @param key required, the key for the desired String
     * @param defaultString required, default value which will be returned if key was not found
     * @param locale required, the locale for which we search a String
     * @return localized String
     * 
     * @throws NullPointerException if {@code bundleName}, {@code key} or {@code locale} is
     *             {@code null} if no String could be found and the {@code defaultString} is
     *             {@code null}
     */
    default String getString(String bundleName, String key, String defaultString, Locale locale) {
        return getString(bundleName, key, locale)
                .orElseGet(() -> requireNonNull(defaultString, "defaultString must not be null"));
    }

    /**
     * @return the {@link NlsService} implementation for the current context.
     */
    public static NlsService get() {
        return BeanInstantiator.getCDIInstance(NlsService.class, DefaultNlsService::new);
    }

}
