/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import java.util.Optional;
import java.util.function.Supplier;

import org.faktorips.runtime.MessageList;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.util.handler.Handler;

public class TestBindingContext extends BindingContext {

    private MessageList msgList = new MessageList();

    public TestBindingContext(PropertyBehaviorProvider behaviorProvider, Handler afterUpdateHandler) {
        super("testContext", behaviorProvider, afterUpdateHandler);
    }

    public static TestBindingContext create() {
        return create(null);
    }

    public static TestBindingContext create(Handler afterUpdateUi) {
        TestValidationService validationService = new TestValidationService();
        TestBindingManager bindingManager = new TestBindingManager(validationService,
                Optional.ofNullable(afterUpdateUi));
        TestBindingContext bindingContext = bindingManager.startNewContext("");
        validationService.msgListSupplier = bindingContext::getMessageList;
        return bindingContext;
    }

    public void setMessageList(MessageList msgList) {
        this.msgList = msgList;
    }

    public MessageList getMessageList() {
        return msgList;
    }

    private static final class TestBindingManager extends BindingManager {
        private Handler afterUpdateUi;

        public TestBindingManager(TestValidationService validationService, Optional<Handler> afterUpdateUi) {
            super(validationService);
            this.afterUpdateUi = afterUpdateUi.orElse(this::afterUpdateUi);
        }

        @Override
        protected TestBindingContext newBindingContext(String name) {
            return new TestBindingContext(PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER, afterUpdateUi);
        }

        @Override
        public TestBindingContext startNewContext(String name) {
            return (TestBindingContext)super.startNewContext(name);
        }
    }

    private static final class TestValidationService implements ValidationService {

        private Supplier<MessageList> msgListSupplier;

        @Override
        public MessageList getValidationMessages() {
            return msgListSupplier.get();
        }
    }

}
