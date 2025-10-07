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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.element.annotation.UIDateFieldIntegrationTest.DateFieldTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.test.KaribuUtils.Fields;
import org.linkki.util.TwoDigitYearUtil;

import com.vaadin.flow.component.datepicker.DatePicker;

import edu.umd.cs.findbugs.annotations.CheckForNull;

class UIDateFieldIntegrationTest extends FieldAnnotationIntegrationTest<DatePicker, DateFieldTestPmo> {

    UIDateFieldIntegrationTest() {
        super(TestModelObjectWithDate::new, DateFieldTestPmo::new);
    }

    @Test
    void testTextFieldValueWithDate() {
        TestModelObjectWithDate modelObject = new TestModelObjectWithDate();
        DatePicker dateField = createFirstComponent(modelObject);

        assertThat(dateField.getValue()).isNull();

        Calendar cal = new GregorianCalendar(2009, 4, 1);
        Date date = cal.getTime();

        Fields.setValue(dateField, LocalDate.of(2009, 5, 1));
        assertThat(modelObject.getValue()).isEqualTo(date);

        date.setTime(0);
        modelObject.setValue(date);
        getBindingContext().modelChanged();
        assertThat(dateField.getValue()).isEqualTo(LocalDate.ofEpochDay(0));

        Fields.setValue(dateField, null);
        assertThat(modelObject.getValue()).isNull();
    }

    @Test
    void testTextFieldValueWithLocalDate() {
        TestModelObjectWithLocalDate modelObject = new TestModelObjectWithLocalDate();
        DatePicker dateField = createFirstComponent(modelObject);

        assertThat(dateField.getValue()).isNull();

        LocalDate localDate = LocalDate.of(2009, 5, 1);

        Fields.setValue(dateField, localDate);
        assertThat(modelObject.getValue()).isEqualTo(localDate);

        localDate = LocalDate.of(1990, 1, 1);

        modelObject.setValue(localDate);
        getBindingContext().modelChanged();
        assertThat(dateField.getValue()).isEqualTo(localDate);

        Fields.setValue(dateField, null);
        assertThat(modelObject.getValue()).isNull();
    }

    @Test
    void testTextFieldValueWithLocalDate_DateConversion() {
        TestModelObjectWithLocalDate modelObject = new TestModelObjectWithLocalDate();
        DatePicker dateField = createFirstComponent(modelObject);

        assertThat(dateField.getValue()).isNull();

        LocalDate localDate = LocalDate.of(19, 5, 1);
        LocalDate expectedConvertedLocalDate = TwoDigitYearUtil.convert(localDate);

        Fields.setValue(dateField, localDate);
        assertThat(modelObject.getValue()).isEqualTo(expectedConvertedLocalDate);

        localDate = LocalDate.of(90, 1, 1);

        modelObject.setValue(localDate);
        getBindingContext().modelChanged();
        assertThat(dateField.getValue()).isEqualTo(localDate);

        Fields.setValue(dateField, null);
        assertThat(modelObject.getValue()).isNull();
    }

    @Test
    @Override
    void testNullInputIfRequired() {
        DatePicker dateField = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(dateField.isRequiredIndicatorVisible()).isTrue();

        LocalDate localDate = LocalDate.of(2009, 5, 1);
        Calendar cal = new GregorianCalendar(2009, 4, 1);
        Date date = cal.getTime();

        Fields.setValue(dateField, localDate);
        assertThat(getDefaultModelObject().getValue()).isEqualTo(date);

        Fields.setValue(dateField, null);
        assertThat(getDefaultModelObject().getValue()).isNull();
    }

    @Test
    void testClearButtonIsVisible() {
        TestModelObjectWithLocalDate modelObject = new TestModelObjectWithLocalDate();
        DatePicker dateField = createFirstComponent(modelObject);

        assertThat(dateField.isClearButtonVisible()).isTrue();
    }

    @Test
    void testDerivedLabel() {
        assertThat(TestUiUtil.getLabelOfComponentAt(getDefaultSection(), 2)).isEqualTo("Foo");
    }

    @Override
    protected TestModelObjectWithDate getDefaultModelObject() {
        return (TestModelObjectWithDate)super.getDefaultModelObject();
    }

    @UISection
    protected static class DateFieldTestPmo extends AnnotationTestPmo {

        public DateFieldTestPmo(Object modelObject) {
            super(modelObject);
        }

        @Override
        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @UIDateField(position = 1,
                label = "",
                enabled = EnabledType.DYNAMIC,
                required = RequiredType.DYNAMIC,
                visible = VisibleType.DYNAMIC)
        public void value() {
            // model binding
        }

        @Override
        @BindTooltip(TEST_TOOLTIP)
        @UIDateField(position = 2,
                label = TEST_LABEL,
                enabled = EnabledType.DISABLED,
                required = RequiredType.REQUIRED,
                visible = VisibleType.INVISIBLE)
        public void staticValue() {
            // model binding
        }

        @UIDateField(position = 3)
        public LocalDate getFoo() {
            return LocalDate.now();
        }
    }

    protected static class TestModelObjectWithDate extends TestModelObject<Date> {

        @CheckForNull
        private Date value = null;

        @CheckForNull
        @Override
        public Date getValue() {
            return value;
        }

        @Override
        public void setValue(@CheckForNull Date value) {
            this.value = value;
        }

        @CheckForNull
        @Override
        public Date getStaticValue() {
            return super.getStaticValue();
        }
    }

    protected static class TestModelObjectWithLocalDate {

        @CheckForNull
        private LocalDate value = null;

        @CheckForNull
        public LocalDate getStaticValue() {
            return getValue();
        }

        @CheckForNull
        public LocalDate getValue() {
            return value;
        }

        public void setValue(@CheckForNull LocalDate value) {
            this.value = value;
        }
    }
}
