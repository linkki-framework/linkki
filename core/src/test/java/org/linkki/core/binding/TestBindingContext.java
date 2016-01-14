/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import java.util.function.Supplier;

import org.faktorips.runtime.MessageList;
import org.linkki.core.binding.validation.ValidationService;

public class TestBindingContext extends BindingContext {

    private MessageList msgList = new MessageList();

    public TestBindingContext(ValidationService validationService) {
        super("testContext", validationService);
    }

    public static TestBindingContext create() {
        TestValidationService validationService = new TestValidationService();
        TestBindingContext bindingContext = new TestBindingContext(validationService);
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

}
