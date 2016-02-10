package org.linkki.core.binding;

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
import org.faktorips.runtime.Severity;
import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.ValidationMarker;
import org.mockito.ArgumentCaptor;

import com.vaadin.server.ErrorMessage.ErrorLevel;
import com.vaadin.server.UserError;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class FieldBindingTest {

    private Object pmo = new Object();

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
        context = TestBindingContext.create();
        propertyDispatcher = mock(PropertyDispatcher.class);

        messageList = new MessageList();
        when(propertyDispatcher.getMessages(any(MessageList.class), anyString())).thenReturn(messageList);

        binding = FieldBinding.create(context, pmo, "value", label, field, propertyDispatcher);
        selectBinding = FieldBinding.create(context, pmo, "enumValue", label, selectField, propertyDispatcher);
        context.add(binding);
        context.add(selectBinding);
    }

    @Test(expected = NullPointerException.class)
    public void testValuePropertyCheck_NullFieldName() {
        FieldBinding.create(context, pmo, null, label, field, propertyDispatcher);
    }

    @Test(expected = NullPointerException.class)
    public void testValuePropertyCheck_NullPmo() {
        FieldBinding.create(context, null, "value", label, field, propertyDispatcher);
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
        binding.updateFromPmo();

        verify(field).setVisible(false);
        verify(label).setVisible(false);
    }

    @Test
    public void testVisibleBinding_ifLabelNull() {
        binding = FieldBinding.create(context, pmo, "value", null, field, propertyDispatcher);
        when(propertyDispatcher.isVisible("value")).thenReturn(false);
        binding.updateFromPmo();

        verify(field).setVisible(false);
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
    public void testDisplayMessages() {
        messageList.add(Message.newError("code", "text"));

        context.updateUI();

        verify(selectField).setComponentError(any(UserError.class));

        ArgumentCaptor<UserError> captor = ArgumentCaptor.forClass(UserError.class);
        verify(selectField).setComponentError(captor.capture());
        assertEquals(captor.getValue().getMessage(), "text");
        assertEquals(captor.getValue().getErrorLevel(), ErrorLevel.ERROR);
    }

    @Test
    public void testDisplayMessages_noMessages() {
        context.updateUI();

        verify(selectField).setComponentError(null);
    }

    @Test
    public void testDisplayMessages_mandatoryFieldMessagesAreFiltered() {
        ValidationMarker mandatoryFieldMarker = () -> true;
        messageList.add(new Message.Builder("text", Severity.ERROR).markers(mandatoryFieldMarker).create());

        context.updateUI();

        verify(selectField).setComponentError(null);
    }

    @Test
    public void testDisplayMessages_nonMandatoryFieldMessagesAreNotFiltered() {
        ValidationMarker nonMandatoryFieldMarker = () -> false;
        messageList.add(new Message.Builder("text", Severity.ERROR).markers(nonMandatoryFieldMarker).create());

        context.updateUI();

        ArgumentCaptor<UserError> captor = ArgumentCaptor.forClass(UserError.class);
        verify(selectField).setComponentError(captor.capture());
        assertEquals(captor.getValue().getMessage(), "text");
        assertEquals(captor.getValue().getErrorLevel(), ErrorLevel.ERROR);
    }

}
