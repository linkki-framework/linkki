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

import javax.swing.text.View;

import org.linkki.framework.ui.application.ApplicationLayout;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;

/**
 * An {@link ErrorHandler} that shows a {@link ConfirmationDialog} with some exception details when an
 * exception occurs, and shows another {@link View} when the user closes that error dialog.
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
 * @implNote The {@link ApplicationLayout} already registers this {@link DialogErrorHandler}.
 * 
 */
// TODO LIN-2203 Error Dialog Handler umstellen
// By default, {@link DefaultErrorDialog} is used to display the exception and the {@link View} named ""
// (empty String) is shown upon confirmation. To use another dialog or a different {@link View} to
// navigate to, use the constructor {@link #DialogErrorHandler(Navigator, BiFunction, String)}.
public class DialogErrorHandler implements ErrorHandler {

    private static final long serialVersionUID = -6253400229098333633L;

    @Override
    public void error(ErrorEvent event) {
        // TODO LIN-2203 Error Dialog Handler umstellen
        Notification.show(event.getThrowable().getLocalizedMessage(), 3000, Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    // TODO LIN-2203 Error Dialog Handler umstellen
    // private static final long serialVersionUID = 1L;
    // private static final Logger LOGGER = Logger.getLogger(DialogErrorHandler.class.getName());
    // private static final String DEFAULT_START_VIEW = "";
    //
    // private final Navigator navigator;
    //
    // private final String startView;
    // private final BiFunction<ErrorEvent, Handler, ConfirmationDialog> dialogCreator;
    //
    // public DialogErrorHandler(Navigator navigator,
    // BiFunction<ErrorEvent, Handler, ConfirmationDialog> dialogCreator) {
    // this(navigator, dialogCreator, DEFAULT_START_VIEW);
    // }
    //
    // public DialogErrorHandler(Navigator navigator, BiFunction<ErrorEvent, Handler,
    // ConfirmationDialog> dialogCreator,
    // String startView) {
    // this.navigator = navigator;
    // this.dialogCreator = dialogCreator;
    // this.startView = startView;
    // }
    //
    // /**
    // * @deprecated since 1.1. The navigator can now be any {@link Navigator}. This constructor was
    // * retained for binary compatibility. Use
    // * {@link #DialogErrorHandler(Navigator, BiFunction)} instead.
    // */
    // @Deprecated
    // public DialogErrorHandler(org.linkki.framework.ui.application.ApplicationNavigator
    // applicationNavigator,
    // BiFunction<ErrorEvent, Handler, ConfirmationDialog> dialogCreator) {
    // this((Navigator)applicationNavigator, dialogCreator);
    // }
    //
    // /**
    // * @deprecated since 1.1. The navigator can now be any {@link Navigator}. This constructor was
    // * retained for binary compatibility. Use
    // * {@link #DialogErrorHandler(Navigator, BiFunction, String)} instead.
    // */
    // @Deprecated
    // public DialogErrorHandler(org.linkki.framework.ui.application.ApplicationNavigator
    // applicationNavigator,
    // BiFunction<ErrorEvent, Handler, ConfirmationDialog> dialogCreator,
    // String startView) {
    // this((Navigator)applicationNavigator, dialogCreator, startView);
    // }
    //
    // @Override
    // public void error(ErrorEvent event) {
    // LOGGER.log(Level.SEVERE, "Unhandled exception", event.getThrowable());
    // showErrorDialog(event);
    // }
    //
    // private void showErrorDialog(ErrorEvent errorEvent) {
    // ConfirmationDialog dialog = dialogCreator.apply(errorEvent, this::navigateToStartView);
    // dialog.open();
    // }
    //
    // // findbugs reports that it should not be necessary to copy the list. but it is
    // @SuppressFBWarnings
    // private void navigateToStartView() {
    // UI ui = UI.getCurrent();
    // // need to copy to new array list to avoid concurrent modification
    // new ArrayList<>(ui.getWindows()).forEach(ui::removeWindow);
    // navigator.navigateTo(startView);
    // }
}
