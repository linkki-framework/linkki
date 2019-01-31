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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.message.Message;
import org.linkki.core.message.MessageList;
import org.linkki.core.ui.components.LabelComponentWrapper;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.util.handler.Handler;
import org.mockito.ArgumentCaptor;

import com.vaadin.server.ErrorMessage.ErrorLevel;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class ElementBindingTest {

    private Label label = spy(new Label());

    private AbstractField<String> field = spy(new TextField());
    private ComboBox selectField = spy(new ComboBox());

    @SuppressWarnings("null")
    private ElementBinding selectBinding;

    @SuppressWarnings("null")
    private PropertyDispatcher propertyDispatcherValue;

    @SuppressWarnings("null")
    private MessageList messageList;

    @SuppressWarnings("null")
    private PropertyDispatcher propertyDispatcherEnumValue;

    @Before
    public void setUp() {
        propertyDispatcherValue = mock(PropertyDispatcher.class);
        when(propertyDispatcherValue.getProperty()).thenReturn("value");
        propertyDispatcherEnumValue = mock(PropertyDispatcher.class);
        when(propertyDispatcherEnumValue.getProperty()).thenReturn("enumValue");
        doReturn(TestEnum.class).when(propertyDispatcherEnumValue).getValueClass();

        messageList = new MessageList();
        when(propertyDispatcherValue.getMessages(any(MessageList.class))).thenReturn(messageList);
        when(propertyDispatcherEnumValue.getMessages(any(MessageList.class))).thenReturn(messageList);

        selectBinding = new ElementBinding(new LabelComponentWrapper(label, selectField),
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
        ElementBinding fieldBinding = new ElementBinding(new LabelComponentWrapper(label, field),
                propertyDispatcherValue,
                Handler.NOP_HANDLER, Arrays.asList(aspectDefinition));
        fieldBinding.updateFromPmo();

        verify(componentUpdater).apply();
    }

    @Test
    public void testDisplayMessages() {
        messageList.add(Message.newError("code", "text"));

        selectBinding.displayMessages(messageList);

        verify(selectField).setComponentError(any(UserError.class));

        ArgumentCaptor<UserError> captor = ArgumentCaptor.forClass(UserError.class);
        verify(selectField).setComponentError(captor.capture());

        @SuppressWarnings("null")
        @NonNull
        UserError userError = captor.getValue();
        assertEquals(userError.getMessage(), "text");
        assertEquals(userError.getErrorLevel(), ErrorLevel.ERROR);
    }

    @Test
    public void testDisplayMessages_noMessages() {
        selectBinding.displayMessages(messageList);

        verify(selectField).setComponentError(null);
    }

    @SuppressWarnings("null")
    @Test(expected = NullPointerException.class)
    public void testDisplayMessages_noMessageList() {
        selectBinding.displayMessages(null);
    }

    protected static class TestModelObject {

        private static final String DEFAULT_TEXT = "";

        private String text = DEFAULT_TEXT;

        public String getText() {
            return text;
        }

        public void setText(String s) {
            this.text = s;
        }
    }

    @UISection
    protected static class TestPmo {

        private final TestModelObject modelObject;

        public TestPmo(TestModelObject modelObject) {
            super();
            this.modelObject = modelObject;
        }

        @UITextField(position = 1, label = "", modelAttribute = "text", required = RequiredType.REQUIRED)
        public void text() {
            // data binding
        }

        @ModelObject
        public TestModelObject getModelObject() {
            return modelObject;
        }
    }

}
