package org.linkki.framework.ui.application;

import java.io.Serializable;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ui.converters.LinkkiConverterFactory;
import org.linkki.util.StreamUtil;

import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.cdi.UIScoped;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.cdi.internal.Conventions;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Overall layout frame for the application. Contains the application header, the main work area and
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
        // init converters
        VaadinSession vaadinSession = VaadinSession.getCurrent();
        if (vaadinSession != null) {
            vaadinSession.setConverterFactory(new LinkkiConverterFactory());
        }

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

        navigator = createNavigator(ui, mainArea);
        navigator.addProvider(getViewProvider());
    }

    protected Navigator createNavigator(UI ui, ComponentContainer componentContainer) {
        return new CdiFixNavigator(ui, componentContainer);
    }

    protected VerticalLayout getMainArea() {
        return mainArea;
    }

    protected ViewProvider getViewProvider() {
        return viewProvider;
    }

    protected Navigator getNavigator() {
        return navigator;
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
        showView(clazz, StringUtils.EMPTY);
    }

    /**
     * Attempts to show a view of the given class, appending pathAndParameters to the URL to provide
     * information to the called view.
     * <p>
     * Note that navigation to a view might not be performed if there is a
     * {@link com.vaadin.navigator.ViewChangeListener ViewChangeListener} that prohibits the
     * navigation. Thus there is no guarantee that a view of the given class is displayed after this
     * method returns.
     * 
     * @param pathAndParameters a string containing an URL-Like path as well as URL parameters. Must
     *            not start with &quot;/&quot;. Example:
     *            &quot;part1/part2/arg1=23&amp;arg2=42&quot;.
     */
    public <T extends View> void showView(Class<T> clazz, String pathAndParameters) {
        String newViewName = Conventions.deriveMappingForView(clazz);
        String newFragment = newViewName + "/" + pathAndParameters;
        navigator.navigateTo(newFragment);
    }

    /**
     * Returns the view that is currently displayed if there is any.
     */
    public Optional<Component> getCurrentView() {
        return StreamUtil.stream(mainArea).findFirst();
    }

    /**
     * Navigates to the same view as the current view. The complete URL will be preserved. This will
     * clear all objects in {@link ViewScoped} of dependency injection but will keep the
     * {@link UIScoped}. As long as all your state is in {@link UIScoped} objects you can call this
     * method to reload the whole UI while keeping current state.
     * <p>
     * Note: You will get two {@link UriFragmentChangedEvent} because we switch to the
     * {@link EmptyCdiView} and back to the current view!
     */
    public void refreshCurrentView() {
        getNavigator().navigateTo(getNavigator().getState());
    }

}
