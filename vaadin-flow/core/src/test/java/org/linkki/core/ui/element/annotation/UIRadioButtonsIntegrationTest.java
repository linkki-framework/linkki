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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.defaults.ui.aspects.types.AlignmentType;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.ui.element.annotation.UIRadioButtonsIntegrationTest.RadioButtonTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;

import edu.umd.cs.findbugs.annotations.CheckForNull;

class UIRadioButtonsIntegrationTest
        extends ComponentAnnotationIntegrationTest<RadioButtonGroup<String>, RadioButtonTestPmo> {

    UIRadioButtonsIntegrationTest() {
        super(TestModelObjectWithString::new, RadioButtonTestPmo::new);
    }

    @Test
    void testContent_Dynamic() {
        RadioButtonGroup<String> radioButtons = getDynamicComponent();
        List<?> items = radioButtons.getListDataView().getItems().collect(Collectors.toList());

        assertThat(items, contains("value1", "value2"));
    }

    @Test
    void testContent_Enum() {
        RadioButtonGroup<?> radioButtons = getComponentById("enumValue");
        List<?> items = radioButtons.getListDataView().getItems().collect(Collectors.toList());
        assertThat(items, contains(TestEnum.ENUM_VALUE1, TestEnum.ENUM_VALUE2));
    }

    @Test
    void testContent_DefaultSelection() {
        RadioButtonGroup<?> radioButtons = getDynamicComponent();

        assertThat(radioButtons.getOptionalValue(), is(Optional.empty()));
    }

    @Test
    void testAlignment_Horizontal() {
        RadioButtonGroup<String> radioButtons = getComponentById("valueHorizontal");

        assertThat(radioButtons.getThemeNames(), not(contains(RadioGroupVariant.LUMO_VERTICAL.getVariantName())));
    }

    @Test
    void testAlignment_Vertical() {
        RadioButtonGroup<String> radioButtons = getComponentById("valueVertical");

        assertThat(radioButtons.getThemeNames(), contains(RadioGroupVariant.LUMO_VERTICAL.getVariantName()));
    }

    @Test
    void testCaptionProvider() {
        RadioButtonGroup<TestEnum> radioButtons = TestUiUtil.getComponentById(getDefaultSection(), "enumValue");

        String caption = radioButtons.getItemRenderer().createComponent(TestEnum.ENUM_VALUE1).getElement().getText();

        assertThat(caption, is("Enum ENUM_VALUE1"));
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
    void testUpdate() {
        TestModelObjectWithString modelObject = (TestModelObjectWithString)getDefaultModelObject();
        RadioButtonGroup<String> radioButtons = getDynamicComponent();

        modelObject.setValue("value2");
        getBindingContext().modelChanged();

        assertThat(radioButtons.getOptionalValue().get(), is("value2"));
    }

    @Test
    void testSelect() {
        TestModelObjectWithString modelObject = (TestModelObjectWithString)getDefaultModelObject();
        RadioButtonGroup<String> radioButtons = getDynamicComponent();

        TestUiUtil.setUserOriginatedValue(radioButtons, "value2");

        assertThat(modelObject.getValue(), is("value2"));
    }

    @UISection
    protected static class RadioButtonTestPmo extends AnnotationTestPmo {

        public RadioButtonTestPmo(Object modelObject) {
            super(modelObject);
        }

        @Override
        @UIRadioButtons(position = 0,
                visible = VisibleType.DYNAMIC,
                enabled = EnabledType.DYNAMIC,
                content = AvailableValuesType.DYNAMIC)
        public void value() {
            // model binding
        }

        public List<String> getValueAvailableValues() {
            return Arrays.asList("value1", "value2");
        }

        @Override
        @UIRadioButtons(position = 10,
                visible = VisibleType.INVISIBLE,
                enabled = EnabledType.DISABLED,
                content = AvailableValuesType.NO_VALUES,
                label = AnnotationTestPmo.TEST_LABEL)
        public void staticValue() {
            // model binding
        }

        @UIRadioButtons(position = 20,
                buttonAlignment = AlignmentType.HORIZONTAL,
                content = AvailableValuesType.NO_VALUES)
        public void valueHorizontal() {
            // model binding
        }

        @UIRadioButtons(position = 30,
                buttonAlignment = AlignmentType.VERTICAL,
                content = AvailableValuesType.NO_VALUES)
        public void valueVertical() {
            // model binding
        }

        @UIRadioButtons(position = 40,
                content = AvailableValuesType.ENUM_VALUES_EXCL_NULL,
                itemCaptionProvider = TestEnumCaptionProvider.class)
        public void enumValue() {
            // model binding
        }
    }

    protected static class TestModelObjectWithString extends TestModelObject<String> {

        @CheckForNull
        private String value = null;

        @CheckForNull
        private TestEnum enumValue = TestEnum.ENUM_VALUE1;

        @CheckForNull
        @Override
        public String getValue() {
            return value;
        }

        @Override
        public void setValue(@CheckForNull String value) {
            this.value = value;
        }

        public String getValueHorizontal() {
            return getStaticValue();
        }

        public String getValueVertical() {
            return getStaticValue();
        }

        public TestEnum getEnumValue() {
            return enumValue;
        }

        public void setEnumValue(TestEnum enumValue) {
            this.enumValue = enumValue;
        }
    }

    protected static enum TestEnum {
        ENUM_VALUE1,
        ENUM_VALUE2;
    }

    public static class TestEnumCaptionProvider implements ItemCaptionProvider<TestEnum> {

        @Override
        public String getCaption(TestEnum value) {
            return "Enum " + value.toString();
        }

    }

    protected static class NoDefaultConstructorCaptionProviderPmo {

        @UIRadioButtons(position = 3, itemCaptionProvider = PrivateItemCaptionProvider.class)
        public TestEnum getFoo() {
            return TestEnum.ENUM_VALUE1;
        }

        private static class PrivateItemCaptionProvider implements ItemCaptionProvider<TestEnum> {

            private PrivateItemCaptionProvider() {
                // hide default constructor
            }

            @Override
            public String getCaption(TestEnum value) {
                return value.name();
            }
        }
    }
}
