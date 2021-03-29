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

package org.linkki.samples.playground.application;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.ObjectProperty;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.ui.element.annotation.UITextField;

import edu.umd.cs.findbugs.annotations.NonNull;

public class SendEmailPmo {

    @NonNull
    public static final String PROPERTY_EMAIL = "email";

    private String email;

    @UITextField(position = 40, label = "Send a copy of the report to")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NonNull
    public MessageList validate() {
        MessageList messages = new MessageList();
        if (StringUtils.isNotBlank(email) && !email.matches("^[a-zA-Z0-9._\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,4}$")) {
            messages.add(Message
                    .builder("The given email \"" + email + "\" is invalid (Pattern: someone@example.com)",
                             Severity.ERROR)
                    .invalidObject(new ObjectProperty(this, PROPERTY_EMAIL)).create());
        }
        return messages;
    }

}
