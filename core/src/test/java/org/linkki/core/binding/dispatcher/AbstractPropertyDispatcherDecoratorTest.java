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
package org.linkki.core.binding.dispatcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.validation.message.MessageList;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AbstractPropertyDispatcherDecoratorTest {

    @Mock
    private PropertyDispatcher wrappedDispatcher;

    private TestDecorator decorator;

    @BeforeEach
    public void setUp() {
        decorator = new TestDecorator(wrappedDispatcher);
    }

    @Test
    @SuppressWarnings("unused")
    // warning suppressed as object is created to test the constructor, not to use it
    public void testConstructor() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new TestDecorator(null);
        });
    }

    @Test
    public void testGetValue() {
        Aspect<Object> aspect = Aspect.of("");
        decorator.pull(aspect);
        verify(wrappedDispatcher).pull(aspect);
    }

    @Test
    public void testGetValueClass() {
        decorator.getValueClass();
        verify(wrappedDispatcher).getValueClass();
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
