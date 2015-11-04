package org.linkki.framework.ui.application;

import javax.inject.Inject;

import org.linkki.framework.state.ApplicationConfig;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * The footer of the application displaying information about the application, version and
 * copyright.
 */
@UIScoped
public class ApplicationFooter extends HorizontalLayout {

    private static final long serialVersionUID = 1L;

    @Inject
    public void init(ApplicationConfig config) {
        setMargin(false);
        addComponent(new Label(buildText(config)));
    }

    /**
     * Returns the text for the footer. May be overwritten by subclasses.
     * 
     * @param config The current application configuration
     * @return The text that is displayed in the footer.
     */
    protected String buildText(ApplicationConfig config) {
        return config.getApplicationName() + ", " + config.getApplicationVersion() + ", " + config.getCopyright();
    }

}
