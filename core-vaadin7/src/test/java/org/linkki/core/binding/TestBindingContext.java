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
package org.linkki.core.binding;

import java.util.Optional;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.Nullable;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.message.MessageList;
import org.linkki.util.handler.Handler;

@SuppressWarnings("null")
public class TestBindingContext extends BindingContext {

    private MessageList msgList = new MessageList();

    public TestBindingContext(PropertyBehaviorProvider behaviorProvider, Handler afterUpdateHandler) {
        super("testContext", behaviorProvider, afterUpdateHandler);
    }

    public static TestBindingContext create() {
        return create(null);
    }

    public static TestBindingContext create(@Nullable Handler afterUpdateUi) {
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
