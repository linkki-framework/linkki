/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.converters;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class LocalDateToDateConverterTest {

    @Parameterized.Parameter(value = 0)
    public String date;

    @Parameterized.Parameter(value = 1)
    public String pattern;

    @Parameterized.Parameter(value = 2)
    public LocalDate localDate;


    @Parameterized.Parameters
    public static Object[][] parameters() {

        return new Object[][] {
                /* [0] */ { null, "yy.MM.dd", null },
                /* [1] */ { "2016.12.29", "yyyy.MM.dd", LocalDate.of(2016, 12, 29) },
                /* [2] */ { "16.12.29", "yy.MM.dd", LocalDate.of(2016, 12, 29) },
                /* [3] */ { "0016.12.29", "yyyy.MM.dd", LocalDate.of(2016, 12, 29) },
                /* [4] */ { "0036.12.29", "yyyy.MM.dd", LocalDate.of(2036, 12, 29) },
                /* [5] */ { "36.12.29", "yy.MM.dd", LocalDate.of(2036, 12, 29) },
                /* [6] */ { "0037.12.29", "yyyy.MM.dd", LocalDate.of(1937, 12, 29) },
                /* [7] */ { "84.12.29", "yy.MM.dd", LocalDate.of(1984, 12, 29) }
        };
    }


    @Test
    public void testConvertToModel() throws Exception {
        LocalDate converted = convert(date, pattern);
        assertThat(converted, is(localDate));
    }

    @Test
    public void testConvertToPresentation() throws Exception {
        Date converted = convert(localDate);

        assertThat(converted, is(convertToDate(getDateToConvert(date, pattern), pattern)));
    }


    private static LocalDate convert(String toConvert, String pattern) throws Exception {
        // we do not need type and locale
        return new LocalDateToDateConverter().convertToModel(convertToDate(toConvert, pattern), null, null);
    }

    private static Date convert(LocalDate toConvert) {
        return new LocalDateToDateConverter().convertToPresentation(toConvert, null, null);
    }

    private static Date convertToDate(String date, String pattern) throws Exception {
        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(pattern).parse(date);
    }

    /**
     * we fake the java heuristic for 2 digit years in dates currently it works like a charm but who
     * knows - sometime it will be broken
     */
    private static String getDateToConvert(String date, String pattern) throws Exception {

        if (date == null || !date.startsWith("00")) {
            return date;
        }

        Date fixedDate = new SimpleDateFormat("yy.MM.dd").parse(date.substring(2));
        return new SimpleDateFormat(pattern).format(fixedDate);
    }
}