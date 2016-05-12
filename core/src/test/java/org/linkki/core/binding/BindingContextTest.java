package org.linkki.core.binding;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.faktorips.runtime.MessageList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.dispatcher.ExceptionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.ReflectionPropertyDispatcher;
import org.linkki.core.ui.section.annotations.FieldDescriptor;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;
import org.linkki.util.handler.Handler;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@RunWith(MockitoJUnitRunner.class)
public class BindingContextTest {

    private TestBindingContext context;

    @Mock
    private Label label1;
    private Label label2;
    private TestPmo pmo = new TestPmo();
    private TestModelObject modelObject = new TestModelObject();
    private Field<String> field1 = spy(new TextField());
    private Field<String> field2 = spy(new TextField());

    private FieldBinding<String> binding1;
    private FieldBinding<String> binding2;

    private void setUpPmo() {
        context = TestBindingContext.create();
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
    public void testUpdateUI_noBindingInContext() {
        Handler afterUpdateUi = mock(Handler.class);
        context = TestBindingContext.create(afterUpdateUi);
        pmo.setModelObject(modelObject);
        setUpBinding1();
        binding1 = spy(binding1);

        context.updateUI();
        verify(binding1, never()).updateFromPmo();
        verify(binding1, never()).displayMessages(any(MessageList.class));
        verify(afterUpdateUi).apply();
    }

    @Test
    public void testUpdateUI() {
        Handler afterUpdateUi = mock(Handler.class);
        context = TestBindingContext.create(afterUpdateUi);
        pmo.setModelObject(modelObject);
        setUpBinding1();
        binding1 = spy(binding1);

        context.add(binding1);

        context.updateUI();
        verify(binding1).updateFromPmo();
        verify(afterUpdateUi).apply();
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
    public void testBind_BoundComponentsAreMadeImmediate() {
        setUpPmo();
        TextField field = new TextField();
        UIFieldDefinition fieldDefintion = mock(UIFieldDefinition.class);
        FieldDescriptor fieldDescriptor = new FieldDescriptor(fieldDefintion, "value", "");

        // Precondition
        assertThat(field.isImmediate(), is(false));

        context.bind(pmo, fieldDescriptor, field, null);
        assertThat(field.isImmediate(), is(true));
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
