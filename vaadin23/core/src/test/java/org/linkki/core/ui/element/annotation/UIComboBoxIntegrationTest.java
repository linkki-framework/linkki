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
package org.linkki.core.ui.element.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.Binding;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.ToStringCaptionProvider;
import org.linkki.core.ui.aspects.types.TextAlignment;
import org.linkki.core.ui.bind.TestEnum;
import org.linkki.core.ui.element.annotation.UIComboBoxIntegrationTest.ComboBoxTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.uiframework.UiFramework;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.data.provider.Query;

import edu.umd.cs.findbugs.annotations.CheckForNull;

class UIComboBoxIntegrationTest extends ComponentAnnotationIntegrationTest<ComboBox<TestEnum>, ComboBoxTestPmo> {

    UIComboBoxIntegrationTest() {
        super(ComboBoxTestModelObject::new, ComboBoxTestPmo::new);
    }

    @Test
    void testDisallowCustomValue() {
        assertThat(getDynamicComponent().isAllowCustomValue(), is(false));
        assertThat(getStaticComponent().isAllowCustomValue(), is(false));
    }

    @Test
    void testClearButton_NullNotAllowed() {
        assertThat(getDefaultPmo().getValueAvailableValues(), not(contains(nullValue())));

        ComboBox<TestEnum> comboBox = getDynamicComponent();

        assertThat(getAllowedValues(comboBox), contains(TestEnum.ONE,
                                                        TestEnum.TWO,
                                                        TestEnum.THREE));
        assertThat(comboBox.isClearButtonVisible(), is(false));
    }

    @Test
    void testClearButton_NullAllowed() {
        List<TestEnum> availableValues = new ArrayList<>(getDefaultPmo().getValueAvailableValues());
        availableValues.add(null);
        getDefaultPmo().setValueAvailableValues(availableValues);
        modelChanged();

        ComboBox<TestEnum> comboBox = getDynamicComponent();

        assertThat(getAllowedValues(comboBox), contains(TestEnum.ONE,
                                                        TestEnum.TWO,
                                                        TestEnum.THREE));
        assertThat(comboBox.isClearButtonVisible(), is(true));
    }

    @Test
    void testStaticAvailableValues() {
        ComboBox<TestEnum> staticComboBox = getStaticComponent();
        assertThat(getAllowedValues(staticComboBox), contains(TestEnum.ONE, TestEnum.TWO,
                                                              TestEnum.THREE));
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
    void testCaptionProvider() {
        assertThat(getDynamicComponent().getItemLabelGenerator().apply(null), is(""));
        assertThat(getDynamicComponent().getItemLabelGenerator().apply(TestEnum.ONE), is("Oans"));
        assertThat(getStaticComponent().getItemLabelGenerator().apply(TestEnum.ONE),
                   is("ONE " + UiFramework.getLocale().toLanguageTag()));
    }

    @Test
    void testCaptionProvider_NoDefaultConstructor() {
        NoDefaultConstructorCaptionProviderPmo pmo = new NoDefaultConstructorCaptionProviderPmo();

        BindingContext bindingContext = new BindingContext();
        Function<Object, NoLabelComponentWrapper> wrapperCreator = c -> new NoLabelComponentWrapper((Component)c);

        assertThrows(LinkkiBindingException.class, () -> UiCreator.createUiElements(pmo, bindingContext,
                                                                                    wrapperCreator)
                .count());
    }

    @Test
    void testValue() {
        ComboBox<TestEnum> comboBox = getDynamicComponent();
        comboBox.setValue(TestEnum.THREE);
        assertThat(comboBox.getValue(), is(TestEnum.THREE));

        getDefaultModelObject().setValue(TestEnum.TWO);
        modelChanged();
        assertThat(comboBox.getValue(), is(TestEnum.TWO));

        TestUiUtil.setUserOriginatedValue(comboBox, TestEnum.ONE);
        assertThat(getDefaultModelObject().getValue(), is(TestEnum.ONE));
    }

    @Test
    void testValueRemainsWhenChangingAvailableValues() {
        getDefaultModelObject().setValue(TestEnum.THREE);
        assertThat(getDynamicComponent().getValue(), is(TestEnum.THREE));

        getDefaultPmo().setValueAvailableValues(List.of(TestEnum.THREE));
        modelChanged();
        assertThat(getDynamicComponent().getValue(), is(TestEnum.THREE));
    }

    @Test
    void testEmptyValuesAllowsNull() {
        ComboBox<TestEnum> comboBox = getDynamicComponent();

        getDefaultPmo().setValueAvailableValues(Collections.emptyList());
        modelChanged();
        assertThat(getAllowedValues(comboBox), is(IsEmptyCollection.empty()));

        getDefaultPmo().setValueAvailableValues(List.of(TestEnum.ONE));
        modelChanged();
        assertThat(getAllowedValues(comboBox), contains(TestEnum.ONE));
    }

    @Test
    void testNullInputIfRequired() {
        ComboBox<TestEnum> comboBox = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        List<TestEnum> availableValues = new ArrayList<>(getDefaultPmo().getValueAvailableValues());
        availableValues.add(null);
        getDefaultPmo().setValueAvailableValues(availableValues);
        modelChanged();
        assertThat(comboBox.isRequiredIndicatorVisible(), is(true));

        TestUiUtil.setUserOriginatedValue(comboBox, TestEnum.ONE);
        assertThat(getDefaultModelObject().getValue(), is(TestEnum.ONE));

        TestUiUtil.setUserOriginatedValue(comboBox, null);
        assertThat(getDefaultModelObject().getValue(), is(nullValue()));
    }

    @Test
    void testNullInput_AvailableValuesDoesNotContainNull() {
        assertThat(getDefaultPmo().getValueAvailableValues(), not(contains(nullValue())));
        TestUiUtil.setUserOriginatedValue(getDynamicComponent(), TestEnum.ONE);
        assertThat(getDefaultModelObject().getValue(), is(TestEnum.ONE));

        TestUiUtil.setUserOriginatedValue(getDynamicComponent(), null);

        assertThat(getDefaultModelObject().getValue(), is(TestEnum.ONE));
    }

    @Disabled("TODO reenable in next subtask")
    @Test
    void testInitReadOnlyField() {
        ComboBox<TestEnum> comboBox = getStaticComponent();
        comboBox.setReadOnly(false);
        ComponentUtil.setData(comboBox, Binding.class, null);
        getBindingContext().removeBindingsForPmo(getDefaultPmo());
        bind(getDefaultPmo(), "staticValue", comboBox);
        assertThat(comboBox.isReadOnly(), is(true));

        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(comboBox.isRequiredIndicatorVisible(), is(true));
        assertThat(comboBox.isReadOnly(), is(true));

        getBindingContext().removeBindingsForComponent(comboBox);
        assertThat(comboBox.isReadOnly(), is(true));

        ComponentUtil.setData(comboBox, Binding.class, null);

        bind(getDefaultPmo(), "staticValue", comboBox);
        assertThat(comboBox.isReadOnly(), is(true));
    }

    @Test
    void testDerivedLabel() {
        assertThat(TestUiUtil.getLabelOfComponentAt(getDefaultSection(), 2), is("Foo"));
    }

    @Test
    void testAlignLeft() {
        ComboBox<TestEnum> comboBox = getComponentById("leftAlign");

        assertThat(comboBox.getThemeNames()).contains(ComboBoxVariant.LUMO_ALIGN_LEFT.getVariantName());
    }

    @Test
    void testAlignCenter() {
        ComboBox<TestEnum> comboBox = getComponentById("centerAlign");

        assertThat(comboBox.getThemeNames()).contains(ComboBoxVariant.LUMO_ALIGN_CENTER.getVariantName());
    }

    @Test
    void testAlignRight() {
        ComboBox<TestEnum> comboBox = getComponentById("rightAlign");

        assertThat(comboBox.getThemeNames()).contains(ComboBoxVariant.LUMO_ALIGN_RIGHT.getVariantName());
    }

    @Override
    protected ComboBoxTestModelObject getDefaultModelObject() {
        return (ComboBoxTestModelObject)super.getDefaultModelObject();
    }

    private static List<TestEnum> getAllowedValues(ComboBox<TestEnum> comboBox) {
        return comboBox.getDataProvider().fetch(new Query<>())
                .collect(Collectors.toList());
    }

    @UISection
    protected static class ComboBoxTestPmo extends AnnotationTestPmo {

        private List<TestEnum> availableValues = new ArrayList<>();

        public ComboBoxTestPmo(Object modelObject) {
            super(modelObject);
            availableValues.add(TestEnum.ONE);
            availableValues.add(TestEnum.TWO);
            availableValues.add(TestEnum.THREE);
        }

        @Override
        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @UIComboBox(position = 1, label = "", enabled = EnabledType.DYNAMIC, required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, content = AvailableValuesType.DYNAMIC, itemCaptionProvider = ToStringCaptionProvider.class)
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
        @UIComboBox(position = 2, label = TEST_LABEL, enabled = EnabledType.DISABLED, required = RequiredType.REQUIRED, visible = VisibleType.INVISIBLE, content = AvailableValuesType.ENUM_VALUES_EXCL_NULL)
        public void staticValue() {
            // model binding
        }

        @UIComboBox(position = 3)
        public TestEnum getFoo() {
            return TestEnum.THREE;
        }

        @UIComboBox(position = 10, textAlign = TextAlignment.LEFT)
        public TestEnum getLeftAlign() {
            return TestEnum.ONE;
        }

        @UIComboBox(position = 11, textAlign = TextAlignment.CENTER)
        public TestEnum getCenterAlign() {
            return TestEnum.ONE;
        }

        @UIComboBox(position = 12, textAlign = TextAlignment.RIGHT)
        public TestEnum getRightAlign() {
            return TestEnum.ONE;
        }

    }

    protected static class ComboBoxTestModelObject {
        @CheckForNull
        private TestEnum value = TestEnum.THREE;

        @CheckForNull
        public TestEnum getValue() {
            return value;
        }

        public void setValue(@CheckForNull TestEnum value) {
            this.value = value;

        }

        @CheckForNull
        public TestEnum getStaticValue() {
            return getValue();
        }

    }

    protected static class NoDefaultConstructorCaptionProviderPmo {

        @UIComboBox(position = 3, itemCaptionProvider = PrivateItemCaptionProvider.class)
        public TestEnum getFoo() {
            return TestEnum.THREE;
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
