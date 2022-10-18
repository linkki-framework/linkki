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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.linkki.core.binding.Binding;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.TestModelObject;
import org.linkki.core.binding.TestPmo;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.nls.TestComponentWrapper;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.core.defaults.nls.TestUiLayoutComponent;
import org.linkki.core.defaults.section.TestSectionPmo;
import org.linkki.core.defaults.section.annotations.TestUIField;
import org.linkki.core.defaults.section.annotations.TestUIField2;
import org.linkki.core.uicreation.UiCreatorTest.UITestSection.TestSectionLayoutComponentDefinitionCreator;
import org.linkki.core.uicreation.UiCreatorTest.UITestSectionWithLayout.TestSectionWithLayoutComponentDefinitionCreator;
import org.linkki.core.uicreation.UiCreatorTest.UITestSectionWithLayout.TestSectionWithLayoutDefinitionCreator;
import org.linkki.core.uicreation.layout.LayoutDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayout;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

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

    @DisplayName("Given a dynamic field annotated with both @TestUIField and @TestUIField2,")
    @ParameterizedTest(name = "when the get~ComponentType method returns {0}")
    @ValueSource(classes = { TestUIField.class, TestUIField2.class })
    public void testCreateUiElements_DynamicField(Class<?> componentType) {
        class DynamicFieldTestPmo {

            @SuppressFBWarnings("UMAC_UNCALLABLE_METHOD_OF_ANONYMOUS_CLASS")
            @TestUIField(position = 10)
            @TestUIField2(position = 10)
            public String getDynamic() {
                return "dyn";
            }

            @SuppressFBWarnings("UMAC_UNCALLABLE_METHOD_OF_ANONYMOUS_CLASS")
            public Class<?> getDynamicComponentType() {
                return componentType;
            }
        }

        DynamicFieldTestPmo testPmo = new DynamicFieldTestPmo();
        BindingContext bindingContext = new BindingContext();

        List<TestUiComponent> components = UiCreator
                .createUiElements(testPmo, bindingContext, TestComponentWrapper::new)
                .map(TestComponentWrapper::getComponent)
                .collect(Collectors.toList());

        assertThat(components, hasSize(1));
        if (componentType.equals(TestUIField.class)) {
            assertThat(components.get(0), is(not(instanceOf(TestUIField2.TestUiComponent2.class))));
        } else if (componentType.equals(TestUIField2.class)) {
            assertThat(components.get(0), is(instanceOf(TestUIField2.TestUiComponent2.class)));
        }

    }

    @Test
    public void testCreateUiComponent_Method() throws NoSuchMethodException, SecurityException {
        TestSectionPmo testSectionPmo = new TestSectionPmo();
        BindingContext bindingContext = new BindingContext();
        Method method = TestSectionPmo.class.getMethod("getValue");

        ComponentWrapper componentWrapper = UiCreator.createComponent(testSectionPmo,
                                                                      bindingContext,
                                                                      ComponentAnnotationReader
                                                                              .findComponentDefinition(method).get(),
                                                                      Optional.empty());

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
                                                                      TestLinkkiComponentDefinition
                                                                              .create(TestUiLayoutComponent::new),
                                                                      Optional.of((parent,
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

    @Test
    public void testCreateUiComponent_WithLayoutDefinition() {
        TestSectionWithLayoutPmo testSectionPmo = new TestSectionWithLayoutPmo();
        BindingContext bindingContext = new BindingContext();

        ComponentWrapper componentWrapper = UiCreator.createComponent(testSectionPmo, bindingContext);

        assertThat(componentWrapper.getComponent(), is(instanceOf(TestUiLayoutComponent.class)));
        TestUiLayoutComponent testUiLayoutComponent = (TestUiLayoutComponent)componentWrapper.getComponent();
        assertThat(testUiLayoutComponent.getId(), is(testSectionPmo.getId()));
        assertThat(testUiLayoutComponent.getChildren(), hasSize(1));

        assertThat(bindingContext.getBindings(), hasSize(1));
        Binding binding = bindingContext.getBindings().iterator().next();
        assertThat(binding.getBoundComponent(), is(componentWrapper.getComponent()));
        assertThat(binding, is(instanceOf(BindingContext.class)));
    }

    @Test
    public void testCreateUiComponent_NoComponentDefinition() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            UiCreator.createComponent(new TestSectionPmo(),
                                      new BindingContext());
        });
    }

    @Test
    public void testCreateUiComponent_NoLayoutDefinition() {
        TestSectionWithoutLayoutPmo testSectionPmo = new TestSectionWithoutLayoutPmo();
        BindingContext bindingContext = new BindingContext();

        ComponentWrapper componentWrapper = UiCreator.createComponent(testSectionPmo, bindingContext);

        assertThat(componentWrapper.getComponent(), is(instanceOf(TestUiLayoutComponent.class)));
        TestUiLayoutComponent testUiLayoutComponent = (TestUiLayoutComponent)componentWrapper.getComponent();
        assertThat(testUiLayoutComponent.getId(), is(testSectionPmo.getId()));

        assertThat(bindingContext.getBindings(), hasSize(1));
        Binding binding = bindingContext.getBindings().iterator().next();
        assertThat(binding.getBoundComponent(), is(componentWrapper.getComponent()));
        assertThat(binding, is(not(instanceOf(BindingContext.class))));
    }

    @UITestSectionWithLayout
    private static class TestSectionWithLayoutPmo extends TestSectionPmo {
        // nop
    }

    @UITestSection
    private static class TestSectionWithoutLayoutPmo extends TestSectionPmo {
        // nop
    }

    @Retention(RetentionPolicy.RUNTIME)
    @LinkkiComponent(TestSectionWithLayoutComponentDefinitionCreator.class)
    @LinkkiLayout(TestSectionWithLayoutDefinitionCreator.class)
    public @interface UITestSectionWithLayout {

        class TestSectionWithLayoutComponentDefinitionCreator
                implements ComponentDefinitionCreator<UITestSectionWithLayout> {
            @Override
            public LinkkiComponentDefinition create(UITestSectionWithLayout annotation,
                    AnnotatedElement annotatedElement) {
                return TestLinkkiComponentDefinition.create(() -> new TestUiLayoutComponent());
            }
        }

        class TestSectionWithLayoutDefinitionCreator implements LayoutDefinitionCreator<Annotation> {

            @Override
            public LinkkiLayoutDefinition create(Annotation annotation, AnnotatedElement annotatedElement) {
                return new TestSectionWithLayoutDefinition();
            }

        }

        class TestSectionWithLayoutDefinition implements LinkkiLayoutDefinition {

            @Override
            public void createChildren(Object parentComponent, Object pmo, BindingContext bindingContext) {
                ((TestUiLayoutComponent)parentComponent).addChild(new TestUiComponent());
            }

        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @LinkkiComponent(TestSectionLayoutComponentDefinitionCreator.class)
    public @interface UITestSection {

        class TestSectionLayoutComponentDefinitionCreator implements ComponentDefinitionCreator<UITestSection> {
            @Override
            public LinkkiComponentDefinition create(UITestSection annotation,
                    AnnotatedElement annotatedElement) {
                return TestLinkkiComponentDefinition.create(() -> new TestUiLayoutComponent());
            }
        }

    }
}
