/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.messages;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.linkki.core.ui.util.UiUtil;

public class Messages {
    private static final String BUNDLE_NAME = "org.linkki.framework.ui.messages.messages"; //$NON-NLS-1$

    private Messages() {
        // do not instantiate
    }

    public static String getString(String key) {
        try {
            return getResourceBundle(BUNDLE_NAME, UiUtil.getUiLocale()).getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    private static ResourceBundle getResourceBundle(String resource, Locale locale) {

        ResourceBundle bundle = ResourceBundle.getBundle(resource, locale);

        Locale bundleLocale = bundle.getLocale();

        if (!bundleLocale.equals(locale) && !locale.equals(Locale.getDefault())
                && !bundleLocale.getLanguage().equals(locale.getLanguage())) {
            bundle = ResourceBundle.getBundle(resource, Locale.ROOT);
        }

        return bundle;
    }
}
