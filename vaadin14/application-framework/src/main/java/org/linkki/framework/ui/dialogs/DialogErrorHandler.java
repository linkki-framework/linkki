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
package org.linkki.framework.ui.dialogs;

import java.util.function.BiFunction;
import java.util.logging.Logger;

import org.linkki.framework.ui.application.ApplicationLayout;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;

/**
 * An {@link ErrorHandler} that shows a {@link ConfirmationDialog} with some exception details when an
 * exception occurs, and shows another {@link Route} when the user closes that error dialog.
 * <p>
 * By default, {@link DefaultErrorDialog} is used to display the exception and the {@link Route} named
 * "" (empty String) is shown upon confirmation. To use another dialog or a different {@link Route} to
 * navigate to, use the constructor {@link #DialogErrorHandler(BiFunction, String)}.
 * 
 * @implNote The {@link ApplicationLayout} already registers this {@link DialogErrorHandler}.
 * 
 */
public class DialogErrorHandler implements ErrorHandler {

    private static final long serialVersionUID = -6253400229098333633L;
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(DialogErrorHandler.class.getName());
    private static final String DEFAULT_START_VIEW = "";

    private final String startView;
    private final BiFunction<ErrorEvent, Handler, ConfirmationDialog> dialogCreator;

    public DialogErrorHandler(BiFunction<ErrorEvent, Handler, ConfirmationDialog> dialogCreator) {
        this(dialogCreator, DEFAULT_START_VIEW);
    }

    public DialogErrorHandler(BiFunction<ErrorEvent, Handler, ConfirmationDialog> dialogCreator,
            String startView) {
        this.dialogCreator = dialogCreator;
        this.startView = startView;
    }

    @Override
    public void error(ErrorEvent event) {
        LOGGER.log(java.util.logging.Level.SEVERE, "Unhandled exception", event.getThrowable());
        showErrorDialog(event);
    }

    private void showErrorDialog(ErrorEvent errorEvent) {
        ConfirmationDialog dialog = dialogCreator.apply(errorEvent, this::navigateToStartView);
        dialog.open();
    }

    private void navigateToStartView() {
        UI.getCurrent().navigate(startView);
    }
}
