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

import java.util.ArrayList;
import java.util.List;

import org.faktorips.runtime.MessageList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.dispatcher.ExceptionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.ReflectionPropertyDispatcher;
import org.linkki.core.ui.section.annotations.FieldDescriptor;
import org.linkki.core.ui.section.annotations.TestUi;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.core.ui.section.annotations.adapters.UIToolTipAdapter;
import org.linkki.util.handler.Handler;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.vaadin.ui.AbstractField;
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
    private AbstractField<String> field1 = spy(new TextField());
    private AbstractField<String> field2 = spy(new TextField());

    private FieldBinding<String> binding1;
    private FieldBinding<String> binding2;

    private void setUpPmo() {
        context = TestBindingContext.create();
        pmo.setModelObject(modelObject);
    }

    private Handler setUpPmoWithAfterUpdateUiHandler() {
        Handler afterUpdateUi = mock(Handler.class);
        context = TestBindingContext.create(afterUpdateUi);
        pmo.setModelObject(modelObject);
        return afterUpdateUi;
    }

    private void setUpBinding2() {
        binding2 = new FieldBinding<String>(label2, field2,
                new ReflectionPropertyDispatcher(this::getPmo, "value", new ExceptionPropertyDispatcher("value", pmo)),
                context::updateUIForBinding);
    }

    private void setUpBinding1() {
        binding1 = new FieldBinding<String>(label1, field1,
                new ReflectionPropertyDispatcher(this::getPmo, "value", new ExceptionPropertyDispatcher("value", pmo)),
                context::updateUIForBinding);
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
        Handler afterUpdateUi = setUpPmoWithAfterUpdateUiHandler();
        setUpBinding1();

        binding1 = spy(binding1);

        context.add(binding1);

        context.updateUI();
        verify(binding1).updateFromPmo();
        verify(afterUpdateUi, never()).apply();
    }

    @Test
    public void testUpdateUI_noBindingInContext() {
        Handler afterUpdateUi = setUpPmoWithAfterUpdateUiHandler();
        setUpBinding1();

        binding1 = spy(binding1);

        context.updateUI();

        verify(binding1, never()).updateFromPmo();
        verify(binding1, never()).displayMessages(any(MessageList.class));
        verify(afterUpdateUi, never()).apply();
    }

    @Test
    public void testUpdateUIAndValidate_noBindingInContext() {
        Handler afterUpdateUi = setUpPmoWithAfterUpdateUiHandler();
        setUpBinding1();

        binding1 = spy(binding1);

        context.updateUIForBinding();

        verify(afterUpdateUi).apply();
    }

    @Test
    public void testUpdateUIAndValidate() {
        Handler afterUpdateUi = setUpPmoWithAfterUpdateUiHandler();

        setUpBinding1();
        binding1 = spy(binding1);

        context.add(binding1);

        context.updateUIForBinding();
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
        FieldDescriptor fieldDescriptor = new FieldDescriptor(fieldDefintion, new UIToolTipAdapter(null), "value");

        // Precondition
        assertThat(field.isImmediate(), is(false));

        context.bind(pmo, fieldDescriptor, field, null);
        assertThat(field.isImmediate(), is(true));
    }

    @Test
    public void testRemoveBinding() {

        context = TestBindingContext.create();

        List<TestModelObject> pmos = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {

            TestModelObject pmo = new TestModelObject();
            pmos.add(pmo);

            TestUi.componentBoundTo(pmo, context);
        }

        pmos.forEach(context::removeBindingsForPmo);
    }

    @UISection
    public static class TestModelObject {

        public static final String PROPERTY_MODEL_PROP = "modelProp";

        private String modelProp;

        @UITextField(position = 1)
        public String getModelProp() {
            return modelProp;
        }

        public void setModelProp(String modelProp) {
            this.modelProp = modelProp;
        }

    }
}
