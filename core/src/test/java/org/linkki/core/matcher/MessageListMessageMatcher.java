/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.linkki.core.message.Message;
import org.linkki.core.message.MessageList;

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
