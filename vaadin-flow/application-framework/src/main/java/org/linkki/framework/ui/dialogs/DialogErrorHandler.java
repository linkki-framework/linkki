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

import java.io.Serial;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.linkki.framework.ui.application.ApplicationConfig;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;

/**
 * An {@link ErrorHandler} that shows a {@link OkCancelDialog} with some exception details when an
 * exception occurs.
 * <p>
 * The displayed error dialog is created as configured in the given
 * {@link ErrorDialogConfiguration}. Use different options in the configuration to customize the
 * error dialog.
 */
public class DialogErrorHandler implements ErrorHandler {

    /**
     * Parameter that is appended as {@link QueryParameters QueryParameter} to the redirected
     * {@link Location} after an error occurred
     */
    public static final String ERROR_PARAM = "errorOccurred";

    @Serial
    private static final long serialVersionUID = -6253400229098333633L;

    private static final Logger LOGGER = Logger.getLogger(DialogErrorHandler.class.getName());

    private final transient Function<ErrorEvent, ? extends OkCancelDialog> dialogCreator;

    /**
     * @deprecated use {@link DialogErrorHandler#DialogErrorHandler(ErrorDialogConfiguration)}
     *             instead. To use a custom dialog, create an own implementation of
     *             {@link ErrorHandler} instead of using {@link DialogErrorHandler} in
     *             {@link ApplicationConfig#getErrorHandler()}.
     */
    @Deprecated(since = "2.4.0")
    public DialogErrorHandler(BiFunction<ErrorEvent, Handler, ConfirmationDialog> dialogCreator) {
        this(dialogCreator, StringUtils.EMPTY);
    }

    /**
     * @deprecated use {@link DialogErrorHandler#DialogErrorHandler(ErrorDialogConfiguration)}
     *             instead. The view to be shown on confirmation can be defined by
     *             {@link ErrorDialogConfiguration#createWithHandlerNavigatingTo(String)}. To use a
     *             custom dialog, create an own implementation of {@link ErrorHandler} instead of
     *             using {@link DialogErrorHandler} in {@link ApplicationConfig#getErrorHandler()}.
     */
    @Deprecated(since = "2.4.0")
    public DialogErrorHandler(BiFunction<ErrorEvent, Handler, ConfirmationDialog> dialogCreator,
            String startView) {
        this.dialogCreator = event -> dialogCreator.apply(event, ErrorDialogConfiguration
                .createWithHandlerNavigatingTo(startView)
                .getConfirmationHandler());
    }

    /**
     * Creates a {@link DialogErrorHandler} which uses an {@link ErrorDialogConfiguration} that can
     * be used to create an error dialog.
     *
     * @param errorDialogConfiguration configuration for the error dialog
     */
    public DialogErrorHandler(ErrorDialogConfiguration errorDialogConfiguration) {
        this.dialogCreator = event -> OkCancelDialog.builder(errorDialogConfiguration.getCaption())
                .buttonOption(OkCancelDialog.ButtonOption.OK_ONLY)
                .okHandler(errorDialogConfiguration.getConfirmationHandler())
                .content(errorDialogConfiguration.getDialogContent(event).toArray(Component[]::new))
                .build();
    }

    @Override
    public void error(ErrorEvent event) {
        var wrappingEvent = new IdErrorEvent(event);
        LOGGER.log(java.util.logging.Level.SEVERE, "Unhandled exception [%s]".formatted(wrappingEvent.getExceptionId()),
                   wrappingEvent.getThrowable());
        dialogCreator.apply(wrappingEvent).open();
    }
}
