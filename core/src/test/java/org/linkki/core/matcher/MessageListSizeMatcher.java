/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.linkki.core.message.MessageList;

public class MessageListSizeMatcher extends TypeSafeMatcher<MessageList> {

    private Matcher<Integer> intMatcher;

    public MessageListSizeMatcher(Matcher<Integer> intMatcher) {
        this.intMatcher = intMatcher;
    }

    @Override
    public void describeTo(Description description) {
        intMatcher.describeTo(description);
        description.appendText(" messages");
    }

    @Override
    protected boolean matchesSafely(MessageList ml) {
        return intMatcher.matches(ml.size());
    }

}
