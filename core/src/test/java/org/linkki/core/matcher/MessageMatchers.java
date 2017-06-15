/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.matcher;

import static org.hamcrest.CoreMatchers.equalTo;

import org.hamcrest.Matcher;
import org.hamcrest.core.CombinableMatcher;
import org.linkki.core.message.Message;
import org.linkki.core.message.ObjectProperty;
import org.linkki.core.message.Severity;

public class MessageMatchers {

    public static EmptyMessageListMatcher emptyMessageList() {
        return new EmptyMessageListMatcher();
    }

    public static MessageListSizeMatcher hasSize(int size) {
        return new MessageListSizeMatcher(equalTo(size));
    }

    public static MessageListSizeMatcher hasSize(Matcher<Integer> intMatcher) {
        return new MessageListSizeMatcher(intMatcher);
    }

    public static MessageListObjectPropertyMatcher hasMessageFor(Object o, String property) {
        return new MessageListObjectPropertyMatcher(new ObjectProperty(o, property));
    }

    public static MessageListObjectPropertyMatcher hasMessagesFor(int count, Object o, String property) {
        return new MessageListObjectPropertyMatcher(new ObjectProperty(o, property), count);
    }

    public static MessageListMessageMatcher hasMessage(String code) {
        return new MessageListMessageMatcher(new MessageCodeMatcher(code));
    }

    public static MessageListMessageMatcher hasInfoMessage(String code) {
        return new MessageListMessageMatcher(codeAndSeverity(code, Severity.INFO));
    }

    public static MessageListMessageMatcher hasWarningMessage(String code) {
        return new MessageListMessageMatcher(codeAndSeverity(code, Severity.WARNING));
    }

    public static MessageListMessageMatcher hasErrorMessage(String code) {
        return new MessageListMessageMatcher(codeAndSeverity(code, Severity.ERROR));
    }

    private static Matcher<Message> codeAndSeverity(String code, Severity severity) {
        return CombinableMatcher.both(new MessageCodeMatcher(code)).and(new MessageSeverityMatcher(severity));
    }

}
