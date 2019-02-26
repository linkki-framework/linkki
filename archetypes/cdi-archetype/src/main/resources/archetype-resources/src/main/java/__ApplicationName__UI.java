package \${package};
/*******************************************************************************
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 * 
 * All Rights Reserved - Alle Rechte vorbehalten.
 *******************************************************************************/

import org.linkki.framework.ui.application.LinkkiUi;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

import ${package}.view.MainView;

/**
 * Base class for {@link UI} implementations.
 */
@SuppressWarnings("serial")
@Theme("\${theme-name}")
@CDIUI(\${ApplicationName}UI.URL)
@PreserveOnRefresh
public class \${ApplicationName}UI extends LinkkiUi implements ViewDisplay {

    public static final String URL = ""; //$NON-NLS-1$

    public \${ApplicationName}UI() {
        super(new \${ApplicationName}ApplicationConfig());
    }

    @Override
    protected void init(VaadinRequest request) {
        super.init(request);
        addView(MainView.NAME, MainView.class);
    }

    @Override
    public void showView(View view) {
        getApplicationLayout().showView(view);
    }

}