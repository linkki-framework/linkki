/*
 * Copyright Faktor Zehn AG.
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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.message.Message;
import org.linkki.core.message.MessageList;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.TestUiUtil;
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

@SuppressWarnings("null")
public class FieldBindingTest {

    private Label label = spy(new Label());

    private AbstractField<String> field = spy(new TextField());
    private ComboBox selectField = spy(new ComboBox());

    private FieldBinding<String> binding;
    private FieldBinding<Object> selectBinding;

    private PropertyDispatcher propertyDispatcherValue;

    private MessageList messageList;

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

        binding = new FieldBinding<>(label, field, propertyDispatcherValue, Handler.NOP_HANDLER, new ArrayList<>());

        selectBinding = new FieldBinding<Object>(label, selectField, propertyDispatcherEnumValue, Handler.NOP_HANDLER,
                new ArrayList<>());
    }

    @Test
    public void testValueBinding() {
        assertEquals(null, binding.getValue());

        when(propertyDispatcherValue.getValue()).thenReturn("test");

        assertEquals("test", binding.getValue());
    }

    @Test
    public void testValueBinding_field() {
        when(propertyDispatcherValue.getValue()).thenReturn("test");
        binding.updateFromPmo();

        assertEquals("test", field.getValue());
    }

    @Test
    public void testVisibleBinding() {
        when(propertyDispatcherValue.isVisible()).thenReturn(true);
        assertTrue(binding.isVisible());
        assertTrue(field.isVisible());
        when(propertyDispatcherValue.isVisible()).thenReturn(false);
        binding.updateFromPmo();

        assertFalse(binding.isVisible());
        assertFalse(field.isVisible());
    }

    @Test
    public void testVisibleBinding_callSetVisibleOnLabelAndField() {
        when(propertyDispatcherValue.isVisible()).thenReturn(false);
        binding.updateFromPmo();

        verify(field).setVisible(false);
        verify(label).setVisible(false);
    }

    @Test
    public void testVisibleBinding_ifLabelNull() {
        binding = new FieldBinding<>(null, field, propertyDispatcherValue, Handler.NOP_HANDLER, new ArrayList<>());
        when(propertyDispatcherValue.isVisible()).thenReturn(false);
        binding.updateFromPmo();

        verify(field).setVisible(false);
    }

    @Test
    public void testUpdateFromPmo_updateAspect() {
        Handler componentUpdater = mock(Handler.class);
        LinkkiAspectDefinition aspectDefinition = mock(LinkkiAspectDefinition.class);
        when(aspectDefinition.createUiUpdater(any(), any())).thenReturn(componentUpdater);
        FieldBinding<String> fieldBinding = new FieldBinding<>(label, field, propertyDispatcherValue,
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
        assertEquals(captor.getValue().getMessage(), "text");
        assertEquals(captor.getValue().getErrorLevel(), ErrorLevel.ERROR);
    }

    @Test
    public void testDisplayMessages_noMessages() {
        selectBinding.displayMessages(messageList);

        verify(selectField).setComponentError(null);
    }

    @Test
    public void testDisplayMessages_noMessageList() {
        selectBinding.displayMessages(null);

        verify(selectField).setComponentError(null);
    }

    @Test
    public void testFieldBindingAllowsNullValueForRequiredFields() {
        TestModelObject testModelObject = new TestModelObject();
        TestPmo testPmo = new TestPmo(testModelObject);

        // This creates a FieldBinding for the PMO
        TextField textField = (TextField)TestUiUtil.createFirstComponentOf(testPmo);

        // Preconditions
        assertThat(textField.isRequired(), is(true));
        assertThat(textField.getValue(), is(TestModelObject.DEFAULT_TEXT));
        assertThat(testModelObject.getText(), is(TestModelObject.DEFAULT_TEXT));

        textField.setValue(null);
        assertThat(textField.getValue(), is(nullValue()));
        assertThat(testModelObject.getText(), is(nullValue()));
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

        @UITextField(position = 1, modelAttribute = "text", required = RequiredType.REQUIRED)
        public void text() {
            // data binding
        }

        @ModelObject
        public TestModelObject getModelObject() {
            return modelObject;
        }
    }

}
