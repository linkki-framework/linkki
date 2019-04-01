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

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.ToStringCaptionProvider;
import org.linkki.core.defaults.ui.element.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.element.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.element.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.element.aspects.types.TooltipType;
import org.linkki.core.defaults.ui.element.aspects.types.VisibleType;
import org.linkki.core.ui.element.annotation.UIYesNoComboBoxIntegrationTest.YesNoComboBoxTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.ui.ComboBox;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class UIYesNoComboBoxIntegrationTest
        extends ComponentAnnotationIntegrationTest<ComboBox<Boolean>, YesNoComboBoxTestPmo> {

    public UIYesNoComboBoxIntegrationTest() {
        super(YesNoComboBoxTestModelObject::new, YesNoComboBoxTestPmo::new);
    }


    @Test
    public void testNullSelection() {
        assertThat(getStaticComponent().isEmptySelectionAllowed(), is(false));

        ComboBox<Boolean> comboBox = getDynamicComponent();
        assertThat(comboBox.isEmptySelectionAllowed(), is(true));
        assertThat(TestUiUtil.getData(getDynamicComponent()), contains(true, false));

        TestUiUtil.setUserOriginatedValue(comboBox, null);
        assertThat(getDefaultModelObject().value, is(nullValue()));

        TestUiUtil.setUserOriginatedValue(comboBox, false);

        getDefaultModelObject().value = true;
        modelChanged();
        assertThat(comboBox.getValue(), is(true));
    }

    @Test
    public void testStaticAvailableValues() {
        assertThat(TestUiUtil.getData(getDynamicComponent()), contains(true, false));
        assertThat(getDynamicComponent().isEmptySelectionAllowed(), is(true));
        assertThat(TestUiUtil.getData(getStaticComponent()), contains(true, false));
        assertThat(getStaticComponent().isEmptySelectionAllowed(), is(false));
    }

    @Test
    public void testCaptionProvider() {
        ComboBox<Boolean> comboBox = getDynamicComponent();
        assertThat(comboBox.getItemCaptionGenerator().apply(true), is("true"));
    }

    @Test
    public void testValue() {
        ComboBox<Boolean> comboBox = getDynamicComponent();

        assertThat(comboBox.getValue(), is(nullValue()));

        getDefaultModelObject().setValue(true);
        modelChanged();
        assertThat(comboBox.getValue(), is(true));

        TestUiUtil.setUserOriginatedValue(comboBox, false);
        assertThat(getDefaultModelObject().getValue(), is(false));
    }


    @Test
    public void testNullInputIfRequired() {
        ComboBox<Boolean> comboBox = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(comboBox.isRequiredIndicatorVisible(), is(true));

        TestUiUtil.setUserOriginatedValue(comboBox, true);
        assertThat(getDefaultModelObject().getValue(), is(true));

        TestUiUtil.setUserOriginatedValue(comboBox, null);
        assertThat(getDefaultModelObject().getValue(), is(nullValue()));
    }

    @Override
    protected YesNoComboBoxTestModelObject getDefaultModelObject() {
        return (YesNoComboBoxTestModelObject)super.getDefaultModelObject();
    }

    @UISection
    protected static class YesNoComboBoxTestPmo extends AnnotationTestPmo {

        public YesNoComboBoxTestPmo(Object modelObject) {
            super(modelObject);
        }

        @Override
        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @UIYesNoComboBox(position = 1, label = "", enabled = EnabledType.DYNAMIC, required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, itemCaptionProvider = ToStringCaptionProvider.class)
        public void value() {
            // model binding
        }

        @Override
        @BindTooltip(TEST_TOOLTIP)
        @UIYesNoComboBox(position = 2, label = TEST_LABEL, enabled = EnabledType.DISABLED, required = RequiredType.REQUIRED, visible = VisibleType.INVISIBLE)
        public void staticValue() {
            // model binding
        }
    }


    protected static class YesNoComboBoxTestModelObject {
        @CheckForNull
        private Boolean value;

        @CheckForNull
        public Boolean getValue() {
            return value;
        }

        public void setValue(Boolean value) {
            this.value = value;

        }

        public boolean getStaticValue() {
            Boolean b = getValue();
            return b == null ? false : b;
        }

    }
}
