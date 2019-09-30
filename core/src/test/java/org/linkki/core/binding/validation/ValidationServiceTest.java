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

package org.linkki.core.binding.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.util.validation.ValidationMarker;

public class ValidationServiceTest {

    @Test
    public void testGetFilteredMessages_Unfiltered() {
        Message requiredMessage = Message.builder("foo", Severity.ERROR).markers(ValidationMarker.REQUIRED).create();
        Message otherMessage = Message.newError("bar", "baz");
        ValidationService validationService = () -> new MessageList(requiredMessage, otherMessage);
        assertThat(validationService.getFilteredMessages(), contains(requiredMessage, otherMessage));
    }

    @Test
    public void testGetFilteredMessages_Filtered() {
        Message requiredMessage = Message.builder("foo", Severity.ERROR).markers(ValidationMarker.REQUIRED).create();
        Message otherMessage = Message.newError("bar", "baz");
        ValidationService validationService = new ValidationService() {
            @Override
            public MessageList getValidationMessages() {
                return new MessageList(requiredMessage, otherMessage);
            }

            @Override
            public ValidationDisplayState getValidationDisplayState() {
                return ValidationDisplayState.HIDE_MANDATORY_FIELD_VALIDATIONS;
            }
        };
        assertThat(validationService.getFilteredMessages(), contains(otherMessage));
    }

}
