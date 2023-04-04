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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.element.annotation.UIDateTimeFieldIntegrationTest.DateTimeFieldTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.util.TwoDigitYearUtil;

import com.vaadin.flow.component.datetimepicker.DateTimePicker;

import edu.umd.cs.findbugs.annotations.CheckForNull;

class UIDateTimeFieldIntegrationTest extends FieldAnnotationIntegrationTest<DateTimePicker, DateTimeFieldTestPmo> {
    UIDateTimeFieldIntegrationTest() {
        super(TestModelObjectWithDate::new, DateTimeFieldTestPmo::new);
    }

    @Test
    void testTextFieldValueWithDateTime() {
        TestModelObjectWithDate modelObject = new TestModelObjectWithDate();
        DateTimePicker dateTimeField = createFirstComponent(modelObject);

        assertThat(dateTimeField.getValue(), is(nullValue()));

        Calendar cal = new GregorianCalendar(2009, 4, 1);
        Date date = cal.getTime();
        LocalDateTime dateTime = LocalDateTime.of(2009, 5, 1, 0, 0);

        TestUiUtil.setUserOriginatedValue(dateTimeField, dateTime);
        assertThat(modelObject.getValue(), is(date));

        date.setTime(0);
        modelObject.setValue(date);
        getBindingContext().modelChanged();
        // Value for time depends on locale/time zone, so only check date
        assertThat(dateTimeField.getValue().toLocalDate(), is(LocalDate.ofEpochDay(0)));

        TestUiUtil.setUserOriginatedValue(dateTimeField, null);
        assertThat(modelObject.getValue(), is(nullValue()));
    }

    @Test
    void testTextFieldValueWithLocalDate() {
        TestModelObjectWithLocalDateTime modelObject = new TestModelObjectWithLocalDateTime();
        DateTimePicker dateTimeField = createFirstComponent(modelObject);

        assertThat(dateTimeField.getValue(), is(nullValue()));

        LocalDateTime localDateTime = LocalDateTime.of(2009, 5, 1, 0, 0);

        TestUiUtil.setUserOriginatedValue(dateTimeField, localDateTime);
        assertThat(modelObject.getValue(), is(localDateTime));

        localDateTime = LocalDateTime.of(1990, 5, 1, 0, 0);

        modelObject.setValue(localDateTime);
        getBindingContext().modelChanged();
        assertThat(dateTimeField.getValue(), is(localDateTime));

        TestUiUtil.setUserOriginatedValue(dateTimeField, null);
        assertThat(modelObject.getValue(), is(nullValue()));
    }


    @Test
    void testTextFieldValueWithLocalDateTime_DateConversion() {
        TestModelObjectWithLocalDateTime modelObject = new TestModelObjectWithLocalDateTime();
        DateTimePicker dateTimeField = createFirstComponent(modelObject);

        assertThat(dateTimeField.getValue(), is(nullValue()));

        LocalDateTime expectedConvertedLocalDateTime = TwoDigitYearUtil.convert(LocalDateTime.of(19, 5, 1, 0, 0));

        TestUiUtil.setUserOriginatedValue(dateTimeField, LocalDateTime.of(19, 5, 1, 0, 0));
        assertThat(modelObject.getValue(), is(expectedConvertedLocalDateTime));

        LocalDateTime localDateTime = LocalDateTime.of(90, 1, 1, 0, 0);
        modelObject.setValue(localDateTime);
        getBindingContext().modelChanged();
        assertThat(dateTimeField.getValue(), is(localDateTime));

        TestUiUtil.setUserOriginatedValue(dateTimeField, null);
        assertThat(modelObject.getValue(), is(nullValue()));
    }

    @Test
    @Override
    void testNullInputIfRequired() {
        DateTimePicker dateTimeField = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(dateTimeField.isRequiredIndicatorVisible(), is(true));

        LocalDateTime localDateTime = LocalDateTime.of(2009, 5, 1, 0, 0);
        Calendar cal = new GregorianCalendar(2009, 4, 1);
        Date date = cal.getTime();

        TestUiUtil.setUserOriginatedValue(dateTimeField, localDateTime);
        assertThat(getDefaultModelObject().getValue(), is(date));

        TestUiUtil.setUserOriginatedValue(dateTimeField, null);
        assertThat(getDefaultModelObject().getValue(), is(nullValue()));
    }

    @Test
    void testDerivedLabel() {
        assertThat(TestUiUtil.getLabelOfComponentAt(getDefaultSection(), 2), is("Foo"));
    }

    @Override
    protected TestModelObjectWithDate getDefaultModelObject() {
        return (TestModelObjectWithDate)super.getDefaultModelObject();
    }

    @UISection
    protected static class DateTimeFieldTestPmo extends AnnotationTestPmo {

        public DateTimeFieldTestPmo(Object modelObject) {
            super(modelObject);
        }

        @Override
        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @UIDateTimeField(position = 1, label = "", enabled = EnabledType.DYNAMIC, required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, step = 30)
        public void value() {
            // model binding
        }

        @Override
        @BindTooltip(TEST_TOOLTIP)
        @UIDateTimeField(position = 2, label = TEST_LABEL, enabled = EnabledType.DISABLED, required = RequiredType.REQUIRED, visible = VisibleType.INVISIBLE)
        public void staticValue() {
            // model binding
        }

        @UIDateTimeField(position = 3)
        public LocalDateTime getFoo() {
            return LocalDateTime.now();
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

    protected static class TestModelObjectWithLocalDateTime {

        @CheckForNull
        private LocalDateTime value = null;

        @CheckForNull
        public LocalDateTime getStaticValue() {
            return getValue();
        }

        @CheckForNull
        public LocalDateTime getValue() {
            return value;
        }

        public void setValue(@CheckForNull LocalDateTime value) {
            this.value = value;
        }
    }
}
