/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.dialogs;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.junit.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ButtonBinding;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;

public class DialogBindingManagerTest {

    @Test
    public void testAfterUpdateUI_ValidatesDialog() {
        MessageList messages = new MessageList();
        OkCancelDialog dialog = new OkCancelDialog("");
        DialogBindingManager manager = new DialogBindingManager(dialog, ValidationService.of(messages));

        manager.afterUpdateUi();
        assertThat(dialog.getMessages(), is(messages));
    }

    @Test
    public void testBindingsInCreatedContextsDisplayMessagesFromDialog() {
        OkCancelDialog dialog = new OkCancelDialog("");

        // Use the NOP validation service in the binding manager
        DialogBindingManager manager = new DialogBindingManager(dialog, ValidationService.NOP_VALIDATION_SERVICE);
        BindingContext ctx = manager.startNewContext("foo");

        // Change the dialog's validation service to make sure dialog's validate method is used and
        // not the validation service the manager was created with (so that the dialog could filter
        // the messages)
        MessageList messages = new MessageList(Message.newError("code", "text"));
        dialog.setValidationService(ValidationService.of(messages));

        ButtonBinding binding = spy(new ButtonBinding(new Label(), new Button(), mock(PropertyDispatcher.class),
                Handler.NOP_HANDLER, false));
        ctx.add(binding);

        ctx.updateUIForBinding();

        verify(binding).displayMessages(messages);
    }

}
