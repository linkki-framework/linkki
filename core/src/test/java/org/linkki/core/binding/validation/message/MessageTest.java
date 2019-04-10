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
package org.linkki.core.binding.validation.message;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.linkki.util.validation.ValidationMarker;

import edu.umd.cs.findbugs.annotations.NonNull;

public class MessageTest {

    @Test
    public void testEquals_Equal() {
        Object invalidObject = new Object();
        ObjectProperty objectProperty = new ObjectProperty(invalidObject);
        ValidationMarker validationMarker = () -> false;

        Message message1 = new Message("code", "text", Severity.INFO, Arrays.asList(objectProperty),
                Collections.singleton(validationMarker));
        Message message2 = new Message("code", "text", Severity.INFO, Arrays.asList(objectProperty),
                Collections.singleton(validationMarker));

        assertThat(message1, is(equalTo(message2)));
        assertThat(message2, is(equalTo(message1)));
    }

    @Test
    public void testEquals_NotEqual_Code() {
        Object invalidObject = new Object();
        ObjectProperty objectProperty = new ObjectProperty(invalidObject);
        ValidationMarker validationMarker = () -> false;

        Message message1 = new Message("code1", "text", Severity.INFO, Arrays.asList(objectProperty),
                Collections.singleton(validationMarker));
        Message message2 = new Message("code2", "text", Severity.INFO, Arrays.asList(objectProperty),
                Collections.singleton(validationMarker));

        assertThat(message1, is(not(equalTo(message2))));
    }

    @Test
    public void testEquals_NotEqual_Text() {
        Object invalidObject = new Object();
        ObjectProperty objectProperty = new ObjectProperty(invalidObject);
        ValidationMarker validationMarker = () -> false;

        Message message1 = new Message("code", "text1", Severity.INFO, Arrays.asList(objectProperty),
                Collections.singleton(validationMarker));
        Message message2 = new Message("code", "text2", Severity.INFO, Arrays.asList(objectProperty),
                Collections.singleton(validationMarker));

        assertThat(message1, is(not(equalTo(message2))));
    }

    @Test
    public void testEquals_NotEqual_Severity() {
        Object invalidObject = new Object();
        ObjectProperty objectProperty = new ObjectProperty(invalidObject);
        ValidationMarker validationMarker = () -> false;

        Message message1 = new Message("code", "text", Severity.INFO, Arrays.asList(objectProperty),
                Collections.singleton(validationMarker));
        Message message2 = new Message("code", "text", Severity.WARNING, Arrays.asList(objectProperty),
                Collections.singleton(validationMarker));

        assertThat(message1, is(not(equalTo(message2))));
    }

    @Test
    public void testEquals_NotEqual_ObjectProperties() {
        Object invalidObject = new Object();
        ObjectProperty objectProperty = new ObjectProperty(invalidObject);
        ValidationMarker validationMarker = () -> false;

        Message message1 = Message.builder("text", Severity.INFO).code("code").markers(validationMarker)
                .invalidObjectWithProperties(invalidObject).create();
        Message message2 = Message.builder("text", Severity.INFO).code("code").markers(validationMarker)
                .invalidObjects(objectProperty, objectProperty).create();
        Message message3 = Message.builder("text", Severity.INFO).code("code").markers(validationMarker)
                .invalidObjectWithProperties(invalidObject, "foo", "bar").create();

        assertThat(message1, is(not(equalTo(message2))));
        assertThat(message1, is(not(equalTo(message3))));
    }

    @Test
    public void testEquals_NotEqual_validationMarker() {
        Object invalidObject = new Object();
        ObjectProperty objectProperty = new ObjectProperty(invalidObject);

        Message message1 = new Message("code", "text", Severity.INFO, Arrays.asList(objectProperty),
                Collections.singleton(() -> false));
        Message message2 = new Message("code", "text", Severity.INFO, Arrays.asList(objectProperty),
                Collections.singleton(() -> true));

        assertThat(message1, is(not(equalTo(message2))));
    }

    @Test
    public void testToString() {
        Message message = new Message("code", "text", Severity.WARNING,
                Arrays.asList(new ObjectProperty("Object", "property", 1)),
                Collections.singleton(() -> true));

        assertThat(message.toString(), is("WARNING code[Object.property]\ntext"));
    }

    @Test
    public void testIsMandatoryFieldMessage() {
        ValidationMarker mandatoryMarker = () -> true;
        ValidationMarker notMandatoryMarker = () -> false;

        assertThat(createMessage().isMandatoryFieldMessage(), is(false));
        assertThat(createMessage(notMandatoryMarker).isMandatoryFieldMessage(), is(false));
        assertThat(createMessage(mandatoryMarker).isMandatoryFieldMessage(), is(true));
        assertThat(createMessage(notMandatoryMarker, mandatoryMarker).isMandatoryFieldMessage(), is(true));
    }

    private Message createMessage(@NonNull ValidationMarker... markers) {
        return Message.builder("", Severity.ERROR).markers(markers).create();
    }

}