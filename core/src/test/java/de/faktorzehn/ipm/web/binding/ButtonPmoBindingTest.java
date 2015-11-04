/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.vaadin.ui.Button;

import de.faktorzehn.ipm.web.ButtonPmo;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;
import de.faktorzehn.ipm.web.binding.dispatcher.ReflectionPropertyDispatcher;

public class ButtonPmoBindingTest {

    static class TestButtonPmo implements ButtonPmo {

        boolean visible = true;
        boolean enabled = true;

        @Override
        public void onClick() {
            // Nothing to do
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public boolean isVisible() {
            return visible;
        }
    }

    private final PropertyDispatcher wrappedDispatcher = mock(PropertyDispatcher.class);
    private final BindingContext bindingContext = new BindingContext();
    private final Button button = new Button();

    @Test
    public void testButtonClickIsForwaredToPmo() {
        ButtonPmo pmo = mock(ButtonPmo.class);
        PropertyDispatcher propertyDispatcher = new ReflectionPropertyDispatcher(() -> pmo, wrappedDispatcher);
        ButtonPmoBinding.create(bindingContext, pmo, button, propertyDispatcher);
        button.click();
        verify(pmo).onClick();
    }

    @Test
    public void testUpdateFromPmo_PmoSublass() {
        TestButtonPmo pmo = new TestButtonPmo();
        PropertyDispatcher propertyDispatcher = new ReflectionPropertyDispatcher(() -> pmo, wrappedDispatcher);
        ButtonPmoBinding binding = ButtonPmoBinding.create(bindingContext, pmo, button, propertyDispatcher);

        pmo.enabled = true;
        pmo.visible = true;
        binding.updateFromPmo();
        assertThat(button.isVisible(), is(true));
        assertThat(button.isEnabled(), is(true));

        pmo.enabled = false;
        pmo.visible = false;
        binding.updateFromPmo();
        assertThat(button.isVisible(), is(false));
        assertThat(button.isEnabled(), is(false));
    }

    @Test
    public void testUpdateFromPmo_LambdaPmo() {
        ButtonPmo lambdaPmo = () -> System.out.println("click");
        PropertyDispatcher dispatcher = new ReflectionPropertyDispatcher(() -> lambdaPmo, wrappedDispatcher);
        ButtonPmoBinding binding = ButtonPmoBinding.create(bindingContext, lambdaPmo, button, dispatcher);

        binding.updateFromPmo();
        assertThat(button.isVisible(), is(true));
        assertThat(button.isEnabled(), is(true));
    }

}
