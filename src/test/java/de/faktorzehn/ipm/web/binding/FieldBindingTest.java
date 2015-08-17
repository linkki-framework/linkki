package de.faktorzehn.ipm.web.binding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.vaadin.server.ErrorMessage.ErrorLevel;
import com.vaadin.server.UserError;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;

public class FieldBindingTest {

    private Label label = spy(new Label());

    private Field<String> field = spy(new TextField());
    private ComboBox selectField = spy(new ComboBox());

    private FieldBinding<String> binding;
    private FieldBinding<Object> selectBinding;

    private List<TestEnum> valueList = Arrays.asList(new TestEnum[] { TestEnum.ONE, TestEnum.THREE });

    private PropertyDispatcher propertyDispatcher;

    private BindingContext context;

    private MessageList messageList;

    @Before
    public void setUp() {
        context = new BindingContext();
        propertyDispatcher = mock(PropertyDispatcher.class);

        messageList = new MessageList();
        when(propertyDispatcher.getMessages(anyString())).thenReturn(messageList);

        binding = FieldBinding.create(context, "value", label, field, propertyDispatcher);
        selectBinding = FieldBinding.create(context, "enumValue", label, selectField, propertyDispatcher);
        context.add(binding);
        context.add(selectBinding);
    }

    @Test(expected = NullPointerException.class)
    public void testValuePropertyCheck() {
        FieldBinding.create(context, null, label, field, propertyDispatcher);
    }

    @Test
    public void testValueBinding() {
        assertEquals(null, binding.getValue());

        when(propertyDispatcher.getValue("value")).thenReturn("test");

        assertEquals("test", binding.getValue());
    }

    @Test
    public void testValueBinding_field() {
        when(propertyDispatcher.getValue("value")).thenReturn("test");
        context.updateUI();

        assertEquals("test", field.getValue());
    }

    @Test
    public void testEnabledBinding() {
        when(propertyDispatcher.isEnabled("value")).thenReturn(true);
        assertTrue(binding.isEnabled());
        assertTrue(field.isEnabled());

        when(propertyDispatcher.isEnabled("value")).thenReturn(false);
        context.updateUI();

        assertFalse(binding.isEnabled());
        assertFalse(field.isEnabled());
    }

    @Test
    public void testEnabledBinding_callSetEnabledOnField() {
        when(propertyDispatcher.isEnabled("value")).thenReturn(false);
        context.updateUI();

        verify(field).setEnabled(false);
    }

    @Test
    public void testVisibleBinding() {
        when(propertyDispatcher.isVisible("value")).thenReturn(true);
        assertTrue(binding.isVisible());
        assertTrue(field.isVisible());
        when(propertyDispatcher.isVisible("value")).thenReturn(false);
        context.updateUI();

        assertFalse(binding.isVisible());
        assertFalse(field.isVisible());
    }

    @Test
    public void testVisibleBinding_callSetVisibleOnLabelAndField() {
        when(propertyDispatcher.isVisible("value")).thenReturn(false);
        binding.updateFieldFromPmo();

        verify(field).setVisible(false);
        verify(label).setVisible(false);
    }

    @Test
    public void testRequiredBinding() {
        when(propertyDispatcher.isRequired("value")).thenReturn(false);
        assertFalse(binding.isRequired());
        when(propertyDispatcher.isRequired("value")).thenReturn(true);
        context.updateUI();

        assertTrue(binding.isRequired());
    }

    @Test
    public void testRequiredBinding_callSetRequiredOnField() {
        when(propertyDispatcher.isRequired("value")).thenReturn(true);
        context.updateUI();

        verify(field).setRequired(true);
    }

    @Test
    public void testBindAvailableValues() {
        assertTrue(selectBinding.getAvailableValues().isEmpty());
        doReturn(valueList).when(propertyDispatcher).getAvailableValues("enumValue");

        assertEquals(2, selectBinding.getAvailableValues().size());
    }

    @Test
    public void testBindAvailableValues_callAddItemsOnField() {
        doReturn(valueList).when(propertyDispatcher).getAvailableValues("enumValue");
        context.updateUI();

        verify(selectField).addItems(valueList);
    }

    @Test
    public void testBindRequired_prohibitNullForSelectFields() {
        when(propertyDispatcher.isRequired("enumValue")).thenReturn(true);
        doReturn(valueList).when(propertyDispatcher).getAvailableValues("enumValue");
        context.updateUI();

        verify(selectField).setNullSelectionAllowed(false);
    }

    @Test
    public void testBindMessages() {
        messageList.add(Message.newError("code", "text"));

        context.updateUI();

        verify(selectField).setComponentError(any(UserError.class));

        ArgumentCaptor<UserError> captor = ArgumentCaptor.forClass(UserError.class);
        verify(selectField).setComponentError(captor.capture());
        assertEquals(captor.getValue().getMessage(), "text");
        assertEquals(captor.getValue().getErrorLevel(), ErrorLevel.ERROR);
    }

    @Test
    public void testBindMessages_noMessages() {
        context.updateUI();

        verify(selectField).setComponentError(null);
    }

}
