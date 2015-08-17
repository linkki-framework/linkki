package de.faktorzehn.ipm.web.binding;

import static org.junit.Assert.assertEquals;
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
        assertEquals(0, context.getFieldBindings().size());
        context.add(binding);
        assertEquals(1, context.getFieldBindings().size());
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

}
