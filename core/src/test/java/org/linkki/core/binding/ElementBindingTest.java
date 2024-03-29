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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.handler.DefaultMessageHandler;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.defaults.nls.TestComponentWrapper;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.util.handler.Handler;

import edu.umd.cs.findbugs.annotations.NonNull;

class ElementBindingTest {

    private final TestUiComponent field = spy(new TestUiComponent());
    private final TestUiComponent selectField = spy(new TestUiComponent());

    private ElementBinding selectBinding;

    private PropertyDispatcher propertyDispatcherValue;

    private MessageList messageList;

    private PropertyDispatcher propertyDispatcherEnumValue;

    @BeforeEach
    void setUp() {
        propertyDispatcherValue = mock(PropertyDispatcher.class);
        when(propertyDispatcherValue.getProperty()).thenReturn("value");
        propertyDispatcherEnumValue = mock(PropertyDispatcher.class);
        when(propertyDispatcherEnumValue.getProperty()).thenReturn("enumValue");
        doReturn(TestEnum.class).when(propertyDispatcherEnumValue).getValueClass();

        messageList = new MessageList();
        when(propertyDispatcherValue.getMessages(any(MessageList.class))).thenReturn(messageList);
        when(propertyDispatcherEnumValue.getMessages(any(MessageList.class))).thenReturn(messageList);

        selectBinding = new ElementBinding(new TestComponentWrapper(selectField),
                propertyDispatcherEnumValue,
                Handler.NOP_HANDLER,
                new ArrayList<>(), new DefaultMessageHandler());
    }

    @Test
    void testUpdateBindings_updateAspect() {
        Handler componentUpdater = mock(Handler.class);
        LinkkiAspectDefinition aspectDefinition = mock(LinkkiAspectDefinition.class);
        when(aspectDefinition.supports(any())).thenReturn(true);
        when(aspectDefinition.createUiUpdater(any(), any())).thenReturn(componentUpdater);
        ElementBinding fieldBinding = new ElementBinding(new TestComponentWrapper(field),
                propertyDispatcherValue,
                Handler.NOP_HANDLER, Arrays.asList(aspectDefinition), new DefaultMessageHandler());
        fieldBinding.updateFromPmo();

        verify(componentUpdater).apply();
    }

    @Test
    void testDisplayMessages() {
        messageList.add(Message.newError("code", "text"));

        selectBinding.displayMessages(messageList);

        @NonNull
        MessageList validationMessages = selectField.getValidationMessages();

        assertThat(validationMessages.getMessageByCode("code")).isPresent();

        Message firstMessage = validationMessages.getFirstMessage(Severity.ERROR).get();
        assertThat(firstMessage.getText()).isEqualTo("text");
    }

    @Test
    void testDisplayMessages_noMessages() {
        selectBinding.displayMessages(messageList);

        assertThat(selectField.getValidationMessages()).isEmpty();
    }

    @Test
    void testDisplayMessages_noMessageList() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            selectBinding.displayMessages(null);
        });
    }

}
