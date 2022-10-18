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

package org.linkki.core.binding.descriptor.messagehandler.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.messagehandler.DefaultMessageHandler;
import org.linkki.core.binding.descriptor.messagehandler.LinkkiMessageHandler;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.binding.wrapper.ComponentWrapper;

/**
 * The annotation reader must always return a {@link LinkkiMessageHandler}. It is either a configured
 * message handler or the {@link DefaultMessageHandler} in case there is no message handler configured.
 * To not test for concrete instances and to not rely on specific implementation the following behavior
 * is assumed: With the mocked {@link ComponentWrapper} and {@link PropertyDispatcher} the default
 * message handler returns {@link #DEFAULT_MESSAGE} whereas a special configured message handler returns
 * {@link #SPECIFIC_MESSAGE}. This way we can test whether the default behavior or the configured
 * message handler is used.
 */
class MessageHandlerAnnotationReaderTest {

    static final Message SPECIFIC_MESSAGE = Message.builder("test-special-message", Severity.ERROR).create();
    static final Message DEFAULT_MESSAGE = Message.builder("default-message", Severity.ERROR).create();

    private final PropertyDispatcher propertyDispatcher = mock(PropertyDispatcher.class);
    private final ComponentWrapper componentWrapper = mock(ComponentWrapper.class);

    @BeforeEach
    void setUp() {
        when(propertyDispatcher.getMessages(any(MessageList.class)))
                .thenReturn(new MessageList(DEFAULT_MESSAGE));
    }

    @Test
    void testGetMessageHandler_AnnotatedElement_NoSpecificHandler() {
        Method currentMethod = new Object() {
            // trick to get the current method
        }.getClass().getEnclosingMethod();

        LinkkiMessageHandler messageHandler = MessageHandlerAnnotationReader.getMessageHandler(currentMethod);
        MessageList messageList = messageHandler.process(new MessageList(SPECIFIC_MESSAGE), componentWrapper,
                propertyDispatcher);

        assertThat(messageList).isEqualTo(new MessageList(DEFAULT_MESSAGE));
    }

    @TestMessageHandlerAnnotation
    @Test
    void testGetMessageHandler_AnnotatedElement_AnnotatedWithSpecificHandler() {
        Method currentMethod = new Object() {
            // trick to get the current method
        }.getClass().getEnclosingMethod();

        LinkkiMessageHandler messageHandler = MessageHandlerAnnotationReader.getMessageHandler(currentMethod);
        MessageList messageList = messageHandler.process(new MessageList(SPECIFIC_MESSAGE), componentWrapper,
                propertyDispatcher);

        assertThat(messageList).isEqualTo(new MessageList(SPECIFIC_MESSAGE));
    }

    @TestMessageHandlerAnnotation
    @TestMessageHandlerAnnotation2
    @Test
    void testGetMessageHandler_AnnotatedElement_AnnotatedWithMultipleSpecificHandler() {
        Method currentMethod = new Object() {
            // trick to get the current method
        }.getClass().getEnclosingMethod();

        assertThatThrownBy(() -> MessageHandlerAnnotationReader.getMessageHandler(currentMethod))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Retention(RUNTIME)
    @Target(METHOD)
    @LinkkiMessages(TestMessageHandlerCreator.class)
    @interface TestMessageHandlerAnnotation {
        // nothing
    }

    @Retention(RUNTIME)
    @Target(METHOD)
    @LinkkiMessages(TestMessageHandlerCreator.class)
    @interface TestMessageHandlerAnnotation2 {
        // nothing
    }


    public static class TestMessageHandlerCreator implements MessageHandlerCreator<Annotation> {

        @Override
        public LinkkiMessageHandler create(Annotation annotation, AnnotatedElement annotatedElement) {
            return (m, cw, p) -> m;
        }

    }

}
