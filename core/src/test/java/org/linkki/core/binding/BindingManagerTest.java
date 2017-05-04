/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.util.MessageListUtil;
import org.linkki.util.handler.Handler;
import org.mockito.runners.MockitoJUnitRunner;

@SuppressWarnings("null")
@RunWith(MockitoJUnitRunner.class)
public class BindingManagerTest {
    private ValidationService validationService;

    @Test
    public void testAfterUpdateUi_sortsMessagesBySeverity() {
        Message e1 = Message.newError("e1", "E1");
        Message e2 = Message.newError("e2", "E2");
        Message e3 = Message.newError("e3", "E3");
        Message w1 = Message.newWarning("w1", "W1");
        Message w2 = Message.newWarning("w2", "W2");
        Message i1 = Message.newInfo("i1", "I1");
        Message i2 = Message.newInfo("i2", "I2");
        MessageList unsortedMessageList = MessageListUtil.newMessageList(i2, e1, w1, e3, i1, e2, w2);
        MessageList sortedMessageList = MessageListUtil.newMessageList(e1, e3, e2, w1, w2, i2, i1);
        validationService = () -> unsortedMessageList;
        TestBindingManager bindingManager = new TestBindingManager(validationService);
        TestBindingContext context = bindingManager.startNewContext("foo");

        bindingManager.afterUpdateUi();

        assertThat(context.messages, is(equalTo(sortedMessageList)));
    }

    @Test
    public void testRegisterUiUpdateObserver() {
        TestBindingManager bindingManager = new TestBindingManager(() -> new MessageList());
        UiUpdateObserver observer = mock(UiUpdateObserver.class);

        bindingManager.afterUpdateUi();
        verify(observer, never()).notifyUIUpdate();

        bindingManager.addUiUpdateObserver(observer);
        bindingManager.afterUpdateUi();

        verify(observer).notifyUIUpdate();
    }

    @Test
    public void testRemoveUiUpdateObserver() {
        TestBindingManager bindingManager = new TestBindingManager(() -> new MessageList());
        UiUpdateObserver observer = mock(UiUpdateObserver.class);
        bindingManager.addUiUpdateObserver(observer);

        bindingManager.removeUiUpdateObserver(observer);
        bindingManager.afterUpdateUi();

        verify(observer, never()).notifyUIUpdate();
    }

    private static class TestBindingContext extends BindingContext {

        private MessageList messages;

        public TestBindingContext(String contextName, PropertyBehaviorProvider behaviorProvider,
                Handler afterUpdateHandler) {
            super(contextName, behaviorProvider, afterUpdateHandler);
        }

        @Override
        public void updateMessages(MessageList newMessages) {
            this.messages = newMessages;
        }
    }

    private static class TestBindingManager extends BindingManager {

        public TestBindingManager(ValidationService validationService) {
            super(validationService);
        }

        @Override
        public TestBindingContext startNewContext(String name) {
            return (TestBindingContext)super.startNewContext(name);
        }

        @Override
        protected TestBindingContext newBindingContext(String name) {
            return new TestBindingContext(name, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER, this::afterUpdateUi);
        }

    }

}
