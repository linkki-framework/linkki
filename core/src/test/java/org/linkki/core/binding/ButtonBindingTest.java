/*
 * Copyright Faktor Zehn AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
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

@SuppressWarnings("null")
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
