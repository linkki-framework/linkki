/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.linkki.framework.ui.component.SidebarComposite;

public final class LinkkiStyles {


    /** Prefix for message label style classes. The errorLevel in lower case is used as suffix */
    public static final String MESSAGE_PREFIX = "linkki-message-"; //$NON-NLS-1$

    /** Style class prefix message labels. The errorLevel in lower case is used as suffix */
    public static final String MESSAGE_LIST_STYLE = "linkki-message-list"; //$NON-NLS-1$

    /** Style class for {@link SidebarComposite} */
    public static final String SIDEBAR_COMPOSITE = "linkki-sidebar-container"; //$NON-NLS-1$

    /** Style class for the sidebar of {@link SidebarComposite} */
    public static final String SIDEBAR = "linkki-sidebar"; //$NON-NLS-1$

    /** Style class for selected icons in the sidebar of {@link SidebarComposite} */
    public static final String SIDEBAR_SELECTED = "selected"; //$NON-NLS-1$

    /** Style class for the content in {@link SidebarComposite} */
    public static final String SIDEBAR_CONTENT = "linkki-sidebar-content"; //$NON-NLS-1$

    /**
     * Vaadin theme name. Loaded from DeltaSpike
     */
    public static final String THEME_NAME;

    /**
     * login.jsp StyleSheet href. Built from Vaadin theme
     */
    public static final String STYLESHEET;

    /**
     * login.jsp icon href. Built from Vaadin theme
     */
    public static final String ICON;

    private static final String FAVICON_ICO = "/favicon.ico"; //$NON-NLS-1$

    private static final String VAADIN_THEMES_PREFIX = "./VAADIN/themes/"; //$NON-NLS-1$

    /**
     * Key of deltaspike property for style sheet href
     */
    private static final String STYLESHEET_KEY = "stylesheet"; //$NON-NLS-1$

    /**
     * Key of deltaspike property for theme name
     */
    private static final String THEME_NAME_KEY = "theme"; //$NON-NLS-1$

    static {

        String themeName = ConfigResolver.getPropertyValue(THEME_NAME_KEY);
        if (themeName == null) {
            throw new RuntimeException("Wrong DeltaSpike configuration. Missed mandatory property 'theme'"); //$NON-NLS-1$
        }
        THEME_NAME = themeName;
        ICON = buildIconPath(themeName);
        STYLESHEET = buildStylesheetPath(themeName);
    }

    private LinkkiStyles() {
        // Class used only to define constants, it should not be instantiated
    }


    private static String buildIconPath(String theme) {
        return VAADIN_THEMES_PREFIX + theme + FAVICON_ICO;
    }

    private static String buildStylesheetPath(String theme) {
        return VAADIN_THEMES_PREFIX + theme + "/" + ConfigResolver.getPropertyValue(STYLESHEET_KEY, "styles.css"); //$NON-NLS-1$
    }


}
