/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.matcher;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.linkki.core.message.Message;

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
