/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding.dispatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.faktorips.runtime.IModelObject;
import org.faktorips.runtime.IValidationContext;
import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.faktorips.runtime.Severity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageDecoratorTest {

    @Mock
    private PropertyDispatcher wrappedDispatcher;

    @SuppressWarnings("synthetic-access")
    private TestModelObject modelObject = new TestModelObject();
    private Object arbitraryObject;

    private MessageDecorator messageDecorator;

    @Before
    public void setUp() {
        messageDecorator = new MessageDecorator(wrappedDispatcher, modelObject);
        messageDecorator.prepareUpdateUI();
    }

    @Test
    public void testGetMessage() {
        assertEquals(1, messageDecorator.getMessages("xyz").size());
    }

    @Test
    public void testGetMessage2() {
        assertEquals(1, messageDecorator.getMessages("abc").size());
    }

    @Test
    public void testGetMessage_illegalProperty() {
        assertEquals(0, messageDecorator.getMessages("illegalProperty").size());
    }

    @Test
    public void testGetMessage_nullProperty() {
        assertEquals(0, messageDecorator.getMessages(null).size());
    }

    @Test
    public void testGetMessage_useCachedMessageList() {
        MessageList firstCall = messageDecorator.getMessages("xyz");
        MessageList secondCall = messageDecorator.getMessages("xyz");
        assertSame(firstCall.getMessage(0), secondCall.getMessage(0));
    }

    @Test
    public void testGetMessage_validateOnPrepareUpdateUI() {
        MessageList firstCall = messageDecorator.getMessages("xyz");
        messageDecorator.prepareUpdateUI();
        MessageList secondCall = messageDecorator.getMessages("xyz");
        assertNotSame(firstCall.getMessage(0), secondCall.getMessage(0));
    }

    @Test
    public void testGetMessage_emptyWithoutPrepareUpdateUI() {
        messageDecorator = new MessageDecorator(wrappedDispatcher, arbitraryObject);
        assertTrue(messageDecorator.getMessages("xyz").isEmpty());
    }

    @Test
    public void testGetMessage_noMessagesFromArbitraryObject() {
        messageDecorator = new MessageDecorator(wrappedDispatcher, arbitraryObject);
        messageDecorator.prepareUpdateUI();
        assertTrue(messageDecorator.getMessages("xyz").isEmpty());
    }

    private static class TestModelObject implements IModelObject {

        @Override
        public MessageList validate(IValidationContext context) {
            MessageList messageList = new MessageList();
            messageList.add(new Message.Builder("text", Severity.ERROR).invalidObjects(this, "xyz").create());
            messageList.add(new Message.Builder("text2", Severity.WARNING).invalidObjects(this, "abc").create());
            return messageList;
        }
    }

}
