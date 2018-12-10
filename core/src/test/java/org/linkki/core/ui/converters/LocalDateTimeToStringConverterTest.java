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
package org.linkki.core.ui.converters;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.Locale;

import org.junit.Test;

public class LocalDateTimeToStringConverterTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testConvertToModel() {
        new LocalDateTimeToStringConverter().convertToModel(null, null, null);
    }

    @Test
    public void testConvertToPresentation_null_shouldReturnEmptyString() {
        String presentation = new LocalDateTimeToStringConverter().convertToPresentation(null, String.class, null);
        assertNotNull(presentation);
        assertThat(presentation, is(""));
    }

    @Test
    public void testConvertToPresentation_locale_de() {
        LocalDateTime dateTime = LocalDateTime.of(2016, 12, 29, 15, 30, 0, 555);
        String presentation = new LocalDateTimeToStringConverter().convertToPresentation(dateTime, null,
                                                                                         Locale.GERMANY);
        assertNotNull(presentation);
        assertThat(presentation, is("29.12.2016 15:30"));
    }

    @Test
    public void testConvertToPresentation_locale_() {
        LocalDateTime dateTime = LocalDateTime.of(2016, 12, 29, 15, 30, 0, 555);
        String presentation = new LocalDateTimeToStringConverter().convertToPresentation(dateTime, null, Locale.US);
        assertNotNull(presentation);
        assertThat(presentation, is("12/29/16 3:30 PM"));
    }

}