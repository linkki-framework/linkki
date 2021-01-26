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

import static org.linkki.util.Objects.requireNonNull;

import org.linkki.core.ui.converters.LinkkiConverterRegistry;
import org.linkki.framework.state.ApplicationConfig;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.Router;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinSession;

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
     * {@link #configure(ApplicationConfig)} after instantiation before the UI is initialized.
     */
    @SuppressFBWarnings(value = "NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR", justification = "applicationConfig/Layout will be non-null once configure was called")
    protected LinkkiUi() {
        super();
    }

    @SuppressFBWarnings(value = "NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR", justification = "applicationConfig/Layout will be non-null once configure was called")
    public LinkkiUi(ApplicationConfig applicationConfig) {
        configure(applicationConfig);
    }

    /**
     * This method has to be called after the default constructor is used, for example in a DI context.
     */
    protected final void configure(ApplicationConfig config) {
        this.applicationConfig = requireNonNull(config, "config must not be null");
    }

    /**
     * {@inheritDoc}
     * <p>
     * The {@link ApplicationLayout} and {@link LinkkiConverterRegistry} are created by the
     * {@link ApplicationConfig}. Thus, adjust the {@link ApplicationConfig} implementation for
     * customization.
     */
    // TODO LIN-2203 Error Dialog Handler umstellen
    // The {@link ErrorHandler} and the {@link Navigator} can be customized by overriding the methods
    // {@link #configureErrorHandler()} and {@link #configureNavigator(ViewDisplay)} respectively.
    @Override
    protected void init(VaadinRequest request) {
        requireNonNull(applicationConfig,
                       "configure must be called before executing any initialization to set an ApplicationConfig");

        this.applicationLayout = applicationConfig.createApplicationLayout();

        // TODO LIN-2203 Error Dialog Handler umstellen
        // configureErrorHandler();

        // init converters
        VaadinSession vaadinSession = VaadinSession.getCurrent();
        if (vaadinSession != null) {
            vaadinSession.setAttribute(LinkkiConverterRegistry.class,
                                       applicationConfig.getConverterRegistry());
            // TODO LIN-2203 Error Dialog Handler umstellen
            // vaadinSession.setErrorHandler(getErrorHandler());
        }

        getUI().ifPresent(ui -> ui.getPage().setTitle(getPageTitle()));
    }

    // TODO LIN-2203 Error Dialog Handler umstellen
    // /**
    // * Configures the error handling. Override to use a different {@link ErrorHandler}.
    // *
    // * @implSpec This method should call {@link #setErrorHandler(ErrorHandler)} with an instance of
    // * {@link ErrorHandler} that may be created and/or configured here.
    // * @implNote linkki uses a {@link DialogErrorHandler} creating a new {@link DefaultErrorDialog} by
    // * default.
    // */
    // protected void configureErrorHandler() {
    // setErrorHandler(createErrorHandler());
    // }
    //
    // /**
    // * @deprecated since 1.1. Create and set the error handler in {@link #configureErrorHandler()}
    // * instead.
    // */
    // @Deprecated
    // protected ErrorHandler createErrorHandler() {
    // return new DialogErrorHandler(getNavigator(), DefaultErrorDialog::new);
    // }

    // TODO LIN-2205 Navigator auf Router umstellen
    // /**
    // * Configures the navigator for this UI. The navigator should use the given
    // * <code>applicationLayout</code> as {@link ViewDisplay}.
    // *
    // * @implSpec This method should call {@link #setNavigator(Navigator)} with a matching
    // * {@link Navigator} instance. If you use CDI, you should inject an instance of
    // * {@code CdiNavigator}, for Spring autowire a {@code SpringNavigator}, calling
    // * {@code init(this, applicationLayout)} in this method before
    // * {@link #setNavigator(Navigator)} in both cases.
    // */
    // @SuppressWarnings("deprecation")
    // protected void configureNavigator(RouterLayout applicationLayout) {
    // setNavigator(applicationConfig.createApplicationNavigator(this,
    // (ApplicationLayout)applicationLayout));
    // }
    //
    // /**
    // *
    // * Add a view to the configured navigator.
    // *
    // * @implNote This method must be called after the navigator is configured either by
    // * {@link #LinkkiUi(ApplicationConfig)} constructor or by calling
    // * {@link #configure(ApplicationConfig)}.
    // *
    // * @param viewName the name of the view that represents the view in the URL
    // * @param viewClass the type of the {@link View}, will be instantiated by default constructor
    // *
    // * @see Navigator#addView(String, View)
    // */
    // protected void addView(String viewName, Class<? extends View> viewClass) {
    // getNavigator().addView(viewName, viewClass);
    // }
    //
    // /**
    // * Add a view provider to the configured navigator.
    // *
    // * @implNote This method must be called after the navigator is configured by
    // * {@link #configureNavigator(ViewDisplay)}.
    // *
    // * @param provider the view provider that should be configured
    // *
    // * @see Navigator#addProvider(ViewProvider)
    // */
    // protected void addProvider(View provider) {
    // getNavigator().addProvider(provider);
    // }

    /**
     * The page title for @see {@link Page#setTitle(String)}.
     * 
     * @return The page title that should be displayed on top of the page.
     */
    protected String getPageTitle() {
        return getApplicationConfig().getApplicationName();
    }

    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    public ApplicationLayout getApplicationLayout() {
        return applicationLayout;
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
     * Returns the {@link Router} of the current UI.
     *
     * @return Router of {@link #getCurrent()}.
     */
    public static Router getCurrentRouter() {
        return getCurrent().getRouter();
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