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

package org.linkki.core.binding.aspect.definition;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.TestComponentWrapper;
import org.linkki.core.ui.TestUiComponent;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.util.handler.Handler;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StaticModelToUiAspectDefinitionTest {

    
    @Mock
    private PropertyDispatcher propertyDispatcher;

    private TestComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());

    private ModelToUiAspectDefinition<Boolean> aspectDefinition = new TestStaticModelToUiAspectDefinition();

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateUiUpdater() {
        when(propertyDispatcher.pull(any(Aspect.class))).thenReturn(true);

        assertThat(componentWrapper.getComponent().isEnabled(), is(false));

        Handler handler = aspectDefinition.createUiUpdater(propertyDispatcher, componentWrapper);

        verify(propertyDispatcher, times(1)).pull(any(Aspect.class));
        assertThat(componentWrapper.getComponent().isEnabled(), is(true));

        handler.apply();
        verify(propertyDispatcher, times(1)).pull(any(Aspect.class));
    }

    @Test(expected = LinkkiBindingException.class)
    public void testCreateUiUpdater_WrapsExceptionInPull() {
        when(propertyDispatcher.pull(Mockito.any())).thenThrow(RuntimeException.class);
        Handler handler = aspectDefinition.createUiUpdater(propertyDispatcher, componentWrapper);
        handler.apply();
    }

    @Test
    public void testCreateAspect() {
        Aspect<Boolean> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName(), is(TestStaticModelToUiAspectDefinition.NAME));
        assertThat(createdAspect.getValue(), is(true));
    }

    @Test
    public void testCreateComponentValueSetter() {
        Consumer<Boolean> setter = aspectDefinition.createComponentValueSetter(componentWrapper);
        assertThat(componentWrapper.getComponent().isEnabled(), is(false));
        setter.accept(true);
        assertThat(componentWrapper.getComponent().isEnabled(), is(true));
    }

    private static class TestStaticModelToUiAspectDefinition extends StaticModelToUiAspectDefinition<Boolean> {

        public static final String NAME = "test";

        @Override
        public Aspect<Boolean> createAspect() {
            return Aspect.of(NAME, true);
        }

        @Override
        public Consumer<Boolean> createComponentValueSetter(ComponentWrapper componentWrapper) {
            return componentWrapper::setEnabled;
        }
    }
}
