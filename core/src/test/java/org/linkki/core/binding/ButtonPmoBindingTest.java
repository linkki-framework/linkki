/*
 * Copyright Faktor Zehn GmbH.
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.linkki.core.ButtonPmo;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.ReflectionPropertyDispatcher;

import com.vaadin.ui.Button;

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
    private final BindingContext bindingContext = TestBindingContext.create();
    private final Button button = new Button();

    @Before
    public void setUp() {
        when(wrappedDispatcher.getProperty()).thenReturn(StringUtils.EMPTY);
    }

    @Test
    public void testButtonClickIsForwaredToPmo() {
        ButtonPmo pmo = mock(ButtonPmo.class);
        PropertyDispatcher propertyDispatcher = new ReflectionPropertyDispatcher(() -> pmo, StringUtils.EMPTY,
                wrappedDispatcher);
        ButtonPmoBinding buttonPmoBinding = new ButtonPmoBinding(button, propertyDispatcher,
                bindingContext::modelChanged);
        bindingContext.add(buttonPmoBinding);
        button.click();
        verify(pmo).onClick();
    }

    @Test
    public void testUpdateFromPmo_PmoSublass() {
        TestButtonPmo pmo = new TestButtonPmo();
        PropertyDispatcher propertyDispatcher = new ReflectionPropertyDispatcher(() -> pmo, StringUtils.EMPTY,
                wrappedDispatcher);
        ButtonPmoBinding binding = new ButtonPmoBinding(button, propertyDispatcher, bindingContext::modelChanged);
        bindingContext.add(binding);

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
        PropertyDispatcher propertyDispatcher = new ReflectionPropertyDispatcher(() -> lambdaPmo, StringUtils.EMPTY,
                wrappedDispatcher);

        ButtonPmoBinding binding = new ButtonPmoBinding(button, propertyDispatcher, bindingContext::modelChanged);
        bindingContext.add(binding);

        binding.updateFromPmo();
        assertThat(button.isVisible(), is(true));
        assertThat(button.isEnabled(), is(true));
    }

}
