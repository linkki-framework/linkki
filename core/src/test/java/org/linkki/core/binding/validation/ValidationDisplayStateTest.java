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
package org.linkki.core.binding.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.linkki.core.binding.validation.ValidationDisplayState.HIDE_MANDATORY_FIELD_VALIDATIONS;
import static org.linkki.core.binding.validation.ValidationDisplayState.SHOW_ALL;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.matcher.MessageMatchers;
import org.linkki.util.validation.ValidationMarker;

public class ValidationDisplayStateTest {

    @Test
    public void testFilter() {
        ValidationMarker mandatoryFieldMarker = () -> true;
        ValidationMarker nonMandatoryFieldMarker = () -> false;
        Message m1 = Message.builder("m1", Severity.ERROR).markers(mandatoryFieldMarker).create();
        Message m2 = Message.builder("m2", Severity.ERROR).markers(nonMandatoryFieldMarker).create();
        Message m3 = Message.builder("m3", Severity.ERROR).create();
        MessageList messages = new MessageList(m1, m2, m3);

        assertThat(SHOW_ALL.filter(messages), contains(m1, m2, m3));
        assertThat(HIDE_MANDATORY_FIELD_VALIDATIONS.filter(messages), contains(m2, m3));
    }

    @Test
    public void testFilter_EmptyMessageList() {
        MessageList emptyMessageList = new MessageList();

        assertThat(SHOW_ALL.filter(emptyMessageList), is(MessageMatchers.emptyMessageList()));
        assertThat(HIDE_MANDATORY_FIELD_VALIDATIONS.filter(emptyMessageList), is(emptyMessageList));
    }

}
