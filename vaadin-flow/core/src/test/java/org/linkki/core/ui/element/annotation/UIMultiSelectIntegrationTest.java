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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.Binding;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.ToStringCaptionProvider;
import org.linkki.core.ui.bind.TestEnum;
import org.linkki.core.ui.element.annotation.UIMultiSelectIntegrationTest.MultiSelectTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.uiframework.UiFramework;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.data.provider.Query;

import edu.umd.cs.findbugs.annotations.CheckForNull;

class UIMultiSelectIntegrationTest
        extends ComponentAnnotationIntegrationTest<MultiSelectComboBox<TestEnum>, MultiSelectTestPmo> {

    UIMultiSelectIntegrationTest() {
        super(MultiSelectTestModelObject::new, MultiSelectTestPmo::new);
    }

    @Test
    void testDisallowCustomValue() {
        assertThat(getDynamicComponent().isAllowCustomValue()).isFalse();
        assertThat(getStaticComponent().isAllowCustomValue()).isFalse();
    }

    @Test
    void testClearButton() {
        assertThat(getDefaultPmo().getValueAvailableValues()).doesNotContainNull();

        var multiselect = getDynamicComponent();

        assertThat(getAllowedValues(multiselect)).contains(TestEnum.ONE,
                TestEnum.TWO,
                TestEnum.THREE);
        assertThat(multiselect.isClearButtonVisible()).isTrue();
    }

    @Test
    void testStaticAvailableValues() {
        MultiSelectComboBox<TestEnum> multiselect = getStaticComponent();
        assertThat(getAllowedValues(multiselect)).contains(TestEnum.ONE, TestEnum.TWO,
                TestEnum.THREE);
    }

    @Test
    void testDynamicAvailableValues() {
        assertThat(getAllowedValues(getDynamicComponent())).contains(TestEnum.ONE, TestEnum.TWO,
                TestEnum.THREE);

        List<TestEnum> availableValues = new ArrayList<>(getDefaultPmo().getValueAvailableValues());
        availableValues.remove(TestEnum.ONE);
        getDefaultPmo().setValueAvailableValues(availableValues);
        modelChanged();
        assertThat(getAllowedValues(getDynamicComponent())).contains(TestEnum.TWO,
                TestEnum.THREE);
    }

    @Test
    void testCaptionProvider() {
        assertThat(getDynamicComponent().getItemLabelGenerator().apply(null)).isEmpty();
        assertThat(getDynamicComponent().getItemLabelGenerator().apply(TestEnum.ONE)).isEqualTo("Oans");
        assertThat(getStaticComponent().getItemLabelGenerator().apply(TestEnum.ONE))
                .isEqualTo("ONE " + UiFramework.getLocale().toLanguageTag());
    }

    @Test
    void testCaptionProvider_NoDefaultConstructor() {
        NoDefaultConstructorCaptionProviderPmo pmo = new NoDefaultConstructorCaptionProviderPmo();

        BindingContext bindingContext = new BindingContext();
        Function<Object, NoLabelComponentWrapper> wrapperCreator = c -> new NoLabelComponentWrapper((Component) c);

        var component = UiCreator.createUiElements(pmo, bindingContext, wrapperCreator);

        assertThatExceptionOfType(LinkkiBindingException.class).isThrownBy(() -> component.count());
    }

    @Test
    void testValue() {
        var multiselect = getDynamicComponent();
        multiselect.setValue(TestEnum.THREE);
        assertThat(multiselect.getValue()).containsExactly(TestEnum.THREE);

        getDefaultModelObject().setValue(Collections.singleton(TestEnum.TWO));
        modelChanged();
        assertThat(multiselect.getValue()).containsExactly(TestEnum.TWO);
    }

    @Test
    void testValueRemainsWhenChangingAvailableValues() {
        getDefaultModelObject().setValue(Collections.singleton(TestEnum.THREE));
        assertThat(getDynamicComponent().getValue()).containsExactly(TestEnum.THREE);

        getDefaultPmo().setValueAvailableValues(List.of(TestEnum.THREE));
        modelChanged();
        assertThat(getDynamicComponent().getValue()).containsExactly(TestEnum.THREE);
    }

    @Test
    void testEmptyValuesAllowsNull() {
        var multiselect = getDynamicComponent();

        getDefaultPmo().setValueAvailableValues(Collections.emptyList());
        modelChanged();
        assertThat(getAllowedValues(multiselect)).isEmpty();

        getDefaultPmo().setValueAvailableValues(List.of(TestEnum.ONE));
        modelChanged();
        assertThat(getAllowedValues(multiselect)).contains(TestEnum.ONE);
    }

    @Test
    void testNullInputIfRequired() {
        var multiselect = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        List<TestEnum> availableValues = new ArrayList<>(getDefaultPmo().getValueAvailableValues());
        availableValues.add(null);
        getDefaultPmo().setValueAvailableValues(availableValues);
        modelChanged();
        assertThat(multiselect.isRequiredIndicatorVisible()).isTrue();

        KaribuUtils.Fields.setValue(multiselect, Collections.singleton(TestEnum.ONE));
        assertThat(getDefaultModelObject().getValue()).containsExactly(TestEnum.ONE);

        KaribuUtils.Fields.setValue(multiselect, null);
        assertThat(getDefaultModelObject().getValue()).isEmpty();
    }

    @Test
    void testInitReadOnlyField() {
        var multiselect = getStaticComponent();
        multiselect.setReadOnly(false);
        ComponentUtil.setData(multiselect, Binding.class, null);
        getBindingContext().removeBindingsForPmo(getDefaultPmo());
        bind(getDefaultPmo(), "staticValue", multiselect);
        assertThat(multiselect.isReadOnly()).isTrue();

        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(multiselect.isRequiredIndicatorVisible()).isTrue();
        assertThat(multiselect.isReadOnly()).isTrue();

        getBindingContext().removeBindingsForComponent(multiselect);
        assertThat(multiselect.isReadOnly()).isTrue();

        ComponentUtil.setData(multiselect, Binding.class, null);

        bind(getDefaultPmo(), "staticValue", multiselect);
        assertThat(multiselect.isReadOnly()).isTrue();
    }

    @Test
    void testDerivedLabel() {
        assertThat(TestUiUtil.getLabelOfComponentAt(getDefaultSection(), 2)).isEqualTo("Foo");
    }

    @Override
    protected MultiSelectTestModelObject getDefaultModelObject() {
        return (MultiSelectTestModelObject) super.getDefaultModelObject();
    }

    private static List<TestEnum> getAllowedValues(MultiSelectComboBox<TestEnum> multiSelectComboBox) {
        return multiSelectComboBox.getDataProvider().fetch(new Query<>())
                .collect(Collectors.toList());
    }

    @UISection
    protected static class MultiSelectTestPmo extends AnnotationTestPmo {

        private List<TestEnum> availableValues = new ArrayList<>();

        public MultiSelectTestPmo(Object modelObject) {
            super(modelObject);
            availableValues.add(TestEnum.ONE);
            availableValues.add(TestEnum.TWO);
            availableValues.add(TestEnum.THREE);
        }

        @Override
        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @UIMultiSelect(position = 1,
                label = "",
                enabled = EnabledType.DYNAMIC,
                required = RequiredType.DYNAMIC,
                visible = VisibleType.DYNAMIC,
                itemCaptionProvider = ToStringCaptionProvider.class)
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
        @UIMultiSelect(position = 2,
                label = TEST_LABEL,
                enabled = EnabledType.DISABLED,
                required = RequiredType.REQUIRED,
                visible = VisibleType.INVISIBLE)
        public void staticValue() {
            // model binding
        }

        public List<TestEnum> getStaticValueAvailableValues() {
            return Collections.unmodifiableList(availableValues);
        }

        @UIMultiSelect(position = 3)
        public Set<TestEnum> getFoo() {
            return Collections.singleton(TestEnum.THREE);
        }

        public List<TestEnum> getFooAvailableValues() {
            return Collections.unmodifiableList(availableValues);
        }

    }

    protected static class MultiSelectTestModelObject {
        @CheckForNull
        private Set<TestEnum> value = Stream.of(TestEnum.THREE).collect(Collectors.toSet());

        @CheckForNull
        public Set<TestEnum> getValue() {
            return value;
        }

        public void setValue(@CheckForNull Set<TestEnum> value) {
            this.value = value;

        }

        @CheckForNull
        public Set<TestEnum> getStaticValue() {
            return getValue();
        }

    }

    protected static class NoDefaultConstructorCaptionProviderPmo {

        @UIMultiSelect(position = 3, itemCaptionProvider = PrivateItemCaptionProvider.class)
        public Set<TestEnum> getFoo() {
            return Collections.singleton(TestEnum.THREE);
        }

        private static class PrivateItemCaptionProvider implements ItemCaptionProvider<TestEnum> {

            private PrivateItemCaptionProvider() {
                // hide default constructor
            }

            @Override
            public String getCaption(TestEnum value) {
                return value.getName();
            }

        }
    }
}
