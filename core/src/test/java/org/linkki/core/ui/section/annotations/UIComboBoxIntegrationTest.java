/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.ui.section.annotations;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.linkki.core.binding.TestEnum;
import org.linkki.core.ui.components.ItemCaptionProvider.ToStringCaptionProvider;
import org.linkki.core.ui.components.LinkkiComboBox;
import org.linkki.core.ui.section.annotations.UIComboBoxIntegrationTest.ComboBoxTestPmo;

import com.vaadin.ui.ComboBox;

public class UIComboBoxIntegrationTest extends FieldAnnotationIntegrationTest<LinkkiComboBox, ComboBoxTestPmo> {

    public UIComboBoxIntegrationTest() {
        super(ComboBoxTestModelObject::new, ComboBoxTestPmo::new);
    }

    @Test
    public void testNullSelection() {
        assertThat(getStaticComponent().isNullSelectionAllowed(), is(false));

        List<TestEnum> availableValues = new ArrayList<>(getDefaultPmo().getValueAvailableValues());

        ComboBox comboBox = getDynamicComponent();
        assertThat(availableValues.contains(null), is(false));
        assertThat(comboBox.isNullSelectionAllowed(), is(false));

        availableValues.add(null);
        assertThat(availableValues.contains(null), is(true));
        getDefaultPmo().setValueAvailableValues(availableValues);
        modelChanged();
        assertThat(comboBox.getItemIds(),
                   contains(TestEnum.ONE, TestEnum.TWO, TestEnum.THREE, comboBox.getNullSelectionItemId()));
    }

    @Test
    public void testStaticAvailableValues() {
        ComboBox staticComboBox = getStaticComponent();
        assertThat(staticComboBox.getItemIds(), contains(TestEnum.ONE, TestEnum.TWO, TestEnum.THREE));
    }

    @Test
    public void testDynamicAvailableValues() {
        assertThat(getDynamicComponent().getItemIds(), contains(TestEnum.ONE, TestEnum.TWO, TestEnum.THREE));

        List<TestEnum> availableValues = new ArrayList<>(getDefaultPmo().getValueAvailableValues());
        availableValues.remove(TestEnum.ONE);
        getDefaultPmo().setValueAvailableValues(availableValues);
        modelChanged();
        assertThat(getDynamicComponent().getItemIds(), contains(TestEnum.TWO, TestEnum.THREE));
    }

    @Test
    public void testCaptionProvider() {
        LinkkiComboBox comboBox = getDynamicComponent();
        assertThat((ToStringCaptionProvider)comboBox.getItemCaptionProvider(), isA(ToStringCaptionProvider.class));
    }

    @Test
    public void testValue() {
        LinkkiComboBox comboBox = getDynamicComponent();
        assertThat(comboBox.getValue(), is(TestEnum.THREE));

        getDefaultModelObject().setValue(TestEnum.TWO);
        modelChanged();
        assertThat(comboBox.getValue(), is(TestEnum.TWO));

        comboBox.setValue(TestEnum.ONE);
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
    @Override
    public void testNullInputIfRequired() {
        LinkkiComboBox comboBox = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(comboBox.isRequired(), is(true));

        comboBox.setValue(TestEnum.ONE);
        assertThat(getDefaultModelObject().getValue(), is(TestEnum.ONE));

        comboBox.setValue(null);
        assertThat(getDefaultModelObject().getValue(), is(nullValue()));
    }

    @Override
    protected ComboBoxTestModelObject getDefaultModelObject() {
        return (ComboBoxTestModelObject)super.getDefaultModelObject();
    }

    @UISection
    protected static class ComboBoxTestPmo extends AnnotationTestPmo {

        private List<TestEnum> availableValues;

        public ComboBoxTestPmo(Object modelObject) {
            super(modelObject);
            availableValues = new ArrayList<>();
            availableValues.add(TestEnum.ONE);
            availableValues.add(TestEnum.TWO);
            availableValues.add(TestEnum.THREE);
        }

        @Override
        @UIToolTip(toolTipType = ToolTipType.DYNAMIC)
        @UIComboBox(position = 1, noLabel = true, enabled = EnabledType.DYNAMIC, required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, content = AvailableValuesType.DYNAMIC, itemCaptionProvider = ToStringCaptionProvider.class)
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
        @UIToolTip(text = TEST_TOOLTIP)
        @UIComboBox(position = 2, label = TEST_LABEL, enabled = EnabledType.DISABLED, required = RequiredType.REQUIRED, visible = VisibleType.INVISIBLE, content = AvailableValuesType.ENUM_VALUES_EXCL_NULL)
        public void staticValue() {
            // model binding
        }
    }

    @SuppressWarnings("null")
    protected static class ComboBoxTestModelObject {

        private TestEnum value = TestEnum.THREE;

        public TestEnum getValue() {
            return value;
        }

        public void setValue(TestEnum value) {
            this.value = value;

        }

        public TestEnum getStaticValue() {
            return getValue();
        }

    }
}
