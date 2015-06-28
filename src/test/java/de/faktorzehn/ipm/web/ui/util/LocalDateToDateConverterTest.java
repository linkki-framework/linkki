/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.LocalDate;
import org.junit.Test;

import de.faktorzehn.ipm.web.ui.converters.LocalDateToDateConverter;

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

    // construct a date without hour, min, second set
    private Date getDate() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2000, 5, 13);
        return cal.getTime();
    }
}
