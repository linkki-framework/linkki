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
package org.linkki.util;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static java.util.Locale.GERMANY;
import static java.util.Locale.US;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

public class DateFormatRegistryTest {

    private static final Locale AUSTRIA = new Locale(Locale.GERMAN.getLanguage(), "AT");

    @Test
    public void testGetPattern() {
        DateFormatRegistry registry = new DateFormatRegistry();
        assertThat(registry.getPattern(GERMAN), is(DateFormatRegistry.PATTERN_DE));
        assertThat(registry.getPattern(GERMANY), is(DateFormatRegistry.PATTERN_DE));
        assertThat(registry.getPattern(AUSTRIA), is(DateFormatRegistry.PATTERN_DE));

        assertThat(registry.getPattern(ENGLISH), is(not(nullValue())));
        assertThat(registry.getPattern(ENGLISH), is(not(DateFormatRegistry.PATTERN_DE)));
        assertThat(registry.getPattern(ENGLISH), is(not(DateFormatRegistry.PATTERN_ISO)));

        assertThat(registry.getPattern(US), is(not(nullValue())));
        assertThat(registry.getPattern(US), is(not(DateFormatRegistry.PATTERN_DE)));
        assertThat(registry.getPattern(US), is(not(DateFormatRegistry.PATTERN_ISO)));

    }

}
