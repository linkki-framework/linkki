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
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.aspects.annotation.BindReadOnly;
import org.linkki.core.ui.aspects.annotation.BindReadOnly.ReadOnlyType;
import org.linkki.core.ui.element.annotation.UICheckBoxIntegrationTest.TestCheckBoxPmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.test.KaribuUtils;

import com.vaadin.flow.component.checkbox.Checkbox;

import edu.umd.cs.findbugs.annotations.CheckForNull;

class UICheckBoxIntegrationTest extends FieldAnnotationIntegrationTest<Checkbox, TestCheckBoxPmo> {

    UICheckBoxIntegrationTest() {
        super(TestModelObjectWithObjectBoolean::new, TestCheckBoxPmo::new);
    }

    @Test
    void testSetValueWithPrimitiveBooleanInModelObject() {
        TestModelObjectWithPrimitiveBoolean modelObject = new TestModelObjectWithPrimitiveBoolean();
        Checkbox checkBox = createFirstComponent(modelObject);

        assertThat(modelObject.getValue(), is(false));

        KaribuUtils.Fields.setValue(checkBox, Boolean.TRUE);
        assertThat(modelObject.getValue(), is(true));

        KaribuUtils.Fields.setValue(checkBox, Boolean.FALSE);
        assertThat(modelObject.getValue(), is(false));

        KaribuUtils.Fields.setValue(checkBox, true);
        assertThat(modelObject.getValue(), is(true));

        KaribuUtils.Fields.setValue(checkBox, false);
        assertThat(modelObject.getValue(), is(false));

        modelObject.setValue(true);
        getBindingContext().modelChanged();
        assertThat(checkBox.getValue(), is(true));

        modelObject.setValue(false);
        getBindingContext().modelChanged();
        assertThat(checkBox.getValue(), is(false));
    }

    @Test
    void testSetValueWithObjectBooleanInModelObject() {
        TestModelObjectWithObjectBoolean modelObject = new TestModelObjectWithObjectBoolean();
        Checkbox checkBox = createFirstComponent(modelObject);

        assertThat(modelObject.getValue(), is(nullValue()));

        KaribuUtils.Fields.setValue(checkBox, Boolean.TRUE);
        assertThat(modelObject.getValue(), is(true));

        KaribuUtils.Fields.setValue(checkBox, Boolean.FALSE);
        assertThat(modelObject.getValue(), is(false));

        KaribuUtils.Fields.setValue(checkBox, true);
        assertThat(modelObject.getValue(), is(true));

        KaribuUtils.Fields.setValue(checkBox, false);
        assertThat(modelObject.getValue(), is(false));

        modelObject.setValue(Boolean.TRUE);
        getBindingContext().modelChanged();
        assertThat(checkBox.getValue(), is(true));

        modelObject.setValue(Boolean.FALSE);
        getBindingContext().modelChanged();
        assertThat(checkBox.getValue(), is(false));

        modelObject.setValue(null);
        getBindingContext().modelChanged();
        assertThat(checkBox.getValue(), is(false));

        KaribuUtils.Fields.setValue(checkBox, true);
        modelObject.setValue(null);
        getBindingContext().modelChanged();
        assertThat(checkBox.getValue(), is(false));
    }

    @Test
    void testCaptionBinding() {
        Checkbox checkBoxWithNoCaption = getDynamicComponent();
        assertThat(checkBoxWithNoCaption.getLabel(), is(emptyString()));

        Checkbox checkBox = getStaticComponent();
        assertThat(checkBox.getLabel(), is(AnnotationTestPmo.TEST_CAPTION));

        Checkbox checkBoxWithDerivedCaption = getComponentById("foo");
        assertThat(checkBoxWithDerivedCaption.getLabel(), is("Foo"));
    }

    @Test
    @Override
    void testNullInputIfRequired() {
        Checkbox checkBox = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(checkBox.isRequiredIndicatorVisible(), is(true));

        KaribuUtils.Fields.setValue(checkBox, true);
        assertThat(getDefaultModelObject().getValue(), is(true));

        KaribuUtils.Fields.setValue(checkBox, false);
        assertThat(getDefaultModelObject().getValue(), is(false));
    }

    @Override
    protected TestModelObjectWithObjectBoolean getDefaultModelObject() {
        return (TestModelObjectWithObjectBoolean)super.getDefaultModelObject();
    }

    @UISection
    protected static class TestCheckBoxPmo extends AnnotationTestPmo {

        private boolean readOnly = false;

        public TestCheckBoxPmo(Object modelObject) {
            super(modelObject);
        }

        @Override
        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @UICheckBox(position = 1,
                caption = "",
                enabled = EnabledType.DYNAMIC,
                required = RequiredType.DYNAMIC,
                visible = VisibleType.DYNAMIC)
        public void value() {
            // model binding
        }

        @Override
        @BindTooltip(TEST_TOOLTIP)
        @UICheckBox(position = 2,
                caption = TEST_CAPTION,
                label = TEST_LABEL,
                enabled = EnabledType.DISABLED,
                required = RequiredType.REQUIRED,
                visible = VisibleType.INVISIBLE)
        public void staticValue() {
            // model binding
        }

        @UICheckBox(position = 3)
        public boolean getFoo() {
            return true;
        }

        @BindReadOnly(ReadOnlyType.DYNAMIC)
        @UICheckBox(position = 4)
        public boolean getDynamicReadonlyCheckBox() {
            return true;
        }

        public void setDynamicReadonlyCheckBox(@SuppressWarnings("unused") boolean booleanValue) {
            // nop
        }

        public boolean isDynamicReadonlyCheckBoxReadOnly() {
            return readOnly;
        }

        public void setDynamicReadonlyCheckBoxReadOnly(boolean readOnly) {
            this.readOnly = readOnly;
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
