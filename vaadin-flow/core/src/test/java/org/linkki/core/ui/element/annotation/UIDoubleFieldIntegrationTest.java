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
import org.linkki.core.ui.element.annotation.UIDoubleFieldIntegrationTest.DoubleFieldTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.core.uiframework.UiFramework;

import com.vaadin.flow.component.textfield.TextField;

import edu.umd.cs.findbugs.annotations.CheckForNull;

class UIDoubleFieldIntegrationTest extends FieldAnnotationIntegrationTest<TextField, DoubleFieldTestPmo> {

    private static final String FANCY_FORMAT = "##,##,##.###";

    private static final int MAX_LENGTH = 8;

    private final NumberFormat formatter;

    UIDoubleFieldIntegrationTest() {
        super(TestModelObjectWithObjectDouble::new, DoubleFieldTestPmo::new);
        formatter = new DecimalFormat(FANCY_FORMAT, DecimalFormatSymbols.getInstance(UiFramework.getLocale()));
    }

    @Test
    void testSetValue_WithPrimitiveDoubleInModelObject() {
        TestModelObjectWithPrimitiveDouble modelObject = new TestModelObjectWithPrimitiveDouble();
        TextField textField = createFirstComponent(modelObject);

        assertThat(textField.getMaxLength(), is(MAX_LENGTH));
        assertThat(textField.getValue(), is(formatter.format(0.0)));

        KaribuUtils.Fields.setValue(textField, formatter.format(1.0));
        assertThat(modelObject.getValue(), is(1.0));

        modelObject.setValue(2.0);
        getBindingContext().modelChanged();
        assertThat(textField.getValue(), is(formatter.format(2.0)));
    }

    @Test
    void testSetValue_WithPrimitiveDoubleInModelObject_RevertsForNull() {
        TextField textField = createFirstComponent(new TestModelObjectWithPrimitiveDouble());
        KaribuUtils.Fields.setValue(textField, formatter.format(1.0));
        assertThat(textField.getValue(), is(formatter.format(1.0)));

        KaribuUtils.Fields.setValue(textField, "");

        // field reverts to last valid value
        assertThat(textField.getValue(), is(formatter.format(1.0)));
    }

    @Test
    void testSetValue_WithObjectDoubleInModelObject() {
        TestModelObjectWithObjectDouble modelObject = new TestModelObjectWithObjectDouble();
        TextField textField = createFirstComponent(modelObject);

        assertThat(textField.getValue(), is(emptyString()));

        KaribuUtils.Fields.setValue(textField, formatter.format(1.0));
        assertThat(modelObject.getValue(), is(1.0));

        modelObject.setValue(2.0);
        getBindingContext().modelChanged();
        assertThat(textField.getValue(), is(formatter.format(2.0)));

        KaribuUtils.Fields.setValue(textField, textField.getEmptyValue());
        assertThat(modelObject.getValue(), is(nullValue()));
    }

    @Test
    @Override
    void testNullInputIfRequired() {
        TextField doubleField = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(doubleField.isRequiredIndicatorVisible(), is(true));

        KaribuUtils.Fields.setValue(doubleField, formatter.format(1.0));
        assertThat(getDefaultModelObject().getValue(), is(1.0));

        KaribuUtils.Fields.setValue(doubleField, doubleField.getEmptyValue());
        assertThat(getDefaultModelObject().getValue(), is(nullValue()));
    }

    @Test
    void testDerivedLabel() {
        assertThat(TestUiUtil.getLabelOfComponentAt(getDefaultSection(), 2), is("Foo"));
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
        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @UIDoubleField(position = 1,
                label = "",
                enabled = EnabledType.DYNAMIC,
                required = RequiredType.DYNAMIC,
                visible = VisibleType.DYNAMIC,
                format = FANCY_FORMAT,
                maxLength = MAX_LENGTH)
        public void value() {
            // model binding
        }

        @Override
        @BindTooltip(TEST_TOOLTIP)
        @UIDoubleField(position = 2,
                label = TEST_LABEL,
                enabled = EnabledType.DISABLED,
                required = RequiredType.REQUIRED,
                visible = VisibleType.INVISIBLE)
        public void staticValue() {
            // model binding
        }

        @UIDoubleField(position = 3)
        public double getFoo() {
            return 23.45;
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

        @CheckForNull
        private Double value = null;

        @CheckForNull
        @Override
        public Double getValue() {
            return value;
        }

        @Override
        public void setValue(@CheckForNull Double value) {
            this.value = value;
        }
    }
}
