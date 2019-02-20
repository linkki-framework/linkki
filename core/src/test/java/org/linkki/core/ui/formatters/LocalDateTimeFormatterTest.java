/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.core.ui.formatters;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.oneOf;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.linkki.core.ui.TestUiFramework;

public class LocalDateTimeFormatterTest {

    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2019, Month.FEBRUARY, 20, 15, 28, 35);

    @SuppressWarnings("null")
    private Locale defaultLocale;

    @Test
    public void testFormat() {
        LocalDateTimeFormatter formatter = new LocalDateTimeFormatter();
        assertThat(formatter.format(DATE_TIME, Locale.GERMAN),
                   is("20.02.2019 15:28"));
        assertThat(formatter.format(DATE_TIME, Locale.GERMANY), is("20.02.2019 15:28"));
        assertThat(formatter.format(DATE_TIME, Locale.ENGLISH), is("2/20/19 3:28 PM"));
        // WTF? UK format switches with Java 11
        assertThat(formatter.format(DATE_TIME, Locale.UK), is(oneOf("20/02/19 15:28", "20/02/2019 15:28")));
        assertThat(formatter.format(DATE_TIME, Locale.US), is("2/20/19 3:28 PM"));
    }

    @Before
    public void rememberLocale() {
        defaultLocale = TestUiFramework.get().getLocale();
    }

    @After
    public void restoreLocale() {
        TestUiFramework.get().setUiLocale(defaultLocale);
    }

    @Test
    public void testFormat_LocaleFromUi() {
        LocalDateTimeFormatter formatter = new LocalDateTimeFormatter();
        TestUiFramework.get().setUiLocale(Locale.GERMAN);
        assertThat(formatter.format(DATE_TIME), is("20.02.2019 15:28"));
        TestUiFramework.get().setUiLocale(Locale.GERMANY);
        assertThat(formatter.format(DATE_TIME), is("20.02.2019 15:28"));
        TestUiFramework.get().setUiLocale(Locale.US);
        assertThat(formatter.format(DATE_TIME, Locale.US), is("2/20/19 3:28 PM"));
    }

}
