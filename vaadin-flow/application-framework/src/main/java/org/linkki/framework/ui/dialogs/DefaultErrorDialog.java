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
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.linkki.core.ui.ComponentStyles;
import org.linkki.framework.ui.nls.NlsText;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.VaadinService;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Default dialog used by {@link DialogErrorHandler} to show error information.
 * <p>
 * By default, the error dialog only shows an exception stacktrace if the Vaadin application does
 * not run in the production mode and hides it otherwise.
 * 
 * @deprecated use {@link ErrorDialogConfiguration} to customize an error dialog. This class is not
 *             used by default anymore. If a subclass has been used to customize the dialog that is
 *             shown by DialogErrorHandler, the customization must be implemented by using an
 *             ErrorDialogConfiguration instead. If the grade of customization exceeds the
 *             possibilities offered by {@link ErrorDialogConfiguration}, a new {@link ErrorHandler}
 *             can be created to use a fully customized dialog.
 */
@Deprecated(since = "2.4.0")
public class DefaultErrorDialog extends ConfirmationDialog {

    private static final long serialVersionUID = 1L;

    public DefaultErrorDialog(ErrorEvent errorEvent, Handler navigateToStartView) {
        super(NlsText.getString("DefaultErrorHandler.errorDialogTitle"), navigateToStartView,
                createContent(errorEvent));
        setSize("80em", "40em");
    }

    private static VerticalLayout createContent(ErrorEvent errorEvent) {
        LocalDateTime timestamp = LocalDateTime.now();

        VerticalLayout content = new VerticalLayout();
        content.add(createLabelWithTimestamp(timestamp));
        content.add(createRootCauseTextField(errorEvent.getThrowable()));

        if (showExceptionStacktrace()) {
            content.add(createStackTraceTextArea(errorEvent.getThrowable()));
        }

        content.setSizeFull();

        return content;
    }

    /**
     * Checks whether the exception stacktrace should be shown.
     * 
     * @return {@code true} if the property {@code vaadin.showExceptionStacktrace} is added or if
     *         the application does not run in the production mode
     */
    private static boolean showExceptionStacktrace() {
        boolean isProductionMode = VaadinService.getCurrent().getDeploymentConfiguration().isProductionMode();
        return VaadinService.getCurrent().getDeploymentConfiguration()
                .getBooleanProperty("showExceptionStacktrace", !isProductionMode);
    }

    private static NativeLabel createLabelWithTimestamp(LocalDateTime timestamp) {
        String formattedTimestamp = timestamp
                .format(DateTimeFormatter.ofPattern(NlsText.getString("DefaultErrorHandler.timestampFormat")));
        return new NativeLabel(NlsText.format("DefaultErrorHandler.errorDialogText", formattedTimestamp));
    }

    private static TextField createRootCauseTextField(@CheckForNull Throwable t) {
        TextField textField = new TextField(NlsText.getString("DefaultErrorHandler.errorDialogDescription"));

        String message = Optional.ofNullable(ExceptionUtils.getRootCause(t))
                .map(Throwable::getLocalizedMessage)
                .orElse(ExceptionUtils.getRootCauseMessage(t));
        textField.setValue(message);
        formatText(textField);
        return textField;
    }

    private static TextArea createStackTraceTextArea(@CheckForNull Throwable t) {
        TextArea textArea = new TextArea(NlsText.getString("DefaultErrorHandler.errorDialogDetails"));
        textArea.setValue(ExceptionUtils.getStackTrace(t));
        formatText(textArea);
        ComponentStyles.setOverflowAuto(textArea);
        return textArea;
    }

    private static <T extends HasValueAndElement<?, ?> & HasSize> void formatText(T textArea) {
        textArea.setReadOnly(true);
        textArea.setWidthFull();
    }
}
