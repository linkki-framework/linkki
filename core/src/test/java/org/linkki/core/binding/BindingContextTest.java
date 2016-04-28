package org.linkki.core.binding;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.faktorips.runtime.MessageList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.dispatcher.ExceptionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.ReflectionPropertyDispatcher;
import org.linkki.core.ui.section.annotations.ElementDescriptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@RunWith(MockitoJUnitRunner.class)
public class BindingContextTest {

    private BindingContext context;

    @Mock
    private Label label1;
    private Label label2;
    private TestPmo pmo = new TestPmo();
    private TestModelObject modelObject;
    private Field<String> field1 = spy(new TextField());
    private Field<String> field2 = spy(new TextField());

    private FieldBinding<String> binding1;
    private FieldBinding<String> binding2;

    @Mock
    private ElementDescriptor elementDescriptor;

    private void setUpPmo() {
        context = TestBindingContext.create();
        modelObject = new TestModelObject();
        pmo.setModelObject(modelObject);
    }

    private void setUpBinding2() {
        binding2 = new FieldBinding<String>(label2, field2,
                new ReflectionPropertyDispatcher(this::getPmo, "value", new ExceptionPropertyDispatcher("value", pmo)),
                context::updateUI);
    }

    private void setUpBinding1() {
        binding1 = new FieldBinding<String>(label1, field1,
                new ReflectionPropertyDispatcher(this::getPmo, "value", new ExceptionPropertyDispatcher("value", pmo)),
                context::updateUI);
    }

    private TestPmo getPmo() {
        return pmo;
    }

    @Test
    public void testAdd() {
        setUpPmo();
        setUpBinding1();
        assertEquals(0, context.getElementBindings().size());
        context.add(binding1);
        assertEquals(1, context.getElementBindings().size());
    }

    @Test
    public void testUpdateUI() {
        setUpPmo();
        setUpBinding1();
        binding1 = spy(binding1);

        context.updateUI();
        verify(binding1, never()).updateFromPmo();
        verify(binding1, never()).displayMessages(any(MessageList.class));

        context.add(binding1);

        context.updateUI();
        verify(binding1).updateFromPmo();
        verify(binding1).displayMessages(any(MessageList.class));
    }

    @Test
    public void testChangeBoundObject() {
        setUpPmo();
        setUpBinding1();
        binding1 = spy(binding1);

        context.updateUI();
        verify(binding1, never()).updateFromPmo();

        context.add(binding1);

        context.updateUI();
        verify(binding1).updateFromPmo();
    }

    @Test
    public void testRemoveBindingsForPmo() {
        setUpPmo();
        setUpBinding1();
        setUpBinding2();
        context.add(binding1);
        context.add(binding2);

        assertThat(context.getElementBindings(), hasSize(2));

        context.removeBindingsForPmo(pmo);
        assertThat(context.getElementBindings(), is(empty()));
    }

    @Test
    public void testRemoveBindingsForComponent() {
        setUpPmo();
        setUpBinding1();
        setUpBinding2();
        context.add(binding1);
        context.add(binding2);

        assertThat(context.getElementBindings(), hasSize(2));

        VerticalLayout layout = new VerticalLayout(field1, field2);

        context.removeBindingsForComponent(layout);
        assertThat(context.getElementBindings(), is(empty()));
    }

    @Test
    public void testCreateDispatcherChain_getValueFromPmo() {
        setUpPmo();
        when(elementDescriptor.getPropertyName()).thenReturn("value");
        PropertyDispatcher defaultDispatcher = context.createDispatcherChain(pmo, elementDescriptor);
        pmo.setValue("testValue");

        Object pmoProp = defaultDispatcher.getValue();

        assertThat(pmoProp, is("testValue"));
    }

    @Test
    public void testCreateDispatcherChain_setValueToPmo() {
        setUpPmo();
        when(elementDescriptor.getPropertyName()).thenReturn("value");
        PropertyDispatcher defaultDispatcher = context.createDispatcherChain(pmo, elementDescriptor);

        defaultDispatcher.setValue("testSetValue");

        assertThat(pmo.getValue(), is("testSetValue"));
    }

    @Test
    public void testCreateDispatcherChain_getValueFromModelObject() {
        setUpPmo();
        when(elementDescriptor.getPropertyName()).thenReturn(TestModelObject.PROPERTY_MODEL_PROP);
        PropertyDispatcher defaultDispatcher = context.createDispatcherChain(pmo, elementDescriptor);
        modelObject.setModelProp("testValue");

        Object modelProp = defaultDispatcher.getValue();

        assertThat(modelProp, is("testValue"));
    }

    @Test
    public void testCreateDispatcherChain_setValueToModelObject() {
        setUpPmo();
        when(elementDescriptor.getPropertyName()).thenReturn(TestModelObject.PROPERTY_MODEL_PROP);
        PropertyDispatcher defaultDispatcher = context.createDispatcherChain(pmo, elementDescriptor);

        defaultDispatcher.setValue("testSetValue");

        assertThat(modelObject.getModelProp(), is("testSetValue"));
    }

    @Test
    public void testCreateDispatcherChain_getValueFromChangedModelObject() {
        setUpPmo();
        when(elementDescriptor.getPropertyName()).thenReturn(TestModelObject.PROPERTY_MODEL_PROP);
        PropertyDispatcher defaultDispatcher = context.createDispatcherChain(pmo, elementDescriptor);
        TestModelObject newModelObject = new TestModelObject();
        pmo.setModelObject(newModelObject);
        newModelObject.setModelProp("testNewValue");

        Object modelProp = defaultDispatcher.getValue();

        assertThat(modelProp, is("testNewValue"));
    }

    @Test
    public void testCreateDispatcherChain_setValueToChangedModelObject() {
        setUpPmo();
        when(elementDescriptor.getPropertyName()).thenReturn(TestModelObject.PROPERTY_MODEL_PROP);
        PropertyDispatcher defaultDispatcher = context.createDispatcherChain(pmo, elementDescriptor);
        TestModelObject newModelObject = new TestModelObject();
        pmo.setModelObject(newModelObject);

        defaultDispatcher.setValue("testNewSetValue");

        assertThat(newModelObject.getModelProp(), is("testNewSetValue"));
    }

    public static class TestModelObject {

        public static final String PROPERTY_MODEL_PROP = "modelProp";

        private String modelProp;

        public String getModelProp() {
            return modelProp;
        }

        public void setModelProp(String modelProp) {
            this.modelProp = modelProp;
        }

    }
}
