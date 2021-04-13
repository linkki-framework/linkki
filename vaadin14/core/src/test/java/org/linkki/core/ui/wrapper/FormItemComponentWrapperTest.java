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

package org.linkki.core.ui.wrapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.ui.bind.TestEnum;
import org.linkki.core.vaadin.component.base.LinkkiFormLayout.LabelComponentFormItem;
import org.linkki.util.handler.Handler;
import org.mockito.ArgumentCaptor;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * basically the same tests as in {@code ElementBindingTest} but focused on the
 * {@link FormItemComponentWrapper}
 **/
public class FormItemComponentWrapperTest {

    private final Label label = spy(new Label());

    private final TextField field = spy(new TextField());
    private final ComboBox<String> selectField = spy(new ComboBox<>());


    private ElementBinding selectBinding;


    private PropertyDispatcher propertyDispatcherValue;


    private MessageList messageList;


    private PropertyDispatcher propertyDispatcherEnumValue;

    @BeforeEach
    public void setUp() {
        propertyDispatcherValue = mock(PropertyDispatcher.class);
        when(propertyDispatcherValue.getProperty()).thenReturn("value");
        propertyDispatcherEnumValue = mock(PropertyDispatcher.class);
        when(propertyDispatcherEnumValue.getProperty()).thenReturn("enumValue");
        doReturn(TestEnum.class).when(propertyDispatcherEnumValue).getValueClass();

        messageList = new MessageList();
        when(propertyDispatcherValue.getMessages(any(MessageList.class))).thenReturn(messageList);
        when(propertyDispatcherEnumValue.getMessages(any(MessageList.class))).thenReturn(messageList);

        selectBinding = new ElementBinding(new FormItemComponentWrapper(new LabelComponentFormItem(selectField, label)),
                propertyDispatcherEnumValue,
                Handler.NOP_HANDLER,
                new ArrayList<>());
    }

    @Test
    public void testUpdateFromPmo_updateAspect() {
        Handler componentUpdater = mock(Handler.class);
        LinkkiAspectDefinition aspectDefinition = mock(LinkkiAspectDefinition.class);
        when(aspectDefinition.supports(any())).thenReturn(true);
        when(aspectDefinition.createUiUpdater(any(), any())).thenReturn(componentUpdater);
        ElementBinding fieldBinding = new ElementBinding(
                new FormItemComponentWrapper(new LabelComponentFormItem(field, label)),
                propertyDispatcherValue,
                Handler.NOP_HANDLER, Arrays.asList(aspectDefinition));
        fieldBinding.updateFromPmo();

        verify(componentUpdater).apply();
    }

    @Test
    public void testDisplayMessages() {
        messageList.add(Message.newError("code", "text"));

        selectBinding.displayMessages(messageList);

        verify(selectField).setErrorMessage(any(String.class));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(selectField).setErrorMessage(captor.capture());


        @NonNull
        String userError = captor.getValue();
        assertEquals(userError, "text");
    }

    @Test
    public void testDisplayMessages_noMessages() {
        selectBinding.displayMessages(messageList);

        verify(selectField).setErrorMessage("");
        assertThat(selectField.isInvalid(), is(false));
    }


    @Test
    public void testDisplayMessages_noMessageList() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            selectBinding.displayMessages(null);
        });
    }

    @Test
    public void testSetTooltip() {
        // TODO LIN-2054
    }
}