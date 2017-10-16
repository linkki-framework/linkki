/*
 * Copyright Faktor Zehn AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.core.ui.converters;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class JodaLocalDateToDateConverterTest {

    @Parameterized.Parameter(value = 0)
    @SuppressWarnings("null")
    public String date;

    @Parameterized.Parameter(value = 1)
    @SuppressWarnings("null")
    public String pattern;

    @Parameterized.Parameter(value = 2)
    @SuppressWarnings("null")
    public LocalDate localDate;


    @Parameterized.Parameters
    public static Object[][] parameters() {

        return new Object[][] {
                /* [0] */ { null, "yy.MM.dd", null },
                /* [1] */ { "2016.12.29", "yyyy.MM.dd", new LocalDate(2016, 12, 29) },
                /* [2] */ { "16.12.29", "yy.MM.dd", new LocalDate(2016, 12, 29) },
                /* [3] */ { "0016.12.29", "yyyy.MM.dd", new LocalDate(2016, 12, 29) },
                /* [4] */ { "0036.12.29", "yyyy.MM.dd", new LocalDate(2036, 12, 29) },
                /* [5] */ { "36.12.29", "yy.MM.dd", new LocalDate(2036, 12, 29) },
                /* [6] */ { "0037.12.29", "yyyy.MM.dd", new LocalDate(1937, 12, 29) },
                /* [7] */ { "84.12.29", "yy.MM.dd", new LocalDate(1984, 12, 29) }
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

    @CheckForNull
    private static LocalDate convert(String toConvert, String pattern) throws Exception {
        // we do not need type and locale
        return new JodaLocalDateToDateConverter().convertToModel(convertToDate(toConvert, pattern), null, null);
    }

    @CheckForNull
    private static Date convert(LocalDate toConvert) {
        return new JodaLocalDateToDateConverter().convertToPresentation(toConvert, null, null);
    }

    @CheckForNull
    private static Date convertToDate(@Nullable String date, String pattern) throws Exception {
        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(pattern).parse(date);
    }

    /**
     * we fake the java heuristic for 2 digit years in dates currently it works like a charm but who
     * knows - sometime it will be broken
     */
    @CheckForNull
    private static String getDateToConvert(@Nullable String date, String pattern) throws Exception {

        if (date == null || !date.startsWith("00")) {
            return date;
        }

        Date fixedDate = new SimpleDateFormat("yy.MM.dd").parse(date.substring(2));
        return new SimpleDateFormat(pattern).format(fixedDate);
    }
}