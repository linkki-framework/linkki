/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding;

import org.faktorips.runtime.MessageList;
import org.faktorips.runtime.Severity;

import com.vaadin.server.ErrorMessage.ErrorLevel;

public class MessageListUtil {

    public static ErrorLevel getErrorLevel(MessageList messages) {
        return getErrorLevel(messages.getSeverity());
    }

    public static ErrorLevel getErrorLevel(Severity severity) {
        switch (severity) {
            case ERROR:
                return ErrorLevel.ERROR;
            case WARNING:
                return ErrorLevel.WARNING;
            case INFO:
                return ErrorLevel.INFORMATION;
            default:
                return null;
        }
    }

}
