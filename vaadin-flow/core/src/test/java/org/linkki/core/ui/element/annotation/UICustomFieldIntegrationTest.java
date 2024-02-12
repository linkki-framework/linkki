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
package org.linkki.core.ui.element.annotation;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.bind.TestEnum;
import org.linkki.core.ui.element.annotation.UICustomFieldIntegrationTest.ComponentAnnotationTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.shared.Registration;

import edu.umd.cs.findbugs.annotations.CheckForNull;

class UICustomFieldIntegrationTest
        extends ComponentAnnotationIntegrationTest<ComboBox<TestEnum>, ComponentAnnotationTestPmo> {

    UICustomFieldIntegrationTest() {
        super(ComponentAnnotationTestModelObject::new, ComponentAnnotationTestPmo::new);
    }

    @Test
    void testDynamicAvailableValues() {
        assertThat(getAllowedValues(getDynamicComponent()), contains(TestEnum.ONE, TestEnum.TWO,
                                                                     TestEnum.THREE));

        List<TestEnum> availableValues = new ArrayList<>(getDefaultPmo().getValueAvailableValues());
        availableValues.remove(TestEnum.ONE);
        getDefaultPmo().setValueAvailableValues(availableValues);
        modelChanged();
        assertThat(getAllowedValues(getDynamicComponent()), contains(TestEnum.TWO,
                                                                     TestEnum.THREE));
    }

    @Test
    void testNullInputIfRequired() {
        ComboBox<TestEnum> component = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(component.isRequiredIndicatorVisible(), is(true));

        TestUiUtil.setUserOriginatedValue(component, TestEnum.ONE);
        assertThat(getDefaultModelObject().getValue(), is(TestEnum.ONE));

        TestUiUtil.setUserOriginatedValue(component, (TestEnum)null);
        assertThat(getDefaultModelObject().getValue(), is(nullValue()));
    }

    @Test
    void testDerivedLabel() {
        assertThat(TestUiUtil.getLabelOfComponentAt(getDefaultSection(), 2), is("Foo"));
    }

    @Test
    void testCreateComponent_NoDefaultConstructor() {
        NoDefaultConstructorComponentPmo pmo = new NoDefaultConstructorComponentPmo();
        BindingContext bindingContext = new BindingContext();
        Function<Component, ComponentWrapper> wrapperCreator = c -> new NoLabelComponentWrapper(c);

        assertThrows(LinkkiBindingException.class,
                     () -> UiCreator.createUiElements(pmo, bindingContext, wrapperCreator).count());
    }

    @Test
    void testWidth() {
        ComponentAnnotationTestPmo pmo = new ComponentAnnotationTestPmo(getDefaultModelObject());
        BindingContext bindingContext = new BindingContext();
        Function<Component, ComponentWrapper> wrapperCreator = c -> new NoLabelComponentWrapper(c);

        ComboBox<?> componentWithWidth = (ComboBox<?>)UiCreator.createUiElements(pmo, bindingContext, wrapperCreator)
                .collect(Collectors.toList()).get(2).getComponent();

        assertThat(componentWithWidth.getWidth(), is("100px"));
    }

    @Test
    void testWidth_IgnoreIfNotHasSize() {
        NoSizeComponentPmo pmo = new NoSizeComponentPmo();
        BindingContext bindingContext = new BindingContext();
        Function<Component, ComponentWrapper> wrapperCreator = c -> new NoLabelComponentWrapper(c);

        assertThat(UiCreator.createUiElements(pmo, bindingContext, wrapperCreator).count(), is(1L));
    }

    @Override
    protected ComponentAnnotationTestModelObject getDefaultModelObject() {
        return (ComponentAnnotationTestModelObject)super.getDefaultModelObject();
    }

    private static List<TestEnum> getAllowedValues(ComboBox<TestEnum> comboBox) {
        return comboBox.getDataProvider().fetch(new Query<>())
                .collect(Collectors.toList());
    }

    @UISection
    protected static class ComponentAnnotationTestPmo extends AnnotationTestPmo {

        private List<TestEnum> availableValues = new ArrayList<>();

        public ComponentAnnotationTestPmo(Object modelObject) {
            super(modelObject);
            availableValues.add(TestEnum.ONE);
            availableValues.add(TestEnum.TWO);
            availableValues.add(TestEnum.THREE);
        }

        @Override
        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @UICustomField(position = 1,
                label = "",
                enabled = EnabledType.DYNAMIC,
                required = RequiredType.DYNAMIC,
                visible = VisibleType.DYNAMIC,
                content = AvailableValuesType.DYNAMIC,
                uiControl = ComboBox.class)
        public void value() {
            // model binding
        }

        public List<TestEnum> getValueAvailableValues() {
            return Collections.unmodifiableList(availableValues);
        }

        public void setValueAvailableValues(List<TestEnum> values) {
            this.availableValues = values;
        }

        @Override
        @BindTooltip(TEST_TOOLTIP)
        @UICustomField(position = 2,
                label = TEST_LABEL,
                enabled = EnabledType.DISABLED,
                required = RequiredType.REQUIRED,
                visible = VisibleType.INVISIBLE,
                content = AvailableValuesType.ENUM_VALUES_EXCL_NULL,
                uiControl = ComboBox.class)
        public void staticValue() {
            // model binding
        }

        @UICustomField(position = 3, uiControl = ComboBox.class, width = "100px")
        public TestEnum getFoo() {
            return TestEnum.THREE;
        }
    }

    protected static class ComponentAnnotationTestModelObject {
        @CheckForNull
        private TestEnum value;

        @CheckForNull
        public TestEnum getValue() {
            return value;
        }

        public void setValue(TestEnum value) {
            this.value = value;

        }

        @CheckForNull
        public TestEnum getStaticValue() {
            return getValue();
        }

    }

    private static class NoDefaultConstructorComponentPmo {

        @UICustomField(position = 1, label = "", uiControl = NoDefaultConstructorComponent.class)
        public String getValue() {
            return "";
        }

        @Tag("no-default")
        private static class NoDefaultConstructorComponent extends Component implements FakeHasStringValue {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unused")
            public NoDefaultConstructorComponent(String value) {
                super();
            }

        }
    }

    private static class NoSizeComponentPmo {

        @UICustomField(position = 1, label = "", uiControl = NoSizeComponent.class, width = "100px")
        public String getValue() {
            return "";
        }

        @Tag("no-size")
        private static class NoSizeComponent extends Component implements FakeHasStringValue {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unused")
            public NoSizeComponent() {
                super();
            }
        }
    }

    private static interface FakeHasStringValue extends HasValue<ValueChangeEvent<String>, String> {
        @Override
        default void setValue(String value) {
            // does nothing
        }

        @Override
        default String getValue() {
            return null;
        }

        @Override
        default Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<String>> listener) {
            return null;
        }

        @Override
        default void setReadOnly(boolean readOnly) {
            // does nothing
        }

        @Override
        default boolean isReadOnly() {
            return false;
        }

        @Override
        default void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
            // does nothing
        }

        @Override
        default boolean isRequiredIndicatorVisible() {
            return false;
        }
    }
}
