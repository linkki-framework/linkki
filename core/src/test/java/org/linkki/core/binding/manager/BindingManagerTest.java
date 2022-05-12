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
package org.linkki.core.binding.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.Binding;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.BindingContext.BindingContextBuilder;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.ValidationDisplayState;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.defaults.nls.TestComponentWrapper;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.util.handler.Handler;
import org.linkki.util.validation.ValidationMarker;

import edu.umd.cs.findbugs.annotations.CheckForNull;

class BindingManagerTest {

    @Test
    void testAfterUpdateUi_sortsMessagesBySeverity() {
        Message e1 = Message.newError("e1", "E1");
        Message e2 = Message.newError("e2", "E2");
        Message e3 = Message.newError("e3", "E3");
        Message w1 = Message.newWarning("w1", "W1");
        Message w2 = Message.newWarning("w2", "W2");
        Message i1 = Message.newInfo("i1", "I1");
        Message i2 = Message.newInfo("i2", "I2");
        MessageList unsortedMessageList = new MessageList(i2, e1, w1, e3, i1, e2, w2);
        MessageList sortedMessageList = new MessageList(e1, e3, e2, w1, w2, i2, i1);
        ValidationService validationService = () -> unsortedMessageList;
        TestBindingManager bindingManager = new TestBindingManager(validationService);
        TestBindingContext context = bindingManager.getContext("foo");

        bindingManager.afterUpdateUi();

        assertThat(context.messages).isEqualTo(sortedMessageList);
    }

    @Test
    void testAfterUpdateUi_filtersMessages() {
        Message e1 = Message.newError("e1", "E1");
        Message e2 = Message.builder("E2", Severity.ERROR).code("e2").markers(ValidationMarker.REQUIRED).create();
        Message e3 = Message.newError("e3", "E3");
        Message w1 = Message.builder("W1", Severity.WARNING).code("w1").markers(ValidationMarker.REQUIRED).create();
        Message w2 = Message.newWarning("w2", "W2");
        Message i1 = Message.builder("I1", Severity.INFO).code("i1").markers(ValidationMarker.REQUIRED).create();
        Message i2 = Message.newInfo("i2", "I2");
        MessageList unsortedMessageList = new MessageList(i2, e1, w1, e3, i1, e2, w2);
        MessageList filteredMessageList = new MessageList(e1, e3, w2, i2);
        ValidationService validationService = new ValidationService() {
            @Override
            public MessageList getValidationMessages() {
                return unsortedMessageList;
            }

            @Override
            public ValidationDisplayState getValidationDisplayState() {
                return ValidationDisplayState.HIDE_MANDATORY_FIELD_VALIDATIONS;
            }
        };
        TestBindingManager bindingManager = new TestBindingManager(validationService);
        TestBindingContext context = bindingManager.getContext("foo");

        bindingManager.afterUpdateUi();

        assertThat(context.messages).isEqualTo(filteredMessageList);
    }

    @Test
    void testAfterUpdateUi_UpdateObserverChangesMessageRelevantField() {
        Message e1 = Message.newError("e1", "E1");
        MessageList messageList = new MessageList(e1);
        TestBinding binding = new TestBinding();
        ValidationService validationService = () -> messageList;
        TestBindingManager bindingManager = new TestBindingManager(validationService);
        TestBindingContext context = bindingManager.getContext("foo");
        bindingManager.addUiUpdateObserver(() -> context.add(binding, TestComponentWrapper.with(binding)));

        bindingManager.afterUpdateUi();

        assertThat(binding.getMessages()).isEqualTo(messageList);
    }

    @Test
    void testRegisterUiUpdateObserver() {
        TestBindingManager bindingManager = new TestBindingManager(MessageList::new);
        UiUpdateObserver observer = mock(UiUpdateObserver.class);

        bindingManager.afterUpdateUi();
        verify(observer, never()).uiUpdated();

        bindingManager.addUiUpdateObserver(observer);
        bindingManager.afterUpdateUi();

        verify(observer).uiUpdated();
    }

    @Test
    void testRemoveUiUpdateObserver() {
        TestBindingManager bindingManager = new TestBindingManager(MessageList::new);
        UiUpdateObserver observer = mock(UiUpdateObserver.class);
        bindingManager.addUiUpdateObserver(observer);

        bindingManager.removeUiUpdateObserver(observer);
        bindingManager.afterUpdateUi();

        verify(observer, never()).uiUpdated();
    }

    @Test
    void testUpdateAll() {
        BindingManager bindingManager = spy(new BindingManager(MessageList::new) {

            @Override
            protected BindingContext newBindingContext(String name, PropertyBehaviorProvider behaviorProvider) {
                return spy(new BindingContextBuilder().name(name).propertyBehaviorProvider(behaviorProvider).build());
            }

            @Override
            protected BindingContext newBindingContext(String name) {
                return newBindingContext(name, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
            }
        });
        BindingContext context1 = bindingManager.createContext("context1",
                                                               PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        BindingContext context2 = bindingManager.createContext("context2",
                                                               PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        bindingManager.updateAll();

        verify(context1).uiUpdated();
        verify(context2).uiUpdated();
        verify(bindingManager).afterUpdateUi();
    }

    private static class TestBindingContext extends BindingContext {

        @CheckForNull
        private MessageList messages;

        public TestBindingContext(String contextName, PropertyBehaviorProvider behaviorProvider,
                Handler afterUpdateHandler) {
            super(contextName, behaviorProvider, new PropertyDispatcherFactory(), afterUpdateHandler,
                    Handler.NOP_HANDLER);
        }

        @Override
        public MessageList displayMessages(MessageList newMessages) {
            super.displayMessages(newMessages);
            return this.messages = newMessages;
        }

    }

    private static class TestBindingManager extends BindingManager {

        public TestBindingManager(ValidationService validationService) {
            super(validationService);
        }

        @Override
        public TestBindingContext getContext(Class<?> clazz) {
            return (TestBindingContext)super.getContext(clazz);
        }

        @Override
        public TestBindingContext getContext(String name) {
            return (TestBindingContext)super.getContext(name);
        }

        @Override
        protected TestBindingContext newBindingContext(String name) {
            return new TestBindingContext(name, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER, this::afterUpdateUi);
        }

        @Override
        protected BindingContext newBindingContext(String name, PropertyBehaviorProvider behaviorProvider) {
            return new TestBindingContext(name, behaviorProvider, this::afterUpdateUi);
        }

    }

    private static class TestBinding implements Binding {

        private final Object boundObject;

        private final Object pmo;

        private MessageList messages = new MessageList();

        public TestBinding() {
            boundObject = new TestUiComponent();
            pmo = new Object();
        }

        @Override
        public Object getBoundComponent() {
            return boundObject;
        }

        @Override
        public Object getPmo() {
            return pmo;
        }

        @Override
        public void updateFromPmo() {
            // do nothing
        }

        @Override
        public MessageList displayMessages(MessageList messagesToDisplay) {
            this.messages = messagesToDisplay;
            return messagesToDisplay;
        }

        public MessageList getMessages() {
            return messages;
        }

    }
}
