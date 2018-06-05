/*
 * Copyright Faktor Zehn AG.
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

import javax.annotation.Nullable;

import org.linkki.framework.state.ApplicationConfig;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * A basic {@link UI} implementation for linkki that uses an {@link ApplicationLayout} and
 * {@link ApplicationNavigator}.
 *
 */
public abstract class LinkkiUi extends UI {

    private static final long serialVersionUID = 1L;

    private ApplicationConfig applicationConfig;

    private ApplicationNavigator applicationNavigator;

    private ApplicationLayout applicationLayout;

    /**
     * Default constructor for dependency injection. Make sure to call
     * {@link #configure(ApplicationConfig)} before the UI is initialized.
     */
    @SuppressWarnings("null")
    protected LinkkiUi() {
        // do nothing, only for subclasses that are instantiated by injection
    }

    @SuppressWarnings("null")
    public LinkkiUi(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
        configure(applicationConfig);
    }

    /**
     * Have to be called when the default constructor is used, for example in DI context.
     */
    public final void configure(ApplicationConfig config) {
        this.applicationConfig = config;
        this.applicationLayout = applicationConfig.getApplicationLayoutBuilder().build();
        this.applicationNavigator = applicationConfig.createApplicationNavigator(this, applicationLayout);
    }

    protected void addView(String viewName, View view) {
        getNavigator().addView(viewName, view);
    }

    protected void addProvider(ViewProvider provider) {
        getNavigator().addProvider(provider);
    }

    @Override
    protected void init(VaadinRequest request) {
        Page.getCurrent().setTitle(getPageTitle());

        setContent(applicationLayout);
    }

    protected String getPageTitle() {
        return applicationConfig.getApplicationName();
    }

    @Override
    protected void refresh(@Nullable VaadinRequest request) {
        super.refresh(request);
        applicationNavigator.refreshCurrentView();
    }

    public ApplicationLayout getApplicationLayout() {
        return (ApplicationLayout)getContent();
    }

    public ApplicationNavigator getApplicationNavigator() {
        return applicationNavigator;
    }

    public static LinkkiUi getCurrent() {
        return (LinkkiUi)UI.getCurrent();
    }

    public static ApplicationNavigator getCurrentApplicationNavigator() {
        return getCurrent().getApplicationNavigator();
    }

    public static ApplicationLayout getCurrentApplicationLayout() {
        return getCurrent().getApplicationLayout();
    }

}
