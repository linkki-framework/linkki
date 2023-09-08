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
 *
 */

package org.linkki.core.binding.validation.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.descriptor.messagehandler.LinkkiMessageHandler;
import org.linkki.core.binding.descriptor.messagehandler.annotation.MessageHandlerAnnotationReader;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.annotation.BindMessages;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.section.annotations.TestUIField;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.uicreation.UiCreatorTest.UITestSection;

/**
 * Test class for {@link BindMessages @BindMessages}.
 */
class BindValidationMessagesHandlerTest {

    private static final Message ERROR_MESSAGE = new Message("Error", "Error text", Severity.ERROR);
    private static final Message WARNING_MESSAGE = new Message("Warning", "Warning text", Severity.WARNING);
    private static final Message INFO_MESSAGE = new Message("Info", "Info text", Severity.INFO);
    private static final MessageList MESSAGES = new MessageList(INFO_MESSAGE, WARNING_MESSAGE, ERROR_MESSAGE);

    private static final String ERRORS_ONLY_TEXT_FIELD = "getOnlyErrorsTextField";

    @Test
    void testProcessMessages_FilterOnlyErrorMessages() throws NoSuchMethodException {
        TestPmo pmo = new TestPmo();
        PropertyDispatcher dispatcher = mockPropertyDispatcher(pmo);
        ComponentWrapper componentWrapper = mock(ComponentWrapper.class);
        LinkkiMessageHandler messageHandler = getMessageHandlerForMethod(pmo, ERRORS_ONLY_TEXT_FIELD);

        MessageList processedMessages = messageHandler.process(MESSAGES, componentWrapper, dispatcher);

        assertThat(processedMessages).containsOnly(ERROR_MESSAGE);
    }

    @Test
    void testProcessMessages_FilterWithMultipleAnnotations() throws NoSuchMethodException {
        TestPmo pmo = new TestPmo();
        PropertyDispatcher dispatcher = mockPropertyDispatcher(pmo);
        ComponentWrapper componentWrapper = mock(ComponentWrapper.class);
        LinkkiMessageHandler messageHandler = getMessageHandlerForMethod(pmo, "getMultipleAnnotationsTextField");

        MessageList processedMessages = messageHandler.process(MESSAGES, componentWrapper, dispatcher);

        assertThat(processedMessages).containsExactlyInAnyOrder(WARNING_MESSAGE, INFO_MESSAGE);
    }

    @Test
    void testProcessMessages_UseCache() throws NoSuchMethodException {
        TestPmo pmo1 = new TestPmo();
        TestPmo pmo2 = new TestPmo();
        ComponentWrapper componentWrapper = mock(ComponentWrapper.class);
        PropertyDispatcher dispatcher1 = mockPropertyDispatcher(pmo1);
        PropertyDispatcher dispatcher2 = mockPropertyDispatcher(pmo2);
        LinkkiMessageHandler messageHandler1 = getMessageHandlerForMethod(pmo1, ERRORS_ONLY_TEXT_FIELD);
        LinkkiMessageHandler messageHandler2 = getMessageHandlerForMethod(pmo2, ERRORS_ONLY_TEXT_FIELD);

        MessageList messages1 = messageHandler1.process(MESSAGES, componentWrapper, dispatcher1);
        MessageList messages2 = messageHandler2.process(MESSAGES, componentWrapper, dispatcher2);

        assertThat(messages1).isEqualTo(messages2);
    }

    @Test
    void testProcessMessages_NoMessageGetterFound() throws NoSuchMethodException {
        Method method = TestPmo.class.getMethod("getNoMessagesGetterTextField");
        assertThatThrownBy(() -> MessageHandlerAnnotationReader.getMessageHandler(method))
                .isInstanceOf(LinkkiBindingException.class)
                .hasMessage("Cannot find method getNoMessagesGetterTextFieldMessages(MessagesList) " +
                        "in " + TestPmo.class.getName());
    }

    @Test
    void testProcessMessages_UsageInGridReturnEmptyMessageList() throws NoSuchMethodException {
        TestPmo pmo = new TestPmo();
        PropertyDispatcher dispatcher = mock(PropertyDispatcher.class);
        when(dispatcher.getBoundObject()).thenReturn(TestPmo.class);
        LinkkiMessageHandler messageHandler = getMessageHandlerForMethod(pmo, ERRORS_ONLY_TEXT_FIELD);

        assertThat(messageHandler).isInstanceOf(BindValidationMessagesHandler.class);

        BindValidationMessagesHandler validationHandler = (BindValidationMessagesHandler)messageHandler;

        var relevantMessages = validationHandler.getRelevantMessages(MESSAGES, dispatcher);
        assertThat(relevantMessages).isEmpty();
    }

    /**
     * Mocks a {@link PropertyDispatcher} which contains the {@link TestPmo} as the bound object and
     * returns defined {@link #MESSAGES messages}.
     *
     * @param pmo The bound PMO object
     */
    private PropertyDispatcher mockPropertyDispatcher(TestPmo pmo) {
        PropertyDispatcher dispatcher = mock(PropertyDispatcher.class);
        when(dispatcher.getMessages(any())).thenReturn(MESSAGES);
        when(dispatcher.getBoundObject()).thenReturn(pmo);
        return dispatcher;
    }

    /**
     * Mocks a {@link LinkkiMessageHandler}.
     *
     * @param pmo The used PMO
     * @param methodName The name of the method of the class {@link TestPmo} for which the handler
     *            should be created
     * @throws NoSuchMethodException when the method with the given name does not exist
     * @return the mock
     */
    private LinkkiMessageHandler getMessageHandlerForMethod(TestPmo pmo, String methodName)
            throws NoSuchMethodException {
        Method method = pmo.getClass().getMethod(methodName);
        return MessageHandlerAnnotationReader.getMessageHandler(method);
    }

    /**
     * Test PMO class with methods which use the {@link BindMessages @BindMessages} annotation.
     */
    @UITestSection
    public static class TestPmo {

        @BindMessages
        @TestUIField(position = 10)
        public String getOnlyErrorsTextField() {
            return "OnlyErrors";
        }

        public MessageList getOnlyErrorsTextFieldMessages(MessageList messages) {
            return messages.stream()
                    .filter(m -> Severity.ERROR == m.getSeverity())
                    .collect(MessageList.collector());
        }

        @BindMessages
        @BindTooltip("Tooltip")
        @TestUIField(position = 20)
        public String getMultipleAnnotationsTextField() {
            return "MultipleAnnotations";
        }

        public MessageList getMultipleAnnotationsTextFieldMessages(MessageList messages) {
            return messages.stream()
                    .filter(m -> Severity.ERROR != m.getSeverity())
                    .collect(MessageList.collector());
        }

        @BindMessages
        @TestUIField(position = 30)
        public String getNoMessagesGetterTextField() {
            return "NoMessagesGetter";
        }
    }
}
