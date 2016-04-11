package org.linkki.core.binding;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
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
import org.faktorips.runtime.Severity;
import org.junit.Before;
import org.junit.Test;
import org.linkki.core.PresentationModelObject;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.TestUi;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.util.validation.ValidationMarker;
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

    private Collection<TestEnum> valueList = Arrays.asList(new TestEnum[] { TestEnum.ONE, TestEnum.THREE });

    private PropertyDispatcher propertyDispatcher;

    private BindingContext context;

    private MessageList messageList;

    @Before
    public void setUp() {
        context = TestBindingContext.create();
        propertyDispatcher = mock(PropertyDispatcher.class);

        messageList = new MessageList();
        when(propertyDispatcher.getMessages(any(MessageList.class), anyString())).thenReturn(messageList);
        doReturn(TestEnum.class).when(propertyDispatcher).getValueClass("enumValue");

        binding = FieldBinding.create(context, pmo, "value", label, field, propertyDispatcher);

        selectBinding = new FieldBinding<Object>(context, pmo, "enumValue", label, selectField, propertyDispatcher);
        context.add(selectBinding);

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
    public void testBindAvailableValues_removeOldItemsFromField() {
        doReturn(valueList).when(propertyDispatcher).getAvailableValues("enumValue");

        context.updateUI();
        Collection<?> itemIds = selectField.getItemIds();
        assertThat(itemIds.size(), is(2));
        Iterator<?> iterator = itemIds.iterator();
        assertThat(iterator.next(), is(TestEnum.ONE));
        assertThat(iterator.next(), is(TestEnum.THREE));

        Collection<TestEnum> valueList2 = Arrays.asList(new TestEnum[] { TestEnum.TWO, TestEnum.THREE });
        doReturn(valueList2).when(propertyDispatcher).getAvailableValues("enumValue");

        context.updateUI();
        Collection<?> itemIds2 = selectField.getItemIds();
        assertThat(itemIds2.size(), is(2));
        Iterator<?> iterator2 = itemIds2.iterator();
        assertThat(iterator2.next(), is(TestEnum.TWO));
        assertThat(iterator2.next(), is(TestEnum.THREE));
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
    protected static class TestPmo implements PresentationModelObject {

        private final TestModelObject modelObject;

        public TestPmo(TestModelObject modelObject) {
            super();
            this.modelObject = modelObject;
        }

        @UITextField(position = 1, modelAttribute = "text", required = RequiredType.REQUIRED)
        public void text() {
            // data binding
        }

        @Override
        public TestModelObject getModelObject() {
            return modelObject;
        }
    }

}
