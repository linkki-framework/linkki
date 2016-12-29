/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.converters;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.junit.Test;
import org.linkki.util.DateFormatRegistry;

public class LocalDateToStringConverterTest {


    @Test(expected = UnsupportedOperationException.class)
    public void testConvertToModel() {
        new LocalDateToStringConverter().convertToModel(null, null, null);
    }

    @Test
    public void testConvertToPresentation_null_shouldReturnEmptyString() {
        String presentation = new LocalDateToStringConverter().convertToPresentation(null, String.class, null);
        assertNotNull(presentation);
        assertThat(presentation, is(""));
    }

    @Test
    public void testConvertToPresentation() {
        LocalDate date = LocalDate.now();
        String presentation = new LocalDateToStringConverter().convertToPresentation(date, null, Locale.GERMANY);
        assertNotNull(presentation);
        assertThat(presentation, is(date.format(DateTimeFormatter.ofPattern(DateFormatRegistry.PATTERN_DE))));
    }

}