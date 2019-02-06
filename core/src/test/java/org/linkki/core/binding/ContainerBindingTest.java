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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Test;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.property.BoundProperty;
import org.linkki.core.ui.TestComponentWrapper;
import org.linkki.core.ui.TestUiComponent;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.util.handler.Handler;

public class ContainerBindingTest {

    @Test
    public void testModelChanged_IsForwardedToParent() {
        BindingContext bindingContext = spy(new BindingContext());
        ComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());
        ContainerBinding binding = bindingContext.bindContainer(new TestPmo(new TestModelObject()),
                                                                BoundProperty.of("test"), Arrays.asList(),
                                                                componentWrapper);

        binding.modelChanged();

        verify(bindingContext).modelChanged();
    }

    @Test
    public void testUpdateFromPmo_InitialUpdated() {
        BindingContext bindingContext = new BindingContext();
        ComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());
        TestAspectDef testAspectDef = new TestAspectDef();

        bindingContext.bindContainer(new TestPmo(new TestModelObject()),
                                     BoundProperty.of("test"),
                                     Arrays.asList(testAspectDef),
                                     componentWrapper);
        assertThat(testAspectDef.triggered, is(true));
    }

    @Test
    public void testUpdateFromPmo_ManuallyUpdated() {
        BindingContext bindingContext = new BindingContext();
        ComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());
        TestAspectDef testAspectDef = new TestAspectDef();

        bindingContext.bindContainer(new TestPmo(new TestModelObject()),
                                     BoundProperty.of("test"),
                                     Arrays.asList(testAspectDef),
                                     componentWrapper);
        testAspectDef.triggered = false;
        bindingContext.updateFromPmo();

        assertThat(testAspectDef.triggered, is(true));
    }

    @Test
    public void testUpdateFromPmo_ContainerBindingBeforeChildren() {
        BindingContext bindingContext = new BindingContext();
        ComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());
        TestAspectDef testAspectDef = new TestAspectDef();
        TestDependantAspectDef testDependantAspectDef = new TestDependantAspectDef(testAspectDef);

        ContainerBinding binding = bindingContext.bindContainer(new Object(),
                                                                BoundProperty.of("test"),
                                                                Arrays.asList(testAspectDef),
                                                                componentWrapper);
        binding.bind(new Object(), BoundProperty.of("test2"), Arrays.asList(testDependantAspectDef), componentWrapper);

        assertThat(testAspectDef.triggered, is(true));
        assertThat(testDependantAspectDef.triggered, is(true));

        testAspectDef.triggered = false;
        testDependantAspectDef.triggered = false;
        binding.updateFromPmo();

        assertThat(testAspectDef.triggered, is(true));
        assertThat(testDependantAspectDef.triggered, is(true));
    }

    @Test
    public void testModelChanged() {
        BindingContext bindingContext = new BindingContext();
        ComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());
        TestAspectDef testAspectDef = new TestAspectDef();

        ContainerBinding binding = bindingContext.bindContainer(new Object(),
                                                                BoundProperty.of("test"),
                                                                Arrays.asList(testAspectDef),
                                                                componentWrapper);
        testAspectDef.triggered = false;
        binding.modelChanged();

        assertThat(testAspectDef.triggered, is(true));
    }

    private static class TestAspectDef implements LinkkiAspectDefinition {

        boolean triggered;

        @Override
        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            return () -> triggered = true;
        }

    }

    private static class TestDependantAspectDef implements LinkkiAspectDefinition {

        private final TestAspectDef other;

        boolean triggered;

        public TestDependantAspectDef(TestAspectDef other) {
            this.other = other;
        }

        @Override
        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            return () -> triggered = other.triggered;
        }

    }

}
