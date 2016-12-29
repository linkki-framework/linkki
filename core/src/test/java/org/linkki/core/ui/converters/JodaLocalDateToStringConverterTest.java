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

import org.joda.time.LocalDate;
import org.junit.Test;
import org.linkki.util.DateFormatRegistry;

public class JodaLocalDateToStringConverterTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testConvertToModel() {
        new JodaLocalDateToStringConverter().convertToModel(null, null, null);
    }

    @Test
    public void testConvertToPresentation_null_shouldReturnEmptyString() {
        String presentation = new JodaLocalDateToStringConverter().convertToPresentation(null, String.class, null);
        assertNotNull(presentation);
        assertThat(presentation, is(""));
    }

    @Test
    public void testConvertToPresentation() {
        LocalDate date = LocalDate.now();
        String presentation = new JodaLocalDateToStringConverter().convertToPresentation(date, null, Locale.GERMANY);
        assertNotNull(presentation);
        assertThat(presentation, is(date.toString(DateFormatRegistry.PATTERN_DE)));
    }

}