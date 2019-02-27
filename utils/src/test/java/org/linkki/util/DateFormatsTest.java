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
package org.linkki.util;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static java.util.Locale.GERMANY;
import static java.util.Locale.UK;
import static java.util.Locale.US;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.oneOf;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

public class DateFormatsTest {

    private static final Locale AUSTRIA = new Locale(Locale.GERMAN.getLanguage(), "AT");

    @Test
    public void testGetPattern() {
        assertThat(DateFormats.getPattern(GERMAN), is(DateFormats.PATTERN_DE));
        assertThat(DateFormats.getPattern(GERMANY), is(DateFormats.PATTERN_DE));
        assertThat(DateFormats.getPattern(AUSTRIA), is(DateFormats.PATTERN_DE));

        assertThat(DateFormats.getPattern(ENGLISH), is(not(nullValue())));
        assertThat(DateFormats.getPattern(ENGLISH), is(not(DateFormats.PATTERN_DE)));
        assertThat(DateFormats.getPattern(ENGLISH), is(not(DateFormats.PATTERN_ISO)));
        assertThat(DateFormats.getPattern(ENGLISH), is("M/d/yy"));

        assertThat(DateFormats.getPattern(US), is(not(nullValue())));
        assertThat(DateFormats.getPattern(US), is(not(DateFormats.PATTERN_DE)));
        assertThat(DateFormats.getPattern(US), is(not(DateFormats.PATTERN_ISO)));
        assertThat(DateFormats.getPattern(US), is("M/d/yy"));
        // WTF? UK format switches with Java 11
        assertThat(DateFormats.getPattern(UK), is(oneOf("dd/MM/yy", "dd/MM/y")));
    }

    @Test
    public void testRegister() {
        String customPattern = "yyMMdd";
        DateFormats.register("foobar", customPattern);

        assertThat(DateFormats.getPattern(new Locale("foobar")), is(customPattern));
    }

}
