/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;

import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;

public class ButtonBindingTest {

    private Label label = spy(new Label());
    private Button button = spy(new Button());

    private ButtonBinding binding;
    private PropertyDispatcher propertyDispatcher;
    private BindingContext context;

    @Before
    public void setUp() {
        context = new BindingContext();
        propertyDispatcher = mock(PropertyDispatcher.class);
        binding = ButtonBinding.create(context, "method", label, button, propertyDispatcher);
        context.add(binding);
    }

    @Test
    public void testClickBinding() {
        button.click();
        verify(propertyDispatcher).invoke("method");
    }

    @Test
    public void testUpdateFromPmo_SetsButtonAndFieldVisible() {
        when(propertyDispatcher.isVisible("method")).thenReturn(false);
        binding.updateFromPmo();
        assertThat(button.isVisible(), is(false));
        assertThat(label.isVisible(), is(false));

        when(propertyDispatcher.isVisible("method")).thenReturn(true);
        binding.updateFromPmo();
        assertThat(button.isVisible(), is(true));
        assertThat(label.isVisible(), is(true));
    }

    @Test
    public void testUpdateFromPmo_EnablesButton() {
        when(propertyDispatcher.isEnabled("method")).thenReturn(false);
        binding.updateFromPmo();
        assertThat(button.isEnabled(), is(false));

        when(propertyDispatcher.isEnabled("method")).thenReturn(true);
        binding.updateFromPmo();
        assertThat(button.isEnabled(), is(true));
    }
}
