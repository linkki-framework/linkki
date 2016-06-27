/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Locale;

import org.joda.time.LocalDateTime;
import org.junit.Test;

public class LocalDateTimeConverterTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testConvertToModel() {
        new LocalDateTimeConverter().convertToModel(null, null, null);
    }

    @Test
    public void testConvertToPresentation() {
        LocalDateTime value = new LocalDateTime(2016, 6, 29, 8, 4, 23, 456);
        LocalDateTimeConverter localDateTimeConverter = new LocalDateTimeConverter();

        String presentation = localDateTimeConverter.convertToPresentation(value, String.class, Locale.GERMANY);

        assertThat(presentation, is(equalTo("29.06.2016 08:04")));

        presentation = localDateTimeConverter.convertToPresentation(value, String.class, Locale.US);

        assertThat(presentation, is(equalTo("6/29/16 8:04 AM")));
    }

}
