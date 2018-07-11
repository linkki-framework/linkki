/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.dialogs;

import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linkki.framework.ui.application.ApplicationNavigator;
import org.linkki.framework.ui.application.LinkkiUi;
import org.linkki.util.handler.Handler;

import com.vaadin.navigator.View;
import com.vaadin.server.ErrorEvent;
import com.vaadin.server.ErrorHandler;

/**
 * An {@link ErrorHandler} that shows a {@link ConfirmationDialog} with some exception details when
 * an exception occurs, and shows another {@link View} when the user closes that error dialog.
 * <p>
 * By default, {@link DefaultErrorDialog} is used to display the exception and the {@link View}
 * named "" (empty String) is shown upon confirmation. To use another dialog or a different
 * {@link View} to navigate to, use the constructor
 * {@link #DialogErrorHandler(ApplicationNavigator, BiFunction, String)}.
 * <p>
 * Register this handler in your main UI-class in order to report all exceptions:
 * 
 * <pre>
 * <code>
 * setErrorHandler(errorHandler);
 * VaadinSession.getCurrent().setErrorHandler(errorHandler);
 * </code>
 * </pre>
 * 
 * @implNote The {@link LinkkiUi} already registers this {@link DialogErrorHandler}.
 * 
 */
public class DialogErrorHandler implements ErrorHandler {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(DialogErrorHandler.class.getName());
    private static final String DEFAULT_START_VIEW = "";

    private final ApplicationNavigator applicationNavigator;

    private final String startView;
    private final BiFunction<ErrorEvent, Handler, ConfirmationDialog> dialogCreator;

    public DialogErrorHandler(ApplicationNavigator applicationNavigator,
            BiFunction<ErrorEvent, Handler, ConfirmationDialog> dialogCreator) {
        this(applicationNavigator, dialogCreator, DEFAULT_START_VIEW);
    }

    public DialogErrorHandler(ApplicationNavigator applicationNavigator,
            BiFunction<ErrorEvent, Handler, ConfirmationDialog> dialogCreator,
            String startView) {
        this.applicationNavigator = applicationNavigator;
        this.dialogCreator = dialogCreator;
        this.startView = startView;
    }

    @Override
    public void error(@SuppressWarnings("null") ErrorEvent event) {
        LOGGER.log(Level.SEVERE, "Unhandled exception", event.getThrowable());
        showErrorDialog(event);
    }

    private void showErrorDialog(ErrorEvent errorEvent) {
        dialogCreator.apply(errorEvent, this::navigateToStartView).open();
    }

    private void navigateToStartView() {
        applicationNavigator.showView(startView);
    }
}
