package org.linkki.framework.ui.application;

import javax.inject.Inject;

import org.linkki.framework.ui.AbstractIpmLayout;

import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.cdi.UIScoped;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Application overall screen layout. Contains the application menu, a footer and the main work
 * area.
 */
@UIScoped
public class ApplicationLayout extends AbstractIpmLayout {

    private static final long serialVersionUID = 1L;

    private VerticalLayout content;
    private VerticalLayout mainArea;

    @Inject
    private CDIViewProvider viewProvider;

    @Inject
    private ApplicationHeader header;

    @Inject
    private ApplicationFooter footer;

    public ApplicationLayout() {
    }

    @Override
    protected VerticalLayout initContent(UI ui) {
        content = new VerticalLayout();
        content.setMargin(false);

        // Header
        content.addComponent(header);
        header.init(this);

        // Main area
        mainArea = new VerticalLayout();
        content.addComponent(mainArea);
        mainArea.setSizeFull();
        content.setExpandRatio(mainArea, 1);

        // Footer
        content.addComponent(footer);
        return content;
    }

    @Override
    public VerticalLayout getMainArea() {
        return mainArea;
    }

    @Override
    protected ViewProvider getViewProvider() {
        return viewProvider;
    }

}
