/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.util;

import java.util.stream.Collectors;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.faktorips.runtime.Severity;
import org.linkki.util.StreamUtil;

import com.vaadin.server.ErrorMessage.ErrorLevel;

public class MessageListUtil {

    private MessageListUtil() {
        // do not instatiate
    }

    public static MessageList newMessageList(@Nullable Message... messages) {
        if (messages == null) {
            return new MessageList();
        }
        MessageList messageList = new MessageList();
        for (Message message : messages) {
            messageList.add(message);
        }
        return messageList;
    }

    @CheckForNull
    public static ErrorLevel getErrorLevel(MessageList messages) {
        return getErrorLevel(messages.getSeverity());
    }

    @CheckForNull
    public static ErrorLevel getErrorLevel(@Nullable Severity severity) {
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

    /**
     * Returns a new {@link MessageList} containing the same {@link Message Messages} as the given
     * list, sorted by descending {@link Severity}. Within each severity the previous order is
     * preserved.
     * <p>
     * {@code null} will remain {@code null}.
     */
    @CheckForNull
    public static MessageList sortBySeverity(@Nullable MessageList unsortedMessageList) {
        if (unsortedMessageList == null) {
            return null;
        }
        MessageList messageList = StreamUtil.stream(unsortedMessageList)
                .collect(Collectors.groupingBy(m -> m.getSeverity()))
                .entrySet().stream().sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                .flatMap(e -> e.getValue().stream())
                .collect(() -> new MessageList(), (ml, m) -> ml.add(m), (ml1, ml2) -> ml1.add(ml2));
        return messageList;
    }

}
