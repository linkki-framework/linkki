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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.linkki.framework.ui.nls.NlsText;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Default dialog used by {@link DialogErrorHandler} to show error information.
 */
public class DefaultErrorDialog extends ConfirmationDialog {

    private static final long serialVersionUID = 1L;

    public DefaultErrorDialog(com.vaadin.server.ErrorEvent errorEvent, Handler navigateToStartView) {
        super(NlsText.getString("DefaultErrorHandler.errorDialogTitle"), navigateToStartView,
                createContent(errorEvent));
        setSize("80em", "40em");
    }

    private static VerticalLayout createContent(com.vaadin.server.ErrorEvent errorEvent) {
        LocalDateTime timestamp = LocalDateTime.now();

        VerticalLayout content = new VerticalLayout();
        content.addComponent(createLabelWithTimestamp(timestamp));
        content.addComponent(createRootCauseTextField(errorEvent.getThrowable()));
        content.addComponent(createStackTraceTextArea(errorEvent.getThrowable()));
        formatContent(content);

        return content;
    }

    private static Label createLabelWithTimestamp(LocalDateTime timestamp) {
        String formattedTimestamp = timestamp
                .format(DateTimeFormatter.ofPattern(NlsText.getString("DefaultErrorHandler.timestampFormat")));
        return new Label(NlsText.format("DefaultErrorHandler.errorDialogText", formattedTimestamp));
    }

    private static TextField createRootCauseTextField(@CheckForNull Throwable t) {
        TextField textField = new TextField(NlsText.getString("DefaultErrorHandler.errorDialogDescription"));

        String message = Optional.ofNullable(ExceptionUtils.getRootCause(t))
                .map(e -> e.getLocalizedMessage())
                .orElse(ExceptionUtils.getRootCauseMessage(t));
        textField.setValue(message);
        textField.setNullRepresentation(NlsText.getString("DefaultErrorHandler.nullRepresentation"));
        formatText(textField);
        return textField;
    }

    private static TextArea createStackTraceTextArea(@CheckForNull Throwable t) {
        TextArea textArea = new TextArea(NlsText.getString("DefaultErrorHandler.errorDialogDetails"));
        textArea.setNullRepresentation(NlsText.getString("DefaultErrorHandler.nullRepresentation"));
        textArea.setValue(ExceptionUtils.getStackTrace(t));
        formatText(textArea);
        return textArea;
    }

    private static void formatText(AbstractTextField textArea) {
        textArea.setReadOnly(true);
        textArea.setSizeFull();
    }

    private static void formatContent(VerticalLayout content) {
        content.setSizeFull();
        content.setExpandRatio(content.getComponent(0), 5);
        content.setExpandRatio(content.getComponent(1), 10);
        content.setExpandRatio(content.getComponent(2), 85);
        content.setSpacing(true);
    }
}
