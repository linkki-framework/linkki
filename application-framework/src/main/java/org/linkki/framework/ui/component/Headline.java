/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.component;


import org.linkki.framework.ui.LinkkiStyles;

import com.vaadin.server.Responsive;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A component that renders a header with a headline caption. It is styled using the stylesheet
 * class name {@link LinkkiStyles#HEADLINE}.
 * <p>
 * This class is intended to be subclassed to add additional components.
 */
public class Headline extends CustomComponent {

    private static final long serialVersionUID = 1L;

    private final HorizontalLayout headerLayout = new HorizontalLayout();

    private final Label policyInfoLabel = new Label();

    public Headline() {
        super();
        setWidth("100%"); //$NON-NLS-1$
        setHeightUndefined();
        initHeaderLayout();
    }

    public Headline(String text) {
        this();
        setHeadline(text);
    }

    protected void initHeaderLayout() {
        setCompositionRoot(headerLayout);
        headerLayout.addStyleName(LinkkiStyles.HEADLINE);
        headerLayout.setWidth("100%"); //$NON-NLS-1$
        headerLayout.setHeightUndefined();
        headerLayout.setSpacing(true);

        Responsive.makeResponsive(headerLayout);

        getPolicyInfoLabel().setWidthUndefined();
        getPolicyInfoLabel().addStyleName(ValoTheme.LABEL_HUGE);
        headerLayout.addComponent(getPolicyInfoLabel());
    }

    protected HorizontalLayout getHeaderLayout() {
        return headerLayout;
    }

    protected Label getPolicyInfoLabel() {
        return policyInfoLabel;
    }

    public void setHeadline(String text) {
        getPolicyInfoLabel().setValue(text);
    }

}