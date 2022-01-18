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
package org.linkki.framework.ui.dialogs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.ui.wrapper.FormItemComponentWrapper;
import org.linkki.core.vaadin.component.base.LabelComponentFormItem;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;

public class DialogBindingManagerTest {

    @Test
    public void testAfterUpdateUI_ValidatesDialog() {
        MessageList messages = new MessageList();
        OkCancelDialog dialog = OkCancelDialog.builder("").build();
        DialogBindingManager manager = new DialogBindingManager(dialog, ValidationService.of(messages));

        manager.afterUpdateUi();
        assertThat(dialog.getValidationService().getValidationMessages(), is(messages));
    }

    @Test
    public void testBindingsInCreatedContextsDisplayMessagesFromDialog() {
        OkCancelDialog dialog = OkCancelDialog.builder("").build();

        // Use the NOP validation service in the binding manager
        DialogBindingManager manager = new DialogBindingManager(dialog, ValidationService.NOP_VALIDATION_SERVICE);
        BindingContext ctx = manager.getContext("foo");

        // Change the dialog's validation service to make sure dialog's validate method is used and
        // not the validation service the manager was created with (so that the dialog could filter
        // the messages)
        MessageList messages = new MessageList(Message.newError("code", "text"));
        dialog.setValidationService(ValidationService.of(messages));

        PropertyDispatcher propertyDispatcher = mock(PropertyDispatcher.class);
        Object pmo = mock(Object.class);
        when(propertyDispatcher.getBoundObject()).thenReturn(pmo);
        when(propertyDispatcher.getMessages(any())).thenReturn(new MessageList());

        FormItemComponentWrapper componentWrapper = new FormItemComponentWrapper(
                new LabelComponentFormItem(new Button(), new Label()));
        ElementBinding binding = spy(new ElementBinding(
                componentWrapper,
                propertyDispatcher, Handler.NOP_HANDLER, new ArrayList<>()));
        ctx.add(binding, componentWrapper);

        ctx.modelChanged();

        verify(binding).displayMessages(messages);
    }

}
