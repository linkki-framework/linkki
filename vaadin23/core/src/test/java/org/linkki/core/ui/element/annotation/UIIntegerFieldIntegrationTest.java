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

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.element.annotation.UIIntegerFieldIntegrationTest.IntegerFieldTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.uiframework.UiFramework;

import com.vaadin.flow.component.textfield.TextField;

import edu.umd.cs.findbugs.annotations.CheckForNull;

class UIIntegerFieldIntegrationTest extends FieldAnnotationIntegrationTest<TextField, IntegerFieldTestPmo> {

    private static final String FANCY_FORMAT = "##,##,##.###";

    private static final int MAX_LENGTH = 8;

    private final NumberFormat formatter;

    UIIntegerFieldIntegrationTest() {
        super(TestModelObjectWithObjectInteger::new, IntegerFieldTestPmo::new);
        formatter = new DecimalFormat(FANCY_FORMAT, DecimalFormatSymbols.getInstance(UiFramework.getLocale()));
    }

    @Test
    void testSetValue_WithPrimitiveIntInModelObject() {
        TestModelObjectWithPrimitiveInteger modelObject = new TestModelObjectWithPrimitiveInteger();
        TextField textField = createFirstComponent(modelObject);

        assertThat(textField.getMaxLength(), is(MAX_LENGTH));
        assertThat(textField.getValue(), is(formatter.format(0)));

        TestUiUtil.setUserOriginatedValue(textField, formatter.format(1));
        assertThat(modelObject.getValue(), is(1));

        modelObject.setValue(2000);
        getBindingContext().modelChanged();
        assertThat(textField.getValue(), is(formatter.format(2000)));
    }

    @Test
    void testSetValue_WithPrimitiveIntegerInModelObject_RevertsForNull() {
        TextField textField = createFirstComponent(new TestModelObjectWithPrimitiveInteger());
        TestUiUtil.setUserOriginatedValue(textField, formatter.format(1));
        assertThat(textField.getValue(), is(formatter.format(1)));

        TestUiUtil.setUserOriginatedValue(textField, "");

        // field reverts to last valid value
        assertThat(textField.getValue(), is(formatter.format(1)));
    }


    @Test
    void testSetValue_WithObjectIntegerInModelObject() {
        TestModelObjectWithObjectInteger modelObject = new TestModelObjectWithObjectInteger();
        TextField textField = createFirstComponent(modelObject);

        assertThat(textField.getValue(), is(emptyString()));

        TestUiUtil.setUserOriginatedValue(textField, formatter.format(1));
        assertThat(modelObject.getValue(), is(1));

        modelObject.setValue(2);
        getBindingContext().modelChanged();
        assertThat(textField.getValue(), is(formatter.format(2)));

        TestUiUtil.setUserOriginatedValue(textField, textField.getEmptyValue());
        assertThat(getDefaultModelObject().getValue(), is(nullValue()));
    }

    @Test
    @Override
    void testNullInputIfRequired() {
        TextField textField = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(textField.isRequiredIndicatorVisible(), is(true));

        TestUiUtil.setUserOriginatedValue(textField, formatter.format(1));
        assertThat(getDefaultModelObject().getValue(), is(1));

        TestUiUtil.setUserOriginatedValue(textField, textField.getEmptyValue());
        assertThat(getDefaultModelObject().getValue(), is(nullValue()));
    }

    @Test
    void testDerivedLabel() {
        assertThat(TestUiUtil.getLabelOfComponentAt(getDefaultSection(), 2), is("Foo"));
    }

    @Override
    protected TestModelObjectWithObjectInteger getDefaultModelObject() {
        return (TestModelObjectWithObjectInteger)super.getDefaultModelObject();
    }

    @UISection
    protected static class IntegerFieldTestPmo extends AnnotationTestPmo {

        public IntegerFieldTestPmo(Object modelObject) {
            super(modelObject);
        }

        @Override
        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @UIIntegerField(position = 1, label = "", enabled = EnabledType.DYNAMIC, required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, format = FANCY_FORMAT, maxLength = MAX_LENGTH)
        public void value() {
            // model binding
        }

        @Override
        @BindTooltip(TEST_TOOLTIP)
        @UIIntegerField(position = 2, label = TEST_LABEL, enabled = EnabledType.DISABLED, required = RequiredType.REQUIRED, visible = VisibleType.INVISIBLE)
        public void staticValue() {
            // model binding
        }

        @UIIntegerField(position = 3)
        public int getFoo() {
            return 42;
        }
    }

    protected static class TestModelObjectWithPrimitiveInteger {

        private int value = 0;

        public int getValue() {
            return value;
        }

        public void setValue(int d) {
            this.value = d;
        }

        public int getStaticValue() {
            return value;
        }
    }

    protected static class TestModelObjectWithObjectInteger extends TestModelObject<Integer> {

        @CheckForNull
        private Integer value = null;

        @CheckForNull
        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        public void setValue(@CheckForNull Integer value) {
            this.value = value;
        }
    }
}
