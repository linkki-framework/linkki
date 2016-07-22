/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;

public class ButtonBindingTest {

    private Label label = spy(new Label());
    private Button button = spy(new Button());

    private ButtonBinding binding;
    private PropertyDispatcher propertyDispatcher = mock(PropertyDispatcher.class);
    private BindingContext context = TestBindingContext.create();

    private void setUpDefaultBinding() {
        binding = new ButtonBinding(label, button, propertyDispatcher, context::updateUI, true);
        context.add(binding);
    }

    @Test
    public void testClickBinding() {
        setUpDefaultBinding();
        button.click();
        verify(propertyDispatcher).invoke();
    }

    @Test
    public void testUpdateFromPmo_SetsButtonAndFieldVisible() {
        setUpDefaultBinding();
        when(propertyDispatcher.isVisible()).thenReturn(false);
        binding.updateFromPmo();
        assertThat(button.isVisible(), is(false));
        assertThat(label.isVisible(), is(false));

        when(propertyDispatcher.isVisible()).thenReturn(true);
        binding.updateFromPmo();
        assertThat(button.isVisible(), is(true));
        assertThat(label.isVisible(), is(true));
    }

    @Test
    public void testUpdateFromPmo_EnablesButton() {
        setUpDefaultBinding();
        when(propertyDispatcher.isEnabled()).thenReturn(false);
        binding.updateFromPmo();
        assertThat(button.isEnabled(), is(false));

        when(propertyDispatcher.isEnabled()).thenReturn(true);
        binding.updateFromPmo();
        assertThat(button.isEnabled(), is(true));
    }

    @Test
    public void testUpdateFromPmo_updateCaption() {
        setUpDefaultBinding();

        binding.updateFromPmo();

        verify(propertyDispatcher).getCaption();
    }

    @Test
    public void testUpdateFromPmo_ignoreCaption() {
        binding = new ButtonBinding(label, button, propertyDispatcher, context::updateUI, false);
        context.add(binding);

        binding.updateFromPmo();

        verify(propertyDispatcher, never()).getCaption();
    }

    @Test
    public void testUpdateFromPmo_ButtonToolTip() {
        setUpDefaultBinding();
        when(propertyDispatcher.getToolTip()).thenReturn("ToolTip");
        binding.updateFromPmo();
        assertThat(button.getDescription(), is("ToolTip"));
    }
}
