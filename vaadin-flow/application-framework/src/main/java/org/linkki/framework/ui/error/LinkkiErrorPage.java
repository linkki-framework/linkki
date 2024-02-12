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
package org.linkki.framework.ui.error;

import java.io.PrintWriter;
import java.io.Serial;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.nls.NlsService;
import org.linkki.core.vaadin.component.base.LinkkiText;
import org.linkki.framework.ui.nls.NlsText;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.internal.DefaultErrorHandler;
import com.vaadin.flow.server.HttpStatusCode;
import com.vaadin.flow.server.VaadinService;

/**
 * Represents a default error page to manage and display {@link Exception exceptions}.
 * <p>
 * By definition, an error message is shown together with the timestamp. Furthermore, a button is
 * offered to navigate to the start view. In development mode, the exception stacktrace is shown. In
 * general, no critical technical details will be shown to the end user. The described components
 * can be overwritten individually while the page layout is fixed. Use {@link ParentLayout} in a
 * subclass to adjust this page to the general application layout.
 * <p>
 * To use this page, it must be found by the Vaadin or the Spring Boot package scan. To use the
 * Spring Boot scan, a subclass must be annotated with {@code @Component}.
 */
@CssImport("./styles/error-page.css")
@DefaultErrorHandler
public class LinkkiErrorPage extends VerticalLayout
        implements HasErrorParameter<Exception>, HasDynamicTitle {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String BUNDLE_NAME = "org/linkki/framework/ui/nls/messages";
    private static final String MSG_KEY_GO_TO_START_VIEW = "ErrorPage.GoToStartView";
    private static final String MSG_KEY_PAGE_TITLE = "ErrorPage.Title";
    private static final String MSG_KEY_TAB_NAME = "ErrorPage.Tab.Name";
    private static final String MSG_KEY_DEFAULT_ERROR_MESSAGE = "ErrorPage.Default.Message";

    private static final Logger LOGGER = Logger.getLogger(LinkkiErrorPage.class.getName());

    private final Div messageWrapper;

    /**
     * Constructs a {@link LinkkiErrorPage} including its general layout.
     */
    public LinkkiErrorPage() {
        setId(getClass().getSimpleName());
        var contentWrapper = createContentWrapper();
        messageWrapper = new Div();
        messageWrapper.addClassName("error-page-message");
        contentWrapper.add(messageWrapper);
    }

    /**
     * Sets the title shown in the browser tab.
     * 
     * @apiNote Override this to customize the title.
     */
    @Override
    public String getPageTitle() {
        return NlsText.getString(MSG_KEY_TAB_NAME);
    }

    /**
     * Processes the given error parameter and updates the content of the error page to display
     * relevant details about the occurred exception.
     * <p>
     * By default, the error page consists of the following elements in the described order:
     * <ul>
     * <li>A generic page title</li>
     * <li>An {@link #createErrorMessage(ErrorParameter) error message}</li>
     * <li>A {@link #createNavigationButton() button} to navigate to start page (using an empty
     * route)</li>
     * <li>{@link #createErrorDetails(ErrorParameter) Error details}: the timestamp of the error
     * occurrence</li>
     * <li>The exception stack trace (only in development mode)</li>
     * </ul>
     * 
     * @param event The before-enter event triggering this error parameter setting
     * @param parameter The error parameter containing {@link Exception} details
     *
     * @return The HTTP status code 500 INTERNAL SERVER ERROR
     *
     * @apiNote The {@link #createErrorMessage(ErrorParameter) error message}, the
     *          {@link #createNavigationButton() navigation button} and the
     *          {@link #createErrorDetails(ErrorParameter) error details} can be overwritten
     *          individually.
     */
    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<Exception> parameter) {
        logException(parameter);

        messageWrapper.removeAll();
        var title = new H4(NlsText.getString(MSG_KEY_PAGE_TITLE));
        messageWrapper.add(title);
        messageWrapper.add(createErrorMessage(parameter));
        messageWrapper.add(createNavigationButton());
        messageWrapper.add(createErrorDetails(parameter));
        if (isDevelopmentMode()) {
            messageWrapper.add(createStackTrace(parameter.getException()));
        }
        return HttpStatusCode.INTERNAL_SERVER_ERROR.getCode();
    }

    /**
     * Logs the exception.
     * 
     * @param parameter The error parameter containing {@link Exception} details
     * 
     * @apiNote Override this method to customize logging.
     * 
     * @implSpec The default implementation logs exceptions as {@link Level#SEVERE}.
     *           {@code MessageExceptions} {@link MessageException#MessageException(String) without
     *           a cause} are purely informational, and therefore not logged.
     */
    protected void logException(ErrorParameter<Exception> parameter) {
        var exception = parameter.getCaughtException();
        if (exception instanceof MessageException) {
            // only log MessageExceptions with cause
            if (exception.getCause() != null) {
                LOGGER.log(Level.SEVERE, "Unhandled exception", exception.getCause());
            }
        } else {
            LOGGER.log(Level.SEVERE, "Unhandled exception", exception);
        }
    }

    /**
     * Creates a component that is used for displaying the error message depending on the actual
     * exception type and production or development mode.
     * <ul>
     * <li>For a {@link MessageException} it always displays the exception message.</li>
     * <li>For all other exceptions it shows the custom message or the message of the exception in
     * development mode or a generic error message in production mode.</li>
     * </ul>
     *
     * @param parameter The error parameter containing {@link Exception} details
     *
     * @return A {@link Component} displaying the error message
     * 
     * @apiNote Override this method to customize the error message.
     */
    protected Component createErrorMessage(ErrorParameter<Exception> parameter) {
        var message = new LinkkiText();
        var exception = parameter.getCaughtException();

        if (exception instanceof MessageException) {
            message.setText(exception.getMessage());
        } else if (isDevelopmentMode()) {
            if (parameter.hasCustomMessage()) {
                message.setText(parameter.getCustomMessage());
            } else {
                message.setText(exception.getMessage());
            }
        } else {
            message.setText(NlsText.getString(MSG_KEY_DEFAULT_ERROR_MESSAGE));
        }

        return message;
    }

    /**
     * Creates component that displays error details. By default, it consists of the timestamp of
     * when the error occurred and the type of the {@link Exception}. The timestamp is formatted in
     * the "yyyy-MM-dd HH:mm:ss" pattern.
     *
     * @param parameter The {@link ErrorParameter} containing {@link Exception} details
     *
     * @return The created error details {@link Component}
     *
     * @apiNote Override this method to show customized error details.
     */
    protected Component createErrorDetails(ErrorParameter<Exception> parameter) {
        var timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        var component = new LinkkiText();
        component.setText("Timestamp: " + timestamp);
        return component;
    }

    /**
     * Creates a button to navigate away from the error page.
     *
     * @return The button used for navigation
     * 
     * @apiNote Override this method to customize the navigation button.
     * 
     * @implSpec The default implementation creates a button that navigates to the default start
     *           view.
     */
    protected Button createNavigationButton() {
        var goToStartViewButton = new Button(localize());
        goToStartViewButton.addClickListener(e -> UI.getCurrent().navigate(StringUtils.EMPTY));
        return goToStartViewButton;
    }

    /**
     * Gets the {@link Div wrapper} that contains the error content.
     */
    protected Div getMessageWrapper() {
        return messageWrapper;
    }

    /**
     * Returns whether the application runs in development mode.
     * <p>
     * A return value of {@code true} results in internal information (e.g. detailed error messages
     * and stack traces) being shown on the page.
     * 
     * @apiNote It should not be necessary to override this method.
     */
    protected boolean isDevelopmentMode() {
        return !VaadinService.getCurrent().getDeploymentConfiguration().isProductionMode();
    }

    private String localize() {
        return NlsService.get().getString(BUNDLE_NAME, LinkkiErrorPage.MSG_KEY_GO_TO_START_VIEW)
                .orElse('!' + LinkkiErrorPage.MSG_KEY_GO_TO_START_VIEW + '!');
    }

    private Div createStackTrace(Exception exception) {
        var sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        var stackTrace = sw.toString();

        var stackTraceDiv = new Div();
        stackTraceDiv.setText(stackTrace);
        stackTraceDiv.addClassName("error-page-stacktrace");
        return stackTraceDiv;
    }

    private Div createContentWrapper() {
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        addClassName("error-page");

        var wrapper = new Div();
        add(wrapper);

        var iconWrapper = new Div();
        iconWrapper.addClassName("error-page-icon");
        iconWrapper.addClassName("linkki-message-error");
        wrapper.add(iconWrapper);

        var icon = VaadinIcon.EXCLAMATION_CIRCLE.create();
        iconWrapper.add(icon);

        return wrapper;
    }
}
