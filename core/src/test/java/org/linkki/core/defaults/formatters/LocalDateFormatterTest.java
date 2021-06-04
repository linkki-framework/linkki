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

package org.linkki.core.defaults.formatters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.uiframework.TestUiFramework;

public class LocalDateFormatterTest {

    private static final LocalDate DATE_2019_02_20 = LocalDate.of(2019, Month.FEBRUARY, 20);


    private Locale defaultLocale;

    @Test
    public void testFormat() {
        LocalDateFormatter formatter = new LocalDateFormatter();
        assertThat(formatter.format(DATE_2019_02_20, Locale.GERMAN), is("20.02.2019"));
        assertThat(formatter.format(DATE_2019_02_20, Locale.GERMANY), is("20.02.2019"));
        assertThat(formatter.format(DATE_2019_02_20, Locale.ENGLISH), is("02/20/2019"));
        assertThat(formatter.format(DATE_2019_02_20, Locale.UK), is("02/20/2019"));
        assertThat(formatter.format(DATE_2019_02_20, Locale.US), is("02/20/2019"));
    }

    @BeforeEach
    public void rememberLocale() {
        defaultLocale = TestUiFramework.get().getLocale();
    }

    @AfterEach
    public void restoreLocale() {
        TestUiFramework.get().setUiLocale(defaultLocale);
    }

    @Test
    public void testFormat_LocaleFromUi() {
        LocalDateFormatter formatter = new LocalDateFormatter();
        TestUiFramework.get().setUiLocale(Locale.GERMAN);
        assertThat(formatter.format(DATE_2019_02_20), is("20.02.2019"));
        TestUiFramework.get().setUiLocale(Locale.GERMANY);
        assertThat(formatter.format(DATE_2019_02_20), is("20.02.2019"));
        TestUiFramework.get().setUiLocale(Locale.US);
        assertThat(formatter.format(DATE_2019_02_20), is("02/20/2019"));
    }

}
