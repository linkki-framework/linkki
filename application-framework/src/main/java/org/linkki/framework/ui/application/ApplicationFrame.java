package org.linkki.framework.ui.application;

import java.io.Serializable;

import javax.inject.Inject;

import com.google.gwt.thirdparty.guava.common.collect.Iterators;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.cdi.UIScoped;
import com.vaadin.cdi.internal.Conventions;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * OVerall layout frame for the application. Contains the application header, the main work area and
 * a footer.
 */
@UIScoped
public class ApplicationFrame implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * This is the layout that contains all content. It is the outermost UI component that contains
     * all child-components that are displayed.
     */
    private VerticalLayout content;

    /** The header that is displayed at the top. */
    @Inject
    private ApplicationHeader header;

    /**
     * The main work area displayed in the center. This component takes up most of the available
     * space. All views that are displayed using {@link ApplicationFrame#showView(Class)} are added
     * as child-components to it.
     */
    private VerticalLayout mainArea;

    /** The footer that is displayed at the bottom. */
    @Inject
    private ApplicationFooter footer;

    @Inject
    private CDIViewProvider viewProvider;

    private Navigator navigator;

    public ApplicationFrame() {
        super();
    }

    /**
     * Initializes the layout for the given UI.
     * <p>
     * Can't be done in a layout's constructor as we need the other beans to be injected and also
     * the UI which is still under construction at this point in time.
     */
    public void init(UI ui) {
        content = new VerticalLayout();
        content.setMargin(false);
        content.setSizeFull();

        // Header
        content.addComponent(header);
        header.init();

        // Main area
        mainArea = new VerticalLayout();
        mainArea.setSizeFull();
        content.addComponent(mainArea);
        content.setExpandRatio(mainArea, 1);

        // Footer
        content.addComponent(footer);

        navigator = new Navigator(ui, mainArea);
        navigator.addProvider(viewProvider);
    }

    /**
     * Returns the vertical layout that displays all content. Make sure that it was initialized
     * using {@link #init(UI)} method before calling this method.
     */
    public VerticalLayout getContent() {
        return content;
    }

    /**
     * Attempts to show a view of the given class.
     * <p>
     * Note that navigation to a view might not be performed if there is a
     * {@link com.vaadin.navigator.ViewChangeListener ViewChangeListener} that prohibits the
     * navigation. Thus there is no guarantee that a view of the given class is displayed after this
     * method returns.
     */
    public <T extends View> void showView(Class<T> clazz) {
        navigator.navigateTo(Conventions.deriveMappingForView(clazz));
    }

    /**
     * Returns the view that is currently displayed. Returns {@code null} if no view is displayed
     * (yet).
     */
    public Component getCurrentView() {
        return Iterators.getNext(mainArea.iterator(), null);
    }

}
