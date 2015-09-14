package de.faktorzehn.ipm.web.binding;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import de.faktorzehn.ipm.web.binding.dispatcher.ExceptionPropertyDispatcher;
import de.faktorzehn.ipm.web.binding.dispatcher.ReflectionPropertyDispatcher;

@RunWith(MockitoJUnitRunner.class)
public class BindingContextTest {

    private BindingContext context;

    @Mock
    private Label label;
    private TestPMO pmo = new TestPMO();
    private Field<String> field = spy(new TextField());

    private FieldBinding<String> binding;

    @Before
    public void setUp() {
        context = new BindingContext();
        binding = new FieldBinding<String>(context, "value", label, field, new ReflectionPropertyDispatcher(
                this::getPmo, new ExceptionPropertyDispatcher(pmo, pmo)));
    }

    private TestPMO getPmo() {
        return pmo;
    }

    @Test
    public void testAdd() {
        assertThat(context.getFieldBindings(), is(empty()));
        context.add(binding);
        assertThat(context.getFieldBindings(), hasSize(1));
    }

    @Test
    public void testUpdateUI() {
        binding = spy(binding);

        context.updateUI();
        verify(binding, never()).updateFieldFromPmo();

        context.add(binding);

        context.updateUI();
        verify(binding).updateFieldFromPmo();
    }

    @Test
    public void testChangeBoundObject() {
        binding = spy(binding);

        context.updateUI();
        verify(binding, never()).updateFieldFromPmo();

        context.add(binding);

        context.updateUI();
        verify(binding).updateFieldFromPmo();
    }

    @Test
    public void testRemoveBindingsForPmo() {
        context.add(binding);
        context.add(binding);

        assertThat(context.getFieldBindings(), hasSize(2));

        context.removeBindingsForPmo(pmo);
        assertThat(context.getFieldBindings(), is(empty()));
    }

}
