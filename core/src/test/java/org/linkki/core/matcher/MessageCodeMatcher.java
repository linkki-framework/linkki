/*
 * Copyright Faktor Zehn GmbH.
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

import org.apache.commons.lang3.Strings;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.linkki.core.binding.validation.message.Message;

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
        return Strings.CS.equals(messageCode, m.getCode());
    }

}
