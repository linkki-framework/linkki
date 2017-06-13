package org.linkki.samples.messages.binding;

import java.util.function.Consumer;

import org.linkki.core.binding.DefaultBindingManager;
import org.linkki.core.message.MessageList;
import org.linkki.samples.messages.pmo.RegistrationValidationService;

public class RegistrationBindingManager extends DefaultBindingManager {

    private final Consumer<MessageList> messageListConsumer;

    public RegistrationBindingManager(RegistrationValidationService registrationValidationService, Consumer<MessageList> messageListConsumer) {
        super(registrationValidationService);
        this.messageListConsumer = messageListConsumer;
    }

    @Override
    protected void updateMessages(MessageList messages) {
        super.updateMessages(messages);
        this.messageListConsumer.accept(messages);
    }
}

