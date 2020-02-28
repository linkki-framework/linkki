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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.ToStringCaptionProvider;
import org.linkki.core.ui.bind.TestEnum;
import org.linkki.core.ui.element.annotation.UIComboBoxIntegrationTest.ComboBoxTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.uiframework.UiFramework;

import com.vaadin.ui.ComboBox;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class UIComboBoxIntegrationTest extends ComponentAnnotationIntegrationTest<ComboBox<TestEnum>, ComboBoxTestPmo> {

    public UIComboBoxIntegrationTest() {
        super(ComboBoxTestModelObject::new, ComboBoxTestPmo::new);
    }

    @Test
    public void testNullSelection() {
        assertThat(getStaticComponent().isEmptySelectionAllowed(), is(false));

        List<TestEnum> availableValues = new ArrayList<>(getDefaultPmo().getValueAvailableValues());

        ComboBox<TestEnum> comboBox = getDynamicComponent();
        assertThat(availableValues.contains(null), is(false));
        assertThat(comboBox.isEmptySelectionAllowed(), is(false));

        availableValues.add(null);
        assertThat(availableValues.contains(null), is(true));
        getDefaultPmo().setValueAvailableValues(availableValues);
        modelChanged();
        assertThat(TestUiUtil.getData(comboBox), contains(TestEnum.ONE, TestEnum.TWO, TestEnum.THREE));
        assertThat(comboBox.isEmptySelectionAllowed(), is(true));
    }

    @Test
    public void testStaticAvailableValues() {
        ComboBox<TestEnum> staticComboBox = getStaticComponent();
        assertThat(TestUiUtil.getData(staticComboBox), contains(TestEnum.ONE, TestEnum.TWO, TestEnum.THREE));
    }

    @Test
    public void testDynamicAvailableValues() {
        assertThat(TestUiUtil.getData(getDynamicComponent()), contains(TestEnum.ONE, TestEnum.TWO, TestEnum.THREE));

        List<TestEnum> availableValues = new ArrayList<>(getDefaultPmo().getValueAvailableValues());
        availableValues.remove(TestEnum.ONE);
        getDefaultPmo().setValueAvailableValues(availableValues);
        modelChanged();
        assertThat(TestUiUtil.getData(getDynamicComponent()), contains(TestEnum.TWO, TestEnum.THREE));
    }

    @Test
    public void testCaptionProvider() {
        assertThat(getDynamicComponent().getItemCaptionGenerator().apply(TestEnum.ONE), is("Oans"));
        assertThat(getStaticComponent().getItemCaptionGenerator().apply(TestEnum.ONE),
                   is("ONE " + UiFramework.getLocale().toLanguageTag()));
    }

    @Test
    public void testValue() {
        ComboBox<TestEnum> comboBox = getDynamicComponent();
        assertThat(comboBox.getValue(), is(TestEnum.THREE));

        getDefaultModelObject().setValue(TestEnum.TWO);
        modelChanged();
        assertThat(comboBox.getValue(), is(TestEnum.TWO));

        TestUiUtil.setUserOriginatedValue(comboBox, TestEnum.ONE);
        assertThat(getDefaultModelObject().getValue(), is(TestEnum.ONE));
    }

    @Test
    public void testValueRemainsWhenChangingAvailableValues() {
        getDefaultModelObject().setValue(TestEnum.THREE);
        assertThat(getDynamicComponent().getValue(), is(TestEnum.THREE));

        getDefaultPmo().setValueAvailableValues(Arrays.asList(TestEnum.THREE));
        modelChanged();
        assertThat(getDynamicComponent().getValue(), is(TestEnum.THREE));
    }


    @Test
    public void testNullInputIfRequired() {
        ComboBox<TestEnum> comboBox = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(comboBox.isRequiredIndicatorVisible(), is(true));

        TestUiUtil.setUserOriginatedValue(comboBox, TestEnum.ONE);
        assertThat(getDefaultModelObject().getValue(), is(TestEnum.ONE));

        TestUiUtil.setUserOriginatedValue(comboBox, null);
        assertThat(getDefaultModelObject().getValue(), is(nullValue()));
    }

    @Test
    public void testInitReadOnlyField() {
        ComboBox<TestEnum> comboBox = getStaticComponent();
        comboBox.setReadOnly(false);
        comboBox.setData(null);
        getBindingContext().removeBindingsForPmo(getDefaultPmo());
        bind(getDefaultPmo(), "staticValue", comboBox);
        assertThat(comboBox.isReadOnly(), is(true));

        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(comboBox.isRequiredIndicatorVisible(), is(true));
        assertThat(comboBox.isReadOnly(), is(true));

        getBindingContext().removeBindingsForComponent(comboBox);
        assertThat(comboBox.isReadOnly(), is(true));

        comboBox.setData(null);

        bind(getDefaultPmo(), "staticValue", comboBox);
        assertThat(comboBox.isReadOnly(), is(true));
    }

    @Test
    public void testDerivedLabel() {
        assertThat(TestUiUtil.getLabelOfComponentAt(getDefaultSection(), 2), is("Foo"));
    }

    @Override
    protected ComboBoxTestModelObject getDefaultModelObject() {
        return (ComboBoxTestModelObject)super.getDefaultModelObject();
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
}
