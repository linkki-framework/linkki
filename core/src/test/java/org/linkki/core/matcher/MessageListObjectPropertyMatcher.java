/*
 * Copyright Faktor Zehn AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.core.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.linkki.core.message.MessageList;
import org.linkki.core.message.ObjectProperty;

public class MessageListObjectPropertyMatcher extends TypeSafeMatcher<MessageList> {

    private final ObjectProperty objectProperty;

    private final int count;

    public MessageListObjectPropertyMatcher(ObjectProperty objectProperty) {
        this(objectProperty, 1);
    }

    public MessageListObjectPropertyMatcher(ObjectProperty objectProperty, int count) {
        this.objectProperty = objectProperty;
        this.count = count;
    }

    @Override
    public void describeTo(Description description) {
        if (count == 1) {
            description.appendText("a message for " + objectProperty);
        } else {
            description.appendText(count + " messages for " + objectProperty);
        }
    }

    @Override
    protected boolean matchesSafely(MessageList ml) {
        return ml.getMessagesFor(objectProperty.getObject(), objectProperty.getProperty()).size() == count;
    }

}
