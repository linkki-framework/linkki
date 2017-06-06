/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.message;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.linkki.core.matcher.MessageMatchers.hasSize;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.junit.Before;
import org.junit.Test;
import org.linkki.util.validation.ValidationMarker;

public class MessageListTest {

    private Message msg1;
    private Message msg2;
    private Message msg3;

    private MessageList msgList1;
    private MessageList msgList2;
    private ObjectProperty invalidObjectProperty1;
    private ObjectProperty invalidObjectProperty2;
    private ObjectProperty invalidObjectProperty3;

    @Before
    public void setUp() {

        invalidObjectProperty1 = new ObjectProperty("A", "testProperty");
        invalidObjectProperty2 = new ObjectProperty("B", "anotherTestProperty");
        invalidObjectProperty3 = new ObjectProperty("C", "higherIndexProperty", 2);


        msg1 = Message.builder("Test1", Severity.INFO).invalidObject(invalidObjectProperty1).create();
        msg2 = Message.builder("Test2", Severity.INFO).invalidObject(invalidObjectProperty2).create();
        msg3 = Message.builder("Test3", Severity.INFO).invalidObject(invalidObjectProperty3).create();

        msgList1 = new MessageList(msg1);
        msgList1.add(msg2);
        msgList1.add(msg3);

        msgList2 = new MessageList();
    }


    @Test
    public void testGetMessagesFor_shouldAddMessage() {

        assertThat(msgList1.getMessagesFor("A", "testProperty", -1).getMessage(0), is(msg1));
        assertThat(msgList1.getMessagesFor("B", "anotherTestProperty", -1).getMessage(0), is(msg2));
        assertThat(msgList1.getMessagesFor("B", null, -1).getMessage(0), is(msg2));
        assertThat(msgList1.getMessagesFor("C", "higherIndexProperty", 2).getMessage(0), is(msg3));
        assertThat(msgList1.getMessagesFor("C", "higherIndexProperty", -1).getMessage(0), is(msg3));
    }

    @Test
    public void testGetMessagesFor_shouldNotAddMessage() {

        assertThat(msgList1.getMessagesFor("A", "anotherTestProperty", -1).isEmpty(), is(true));
        assertThat(msgList1.getMessagesFor("B", "anotherTestProperty", 1).isEmpty(), is(true));
        assertThat(msgList1.getMessagesFor("C", "higherIndexProperty", 1).isEmpty(), is(true));
    }

    @Test
    public void testGetMessagesByMarker_withValidationMarker() {
        ValidationMarker marker = () -> false;

        MessageList messages = new MessageList(Message.builder("msg1", Severity.INFO).create(),
                Message.builder("msgWithMarker", Severity.WARNING).markers(marker).create());

        MessageList messagesByMarker = messages.getMessagesByMarker(marker);
        assertThat(messagesByMarker, is(not(sameInstance(messages))));
        assertThat(messagesByMarker, hasSize(1));
        assertThat(messagesByMarker.getMessage(0), is(messages.getMessage(1)));
    }

    @Test
    public void testGetMessagesByMarker_markerNull_shouldReturnAllMessagesWithoutMarker() {

        MessageList messages = new MessageList(Message.builder("msg1", Severity.INFO).create(),
                Message.builder("msgWithMarker", Severity.WARNING).markers(() -> false).create());

        MessageList messagesByMarker = messages.getMessagesByMarker((ValidationMarker)null);
        assertThat(messagesByMarker, is(not(sameInstance(messages))));
        assertThat(messagesByMarker, hasSize(1));
        assertThat(messagesByMarker.getMessage(0), is(messages.getMessage(0)));
    }

    @Test
    public void testGetMessagesByMarker_predicate() {

        MessageList messages = new MessageList(Message.builder("msgWithoutMarker", Severity.INFO).create(),
                Message.builder("msgWithMatchingMarker", Severity.WARNING).markers(() -> true).create(),
                Message.builder("msgWithNonMathingMarker", Severity.ERROR).markers(() -> false).create());

        MessageList messagesByMarker = messages.getMessagesByMarker(ValidationMarker::isMandatoryFieldValidation);
        assertThat(messagesByMarker, is(not(sameInstance(messages))));
        assertThat(messagesByMarker, hasSize(1));
        assertThat(messagesByMarker.getMessage(0), is(messages.getMessage(1)));
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessagesByMarker_predicateNull_shouldThrowNullPointerException() {
        new MessageList().getMessagesByMarker((Predicate)null);
    }

    @Test
    public void testGetMessageWithHighestSeverity() {

        MessageList messages = new MessageList(Message.builder("info", Severity.INFO).create(),
                Message.builder("error", Severity.ERROR).create(),
                Message.builder("warning", Severity.WARNING).create());

        @Nonnull
        Message message = messages.getMessageWithHighestSeverity();
        assertThat(message.getSeverity(), is(Severity.ERROR));

    }

    @Test
    public void testGetMessageWithHighestSeverity_null() {

        MessageList messages = new MessageList();

        @Nonnull
        Message message = messages.getMessageWithHighestSeverity();
        assertNull(message);

    }
}
