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

package org.linkki.core.binding.descriptor.aspect.base;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.nls.TestComponentWrapper;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.util.handler.Handler;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StaticModelToUiAspectDefinitionTest {

    @Mock
    private PropertyDispatcher propertyDispatcher;

    private final TestComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());

    private ModelToUiAspectDefinition<Boolean> aspectDefinition = new TestStaticModelToUiAspectDefinition();

    @SuppressWarnings("unchecked")
    @Test
    void testCreateUiUpdater() {
        when(propertyDispatcher.pull(any())).thenReturn(true);

        assertThat(componentWrapper.getComponent().isEnabled(), is(false));

        Handler handler = aspectDefinition.createUiUpdater(propertyDispatcher, componentWrapper);

        verify(propertyDispatcher, times(1)).pull(any(Aspect.class));
        assertThat(componentWrapper.getComponent().isEnabled(), is(true));

        handler.apply();
        verify(propertyDispatcher, times(1)).pull(any(Aspect.class));
    }

    @Test
    void testCreateUiUpdater_WrapsExceptionInPull() {
        when(propertyDispatcher.pull(any())).thenThrow(RuntimeException.class);

        Assertions.assertThrows(LinkkiBindingException.class,
                                () -> aspectDefinition.createUiUpdater(propertyDispatcher, componentWrapper));
    }

    @Test
    public void testCreateAspect() {
        Aspect<Boolean> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName(), is(TestStaticModelToUiAspectDefinition.NAME));
        assertThat(createdAspect.getValue(), is(true));
    }

    @Test
    void testCreateComponentValueSetter() {
        Consumer<Boolean> setter = aspectDefinition.createComponentValueSetter(componentWrapper);
        assertThat(componentWrapper.getComponent().isEnabled(), is(false));
        setter.accept(true);
        assertThat(componentWrapper.getComponent().isEnabled(), is(true));
    }

    @Test
    void testComponentValueSetterOnlyCalledOnce() {
        when(propertyDispatcher.pull(any())).thenReturn(2);
        aspectDefinition = spy(new TestStaticModelToUiAspectDefinition());
        var componentValueSetter = spy(new ComponentValueSetterConsumer());
        doReturn(componentValueSetter).when(aspectDefinition).createComponentValueSetter(any());

        var uiUpdater = aspectDefinition.createUiUpdater(propertyDispatcher, componentWrapper);
        uiUpdater.apply();
        verify(componentValueSetter).accept(any());
    }

    static class ComponentValueSetterConsumer implements Consumer<Integer> {
        @Override
        public void accept(Integer integer) {
            // nothing
        }
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
