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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.TestUi;
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

    private Collection<TestEnum> valueList = Arrays.asList(new TestEnum[] { TestEnum.ONE, TestEnum.THREE });

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

        binding = new FieldBinding<>(label, field, propertyDispatcherValue, Handler.NOP_HANDLER);

        selectBinding = new FieldBinding<Object>(label, selectField, propertyDispatcherEnumValue, Handler.NOP_HANDLER);
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
    public void testEnabledBinding() {
        when(propertyDispatcherValue.isEnabled()).thenReturn(true);
        assertTrue(binding.isEnabled());
        assertTrue(field.isEnabled());

        when(propertyDispatcherValue.isEnabled()).thenReturn(false);

        binding.updateFromPmo();

        assertFalse(binding.isEnabled());
        assertFalse(field.isEnabled());
    }

    @Test
    public void testEnabledBinding_callSetEnabledOnField() {
        when(propertyDispatcherValue.isEnabled()).thenReturn(false);
        binding.updateFromPmo();

        verify(field).setEnabled(false);
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
        binding = new FieldBinding<>(null, field, propertyDispatcherValue, Handler.NOP_HANDLER);
        when(propertyDispatcherValue.isVisible()).thenReturn(false);
        binding.updateFromPmo();

        verify(field).setVisible(false);
    }

    @Test
    public void testRequiredBinding() {
        when(propertyDispatcherValue.isRequired()).thenReturn(false);
        assertFalse(binding.isRequired());
        when(propertyDispatcherValue.isRequired()).thenReturn(true);
        binding.updateFromPmo();

        assertTrue(binding.isRequired());
    }

    @Test
    public void testRequiredBinding_callSetRequiredOnField() {
        when(propertyDispatcherValue.isRequired()).thenReturn(true);
        binding.updateFromPmo();

        verify(field).setRequired(true);
    }

    @Test
    public void testBindAvailableValues() {
        assertTrue(selectBinding.getAvailableValues().isEmpty());
        doReturn(valueList).when(propertyDispatcherEnumValue).getAvailableValues();

        assertEquals(2, selectBinding.getAvailableValues().size());
    }

    @Test
    public void testBindAvailableValues_removeOldItemsFromField() {
        doReturn(valueList).when(propertyDispatcherEnumValue).getAvailableValues();

        selectBinding.updateFromPmo();
        Collection<?> itemIds = selectField.getItemIds();
        assertThat(itemIds.size(), is(2));
        Iterator<?> iterator = itemIds.iterator();
        assertThat(iterator.next(), is(TestEnum.ONE));
        assertThat(iterator.next(), is(TestEnum.THREE));

        Collection<TestEnum> valueList2 = Arrays.asList(new TestEnum[] { TestEnum.TWO, TestEnum.THREE });
        doReturn(valueList2).when(propertyDispatcherEnumValue).getAvailableValues();

        selectBinding.updateFromPmo();
        Collection<?> itemIds2 = selectField.getItemIds();
        assertThat(itemIds2.size(), is(2));
        Iterator<?> iterator2 = itemIds2.iterator();
        assertThat(iterator2.next(), is(TestEnum.TWO));
        assertThat(iterator2.next(), is(TestEnum.THREE));
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
        TextField textField = (TextField)TestUi.componentBoundTo(testPmo);

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
