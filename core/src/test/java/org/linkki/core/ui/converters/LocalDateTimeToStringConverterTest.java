/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.converters;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.Locale;

import org.junit.Test;

public class LocalDateTimeToStringConverterTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testConvertToModel() {
        new LocalDateTimeToStringConverter().convertToModel(null, null, null);
    }

    @Test
    public void testConvertToPresentation_null_shouldReturnEmptyString() {
        String presentation = new LocalDateTimeToStringConverter().convertToPresentation(null, String.class, null);
        assertNotNull(presentation);
        assertThat(presentation, is(""));
    }

    @Test
    public void testConvertToPresentation_locale_de() {
        LocalDateTime dateTime = LocalDateTime.of(2016, 12, 29, 15, 30, 0, 555);
        String presentation = new LocalDateTimeToStringConverter().convertToPresentation(dateTime, null,
                                                                                         Locale.GERMANY);
        assertNotNull(presentation);
        assertThat(presentation, is("29.12.2016 15:30"));
    }

    @Test
    public void testConvertToPresentation_locale_() {
        LocalDateTime dateTime = LocalDateTime.of(2016, 12, 29, 15, 30, 0, 555);
        String presentation = new LocalDateTimeToStringConverter().convertToPresentation(dateTime, null, Locale.US);
        assertNotNull(presentation);
        assertThat(presentation, is("12/29/16 3:30 PM"));
    }

}