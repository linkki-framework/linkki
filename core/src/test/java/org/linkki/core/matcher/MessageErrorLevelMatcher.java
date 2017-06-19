/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.linkki.core.message.Message;

import com.vaadin.server.ErrorMessage.ErrorLevel;

public class MessageErrorLevelMatcher extends TypeSafeMatcher<Message> {

    private final ErrorLevel errorLevel;

    public MessageErrorLevelMatcher(ErrorLevel errorLevel) {
        this.errorLevel = errorLevel;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a message with errorLevel " + errorLevel);
    }

    @Override
    protected boolean matchesSafely(Message m) {
        return errorLevel == m.getErrorLevel();
    }

}
