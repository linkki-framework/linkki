/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.linkki.core.message.Message;
import org.linkki.core.message.Severity;

public class MessageSeverityMatcher extends TypeSafeMatcher<Message> {

    private final Severity severity;

    public MessageSeverityMatcher(Severity severity) {
        this.severity = severity;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a message with severity " + severity);
    }

    @Override
    protected boolean matchesSafely(Message m) {
        return severity == m.getSeverity();
    }

}
