/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.test.matcher;

import org.faktorips.runtime.Message;
import org.faktorips.runtime.Severity;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

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
