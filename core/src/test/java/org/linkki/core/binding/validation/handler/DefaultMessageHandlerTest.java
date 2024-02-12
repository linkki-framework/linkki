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

package org.linkki.core.binding.validation.handler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.fallback.ExceptionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.reflection.ReflectionPropertyDispatcher;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.defaults.nls.TestComponentWrapper;
import org.linkki.core.defaults.nls.TestUiComponent;

/**
 * Tests the {@link DefaultMessageHandler} which provides the default way how messages should be
 * handled if no specific message handler is specified.
 */
class DefaultMessageHandlerTest {

    private static final String BOUND_PROPERTY = "bound-prop";

    private final Object boundObject = new Object();

    private final DefaultMessageHandler defaultMessageHandler = new DefaultMessageHandler();

    @Test
    void testProcess() {
        Message validMessage = Message.builder("test", Severity.ERROR)
                .invalidObjectWithProperties(boundObject, BOUND_PROPERTY)
                .create();
        Message invalidMessage = Message.builder("test", Severity.ERROR)
                .invalidObjectWithProperties(boundObject, "other-prop")
                .create();
        MessageList messages = new MessageList(validMessage, invalidMessage);
        TestComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());
        PropertyDispatcher propertyDispatcher = new ReflectionPropertyDispatcher(() -> boundObject, Object.class,
                BOUND_PROPERTY,
                new ExceptionPropertyDispatcher(BOUND_PROPERTY));

        defaultMessageHandler.process(messages, componentWrapper, propertyDispatcher);

        assertThat(componentWrapper.getComponent().getValidationMessages()).containsExactly(validMessage);
    }

    @Test
    void testProcess_ContainsFatalErrors() {
        Message fatalMessage = Message.builder("test", Severity.ERROR)
                .invalidObjectWithProperties(boundObject, "any")
                .code(ValidationService.FATAL_ERROR_MESSAGE_CODE)
                .create();
        MessageList messages = new MessageList(fatalMessage);
        TestComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());
        PropertyDispatcher propertyDispatcher = new ReflectionPropertyDispatcher(() -> boundObject, Object.class,
                BOUND_PROPERTY,
                new ExceptionPropertyDispatcher(BOUND_PROPERTY));

        defaultMessageHandler.process(messages, componentWrapper, propertyDispatcher);

        assertThat(componentWrapper.getComponent().getValidationMessages()).containsExactly(fatalMessage);
    }

}
