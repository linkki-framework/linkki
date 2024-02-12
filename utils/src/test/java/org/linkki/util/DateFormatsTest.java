/*
 * Copyright Faktor Zehn GmbH.
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
package org.linkki.util;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static java.util.Locale.GERMANY;
import static java.util.Locale.UK;
import static java.util.Locale.US;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.Locale;

import org.junit.jupiter.api.Test;

public class DateFormatsTest {

    private static final Locale AUSTRIA = new Locale(Locale.GERMAN.getLanguage(), "AT");

    @Test
    public void testGetPattern_German() {
        assertThat(DateFormats.getPattern(GERMAN), is("dd.MM.yyyy"));
        assertThat(DateFormats.getPattern(GERMANY), is("dd.MM.yyyy"));
        assertThat(DateFormats.getPattern(AUSTRIA), is("dd.MM.yyyy"));
    }

    @Test
    public void testGetPattern_English() {
        assertThat(DateFormats.getPattern(ENGLISH), is("MM/dd/yyyy"));
    }

    @Test
    public void testGetPattern_US() {
        assertThat(DateFormats.getPattern(US), is("MM/dd/yyyy"));
    }

    @Test
    public void testGetPattern_UK() {
        assertThat(DateFormats.getPattern(UK), is("dd/MM/yyyy"));
    }

    @Test
    public void testGetPattern_ISOFallback() {
        assertThat(DateFormats.getPattern(new Locale("unknown language")), is(DateFormats.PATTERN_ISO));
    }

    @Test
    public void testRegister_Language() {
        String customPattern = "yyMMdd";

        DateFormats.register("foo-language", customPattern);

        assertThat(DateFormats.getPattern(new Locale("foo-language", "country1")), is(customPattern));
        assertThat(DateFormats.getPattern(new Locale("foo-language", "country2")), is(customPattern));
    }

    @Test
    public void testRegister_Locale() {
        String customPattern = "yy:MM:dd";
        Locale locale = new Locale("bar-language", "country1");

        DateFormats.register(locale, customPattern);

        assertThat(DateFormats.getPattern(new Locale("bar-language", "country1")), is(customPattern));
        assertThat(DateFormats.getPattern(new Locale("bar-language", "country2")), is(not(customPattern)));
        assertThat(DateFormats.getPattern(new Locale("bar-language")), is(not(customPattern)));
    }

}
