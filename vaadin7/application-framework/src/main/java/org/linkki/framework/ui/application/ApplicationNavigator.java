/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.linkki.framework.ui.application;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.UI;

/**
 * Extends {@link Navigator} with some convenient methods.
 */
public class ApplicationNavigator extends Navigator {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor used by injection framework. Make sure to call
     * {@link #init(UI, ApplicationLayout)} afterwards.
     */
    protected ApplicationNavigator() {
        super();
    }

    /**
     * Creates a new {@link ApplicationNavigator} with the specified {@link UI},
     * {@link ApplicationLayout} and {@link ViewProvider}.
     */
    public ApplicationNavigator(UI ui, ApplicationLayout applicationLayout) {
        super(ui, (ViewDisplay)applicationLayout);
    }

    /**
     * Creates a new {@link ApplicationNavigator} with the specified {@link UI},
     * {@link ApplicationLayout} and {@link ViewProvider}.
     */
    public ApplicationNavigator(UI ui, ApplicationLayout applicationLayout, ViewProvider viewProvider) {
        this(ui, applicationLayout);
        addProvider(viewProvider);
    }

    protected void init(UI ui, ApplicationLayout applicationLayout) {
        init(ui, new UriFragmentManager(ui.getPage()), applicationLayout);
    }

    /**
     * Attempts to show a view of the given name.
     * <p>
     * Navigation to a view might not be performed if there is a
     * {@link com.vaadin.navigator.ViewChangeListener ViewChangeListener} that prohibits the
     * navigation. Thus there is no guarantee that a view of the given name is displayed after this
     * method returns.
     * 
     * @param newViewName the name of the view, may be derived from view class using a convention
     *            utility
     */
    public <T extends View> void showView(String newViewName) {
        showView(newViewName, StringUtils.EMPTY);
    }

    /**
     * Attempts to show a view of the given class, appending pathAndParameters to the URL to provide
     * information to the called view.
     * <p>
     * Navigation to a view might not be performed if there is a
     * {@link com.vaadin.navigator.ViewChangeListener ViewChangeListener} that prohibits the
     * navigation. Thus there is no guarantee that a view of the given class is displayed after this
     * method returns.
     * <p>
     * If you use any extension framework like vaadin-cdi or vaadin-spring there might be a helpful
     * convention class to get the view name from your view class, for example:
     * 
     * <pre>
     * Conventions.deriveMappingForView(viewClass)
     * </pre>
     * 
     * @param newViewName the name of the view, may be derived from the view class using a
     *            convention utility
     * @param pathAndParameters A string containing a URL-like path as well as URL parameters. Must
     *            not start with &quot;/&quot;. Example:
     *            &quot;part1/part2/arg1=23&amp;arg2=42&quot;.
     */
    public <T extends View> void showView(String newViewName, String pathAndParameters) {
        String newFragment = newViewName + "/" + pathAndParameters;
        navigateTo(newFragment);
    }

    /**
     * Navigates to the same view as the current view. The complete URL will be preserved.
     */
    public void refreshCurrentView() {
        navigateTo(getState());
    }

}