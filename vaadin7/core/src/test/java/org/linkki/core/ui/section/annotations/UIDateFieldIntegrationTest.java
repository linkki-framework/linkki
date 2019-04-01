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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.linkki.core.defaults.ui.element.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.element.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.element.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.element.aspects.types.TooltipType;
import org.linkki.core.defaults.ui.element.aspects.types.VisibleType;
import org.linkki.core.ui.converters.JodaLocalDateToDateConverter;
import org.linkki.core.ui.converters.LinkkiConverterFactory;
import org.linkki.core.ui.converters.LocalDateToDateConverter;
import org.linkki.core.ui.section.annotations.UIDateFieldIntegrationTest.DateFieldTestPmo;
import org.linkki.util.Sequence;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.DateField;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class UIDateFieldIntegrationTest extends FieldAnnotationIntegrationTest<DateField, DateFieldTestPmo> {

    private static final String FANCY_FORMAT = "yyyy#MM#dd";

    public UIDateFieldIntegrationTest() {
        super(TestModelObjectWithDate::new, DateFieldTestPmo::new);
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();

        VaadinSession vaadinSession = mock(VaadinSession.class);
        LinkkiConverterFactory converterFactory = new LinkkiConverterFactory(
                () -> Sequence.of(new LocalDateToDateConverter(),
                                  new JodaLocalDateToDateConverter()));
        when(vaadinSession.getConverterFactory()).thenReturn(converterFactory);
        VaadinSession.setCurrent(vaadinSession);
    }

    @After
    public void tearDown() {
        VaadinSession.setCurrent(null);
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

        Calendar cal = Calendar.getInstance();
        cal.set(2009, 4, 1);
        Date date = cal.getTime();

        dateField.setValue(date);
        assertThat(modelObject.getValue(), is(date));

        date.setTime(0);
        modelObject.setValue(date);
        // updateUi(); not needed at the moment
        assertThat(dateField.getValue(), is(date));

        dateField.setValue(null);
        assertThat(modelObject.getValue(), is(nullValue()));
    }

    @Test
    public void testTextFieldValueWithLocalDate() {
        TestModelObjectWithLocalDate modelObject = new TestModelObjectWithLocalDate();
        DateField dateField = createFirstComponent(modelObject);

        assertThat(dateField.getValue(), is(nullValue()));

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(2009, 4, 1, 0, 0, 0);
        Date date = cal.getTime();
        LocalDate localDate = LocalDate.of(2009, 5, 1);

        dateField.setValue(date);
        assertThat(modelObject.getValue(), is(localDate));

        localDate = LocalDate.of(1990, 1, 1);
        cal.set(1990, 0, 1, 0, 0, 0);
        date = cal.getTime();

        modelObject.setValue(localDate);
        // updateUi(); not needed at the moment
        assertThat(dateField.getValue(), is(date));

        dateField.setValue(null);
        assertThat(modelObject.getValue(), is(nullValue()));
    }

    @Test
    public void testTextFieldValueWithJodaLocalDate() {
        TestModelObjectWithJodaLocalDate modelObject = new TestModelObjectWithJodaLocalDate();
        DateField dateField = createFirstComponent(modelObject);

        assertThat(dateField.getValue(), is(nullValue()));

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(2009, 4, 1, 0, 0, 0);
        Date date = cal.getTime();
        org.joda.time.LocalDate localDate = new org.joda.time.LocalDate(2009, 5, 1);

        dateField.setValue(date);
        assertThat(modelObject.getValue(), is(localDate));

        localDate = new org.joda.time.LocalDate(1990, 1, 1);
        cal.set(1990, 0, 1, 0, 0, 0);
        date = cal.getTime();

        modelObject.setValue(localDate);
        // updateUi(); not needed at the moment
        assertThat(dateField.getValue(), is(date));

        dateField.setValue(null);
        assertThat(modelObject.getValue(), is(nullValue()));
    }

    @Test
    @Override
    public void testNullInputIfRequired() {
        DateField dateField = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(dateField.isRequired(), is(true));

        Calendar cal = Calendar.getInstance();
        cal.set(2009, 4, 1);
        Date date = cal.getTime();

        dateField.setValue(date);
        assertThat(getDefaultModelObject().getValue(), is(date));

        dateField.setValue(null);
        assertThat(getDefaultModelObject().getValue(), is(nullValue()));
    }

    @Test
    public void testNullInputIfRequired_withConverter() {
        TestModelObjectWithLocalDate modelObject = new TestModelObjectWithLocalDate();
        DateFieldTestPmo pmo = new DateFieldTestPmo(modelObject);
        DateField dynamicField = TestUiUtil.getComponentById(createSection(pmo),
                                                             ComponentAnnotationIntegrationTest.PROPERTY_VALUE);
        pmo.setRequired(true);
        modelChanged();
        assertThat(dynamicField.isRequired(), is(true));

        dynamicField.setConvertedValue(LocalDate.of(2018, 1, 1));
        assertThat(modelObject.getValue(), is(LocalDate.of(2018, 1, 1)));

        dynamicField.setConvertedValue(null);
        assertThat(modelObject.getValue(), is(nullValue()));
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

    protected static class TestModelObjectWithJodaLocalDate {

        @CheckForNull
        private org.joda.time.LocalDate value = null;

        @CheckForNull
        public org.joda.time.LocalDate getStaticValue() {
            return getValue();
        }

        @CheckForNull
        public org.joda.time.LocalDate getValue() {
            return value;
        }

        public void setValue(org.joda.time.LocalDate value) {
            this.value = value;
        }
    }
}
