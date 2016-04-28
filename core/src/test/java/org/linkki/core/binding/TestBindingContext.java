/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

import org.faktorips.runtime.MessageList;
import org.linkki.core.binding.aspect.InjectablePropertyBehavior;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.ValidationService;

public class TestBindingContext extends BindingContext {

    private MessageList msgList = new MessageList();

    public TestBindingContext(ValidationService validationService, PropertyBehaviorProvider behaviorProvider) {
        super("testContext", validationService, behaviorProvider);
    }

    public static TestBindingContext create() {
        TestValidationService validationService = new TestValidationService();
        TestPropertyBehaviorProvider propertyBehaviorProvider = new TestPropertyBehaviorProvider();
        TestBindingContext bindingContext = new TestBindingContext(validationService, propertyBehaviorProvider);
        validationService.msgListSupplier = bindingContext::getMessageList;
        return bindingContext;
    }

    public void setMessageList(MessageList msgList) {
        this.msgList = msgList;
    }

    public MessageList getMessageList() {
        return msgList;
    }

    private static final class TestValidationService implements ValidationService {

        private Supplier<MessageList> msgListSupplier;

        @Override
        public MessageList getValidationMessages() {
            return msgListSupplier.get();
        }
    }

    private static final class TestPropertyBehaviorProvider implements PropertyBehaviorProvider {

        @Override
        public Collection<InjectablePropertyBehavior> getBehaviors() {
            return Collections.emptySet();
        }

    }

}
