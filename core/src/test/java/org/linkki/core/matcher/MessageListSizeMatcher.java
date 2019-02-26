/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
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
