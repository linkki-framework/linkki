/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.framework.ui.application;

import static java.util.Objects.requireNonNull;

import org.eclipse.jdt.annotation.Nullable;
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
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

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
    // applicationConfig/Layout will be non-null once configure was called
    @SuppressWarnings("null")
    @SuppressFBWarnings("NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
    protected LinkkiUi() {
        super();
    }

    // applicationConfig/Layout are set to non-null values in configure
    @SuppressWarnings("null")
    @SuppressFBWarnings("NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
    public LinkkiUi(ApplicationConfig applicationConfig) {
        configure(applicationConfig);
    }

    /**
     * This method has to be called after the default constructor is used, for example in a DI context.
     */
    protected final void configure(ApplicationConfig config) {
        this.applicationConfig = requireNonNull(config, "config must not be null");
        this.applicationLayout = applicationConfig.createApplicationLayout();
        setNavigator(config.createApplicationNavigator(this, applicationLayout));
    }

    @SuppressWarnings("null")
    @Override
    public ApplicationNavigator getNavigator() {
        return (ApplicationNavigator)super.getNavigator();
    }

    @Override
    protected void init(VaadinRequest request) {
        requireNonNull(applicationConfig,
                       "configure must be called before any other methods to set applicationConfig and applicationLayout");
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
        return getApplicationConfig().getApplicationName();
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
        return applicationLayout;
    }

    @SuppressWarnings("null")
    @Override
    public void setContent(@Nullable Component content) {
        // if applicationLayout == null, configure was not yet called and this is just the call from the
        // super constructor; Otherwise only an ApplicationLayout is accepted
        if (applicationLayout != null && !(content instanceof ApplicationLayout)) {
            throw new IllegalArgumentException("content must be an " + ApplicationLayout.class.getSimpleName());
        }
        applicationLayout = (ApplicationLayout)content;
        super.setContent(content);
    }

    /**
     * Returns the currently active {@link LinkkiUi}.
     * 
     * @return {@link UI#getCurrent()}
     */
    @SuppressWarnings("null")
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