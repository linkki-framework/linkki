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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.linkki.core.ui.section.annotations.BindTooltip.TooltipType;
import org.linkki.core.ui.section.annotations.UIDateFieldIntegrationTest.DateFieldTestPmo;
import org.linkki.util.TwoDigitYearUtil;

import com.vaadin.ui.DateField;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class UIDateFieldIntegrationTest extends FieldAnnotationIntegrationTest<DateField, DateFieldTestPmo> {

    private static final String FANCY_FORMAT = "yyyy#MM#dd";

    public UIDateFieldIntegrationTest() {
        super(TestModelObjectWithDate::new, DateFieldTestPmo::new);
    }

    @Test
    public void testDateFormat() {
        DateField dateField = createFirstComponent(new TestModelObjectWithDate());

        assertThat(dateField.getDateFormat(), is(FANCY_FORMAT));
    }

    @Test
    public void testTextFieldValueWithDate() {
        TestModelObjectWithDate modelObject = new TestModelObjectWithDate();
        DateField dateField = createFirstComponent(modelObject);

        assertThat(dateField.getValue(), is(nullValue()));

        Calendar cal = new GregorianCalendar(2009, 4, 1);
        Date date = cal.getTime();

        TestUiUtil.setUserOriginatedValue(dateField, LocalDate.of(2009, 5, 1));
        assertThat(modelObject.getValue(), is(date));

        date.setTime(0);
        modelObject.setValue(date);
        getBindingContext().modelChanged();
        assertThat(dateField.getValue(), is(LocalDate.ofEpochDay(0)));

        TestUiUtil.setUserOriginatedValue(dateField, null);
        assertThat(modelObject.getValue(), is(nullValue()));
    }

    @Test
    public void testTextFieldValueWithLocalDate() {
        TestModelObjectWithLocalDate modelObject = new TestModelObjectWithLocalDate();
        DateField dateField = createFirstComponent(modelObject);

        assertThat(dateField.getValue(), is(nullValue()));

        LocalDate localDate = LocalDate.of(2009, 5, 1);

        TestUiUtil.setUserOriginatedValue(dateField, localDate);
        assertThat(modelObject.getValue(), is(localDate));

        localDate = LocalDate.of(1990, 1, 1);

        modelObject.setValue(localDate);
        getBindingContext().modelChanged();
        assertThat(dateField.getValue(), is(localDate));

        TestUiUtil.setUserOriginatedValue(dateField, null);
        assertThat(modelObject.getValue(), is(nullValue()));
    }

    @Test
    public void testTextFieldValueWithLocalDate_DateConversion() {
        TestModelObjectWithLocalDate modelObject = new TestModelObjectWithLocalDate();
        DateField dateField = createFirstComponent(modelObject);

        assertThat(dateField.getValue(), is(nullValue()));

        LocalDate localDate = LocalDate.of(19, 5, 1);
        LocalDate expectedConvertedLocalDate = TwoDigitYearUtil.convert(localDate);

        TestUiUtil.setUserOriginatedValue(dateField, localDate);
        assertThat(modelObject.getValue(), is(expectedConvertedLocalDate));

        localDate = LocalDate.of(90, 1, 1);

        modelObject.setValue(localDate);
        getBindingContext().modelChanged();
        assertThat(dateField.getValue(), is(localDate));

        TestUiUtil.setUserOriginatedValue(dateField, null);
        assertThat(modelObject.getValue(), is(nullValue()));
    }

    @Test
    @Override
    public void testNullInputIfRequired() {
        DateField dateField = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(dateField.isRequiredIndicatorVisible(), is(true));

        LocalDate localDate = LocalDate.of(2009, 5, 1);
        Calendar cal = new GregorianCalendar(2009, 4, 1);
        Date date = cal.getTime();

        TestUiUtil.setUserOriginatedValue(dateField, localDate);
        assertThat(getDefaultModelObject().getValue(), is(date));

        TestUiUtil.setUserOriginatedValue(dateField, null);
        assertThat(getDefaultModelObject().getValue(), is(nullValue()));
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
        @UIDateField(position = 1, label = "", enabled = EnabledType.DYNAMIC, required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, dateFormat = FANCY_FORMAT)
        public void value() {
            // model binding
        }

        @Override
        @BindTooltip(TEST_TOOLTIP)
        @UIDateField(position = 2, label = TEST_LABEL, enabled = EnabledType.DISABLED, required = RequiredType.REQUIRED, visible = VisibleType.INVISIBLE)
        public void staticValue() {
            // model binding
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
