/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.converters;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.joda.time.LocalDateTime;
import org.junit.Test;

public class JodaLocalDateTimeToStringConverterTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testConvertToModel() {
        new JodaLocalDateTimeToStringConverter().convertToModel(null, null, null);
    }

    @Test
    public void testConvertToPresentation_null_shouldReturnEmptyString() {
        String presentation = new JodaLocalDateTimeToStringConverter().convertToPresentation(null, String.class, null);
        assertNotNull(presentation);
        assertThat(presentation, is(""));
    }

    @Test
    public void testConvertToPresentation_locale_de() {
        LocalDateTime dateTime = new LocalDateTime(2016, 12, 29, 15, 30, 0, 555);
        String presentation = new JodaLocalDateTimeToStringConverter().convertToPresentation(dateTime, null,
                                                                                             Locale.GERMANY);
        assertNotNull(presentation);
        assertThat(presentation, is("29.12.2016\t15:30"));
    }

    @Test
    public void testConvertToPresentation_locale_() {
        LocalDateTime dateTime = new LocalDateTime(2016, 12, 29, 15, 30, 0, 555);
        String presentation = new JodaLocalDateTimeToStringConverter().convertToPresentation(dateTime, null, Locale.US);
        assertNotNull(presentation);
        assertThat(presentation, is("12/29/16\t3:30 PM"));
    }

}