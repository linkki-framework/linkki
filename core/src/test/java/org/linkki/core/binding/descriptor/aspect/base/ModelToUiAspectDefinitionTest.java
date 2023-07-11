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

package org.linkki.core.binding.descriptor.aspect.base;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;

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
class ModelToUiAspectDefinitionTest {

    @Mock
    private PropertyDispatcher propertyDispatcher;

    private final TestComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());

    private final TestModelToUiAspectDefinition aspectDefinition = new TestModelToUiAspectDefinition();

    @SuppressWarnings("unchecked")
    @Test
    void testCreateUiUpdater() {
        when(propertyDispatcher.pull(any(Aspect.class))).thenReturn(true);

        Handler handler = aspectDefinition.createUiUpdater(propertyDispatcher, componentWrapper);
        assertThat(componentWrapper.getComponent().isEnabled(), is(false));

        handler.apply();
        verify(propertyDispatcher, times(1)).pull(any(Aspect.class));
        assertThat(componentWrapper.getComponent().isEnabled(), is(true));

        handler.apply();
        verify(propertyDispatcher, times(2)).pull(any(Aspect.class));
    }

    @Test
    void testCreateUiUpdater_WrapsExceptionInPull() {
        when(propertyDispatcher.pull(any())).thenThrow(RuntimeException.class);
        Handler handler = aspectDefinition.createUiUpdater(propertyDispatcher, componentWrapper);

        assertThrows(LinkkiBindingException.class, handler::apply);
    }

    @Test
    void testCreateUiUpdate_HandleNullValue_ComponentDoesNotAllowNull() {
        assertThat(componentWrapper.getComponent().isEnabled(), is(false));
        when(propertyDispatcher.pull(any())).thenReturn(null);
        var updater = aspectDefinition.createUiUpdater(propertyDispatcher, componentWrapper);

        assertThrows(LinkkiBindingException.class, updater::apply);
    }

    @Test
    void testCreateUiUpdate_HandleNullValue() {
        componentWrapper.getComponent().setTooltipText("text");
        when(propertyDispatcher.pull(any())).thenReturn(null);
        var aspectDefinition = new TestModelToUiAspectDefinitionAllowingNull();
        var updater = aspectDefinition.createUiUpdater(propertyDispatcher, componentWrapper);

        updater.apply();

        assertThat(componentWrapper.getComponent().getTooltipText(), is(nullValue()));
    }

    @Test
    void testCreateAspect() {
        Aspect<Boolean> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName(), is(TestModelToUiAspectDefinition.NAME));
        assertThat(createdAspect.getValue(), is(true));
    }

    @Test
    void testCreateComponentValueSetter() {
        Consumer<Boolean> setter = aspectDefinition.createComponentValueSetter(componentWrapper);
        assertThat(componentWrapper.getComponent().isEnabled(), is(false));
        setter.accept(true);
        assertThat(componentWrapper.getComponent().isEnabled(), is(true));
    }


    private static class TestModelToUiAspectDefinition extends ModelToUiAspectDefinition<Boolean> {

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

    private static class TestModelToUiAspectDefinitionAllowingNull extends ModelToUiAspectDefinition<String> {

        public static final String NAME = "allowsNull";

        @Override
        public Aspect<String> createAspect() {
            return Aspect.of(NAME, "");
        }

        @Override
        public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
            return componentWrapper::setTooltip;
        }
    }

}
