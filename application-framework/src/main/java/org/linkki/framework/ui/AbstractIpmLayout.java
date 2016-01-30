/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui;

import java.io.Serializable;

import com.google.gwt.thirdparty.guava.common.collect.Iterators;
import com.vaadin.cdi.internal.Conventions;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;

/**
 * Abstract base class for layouts in the IPM web application. Note that a IPM layout <i>is not</i>
 * a Vaadin {@link Layout} itself but <i>has</i> a layout component that is displayed as its
 * content. Furthermore a layout provides functionality to navigate between views that are displayed
 * inside the content.
 */
public abstract class AbstractIpmLayout implements Serializable {

    private static final long serialVersionUID = 1L;

    private Navigator navigator;

    /**
     * This is the Vaadin-{@link Layout} that is displayed as the content. It is the outermost UI
     * component that contains all child-components that are displayed.
     */
    private Layout content;

    /**
     * Initializes the layout for the given UI.
     * <p>
     * Can't be done in a layout's constructor as we need the other beans to be injected and also
     * the UI which is still under construction at this point in time.
     */
    public void init(UI ui) {
        content = initContent(ui);
        content.setSizeFull();
        navigator = new Navigator(ui, getMainArea());
        navigator.addProvider(getViewProvider());
    }

    /**
     * Creates and initializes the content that is displayed by this IPM layout.
     * <p>
     * Note that it is the responsibility of the implementing subclass to add the container used for
     * navigation ({@link #getMainArea()}) to the content.
     */
    protected abstract Layout initContent(UI ui);

    /**
     * Returns the container that is used for navigation between views, i.e. whose content is
     * replaced when showing a view (as in {@link #showView(Class)} or {@link #showView(String)}).
     */
    public abstract ComponentContainer getMainArea();

    /** Returns the {@link ViewProvider} to use for navigation. */
    protected abstract ViewProvider getViewProvider();

    /**
     * Returns the {@link Layout} that is displayed as the content. Make sure that the content was
     * initialized using <code>initContent(UI)</code> before calling this method.
     */
    public Layout getContent() {
        return content;
    }

    /**
     * Returns the {@link Navigator} that is used for navigation in this layout (as in
     * {@link #showView(Class)} and {@link #showView(String)}).
     */
    protected Navigator getNavigator() {
        return navigator;
    }

    /**
     * Shows the view identified by the given class, i.e. replaces the contents of
     * {@link #getMainArea()} with that view.
     */
    public Component showView(Class<?> clazz) {
        navigator.navigateTo(Conventions.deriveMappingForView(clazz));
        return getCurrentView();
    }

    /**
     * Shows the view identified by the given view name, i.e. replaces the contents of
     * {@link #getMainArea()} with that view.
     */
    public void showView(String viewName) {
        navigator.navigateTo(viewName);
    }

    /**
     * Returns the current view.
     */
    public Component getCurrentView() {
        return Iterators.getNext(getMainArea().iterator(), null);
    }
}
