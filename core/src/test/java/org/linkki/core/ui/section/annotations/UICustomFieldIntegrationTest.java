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

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.linkki.core.binding.TestEnum;
import org.linkki.core.ui.components.LinkkiComboBox;
import org.linkki.core.ui.section.annotations.UICustomFieldIntegrationTest.ComboBoxTestPmo;

public class UICustomFieldIntegrationTest extends FieldAnnotationIntegrationTest<LinkkiComboBox, ComboBoxTestPmo> {

    public UICustomFieldIntegrationTest() {
        super(ComboBoxTestModelObject::new, ComboBoxTestPmo::new);
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
    @Override
    public void testNullInputIfRequired() {
        LinkkiComboBox component = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(component.isRequired(), is(true));

        component.setValue(TestEnum.ONE);
        assertThat(getDefaultModelObject().getValue(), is(TestEnum.ONE));

        component.setValue(null);
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
        @BindTooltip(tooltipType = BindTooltipType.DYNAMIC)
        @UICustomField(position = 1, noLabel = true, enabled = EnabledType.DYNAMIC, required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, content = AvailableValuesType.DYNAMIC, uiControl = LinkkiComboBox.class)
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
        @BindTooltip(text = TEST_TOOLTIP)
        @UICustomField(position = 2, label = TEST_LABEL, enabled = EnabledType.DISABLED, required = RequiredType.REQUIRED, visible = VisibleType.INVISIBLE, content = AvailableValuesType.ENUM_VALUES_EXCL_NULL, uiControl = LinkkiComboBox.class)
        public void staticValue() {
            // model binding
        }
    }

    @SuppressWarnings("null")
    protected static class ComboBoxTestModelObject {

        private TestEnum value;

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
