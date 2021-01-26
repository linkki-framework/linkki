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
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.bind.TestEnum;
import org.linkki.core.ui.element.annotation.UICustomFieldIntegrationTest.ComponentAnnotationTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.ui.ComboBox;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class UICustomFieldIntegrationTest
        extends ComponentAnnotationIntegrationTest<ComboBox<TestEnum>, ComponentAnnotationTestPmo> {

    public UICustomFieldIntegrationTest() {
        super(ComponentAnnotationTestModelObject::new, ComponentAnnotationTestPmo::new);
    }

    @Test
    public void testDynamicAvailableValues() {
        assertThat(TestUiUtil.getData(getDynamicComponent()), contains(TestEnum.ONE, TestEnum.TWO, TestEnum.THREE));

        List<TestEnum> availableValues = new ArrayList<>(getDefaultPmo().getDynamicAvailableValues());
        availableValues.remove(TestEnum.ONE);
        getDefaultPmo().setDynamicAvailableValues(availableValues);
        modelChanged();
        assertThat(TestUiUtil.getData(getDynamicComponent()), contains(TestEnum.TWO, TestEnum.THREE));
    }


    @Test
    public void testNullInputIfRequired() {
        ComboBox<TestEnum> component = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(component.isRequiredIndicatorVisible(), is(true));

        TestUiUtil.setUserOriginatedValue(component, TestEnum.ONE);
        assertThat(getDefaultModelObject().getValue(), is(TestEnum.ONE));

        TestUiUtil.setUserOriginatedValue(component, null);
        assertThat(getDefaultModelObject().getValue(), is(nullValue()));
    }

    @Test
    public void testDerivedLabel() {
        assertThat(TestUiUtil.getLabelOfComponentAt(getDefaultSection(), 2), is("Foo"));
    }

    @Override
    protected ComponentAnnotationTestModelObject getDefaultModelObject() {
        return (ComponentAnnotationTestModelObject)super.getDefaultModelObject();
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
        @UICustomField(position = 1, //
                label = "", //
                enabled = EnabledType.DYNAMIC, //
                required = RequiredType.DYNAMIC, //
                visible = VisibleType.DYNAMIC, //
                content = AvailableValuesType.DYNAMIC, //
                uiControl = ComboBox.class, //
                modelAttribute = TestModelObject.PROPERTY_VALUE)
        public void dynamic() {
            // model binding
        }

        public List<TestEnum> getDynamicAvailableValues() {
            return Collections.unmodifiableList(availableValues);
        }

        public void setDynamicAvailableValues(List<TestEnum> values) {
            this.availableValues = values;
        }

        @Override
        @BindTooltip(TEST_TOOLTIP)
        @UICustomField(position = 2, label = TEST_LABEL, enabled = EnabledType.DISABLED, required = RequiredType.REQUIRED, visible = VisibleType.INVISIBLE, content = AvailableValuesType.ENUM_VALUES_EXCL_NULL, uiControl = ComboBox.class)
        public void staticValue() {
            // model binding
        }

        @UICustomField(position = 3, uiControl = ComboBox.class)
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
}
