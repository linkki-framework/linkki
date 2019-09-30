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
package org.linkki.core.ui.converters;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.Date;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.linkki.util.Sequence;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDateConverter;

public class LinkkiConverterFactoryTest {

    @Test
    public void testFindConverter_noneRegistered() {
        LinkkiConverterFactory linkkiConverterFactory = new LinkkiConverterFactory(() -> Sequence.empty());

        assertThat(linkkiConverterFactory.findConverter(String.class, Date.class),
                   is(instanceOf(StringToDateConverter.class)));
        assertThat(linkkiConverterFactory.findConverter(String.class, java.time.LocalDate.class), is(nullValue()));
        assertThat(linkkiConverterFactory.findConverter(String.class, org.joda.time.LocalDate.class), is(nullValue()));
    }

    @Test
    public void testFindConverter_allRegistered() {
        LinkkiConverterFactory linkkiConverterFactory = new LinkkiConverterFactory(
                () -> Sequence.of(new JodaLocalDateToStringConverter(), new JodaLocalDateTimeToStringConverter(),
                                  new JodaLocalDateToDateConverter(), new LocalDateToStringConverter(),
                                  new LocalDateTimeToStringConverter(), new LocalDateToDateConverter()));

        assertThat(linkkiConverterFactory.findConverter(String.class, Date.class),
                   is(instanceOf(StringToDateConverter.class)));
        assertThat(linkkiConverterFactory.findConverter(String.class, java.time.LocalDate.class),
                   is(instanceOf(LocalDateToStringConverter.class)));
        assertThat(linkkiConverterFactory.findConverter(String.class, org.joda.time.LocalDate.class),
                   is(instanceOf(JodaLocalDateToStringConverter.class)));
        assertThat(linkkiConverterFactory.findConverter(String.class, java.time.LocalDateTime.class),
                   is(instanceOf(LocalDateTimeToStringConverter.class)));
        assertThat(linkkiConverterFactory.findConverter(String.class, org.joda.time.LocalDateTime.class),
                   is(instanceOf(JodaLocalDateTimeToStringConverter.class)));
        assertThat(linkkiConverterFactory.findConverter(Date.class, java.time.LocalDate.class),
                   is(instanceOf(LocalDateToDateConverter.class)));
        assertThat(linkkiConverterFactory.findConverter(Date.class, org.joda.time.LocalDate.class),
                   is(instanceOf(JodaLocalDateToDateConverter.class)));
    }

    @Test
    public void testFindConverter_overrideDefault() {
        LinkkiConverterFactory linkkiConverterFactory = new LinkkiConverterFactory(
                () -> Sequence.of(new MyStringToDateConverter()));

        assertThat(linkkiConverterFactory.findConverter(String.class, Date.class),
                   is(instanceOf(MyStringToDateConverter.class)));
    }

    
    public static class MyStringToDateConverter implements Converter<String, Date> {

        private static final long serialVersionUID = 1L;

        @Override
        public Date convertToModel(String value, Class<? extends Date> targetType, Locale locale)
                throws ConversionException {
            return null;
        }

        @Override
        public String convertToPresentation(Date value, Class<? extends String> targetType, Locale locale)
                throws ConversionException {
            return null;
        }

        @Override
        public Class<Date> getModelType() {
            return Date.class;
        }

        @Override
        public Class<String> getPresentationType() {
            return String.class;
        }

    }

}
