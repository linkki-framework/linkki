/*******************************************************************************
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 * 
 * All Rights Reserved - Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.ips.messages;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.faktorips.runtime.IMarker;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.util.validation.ValidationMarker;

public class MessageConverterTest {

    private final class TestMarker implements IMarker {

        @Override
        public boolean isRequiredInformationMissing() {
            return true;
        }

        @Override
        public boolean isTechnicalConstraintViolated() {
            return false;
        }
    }

    @Test
    public void testConvertMessage() {

        org.faktorips.runtime.Message ipsMessage = new org.faktorips.runtime.Message.Builder("the text",
                org.faktorips.runtime.Severity.ERROR)
                        .markers(new TestMarker())
                        .invalidObject(new org.faktorips.runtime.ObjectProperty("TheObject", "theProperty"))
                        .replacements(new org.faktorips.runtime.MsgReplacementParameter("param1", "theValue"))
                        .code("IPS-0001")
                        .create();

        Message message = MessageConverter.convert(ipsMessage);
        assertThat(message.getText(), is("the text"));
        assertThat(message.getSeverity(), is(Severity.ERROR));
        assertThat(message.getMarkers(), Matchers.hasSize(1));

        ValidationMarker marker = message.getMarkers().iterator().next();
        assertThat(marker, is(instanceOf(ValidationMarkerWrapper.class)));
        assertThat(marker, is(instanceOf(ValidationMarkerWrapper.class)));
        assertThat(((ValidationMarkerWrapper)marker).getWrapped(), is(instanceOf(TestMarker.class)));

        assertThat(message.getInvalidObjectProperties(), Matchers.hasSize(1));
        assertThat(message.getText(), is("the text"));
    }

    @Test
    public void testConvertMessage_textNull_shouldConvertToEmptyString() {
        org.faktorips.runtime.Message ipsMessage = new org.faktorips.runtime.Message(null,
                org.faktorips.runtime.Severity.INFO);

        Message message = MessageConverter.convert(ipsMessage);
        assertThat(message.getText(), is(emptyString()));
    }

    @Test
    public void testConvertMessage_severityNull_shouldConvertToInfo() {
        org.faktorips.runtime.Message ipsMessage = new org.faktorips.runtime.Message("text", null);

        Message message = MessageConverter.convert(ipsMessage);
        assertThat(message.getSeverity(), is(Severity.INFO));
    }

    @Test
    public void testConvertMessage_severityNone_shouldConvertToInfo() {
        org.faktorips.runtime.Message ipsMessage = new org.faktorips.runtime.Message("text",
                org.faktorips.runtime.Severity.NONE);

        Message message = MessageConverter.convert(ipsMessage);
        assertThat(message.getSeverity(), is(Severity.INFO));
    }

    @Test
    public void testConvertMessageList() {
        org.faktorips.runtime.MessageList ipsList = new org.faktorips.runtime.MessageList();
        ipsList.add(new org.faktorips.runtime.Message.Builder("message1",
                org.faktorips.runtime.Severity.WARNING)
                        .code("code").create());
        ipsList.add(new org.faktorips.runtime.Message.Builder("message2",
                org.faktorips.runtime.Severity.ERROR)
                        .code("code").create());
        ipsList.add(new org.faktorips.runtime.Message.Builder("message3",
                org.faktorips.runtime.Severity.INFO)
                        .code("code").create());
        ipsList.add(new org.faktorips.runtime.Message.Builder("message4",
                org.faktorips.runtime.Severity.NONE)
                        .code("code").create());

        MessageList messages = MessageConverter.convert(ipsList);
        assertThat(messages.size(), is(4));
        assertTrue(messages.stream().allMatch(m -> "code".equals(m.getCode())));
        assertTrue(messages.stream().anyMatch(m -> Severity.ERROR.equals(m.getSeverity())));
        assertTrue(messages.stream().anyMatch(m -> Severity.WARNING.equals(m.getSeverity())));
        assertTrue(messages.stream().anyMatch(m -> Severity.INFO.equals(m.getSeverity())));

        List<Message> infoMessages = messages.stream()
                .filter(m -> m.getSeverity() == Severity.INFO)
                .collect(toList());

        assertThat(infoMessages, hasSize(2));
    }

}