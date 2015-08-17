/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding.dispatcher;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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
    public void testPrepareUpdateUI() {
        decorator.prepareUpdateUI();
        verify(wrappedDispatcher).prepareUpdateUI();
    }

    @Test
    public void testGetValue() {
        decorator.getValue("xyz");
        verify(wrappedDispatcher).getValue("xyz");
    }

    @Test
    public void testSetValue() {
        decorator.setValue("xyz", "value");
        verify(wrappedDispatcher).setValue("xyz", "value");
    }

    @Test
    public void testGetValueClass() {
        decorator.getValueClass("xyz");
        verify(wrappedDispatcher).getValueClass("xyz");
    }

    @Test
    public void testIsEnabled() {
        decorator.isEnabled("xyz");
        verify(wrappedDispatcher).isEnabled("xyz");
    }

    @Test
    public void testIsReadonly() {
        decorator.isReadonly("xyz");
        verify(wrappedDispatcher).isReadonly("xyz");
    }

    @Test
    public void testIsVisible() {
        decorator.isVisible("xyz");
        verify(wrappedDispatcher).isVisible("xyz");
    }

    @Test
    public void testIsRequired() {
        decorator.isRequired("xyz");
        verify(wrappedDispatcher).isRequired("xyz");
    }

    @Test
    public void testGetAvailableValues() {
        decorator.getAvailableValues("xyz");
        verify(wrappedDispatcher).getAvailableValues("xyz");
    }

    @Test
    public void testMessages() {
        decorator.getMessages("xyz");
        verify(wrappedDispatcher).getMessages("xyz");
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
