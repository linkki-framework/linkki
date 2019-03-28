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

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.linkki.core.ui.components.ItemCaptionProvider.ToStringCaptionProvider;
import org.linkki.core.ui.components.LinkkiComboBox;
import org.linkki.core.ui.section.annotations.BindTooltip.TooltipType;
import org.linkki.core.ui.section.annotations.UIYesNoComboBoxIntegrationTest.ComboBoxTestPmo;

import com.vaadin.ui.ComboBox;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class UIYesNoComboBoxIntegrationTest extends FieldAnnotationIntegrationTest<LinkkiComboBox, ComboBoxTestPmo> {

    public UIYesNoComboBoxIntegrationTest() {
        super(ComboBoxTestModelObject::new, ComboBoxTestPmo::new);
    }

    @Test
    public void testNullSelection() {
        assertThat(getStaticComponent().isNullSelectionAllowed(), is(false));

        ComboBox comboBox = getDynamicComponent();
        assertThat(comboBox.isNullSelectionAllowed(), is(false));
        assertThat(getDynamicComponent().getItemIds(), contains(null, true, false));

        comboBox.setValue(null);
        assertThat(getDefaultModelObject().value, is(nullValue()));

        comboBox.setValue(false);

        getDefaultModelObject().value = true;
        modelChanged();
        assertThat(comboBox.getValue(), is(true));
    }

    @Test
    public void testStaticAvailableValues() {
        assertThat(getDynamicComponent().getItemIds(), contains(null, true, false));
        assertThat(getStaticComponent().getItemIds(), contains(true, false));
    }

    @Test
    public void testCaptionProvider() {
        LinkkiComboBox comboBox = getDynamicComponent();
        assertThat((ToStringCaptionProvider)comboBox.getItemCaptionProvider(), isA(ToStringCaptionProvider.class));
    }

    @Test
    public void testValue() {
        LinkkiComboBox comboBox = getDynamicComponent();

        assertThat(comboBox.getValue(), is(nullValue()));

        getDefaultModelObject().setValue(true);
        modelChanged();
        assertThat(comboBox.getValue(), is(true));

        comboBox.setValue(false);
        assertThat(getDefaultModelObject().getValue(), is(false));
    }

    @Test
    @Override
    public void testNullInputIfRequired() {
        LinkkiComboBox comboBox = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(comboBox.isRequired(), is(true));

        comboBox.setValue(true);
        assertThat(getDefaultModelObject().getValue(), is(true));

        comboBox.setValue(null);
        assertThat(getDefaultModelObject().getValue(), is(nullValue()));
    }

    @Override
    protected ComboBoxTestModelObject getDefaultModelObject() {
        return (ComboBoxTestModelObject)super.getDefaultModelObject();
    }

    @UISection
    protected static class ComboBoxTestPmo extends AnnotationTestPmo {

        public ComboBoxTestPmo(Object modelObject) {
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


    protected static class ComboBoxTestModelObject {
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
