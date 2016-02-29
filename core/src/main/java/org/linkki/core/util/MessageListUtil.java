/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.util;

import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.faktorips.runtime.Severity;

import com.vaadin.server.ErrorMessage.ErrorLevel;

public class MessageListUtil {

    private MessageListUtil() {
        // do not instatiate
    }

    public static MessageList newMessageList(Message... messages) {
        if (messages == null) {
            return new MessageList();
        }
        MessageList messageList = new MessageList();
        for (Message message : messages) {
            messageList.add(message);
        }
        return messageList;
    }

    public static ErrorLevel getErrorLevel(MessageList messages) {
        return getErrorLevel(messages.getSeverity());
    }

    public static ErrorLevel getErrorLevel(Severity severity) {
        if (severity == null) {
            return null;
        }
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
