/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.test.matcher;

import org.apache.commons.lang3.StringUtils;
import org.faktorips.runtime.Message;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class MessageCodeMatcher extends TypeSafeMatcher<Message> {

    private final String messageCode;

    public MessageCodeMatcher(String messageCode) {
        this.messageCode = messageCode;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a message with message code " + messageCode);
    }

    @Override
    protected boolean matchesSafely(Message m) {
        return StringUtils.equals(messageCode, m.getCode());
    }

}
