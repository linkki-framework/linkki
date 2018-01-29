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
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.linkki.core.binding.TestEnum;
import org.linkki.core.ui.components.SubsetChooser;
import org.linkki.core.ui.section.annotations.UISubsetChooserIntegrationTest.SubsetChooserBoxTestPmo;

public class UISubsetChooserIntegrationTest
        extends FieldAnnotationIntegrationTest<SubsetChooser, SubsetChooserBoxTestPmo> {

    public UISubsetChooserIntegrationTest() {
        super(SubsetChooserModelObject::new, SubsetChooserBoxTestPmo::new);
    }

    @Test
    public void testLeftRightCaptions() {
        SubsetChooser subsetChooser = getDynamicComponent();

        assertThat(subsetChooser.getLeftColumnCaption(), is("leftC"));
        assertThat(subsetChooser.getRightColumnCaption(), is("rightC"));
    }

    @Test
    public void testDynamicAvailableValues() {
        assertThat(getDynamicComponent().getItemIds(), contains(TestEnum.ONE, TestEnum.TWO, TestEnum.THREE));

        List<TestEnum> availableValues = new ArrayList<>(getDefaultPmo().getValueAvailableValues());
        availableValues.remove(TestEnum.ONE);
        getDefaultPmo().setValueAvailableValues(availableValues);
        updateUi();
        assertThat(getDynamicComponent().getItemIds(), contains(TestEnum.TWO, TestEnum.THREE));
    }

    @UISection
    protected static class SubsetChooserBoxTestPmo extends AnnotationTestPmo {

        private List<TestEnum> availableValues;

        public SubsetChooserBoxTestPmo(Object modelObject) {
            super(modelObject);
            availableValues = new ArrayList<>();
            availableValues.add(TestEnum.ONE);
            availableValues.add(TestEnum.TWO);
            availableValues.add(TestEnum.THREE);
        }

        @Override
        @UIToolTip(toolTipType = ToolTipType.DYNAMIC)
        @UISubsetChooser(position = 1, noLabel = true, enabled = EnabledType.DYNAMIC, required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, leftColumnCaption = "leftC", rightColumnCaption = "rightC")
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
        @UISubsetChooser(position = 2, label = TEST_LABEL, enabled = EnabledType.DISABLED, required = RequiredType.REQUIRED, visible = VisibleType.INVISIBLE)
        public void staticValue() {
            // model binding
        }

        public List<TestEnum> getStaticValueAvailableValues() {
            return Collections.unmodifiableList(availableValues);
        }

    }

    @SuppressWarnings("null")
    protected static class SubsetChooserModelObject {

        private List<TestEnum> value;

        public List<TestEnum> getValue() {
            return value;
        }

        public void setValue(List<TestEnum> value) {
            this.value = value;

        }

        public List<TestEnum> getStaticValue() {
            return getValue();
        }

    }
}
