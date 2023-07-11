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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.BindingDescriptor;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.nls.TestComponentWrapper;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.util.handler.Handler;

class ContainerBindingTest {

    @Test
    void testModelChanged_IsForwardedToParent() {
        BindingContext bindingContext = spy(new BindingContext());
        ComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());
        ContainerBinding binding = bindingContext.bindContainer(new TestPmo(new TestModelObject()),
                                                                new BindingDescriptor(BoundProperty.of("test")),
                                                                componentWrapper);

        binding.modelChanged();

        verify(bindingContext).modelChanged();
    }

    @Test
    void testUpdateBindings_InitialUpdated() {
        BindingContext bindingContext = new BindingContext();
        ComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());
        TestAspectDef testAspectDef = new TestAspectDef();

        bindingContext.bindContainer(new TestPmo(new TestModelObject()),
                                     new BindingDescriptor(BoundProperty.of("test"), testAspectDef),
                                     componentWrapper);

        assertThat(testAspectDef.triggered).isTrue();
    }

    @Test
    void testUpdateFromPmo_ManuallyUpdated() {
        BindingContext bindingContext = new BindingContext();
        ComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());
        TestAspectDef testAspectDef = new TestAspectDef();

        bindingContext.bindContainer(new TestPmo(new TestModelObject()),
                                     new BindingDescriptor(BoundProperty.of("test"), testAspectDef),
                                     componentWrapper);
        testAspectDef.triggered = false;
        bindingContext.updateBindings();

        assertThat(testAspectDef.triggered).isTrue();
    }

    @Test
    void testUpdateFromPmo_ContainerBindingBeforeChildren() {
        BindingContext bindingContext = new BindingContext();
        ComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());
        TestAspectDef testAspectDef = new TestAspectDef();
        TestDependantAspectDef testDependantAspectDef = new TestDependantAspectDef(testAspectDef);

        ContainerBinding binding = bindingContext.bindContainer(new Object(), new BindingDescriptor(
                BoundProperty.of("test"), testAspectDef),
                                                                componentWrapper);
        binding.bind(new Object(), new BindingDescriptor(BoundProperty.of("test2"), testDependantAspectDef),
                     componentWrapper);

        assertThat(testAspectDef.triggered).isTrue();
        assertThat(testDependantAspectDef.triggered).isTrue();

        testAspectDef.triggered = false;
        testDependantAspectDef.triggered = false;
        binding.updateFromPmo();

        assertThat(testAspectDef.triggered).isTrue();
        assertThat(testDependantAspectDef.triggered).isTrue();
    }

    @Test
    void testUpdateBindings_ManuallyUpdated() {
        BindingContext bindingContext = new BindingContext();
        ComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());
        TestAspectDef testAspectDef = new TestAspectDef();

        bindingContext.bindContainer(new TestPmo(new TestModelObject()),
                                     new BindingDescriptor(BoundProperty.of("test"), testAspectDef),
                                     componentWrapper);
        testAspectDef.triggered = false;
        bindingContext.updateBindings();

        assertThat(testAspectDef.triggered).isTrue();
    }

    @Test
    void testUpdateBindings_ContainerBindingBeforeChildren() {
        BindingContext bindingContext = new BindingContext();
        ComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());
        TestAspectDef testAspectDef = new TestAspectDef();
        TestDependantAspectDef testDependantAspectDef = new TestDependantAspectDef(testAspectDef);

        ContainerBinding binding = bindingContext.bindContainer(new Object(),
                                                                new BindingDescriptor(BoundProperty.of("test"),
                                                                        testAspectDef),
                                                                componentWrapper);
        binding.bind(new Object(), new BindingDescriptor(BoundProperty.of("test2"), testDependantAspectDef),
                     componentWrapper);

        assertThat(testAspectDef.triggered).isTrue();
        assertThat(testDependantAspectDef.triggered).isTrue();

        testAspectDef.triggered = false;
        testDependantAspectDef.triggered = false;
        binding.updateBindings();

        assertThat(testAspectDef.triggered).isTrue();
        assertThat(testDependantAspectDef.triggered).isTrue();
    }

    @Test
    void testUpdateBindings_NotUpdateInvisibleChildren() {
        var bindingContext = new BindingContext();
        var component = new TestUiComponent();
        component.setVisible(false);
        var componentWrapper = new TestComponentWrapper(component);
        var testAspectDef = new TestAspectDef();
        var testDependantAspectDef = new TestDependantAspectDef(testAspectDef);
        var bindingDescriptor = new BindingDescriptor(BoundProperty.of("test"),
                List.of(testAspectDef));
        var bindingDescriptorDependants = new BindingDescriptor(BoundProperty.of("test2"),
                List.of(testDependantAspectDef));
        var binding = bindingContext.bindContainer(new Object(), bindingDescriptor, componentWrapper);
        binding.bind(new Object(), bindingDescriptorDependants, componentWrapper);

        // resetting to false because an initial update has been triggered
        testAspectDef.triggered = false;
        testDependantAspectDef.triggered = false;
        binding.updateBindings();

        assertThat(testAspectDef.triggered).isTrue();
        // not triggered since the component is invisible
        assertThat(testDependantAspectDef.triggered).isFalse();
    }

    @Test
    void testModelChanged() {
        BindingContext bindingContext = new BindingContext();
        ComponentWrapper componentWrapper = new TestComponentWrapper(new TestUiComponent());
        TestAspectDef testAspectDef = new TestAspectDef();

        ContainerBinding binding = bindingContext
                .bindContainer(new Object(), new BindingDescriptor(BoundProperty.of("test"), testAspectDef),
                               componentWrapper);
        testAspectDef.triggered = false;
        binding.modelChanged();

        assertThat(testAspectDef.triggered).isTrue();
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
