/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.message.MessageList;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("null")
public class AbstractPropertyDispatcherDecoratorTest {
    @Mock
    private PropertyDispatcher wrappedDispatcher;

    private TestDecorator decorator;

    @Before
    public void setUp() {
        decorator = new TestDecorator(wrappedDispatcher);
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("unused")
    // warning suppressed as object is created to test the constructor, not to use it
    public void testConstructor() {
        new TestDecorator(null);
    }

    @Test
    public void testGetValue() {
        decorator.getValue();
        verify(wrappedDispatcher).getValue();
    }

    @Test
    public void testSetValue() {
        decorator.setValue("value");
        verify(wrappedDispatcher).setValue("value");
    }

    @Test
    public void testGetValueClass() {
        decorator.getValueClass();
        verify(wrappedDispatcher).getValueClass();
    }

    @Test
    public void testIsEnabled() {
        decorator.isEnabled();
        verify(wrappedDispatcher).isEnabled();
    }

    @Test
    public void testIsReadOnly() {
        decorator.isReadOnly();
        verify(wrappedDispatcher).isReadOnly();
    }

    @Test
    public void testIsVisible() {
        decorator.isVisible();
        verify(wrappedDispatcher).isVisible();
    }

    @Test
    public void testIsRequired() {
        decorator.isRequired();
        verify(wrappedDispatcher).isRequired();
    }

    @Test
    public void testGetAvailableValues() {
        decorator.getAvailableValues();
        verify(wrappedDispatcher).getAvailableValues();
    }

    @Test
    public void testMessages() {
        MessageList messageList = new MessageList();
        decorator.getMessages(messageList);
        verify(wrappedDispatcher).getMessages(messageList);
    }

    @Test
    public void testGetWrappedDispatcher() {
        assertEquals(wrappedDispatcher, decorator.getWrappedDispatcher());
    }

    private static class TestDecorator extends AbstractPropertyDispatcherDecorator {

        public TestDecorator(PropertyDispatcher wrappedDispatcher) {
            super(wrappedDispatcher);
        }

    }

}
