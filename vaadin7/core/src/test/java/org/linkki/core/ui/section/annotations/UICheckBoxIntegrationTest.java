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

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.linkki.core.defaults.ui.element.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.element.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.element.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.element.aspects.types.TooltipType;
import org.linkki.core.defaults.ui.element.aspects.types.VisibleType;
import org.linkki.core.ui.section.annotations.UICheckBoxIntegrationTest.TestCheckboxPmo;

import com.vaadin.ui.CheckBox;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class UICheckBoxIntegrationTest extends FieldAnnotationIntegrationTest<CheckBox, TestCheckboxPmo> {

    public UICheckBoxIntegrationTest() {
        super(TestModelObjectWithObjectBoolean::new, TestCheckboxPmo::new);
    }

    @Test
    public void testSetValueWithPrimitiveBooleanInModelObject() {
        TestModelObjectWithPrimitiveBoolean modelObject = new TestModelObjectWithPrimitiveBoolean();
        CheckBox checkBox = createFirstComponent(modelObject);

        assertThat(modelObject.getValue(), is(false));

        checkBox.setValue(Boolean.TRUE);
        assertThat(modelObject.getValue(), is(true));

        checkBox.setValue(Boolean.FALSE);
        assertThat(modelObject.getValue(), is(false));

        checkBox.setValue(true);
        assertThat(modelObject.getValue(), is(true));

        checkBox.setValue(false);
        assertThat(modelObject.getValue(), is(false));

        modelObject.setValue(true);
        // updateUi(); not needed at the moment
        assertThat(checkBox.getValue(), is(true));

        modelObject.setValue(false);
        // updateUi(); not needed at the moment
        assertThat(checkBox.getValue(), is(false));
    }

    @Test
    public void testSetValueWithObjectBooleanInModelObject() {
        TestModelObjectWithObjectBoolean modelObject = new TestModelObjectWithObjectBoolean();
        CheckBox checkBox = createFirstComponent(modelObject);

        assertThat(modelObject.getValue(), is(nullValue()));

        checkBox.setValue(Boolean.TRUE);
        assertThat(modelObject.getValue(), is(true));

        checkBox.setValue(Boolean.FALSE);
        assertThat(modelObject.getValue(), is(false));

        checkBox.setValue(true);
        assertThat(modelObject.getValue(), is(true));

        checkBox.setValue(false);
        assertThat(modelObject.getValue(), is(false));

        checkBox.setValue(null);
        assertThat(modelObject.getValue(), is(nullValue()));

        modelObject.setValue(Boolean.TRUE);
        // updateUi(); not needed at the moment
        assertThat(checkBox.getValue(), is(true));

        modelObject.setValue(Boolean.FALSE);
        // updateUi(); not needed at the moment
        assertThat(checkBox.getValue(), is(false));

        modelObject.setValue(null);
        // updateUi(); not needed at the moment
        assertThat(checkBox.getValue(), is(nullValue()));
    }

    @Test
    public void testCaptionBinding() {
        CheckBox checkBoxWithNoCaption = getDynamicComponent();
        assertThat(checkBoxWithNoCaption.getCaption(), is(emptyString()));

        CheckBox checkBox = getStaticComponent();
        assertThat(checkBox.getCaption(), is(AnnotationTestPmo.TEST_CAPTION));
    }

    @Test
    @Override
    public void testNullInputIfRequired() {
        CheckBox checkBox = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(checkBox.isRequired(), is(true));

        checkBox.setValue(false);
        assertThat(getDefaultModelObject().getValue(), is(false));

        checkBox.setValue(null);
        assertThat(getDefaultModelObject().getValue(), is(nullValue()));
    }

    @Override
    protected TestModelObjectWithObjectBoolean getDefaultModelObject() {
        return (TestModelObjectWithObjectBoolean)super.getDefaultModelObject();
    }

    @UISection
    protected static class TestCheckboxPmo extends AnnotationTestPmo {

        public TestCheckboxPmo(Object modelObject) {
            super(modelObject);
        }

        @Override
        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @UICheckBox(position = 1, caption = "", enabled = EnabledType.DYNAMIC, required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC)
        public void value() {
            // model binding
        }

        @Override
        @BindTooltip(TEST_TOOLTIP)
        @UICheckBox(position = 2, caption = TEST_CAPTION, label = TEST_LABEL, enabled = EnabledType.DISABLED, required = RequiredType.REQUIRED, visible = VisibleType.INVISIBLE)
        public void staticValue() {
            // model binding
        }
    }

    protected static class TestModelObjectWithPrimitiveBoolean {

        private boolean value = false;

        public boolean getStaticValue() {
            return value;
        }

        public boolean getValue() {
            return value;
        }

        public void setValue(boolean b) {
            this.value = b;
        }
    }

    protected static class TestModelObjectWithObjectBoolean extends TestModelObject<Boolean> {

        @CheckForNull
        private Boolean value = null;

        @Override

        @CheckForNull
        public Boolean getValue() {
            return value;
        }

        @Override
        public void setValue(@CheckForNull Boolean value) {
            this.value = value;
        }
    }
}
