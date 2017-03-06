/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.converters;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import org.junit.Test;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDateConverter;

public class LinkkiConverterFactoryTest {

    @Test
    public void testFindConverter_noneRegistered() {
        LinkkiConverterFactory linkkiConverterFactory = new LinkkiConverterFactory(() -> Collections.emptyList());

        assertThat(linkkiConverterFactory.findConverter(String.class, Date.class),
                   is(instanceOf(StringToDateConverter.class)));
        assertThat(linkkiConverterFactory.findConverter(String.class, java.time.LocalDate.class), is(nullValue()));
        assertThat(linkkiConverterFactory.findConverter(String.class, org.joda.time.LocalDate.class), is(nullValue()));
    }

    @Test
    public void testFindConverter_allRegistered() {
        LinkkiConverterFactory linkkiConverterFactory = new LinkkiConverterFactory(
                () -> Arrays.asList(new JodaLocalDateToStringConverter(), new JodaLocalDateTimeToStringConverter(),
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
                () -> Collections.singleton(new MyStringToDateConverter()));

        assertThat(linkkiConverterFactory.findConverter(String.class, Date.class),
                   is(instanceOf(MyStringToDateConverter.class)));
    }

    @SuppressWarnings("null")
    public static class MyStringToDateConverter implements Converter<String, Date>, AutoDiscoveredConverter {

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
