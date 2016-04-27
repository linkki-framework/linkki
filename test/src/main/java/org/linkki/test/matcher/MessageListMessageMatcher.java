/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.test.matcher;

import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class MessageListMessageMatcher extends TypeSafeMatcher<MessageList> {

    private final Matcher<Message> messageMatcher;

    public MessageListMessageMatcher(Matcher<Message> messageMatcher) {
        this.messageMatcher = messageMatcher;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a message list containing ");
        messageMatcher.describeTo(description);
    }

    @Override
    protected boolean matchesSafely(MessageList ml) {
        for (Message m : ml) {
            if (messageMatcher.matches(m)) {
                return true;
            }
        }
        return false;
    }

}
