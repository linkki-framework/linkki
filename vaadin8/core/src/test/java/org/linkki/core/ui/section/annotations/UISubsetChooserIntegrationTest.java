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
package org.linkki.core.ui.section.annotations;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.linkki.core.binding.TestEnum;
import org.linkki.core.defaults.uielement.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.uielement.aspects.types.EnabledType;
import org.linkki.core.defaults.uielement.aspects.types.RequiredType;
import org.linkki.core.defaults.uielement.aspects.types.TooltipType;
import org.linkki.core.defaults.uielement.aspects.types.VisibleType;
import org.linkki.core.ui.section.annotations.UISubsetChooserIntegrationTest.SubsetChooserBoxTestPmo;

import com.vaadin.ui.TwinColSelect;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class UISubsetChooserIntegrationTest
        extends ComponentAnnotationIntegrationTest<TwinColSelect<TestEnum>, SubsetChooserBoxTestPmo> {

    public UISubsetChooserIntegrationTest() {
        super(SubsetChooserModelObject::new, SubsetChooserBoxTestPmo::new);
    }

    @Test
    public void testLeftRightCaptions() {
        TwinColSelect<TestEnum> subsetChooser = getDynamicComponent();

        assertThat(subsetChooser.getLeftColumnCaption(), is("leftC"));
        assertThat(subsetChooser.getRightColumnCaption(), is("rightC"));
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
    public void testValue() {
        TwinColSelect<TestEnum> subsetChooser = getDynamicComponent();

        getDefaultModelObject().setValue(new LinkedHashSet<>(Arrays.asList(TestEnum.ONE, TestEnum.THREE)));
        modelChanged();
        assertThat((Collection<?>)subsetChooser.getValue(), contains(TestEnum.ONE, TestEnum.THREE));

        TestUiUtil.setUserOriginatedValue(subsetChooser, new LinkedHashSet<>(Arrays.asList(TestEnum.TWO)));
        assertThat(getDefaultModelObject().getValue(), contains(TestEnum.TWO));
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputIfRequired() {
        TwinColSelect<TestEnum> subsetChooser = getDynamicComponent();

        subsetChooser.setValue(null);
    }

    @Override
    protected SubsetChooserModelObject getDefaultModelObject() {
        return (SubsetChooserModelObject)super.getDefaultModelObject();
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
        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @UISubsetChooser(position = 1, label = "", enabled = EnabledType.DYNAMIC, required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, leftColumnCaption = "leftC", rightColumnCaption = "rightC")
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
        @UISubsetChooser(position = 2, label = TEST_LABEL, enabled = EnabledType.DISABLED, required = RequiredType.REQUIRED, visible = VisibleType.INVISIBLE)
        public void staticValue() {
            // model binding
        }

        public List<TestEnum> getStaticValueAvailableValues() {
            return Collections.unmodifiableList(availableValues);
        }

    }


    protected static class SubsetChooserModelObject {
        @CheckForNull
        private Set<TestEnum> value;

        @CheckForNull
        public Set<TestEnum> getValue() {
            return value;
        }

        public void setValue(Set<TestEnum> value) {
            this.value = value;

        }

        @CheckForNull
        public Set<TestEnum> getStaticValue() {
            return getValue();
        }

    }
}
