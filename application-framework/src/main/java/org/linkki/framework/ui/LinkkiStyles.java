/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui;

import javax.annotation.Nullable;

import org.apache.deltaspike.core.api.config.ConfigResolver;

public class LinkkiStyles {


    /**
     * Prefix for message label style classes. The severity in lower case is used as suffix
     */
    public static final String MESSAGE_PREFIX = "linkki-message-"; //$NON-NLS-1$

    /**
     * Style class prefix message labels. The severity in lower case is used as suffix
     */
    public static final String MESSAGE_LIST_STYLE = "linkki-message-list"; //$NON-NLS-1$

    /**
     * Vaadin theme name. Loaded from DeltaSpike
     */

    public static final String THEME_NAME;

    /**
     * login.jsp StyleSheet href. Built from Vaadin theme
     */
    public static final String STYLESHEET;

    /**
     * login.jsp Icon href. Built from Vaadin theme
     */
    public static final String ICON;


    private static final String THEME = "theme"; //$NON-NLS-1$

    private static final String STYLES_CSS = "/styles.css"; //$NON-NLS-1$

    private static final String FAVICON_ICO = "/favicon.ico"; //$NON-NLS-1$

    private static final String VAADIN_THEMES_PREFIX = "./VAADIN/themes/"; //$NON-NLS-1$

    private static final String STYLESHEET_DS = "stylesheet"; //$NON-NLS-1$

    static {

        String themeName = ConfigResolver.getPropertyValue(THEME);
        if (themeName == null) {
            throw new RuntimeException("Wrong DeltaSpike configuration. Missed mandatory property 'theme'"); //$NON-NLS-1$
        }
        THEME_NAME = themeName;
        ICON = buildIconPath(themeName);
        STYLESHEET = buildStylesheetPath(themeName, ConfigResolver.getPropertyValue(STYLESHEET_DS));
    }

    // Class used only to define constants, it should not be instantiated
    private LinkkiStyles() {
        super();
    }


    private static String buildIconPath(String theme) {
        return VAADIN_THEMES_PREFIX + theme + FAVICON_ICO;
    }

    private static String buildStylesheetPath(String theme, @Nullable String stylesheet) {
        if (stylesheet != null) {
            return VAADIN_THEMES_PREFIX + theme + "/" + stylesheet; //$NON-NLS-1$
        } else {
            return VAADIN_THEMES_PREFIX + theme + STYLES_CSS;
        }
    }


}
