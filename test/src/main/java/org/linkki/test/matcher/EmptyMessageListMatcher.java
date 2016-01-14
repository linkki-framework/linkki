/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.test.matcher;

import org.faktorips.runtime.MessageList;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class EmptyMessageListMatcher extends TypeSafeMatcher<MessageList> {

    @Override
    public void describeTo(Description description) {
        description.appendText("an empty message list");

    }

    @Override
    protected boolean matchesSafely(MessageList ml) {
        return ml.isEmpty();
    }

}
