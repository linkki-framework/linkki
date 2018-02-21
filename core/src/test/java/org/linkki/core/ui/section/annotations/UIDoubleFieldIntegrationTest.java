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

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.junit.Test;
import org.linkki.core.ui.components.DoubleField;
import org.linkki.core.ui.section.annotations.UIDoubleFieldIntegrationTest.DoubleFieldTestPmo;
import org.linkki.core.ui.util.UiUtil;

import com.vaadin.data.Buffered;
import com.vaadin.ui.TextField;

public class UIDoubleFieldIntegrationTest extends FieldAnnotationIntegrationTest<DoubleField, DoubleFieldTestPmo> {

    private static final String FANCY_FORMAT = "##,##,##.###";

    private static final int MAX_LENGTH = 8;

    private NumberFormat formatter;

    @SuppressWarnings("null")
    public UIDoubleFieldIntegrationTest() {
        super(TestModelObjectWithObjectDouble::new, DoubleFieldTestPmo::new);
        Locale.setDefault(UiUtil.getUiLocale());
        formatter = new DecimalFormat(FANCY_FORMAT, DecimalFormatSymbols.getInstance(UiUtil.getUiLocale()));
    }

    @Test
    public void testSetValueWithPrimitiveDoubleInModelObject() {
        TestModelObjectWithPrimitiveDouble modelObject = new TestModelObjectWithPrimitiveDouble();
        TextField textField = createFirstComponent(modelObject);

        assertThat(textField.getMaxLength(), is(MAX_LENGTH));
        assertThat(textField.getValue(), is(formatter.format(0.0)));

        textField.setValue(formatter.format(1.0));
        assertThat(modelObject.getValue(), is(1.0));

        modelObject.setValue(2.0);
        // updateUi(); not needed at the moment
        assertThat(textField.getValue(), is(formatter.format(2.0)));
    }

    @Test
    public void testSetValueWithPrimitiveDoubleInModelObject_IllegalNumbers() {
        TestModelObjectWithPrimitiveDouble modelObject = new TestModelObjectWithPrimitiveDouble();
        TextField textField = createFirstComponent(modelObject);

        textField.setValue("asd");
        assertThat(modelObject.getValue(), is(0.0));
    }

    @Test(expected = Buffered.SourceException.class)
    public void testSetValueWithPrimitiveDoubleInModelObjectFailsForNull() {
        TextField textField = createFirstComponent(new TestModelObjectWithPrimitiveDouble());
        textField.setValue(null);
    }

    @Test
    public void testSetValueWithObjectDoubleInModelObject() {
        TestModelObjectWithObjectDouble modelObject = new TestModelObjectWithObjectDouble();
        TextField textField = createFirstComponent(modelObject);

        assertThat(textField.getValue(), is(emptyString()));

        textField.setValue(formatter.format(1.0));
        assertThat(modelObject.getValue(), is(1.0));

        modelObject.setValue(2.0);
        assertThat(textField.getValue(), is(formatter.format(2.0)));

        textField.setValue(null);
        assertThat(modelObject.getValue(), is(nullValue()));
    }

    @Test
    public void testSetValueWithObjectDoubleInModelObject_IllegalNumbers() {
        TestModelObjectWithObjectDouble modelObject = new TestModelObjectWithObjectDouble();
        TextField textField = createFirstComponent(modelObject);

        textField.setValue("asd");
        assertThat(modelObject.getValue(), is(nullValue()));
    }

    @Test
    @Override
    public void testNullInputIfRequired() {
        DoubleField doubleField = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        updateUi();
        assertThat(doubleField.isRequired(), is(true));

        doubleField.setValue(formatter.format(1.0));
        assertThat(getDefaultModelObject().getValue(), is(1.0));

        doubleField.setValue(null);
        assertThat(getDefaultModelObject().getValue(), is(nullValue()));
    }

    @Override
    protected TestModelObjectWithObjectDouble getDefaultModelObject() {
        return (TestModelObjectWithObjectDouble)super.getDefaultModelObject();
    }

    @UISection
    protected static class DoubleFieldTestPmo extends AnnotationTestPmo {

        public DoubleFieldTestPmo(Object modelObject) {
            super(modelObject);
        }

        @Override
        @UIToolTip(toolTipType = ToolTipType.DYNAMIC)
        @UIDoubleField(position = 1, noLabel = true, enabled = EnabledType.DYNAMIC, required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, format = FANCY_FORMAT, maxLength = MAX_LENGTH)
        public void value() {
            // model binding
        }

        @Override
        @UIToolTip(text = TEST_TOOLTIP)
        @UIDoubleField(position = 2, label = TEST_LABEL, enabled = EnabledType.DISABLED, required = RequiredType.REQUIRED, visible = VisibleType.INVISIBLE)
        public void staticValue() {
            // model binding
        }
    }

    protected static class TestModelObjectWithPrimitiveDouble {

        private double value = 0;

        public double getValue() {
            return value;
        }

        public void setValue(double d) {
            this.value = d;
        }

        public double getStaticValue() {
            return value;
        }
    }

    protected static class TestModelObjectWithObjectDouble extends TestModelObject<Double> {

        @Nullable
        private Double value = null;

        @SuppressWarnings("null")
        @CheckForNull
        @Override
        public Double getValue() {
            return value;
        }

        @Override
        public void setValue(@Nullable Double value) {
            this.value = value;
        }
    }
}
