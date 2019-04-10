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

package org.linkki.core.uicreation;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.linkki.core.binding.Binding;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.TestModelObject;
import org.linkki.core.binding.TestPmo;
import org.linkki.core.binding.descriptor.TestLinkkiComponentDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.nls.TestComponentWrapper;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.core.defaults.nls.TestUiLayoutComponent;
import org.linkki.core.defaults.section.TestSectionPmo;

public class UiCreatorTest {

    @Test
            public void testCreateUiElements() {
                TestSectionPmo testSectionPmo = new TestSectionPmo();
                BindingContext bindingContext = new BindingContext();
                List<TestComponentWrapper> wrappers = UiCreator
                        .createUiElements(testSectionPmo, bindingContext, TestComponentWrapper::new)
                        .collect(Collectors.toList());
        
                assertThat(wrappers, hasSize(2));
                List<String> componentIds = wrappers.stream().map(TestComponentWrapper::getComponent)
                        .map(TestUiComponent::getId).collect(Collectors.toList());
                assertThat(componentIds, contains(TestPmo.PROPERTY_VALUE, TestModelObject.PROPERTY_MODEL_PROP));
        
                List<String> boundIds = bindingContext.getBindings().stream().map(Binding::getBoundComponent)
                        .map(TestUiComponent.class::cast).map(TestUiComponent::getId).collect(Collectors.toList());
                assertThat(boundIds, containsInAnyOrder(TestPmo.PROPERTY_VALUE, TestModelObject.PROPERTY_MODEL_PROP));
            }

    @Test
    public void testCreateUiComponent_Method() throws NoSuchMethodException, SecurityException {
        TestSectionPmo testSectionPmo = new TestSectionPmo();
        BindingContext bindingContext = new BindingContext();
        Method method = TestSectionPmo.class.getMethod("getValue");

        ComponentWrapper componentWrapper = UiCreator.createComponent(method, testSectionPmo, bindingContext,
                                                                             ComponentAnnotationReader::findComponentDefinition,
                                                                             c -> Optional.empty());

        assertThat(componentWrapper.getComponent(), is(instanceOf(TestUiComponent.class)));
        TestUiComponent testUiComponent = (TestUiComponent)componentWrapper.getComponent();
        assertThat(testUiComponent.getId(), is(testSectionPmo.getId()));

        assertThat(bindingContext.getBindings(), hasSize(1));
        Binding binding = bindingContext.getBindings().iterator().next();
        assertThat(binding.getBoundComponent(), is(componentWrapper.getComponent()));
        assertThat(binding, is(not(instanceOf(BindingContext.class))));
    }

    @Test
    public void testCreateUiComponent() {
        TestSectionPmo testSectionPmo = new TestSectionPmo();
        BindingContext bindingContext = new BindingContext();

        ComponentWrapper componentWrapper = UiCreator.createComponent(testSectionPmo, bindingContext,
                                                                             c -> Optional
                                                                                     .of(TestLinkkiComponentDefinition
                                                                                             .create(TestUiLayoutComponent::new)),
                                                                             c -> Optional.of((parent,
                                                                                     pmo,
                                                                                     bc) -> ((TestUiLayoutComponent)parent)
                                                                                             .addChild(new TestUiComponent())));

        assertThat(componentWrapper.getComponent(), is(instanceOf(TestUiLayoutComponent.class)));
        TestUiLayoutComponent testUiLayoutComponent = (TestUiLayoutComponent)componentWrapper.getComponent();
        assertThat(testUiLayoutComponent.getId(), is(testSectionPmo.getId()));
        assertThat(testUiLayoutComponent.getChildren(), hasSize(1));

        assertThat(bindingContext.getBindings(), hasSize(1));
        Binding binding = bindingContext.getBindings().iterator().next();
        assertThat(binding.getBoundComponent(), is(componentWrapper.getComponent()));
        assertThat(binding, is(instanceOf(BindingContext.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUiComponent_NoComponentDefinition() {
        UiCreator.createComponent(new TestSectionPmo(),
                                         new BindingContext(),
                                         c -> Optional.empty(),
                                         c -> Optional.empty());
    }

    @Test
    public void testCreateUiComponent_NoLayoutDefinition() {
        TestSectionPmo testSectionPmo = new TestSectionPmo();
        BindingContext bindingContext = new BindingContext();

        ComponentWrapper componentWrapper = UiCreator.createComponent(testSectionPmo, bindingContext,
                                                                             c -> Optional
                                                                                     .of(TestLinkkiComponentDefinition
                                                                                             .create(TestUiLayoutComponent::new)),
                                                                             c -> Optional.empty());

        assertThat(componentWrapper.getComponent(), is(instanceOf(TestUiLayoutComponent.class)));
        TestUiLayoutComponent testUiLayoutComponent = (TestUiLayoutComponent)componentWrapper.getComponent();
        assertThat(testUiLayoutComponent.getId(), is(testSectionPmo.getId()));

        assertThat(bindingContext.getBindings(), hasSize(1));
        Binding binding = bindingContext.getBindings().iterator().next();
        assertThat(binding.getBoundComponent(), is(componentWrapper.getComponent()));
        assertThat(binding, is(not(instanceOf(BindingContext.class))));
    }


}
