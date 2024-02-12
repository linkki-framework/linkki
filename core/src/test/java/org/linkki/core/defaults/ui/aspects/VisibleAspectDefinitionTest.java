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

package org.linkki.core.defaults.ui.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.nls.TestComponentWrapper;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
class VisibleAspectDefinitionTest {

    private final TestComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());

    @Test
    void testCreateAspect_Visible() {
        VisibleAspectDefinition aspectDefinition = new VisibleAspectDefinition(VisibleType.VISIBLE);

        Aspect<Boolean> createdAspect = aspectDefinition.createAspect(() -> null);

        assertThat(createdAspect.getName(), is(VisibleAspectDefinition.NAME));
        assertThat(createdAspect.getValue(), is(true));
    }

    @Test
    void testCreateAspect_Invisible() {
        VisibleAspectDefinition aspectDefinition = new VisibleAspectDefinition(VisibleType.INVISIBLE);

        Aspect<Boolean> createdAspect = aspectDefinition.createAspect(() -> null);

        assertThat(createdAspect.getName(), is(VisibleAspectDefinition.NAME));
        assertThat(createdAspect.isValuePresent(), is(true));
        assertThat(createdAspect.getValue(), is(false));
    }

    @Test
    void testCreateAspect_Dynamic() {
        VisibleAspectDefinition aspectDefinition = new VisibleAspectDefinition(VisibleType.DYNAMIC);

        Aspect<Boolean> createdAspect = aspectDefinition.createAspect(() -> null);

        assertThat(createdAspect.getName(), is(VisibleAspectDefinition.NAME));
        assertThat(createdAspect.isValuePresent(), is(false));
    }

    @Test
    void testInvisibleIfEmpty_null() {
        var wrapper = spy(ComponentWrapper.class);
        var propertyDispatcher = mock(PropertyDispatcher.class);

        when(propertyDispatcher.pull(any())).then(invocationOnMock -> {
            var aspect = (Aspect<?>)invocationOnMock.getArgument(0);

            return switch (aspect.getName()) {
                case VisibleAspectDefinition.VALUE_ASPECT_NAME -> null;
                case VisibleAspectDefinition.NAME -> aspect.getValue();
                default -> throw new IllegalStateException("Test should not reach to this point");
            };
        });

        var aspectDefinition = new VisibleAspectDefinition(VisibleType.INVISIBLE_IF_EMPTY);
        aspectDefinition.createUiUpdater(propertyDispatcher, wrapper).apply();

        verify(wrapper).setVisible(false);
    }

    @Test
    void testUiUpdater_exception() {
        var wrapper = mock(ComponentWrapper.class);
        var propertyDispatcher = mock(PropertyDispatcher.class);

        when(propertyDispatcher.pull(any())).then(returnValueForAspect(null, aspect -> {
            throw new RuntimeException("Should be caught by handler created by createUiUpdater");
        }));

        var aspectDefinition = new VisibleAspectDefinition(VisibleType.VISIBLE);
        var uiUpdater = aspectDefinition.createUiUpdater(propertyDispatcher, wrapper);

        assertThrows(LinkkiBindingException.class, uiUpdater::apply);
    }

    @Test
    void testVisibleAspectIsNull() {
        var wrapper = mock(ComponentWrapper.class);
        var propertyDispatcher = mock(PropertyDispatcher.class);

        when(propertyDispatcher.pull(any())).thenReturn(null);

        var aspectDefinition = spy(new VisibleAspectDefinition(VisibleType.VISIBLE));
        aspectDefinition.createUiUpdater(propertyDispatcher, wrapper).apply();

        verify(aspectDefinition).handleNullValue(any(), any());
    }

    @Test
    void testInvisibleIfEmpty_empty() {
        var wrapper = spy(ComponentWrapper.class);
        var propertyDispatcher = mock(PropertyDispatcher.class);

        when(propertyDispatcher.pull(any())).then(returnValueForAspect(""));

        var aspectDefinition = new VisibleAspectDefinition(VisibleType.INVISIBLE_IF_EMPTY);
        aspectDefinition.createUiUpdater(propertyDispatcher, wrapper).apply();

        verify(wrapper).setVisible(false);
    }

    @Test
    void testInvisibleIfEmpty_nonEmpty() {
        var wrapper = spy(ComponentWrapper.class);
        var propertyDispatcher = mock(PropertyDispatcher.class);

        when(propertyDispatcher.pull(any())).then(returnValueForAspect("sample value"));

        var aspectDefinition = new VisibleAspectDefinition(VisibleType.INVISIBLE_IF_EMPTY);
        aspectDefinition.createUiUpdater(propertyDispatcher, wrapper).apply();

        verify(wrapper).setVisible(true);
    }

    private Answer<Object> returnValueForAspect(String valueAspectValue) {
        return returnValueForAspect(valueAspectValue, Aspect::getValue);
    }

    private Answer<Object> returnValueForAspect(String valueAspectValue,
            Function<Aspect<?>, Object> visibleAspect) {
        return invocationOnMock -> {
            var aspect = (Aspect<?>)invocationOnMock.getArgument(0);

            return switch (aspect.getName()) {
                case VisibleAspectDefinition.VALUE_ASPECT_NAME -> valueAspectValue;
                case VisibleAspectDefinition.NAME -> visibleAspect.apply(aspect);
                default -> throw new IllegalStateException("Test should not reach to this point");
            };

        };
    }

    @Test
    void testCreateComponentValueSetterComponentWrapper() {
        VisibleAspectDefinition aspectDefinition = new VisibleAspectDefinition(
                VisibleType.VISIBLE);
        Consumer<Boolean> setter = aspectDefinition.createComponentValueSetter(componentWrapper);
        assertThat(componentWrapper.getComponent().isVisible(), is(true));

        setter.accept(false);

        assertThat(componentWrapper.getComponent().isVisible(), is(false));
    }

    @Test
    void testHandleNullValue() {
        var aspectDefinition = new VisibleAspectDefinition(VisibleType.DYNAMIC);
        Consumer<Boolean> setter = aspectDefinition.createComponentValueSetter(componentWrapper);
        setter.accept(false);
        assertThat(componentWrapper.getComponent().isVisible(), is(false));

        aspectDefinition.handleNullValue(setter, componentWrapper);

        assertThat(componentWrapper.getComponent().isVisible(), is(false));

        setter.accept(true);

        aspectDefinition.handleNullValue(setter, componentWrapper);

        assertThat(componentWrapper.getComponent().isVisible(), is(false));
    }

}
