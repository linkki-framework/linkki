/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.core.binding;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.nls.TestComponentWrapper;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.util.handler.Handler;
import org.mockito.Mockito;


public class ButtonBindingTest {

    private TestUiComponent button = spy(new TestUiComponent());

    private ElementBinding binding;
    private PropertyDispatcher propertyDispatcher = mock(PropertyDispatcher.class);
    private BindingContext context = new BindingContext();
    private TestAspectDefinition aspectDefinition;

    private void setUpDefaultBinding() {
        aspectDefinition = spy(new TestAspectDefinition());
        Object pmo = mock(Object.class);
        when(propertyDispatcher.getBoundObject()).thenReturn(pmo);

        binding = new ElementBinding(new TestComponentWrapper(button), propertyDispatcher,
                context::modelChanged,
                Arrays.asList(aspectDefinition));
        context.add(binding, TestComponentWrapper.with(binding));
    }

    @Test
    public void testUpdateFromUI_updateAspect() {
        setUpDefaultBinding();
        verify(aspectDefinition).initModelUpdate(Mockito.eq(propertyDispatcher), Mockito.any(ComponentWrapper.class),
                                                 Mockito.any(Handler.class));
    }

    @Test
    public void testUpdateFromPmo_updateAspect() {
        setUpDefaultBinding();
        binding.updateFromPmo();

        assertThat(aspectDefinition.aspectUpdated, is(true));
    }

    private static class TestAspectDefinition implements LinkkiAspectDefinition {

        private boolean aspectUpdated;

        @Override
        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            return () -> {
                aspectUpdated = true;
            };
        }

        @Override
        public void initModelUpdate(PropertyDispatcher propertyDispatcher,
                ComponentWrapper componentWrapper,
                Handler modelUpdated) {
            // does nothing
        }
    }
}
