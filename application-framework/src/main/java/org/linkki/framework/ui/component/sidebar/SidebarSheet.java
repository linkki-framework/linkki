/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.component.sidebar;

import static java.util.Objects.requireNonNull;

import org.apache.commons.lang3.StringUtils;
import org.linkki.framework.ui.LinkkiStyles;
import org.linkki.util.handler.Handler;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * Wrapper class for a component that is displayed in a {@link SidebarLayout}.
 * <p>
 * This consists of a {@link Component} that is shown in the content area and metadata including an
 * {@link Resource button} for the side bar and a tooltip. Additionally, a {@link Handler} must be
 * given that is called when the sheet is added.
 */
public class SidebarSheet {

    private final Button button;
    private final Component content;
    private final String tooltip;

    public SidebarSheet(Resource icon, Component content, String tooltip) {
        this.button = new Button("", requireNonNull(icon, "icon must not be null")); // $NON-NLS-1
        this.content = requireNonNull(content, "content must not be null");
        this.tooltip = requireNonNull(tooltip, "tooltip must not be null");
        if (StringUtils.isNotEmpty(tooltip)) {
            this.button.setDescription(tooltip);
        }
    }

    public Button getButton() {
        return button;
    }

    public Component getContent() {
        return content;
    }

    public String getToolTip() {
        return tooltip;
    }

    void select() {
        getContent().setVisible(true);
        getButton().addStyleName(LinkkiStyles.SIDEBAR_SELECTED);
    }

    void unselect() {
        getButton().removeStyleName(LinkkiStyles.SIDEBAR_SELECTED);
        getContent().setVisible(false);
    }
}