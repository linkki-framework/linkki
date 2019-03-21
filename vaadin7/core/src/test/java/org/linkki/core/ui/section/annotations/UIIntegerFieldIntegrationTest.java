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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import org.junit.Test;
import org.linkki.core.defaults.uielement.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.uielement.aspects.types.EnabledType;
import org.linkki.core.defaults.uielement.aspects.types.RequiredType;
import org.linkki.core.defaults.uielement.aspects.types.TooltipType;
import org.linkki.core.defaults.uielement.aspects.types.VisibleType;
import org.linkki.core.ui.components.IntegerField;
import org.linkki.core.ui.section.annotations.UIIntegerFieldIntegrationTest.IntegerFieldTestPmo;
import org.linkki.core.uiframework.UiFramework;

import com.vaadin.data.Buffered;
import com.vaadin.ui.TextField;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class UIIntegerFieldIntegrationTest extends FieldAnnotationIntegrationTest<IntegerField, IntegerFieldTestPmo> {

    private static final String FANCY_FORMAT = "##,##,##.###";

    private static final int MAX_LENGTH = 8;

    private NumberFormat formatter;

    public UIIntegerFieldIntegrationTest() {
        super(TestModelObjectWithObjectInteger::new, IntegerFieldTestPmo::new);
        formatter = new DecimalFormat(FANCY_FORMAT, DecimalFormatSymbols.getInstance(UiFramework.getLocale()));
    }

    @Test
    public void testSetValueWithPrimitiveIntInModelObject() {
        TestModelObjectWithPrimitiveInteger modelObject = new TestModelObjectWithPrimitiveInteger();
        TextField textField = createFirstComponent(modelObject);

        assertThat(textField.getMaxLength(), is(MAX_LENGTH));
        assertThat(textField.getValue(), is(formatter.format(0)));

        textField.setValue(formatter.format(1));
        assertThat(modelObject.getValue(), is(1));

        modelObject.setValue(2000);
        // updateUi(); not needed at the moment
        assertThat(textField.getValue(), is(formatter.format(2000)));
    }

    @Test
    public void testSetValueWithPrimitiveIntInModelObject_IllegalNumbers() {
        TestModelObjectWithPrimitiveInteger modelObject = new TestModelObjectWithPrimitiveInteger();
        TextField textField = createFirstComponent(modelObject);

        assertThat(textField.isReadOnly(), is(false));
        textField.setValue("asd");
        assertThat(modelObject.getValue(), is(0));
    }

    @Test(expected = Buffered.SourceException.class)
    public void testSetValueWithPrimitiveIntegerInModelObjectFailsForNull() {
        TextField textField = createFirstComponent(new TestModelObjectWithPrimitiveInteger());
        textField.setValue(null);
    }


    @Test
    public void testSetValueWithObjectIntegerInModelObject() {
        TestModelObjectWithObjectInteger modelObject = new TestModelObjectWithObjectInteger();
        TextField textField = createFirstComponent(modelObject);

        assertThat(textField.getValue(), is(emptyString()));

        textField.setValue(formatter.format(1));
        assertThat(modelObject.getValue(), is(1));

        modelObject.setValue(2);
        assertThat(textField.getValue(), is(formatter.format(2)));

        textField.setValue(null);
        assertThat(modelObject.getValue(), is(nullValue()));
    }

    @Test
    public void testSetValueWithObjectIntegerInModelObject_IllegalNumbers() {
        TestModelObjectWithObjectInteger modelObject = new TestModelObjectWithObjectInteger();
        TextField textField = createFirstComponent(modelObject);

        textField.setValue("asd");
        assertThat(modelObject.getValue(), is(nullValue()));
    }

    @Test
    @Override
    public void testNullInputIfRequired() {
        TextField textField = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(textField.isRequired(), is(true));

        textField.setValue(formatter.format(1));
        assertThat(getDefaultModelObject().getValue(), is(1));

        textField.setValue(null);
        assertThat(getDefaultModelObject().getValue(), is(nullValue()));
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
