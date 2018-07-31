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
import org.linkki.framework.ui.dialogs.DefaultErrorDialog;
import org.linkki.framework.ui.dialogs.DialogErrorHandler;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

/**
 * A base {@link UI} implementation for linkki used to configure the application. Subclasses should
 * provide the {@link ApplicationConfig} in their constructor.
 */
public class LinkkiUi extends UI {

    private static final long serialVersionUID = 1L;

    private ApplicationConfig applicationConfig;

    private ApplicationLayout applicationLayout;

    /**
     * Default constructor for dependency injection. Make sure to call
     * {@link #configure(ApplicationConfig)} before the UI is initialized.
     */
    @SuppressWarnings("null")
    protected LinkkiUi() {
        super();
    }

    @SuppressWarnings("null")
    public LinkkiUi(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
        configure(applicationConfig);
    }

    /**
     * Have to be called when the default constructor is used, for example in DI context.
     */
    protected final void configure(ApplicationConfig config) {
        this.applicationConfig = config;
        this.applicationLayout = applicationConfig.createApplicationLayout();
        setNavigator(applicationConfig.createApplicationNavigator(this, applicationLayout));
    }

    @Override
    public ApplicationNavigator getNavigator() {
        return (ApplicationNavigator)super.getNavigator();
    }

    @Override
    protected void init(@Nullable VaadinRequest request) {
        setErrorHandler(createErrorHandler());
        // init converters
        VaadinSession vaadinSession = VaadinSession.getCurrent();
        if (vaadinSession != null) {
            vaadinSession.setConverterFactory(applicationConfig.getConverterFactory());
            vaadinSession.setErrorHandler(getErrorHandler());
        }

        Page.getCurrent().setTitle(getPageTitle());

        setContent(applicationLayout);
    }

    protected ErrorHandler createErrorHandler() {
        return new DialogErrorHandler(getNavigator(), DefaultErrorDialog::new);
    }

    /**
     * Add a view to the configured navigator.
     * 
     * @implNote This method must be called after the navigator is configured either by
     *           {@link #LinkkiUi(ApplicationConfig)} constructor or by calling
     *           {@link #configure(ApplicationConfig)}.
     * 
     * @param viewName the name of the view that represents the view in the URL
     * @param viewClass the type of the {@link View}, will be instantiated by default constructor
     * 
     * @see Navigator#addView(String, View)
     */
    protected void addView(String viewName, Class<? extends View> viewClass) {
        getNavigator().addView(viewName, viewClass);
    }

    /**
     * Add a view provider to the configured navigator.
     * 
     * @implNote This method must be called after the navigator is configured either by
     *           {@link #LinkkiUi(ApplicationConfig)} constructor or by calling
     *           {@link #configure(ApplicationConfig)}.
     * 
     * @param provider the view provider that should be configured
     * 
     * @see Navigator#addProvider(ViewProvider)
     */
    protected void addProvider(ViewProvider provider) {
        getNavigator().addProvider(provider);
    }

    /**
     * The page title for @see {@link Page#setTitle(String)}.
     * 
     * @return The page title that should be displayed on top of the page.
     */
    protected String getPageTitle() {
        return applicationConfig.getApplicationName();
    }

    @Override
    protected void refresh(@Nullable VaadinRequest request) {
        super.refresh(request);
        getNavigator().refreshCurrentView();
    }

    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    public ApplicationLayout getApplicationLayout() {
        return (ApplicationLayout)getContent();
    }

    /**
     * Returns the currently active {@link LinkkiUi}.
     * 
     * @return {@link UI#getCurrent()}
     */
    public static LinkkiUi getCurrent() {
        return (LinkkiUi)UI.getCurrent();
    }

    /**
     * Returns the application's configuration of the current UI.
     * 
     * @return the application configuration of {@link #getCurrent()}.
     */
    public static ApplicationConfig getCurrentApplicationConfig() {
        return getCurrent().getApplicationConfig();
    }

    /**
     * Returns the navigator of the current UI.
     * 
     * @return Navigator of {@link #getCurrent()}.
     */
    public static ApplicationNavigator getCurrentApplicationNavigator() {
        return getCurrent().getNavigator();
    }

    /**
     * Returns the navigator of the current {@link ApplicationLayout}.
     * 
     * @return The application layout of {@link #getCurrent()}.
     */
    public static ApplicationLayout getCurrentApplicationLayout() {
        return getCurrent().getApplicationLayout();
    }
}