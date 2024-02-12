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

package org.linkki.framework.ui.dialogs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.linkki.core.ui.ComponentStyles;
import org.linkki.framework.ui.nls.NlsText;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.VaadinService;

/**
 * Configuration for an error dialog.
 * <p>
 * By default, following content components are provided in this order:
 * <ul>
 * <li>Timestamp</li>
 * <li>Error message</li>
 * <li>Exception message</li>
 * <li>Exception stacktrace</li>
 * </ul>
 * For security reasons, the exception's message and stack trace are only shown by default if the
 * application is not in the production mode.
 */
public class ErrorDialogConfiguration {

    private final Handler handler;

    private String caption = NlsText.getString("DefaultErrorHandler.errorDialogTitle");
    private String errorMessage = NlsText.getString("DefaultErrorHandler.errorDialogText");

    private boolean showExceptionMessage = isDevelopmentMode();
    private boolean showExceptionStacktrace = isDevelopmentMode();

    private ErrorDialogConfiguration(Handler handler) {
        this.handler = handler;
    }

    /**
     * Creates a new dialog configuration with an OK handler that navigates to the given view. In
     * addition, an {@link DialogErrorHandler#ERROR_PARAM error parameter} is appended to the URL.
     * 
     * @param view the route of the view to navigate to on confirmation
     */
    public static ErrorDialogConfiguration createWithHandlerNavigatingTo(String view) {
        return new ErrorDialogConfiguration(() -> navigateToStartView(view));
    }

    private static void navigateToStartView(String startView) {
        UI.getCurrent().navigate(startView, QueryParameters.fromString(DialogErrorHandler.ERROR_PARAM));
    }

    private static boolean isDevelopmentMode() {
        return !VaadinService.getCurrent().getDeploymentConfiguration().isProductionMode();
    }

    /**
     * Customizes the dialog caption. The default is a localized version of 'Error in the
     * Application'.
     */
    public ErrorDialogConfiguration withCaption(String dialogCaption) {
        this.caption = dialogCaption;
        return this;
    }

    /**
     * Customizes the dialog error message. The default is a localized version of 'An unexpected
     * error occurred.'.
     */
    public ErrorDialogConfiguration withErrorMessage(String message) {
        this.errorMessage = message;
        return this;
    }

    /**
     * Shows the exception message within the dialog. By default, the message is only shown if the
     * application does not run in the production mode.
     */
    public ErrorDialogConfiguration showExceptionMessage() {
        this.showExceptionMessage = true;
        return this;
    }

    /**
     * Hides the exception message within the dialog. By default, the message is only shown if the
     * application does not run in the production mode.
     */
    public ErrorDialogConfiguration hideExceptionMessage() {
        this.showExceptionMessage = false;
        return this;
    }

    /**
     * Shows the exception stacktrace within the dialog. By default, the stacktrace is only shown if
     * the application does not run in the production mode.
     */
    public ErrorDialogConfiguration showExceptionStacktrace() {
        this.showExceptionStacktrace = true;
        return this;
    }

    /**
     * Hides the exception stacktrace within the dialog. By default, the stacktrace is only shown if
     * the application does not run in the production mode.
     */
    public ErrorDialogConfiguration hideExceptionStacktrace() {
        this.showExceptionStacktrace = false;
        return this;
    }

    /**
     * Returns the handler that is executed on confirmation.
     */
    public Handler getConfirmationHandler() {
        return handler;
    }

    /**
     * Gets the dialog caption.
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Gets the configured dialog content.
     * 
     * @param event the event to be handled
     */
    public List<Component> getDialogContent(ErrorEvent event) {
        var content = new ArrayList<Component>();
        content.add(createTimestamp());
        content.add(createErrorMessage());
        if (showExceptionMessage) {
            content.add(createExceptionMessage(event.getThrowable()));
        }
        if (showExceptionStacktrace) {
            content.add(createExceptionStacktrace(event.getThrowable()));
        }
        return content;
    }

    private Component createTimestamp() {
        var formattedTimestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(NlsText.getString("DefaultErrorHandler.timestampFormat")));
        return new Span(NlsText.format("DefaultErrorHandler.errorDialogTimestamp", formattedTimestamp));
    }

    private Span createErrorMessage() {
        return new Span(errorMessage);
    }

    private TextField createExceptionMessage(Throwable exception) {
        var textField = new TextField(NlsText.getString("DefaultErrorHandler.errorDialogDescription"));
        textField.setValue(ExceptionUtils.getRootCauseMessage(exception));
        formatTextComponent(textField);
        return textField;
    }

    private TextArea createExceptionStacktrace(Throwable exception) {
        var textArea = new TextArea(NlsText.getString("DefaultErrorHandler.errorDialogDetails"));
        textArea.setValue(ExceptionUtils.getStackTrace(exception));
        textArea.setHeight("25em");
        formatTextComponent(textArea);
        ComponentStyles.setOverflowAuto(textArea);
        return textArea;
    }

    private <T extends HasValueAndElement<?, ?> & HasSize> void formatTextComponent(T textComponent) {
        textComponent.setReadOnly(true);
        textComponent.setWidthFull();
        textComponent.setMinWidth("65em");
    }
}
