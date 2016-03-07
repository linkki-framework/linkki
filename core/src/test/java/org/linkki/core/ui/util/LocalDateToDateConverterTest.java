/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.util;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.linkki.core.ui.converters.LocalDateToDateConverter;

public class LocalDateToDateConverterTest {

    @Test
    public void testToDate() {
        LocalDateToDateConverter conv = new LocalDateToDateConverter();

        Date date = conv.convertToPresentation(null, null, null);
        assertNull(date);

        Date expectedDate = getDate();
        date = conv.convertToPresentation(LocalDate.fromDateFields(expectedDate), null, null);

        assertEquals(expectedDate, date);

    }

    @Test
    public void testToLocalDate() {
        LocalDateToDateConverter conv = new LocalDateToDateConverter();

        LocalDate localDate = conv.convertToModel(null, null, null);
        assertNull(localDate);

        Date date = getDate();

        localDate = conv.convertToModel(date, null, null);

        assertEquals(date, localDate.toDate());

    }

    @Test
    public void testYearPlus2000() {
        LocalDateToDateConverter converter = new LocalDateToDateConverter();
        LocalDate localDate = converter.convertToModel(new LocalDateTime(15, 2, 5, 3, 4, 15).toDate(), LocalDate.class,
                                                       Locale.getDefault());

        assertThat(localDate.getYear(), is(2015));
        assertThat(localDate.getMonthOfYear(), is(2));
        assertThat(localDate.getDayOfMonth(), is(5));
    }

    @Test
    public void testYearZero() {
        LocalDateToDateConverter converter = new LocalDateToDateConverter();
        LocalDate localDate = converter.convertToModel(new LocalDate(0, 1, 1).toDate(), LocalDate.class,
                                                       Locale.getDefault());

        assertThat(localDate.getYear(), is(2000));
    }

    @Test
    public void testPivotYear() {
        int pivotYear = new LocalDate().minusYears(80).getYearOfCentury();
        LocalDateToDateConverter converter = new LocalDateToDateConverter();
        LocalDate localDate = converter.convertToModel(new LocalDate(pivotYear, 1, 1).toDate(), LocalDate.class,
                                                       Locale.getDefault());

        assertThat(localDate.getYear(), is(new LocalDate().minusYears(80).getYear()));
    }

    @Test
    public void testBelowPivotYear() {
        int pivotYearMinusOne = new LocalDate().minusYears(80).getYearOfCentury() - 1;
        LocalDateToDateConverter converter = new LocalDateToDateConverter();
        LocalDate localDate = converter.convertToModel(new LocalDate(pivotYearMinusOne, 1, 1).toDate(), LocalDate.class,
                                                       Locale.getDefault());

        assertThat(localDate.getYear(), is(new LocalDate().plusYears(19).getYear()));
    }

    // will fail in around 63 years from now
    @Test
    public void testYearPlus1900() {
        LocalDateToDateConverter converter = new LocalDateToDateConverter();
        LocalDate localDate = converter.convertToModel(new LocalDate(99, 1, 1).toDate(), LocalDate.class,
                                                       Locale.getDefault());

        assertThat(localDate.getYear(), is(1999));
    }

    // construct a date without hour, min, second set
    private Date getDate() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2000, 5, 13);
        return cal.getTime();
    }

}
