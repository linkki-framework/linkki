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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValueContext;

public class TwoDigitYearLocalDateTimeConverterTest {

    private final TwoDigitYearLocalDateTimeConverter converter = new TwoDigitYearLocalDateTimeConverter();
    private final ValueContext context = new ValueContext(new Binder<>(), Locale.GERMAN);

    @Test
    public void testConvertToModel_FourDigitYear_NoConversion() {
        LocalDateTime dateTime = LocalDateTime.of(2039, 12, 31, 0, 0);
        assertThat((converter.convertToModel(dateTime, context))
                .getOrThrow(s -> new AssertionError(s)),
                   is(LocalDateTime.of(2039, 12, 31, 0, 0)));
    }

    @Test
    public void testConvertToModel_NullValue() {
        assertNull((converter.convertToModel(null, context))
                .getOrThrow(s -> new AssertionError(s)));
    }

    @Test
    public void testConvertToModel_MaxTwoDigitYear() {
        LocalDateTime dateTime = LocalDateTime.of(39, 12, 31, 0, 0);
        assertThat((converter.convertToModel(dateTime, context))
                .getOrThrow(s -> new AssertionError(s)),
                   is(LocalDateTime.of(2039, 12, 31, 0, 0)));
    }

    @Test
    public void testConvertToPresentation_FourDigitYear_NoConversion() {
        LocalDateTime dateTime = LocalDateTime.of(2011, 9, 5, 0, 0);
        assertThat((converter.convertToPresentation(dateTime, context)),
                   is(LocalDateTime.of(2011, 9, 5, 0, 0)));
    }

    @Test
    public void testConvertToPresentation_NullValue() {
        assertNull(converter.convertToPresentation(null, context));
    }

    @Test
    public void testConvertToPresentation_ThreeDigitYear_NoConversion() {
        LocalDateTime dateTime = LocalDateTime.of(312, 5, 1, 0, 0);
        assertThat((converter.convertToPresentation(dateTime, context)),
                   is(LocalDateTime.of(312, 5, 1, 0, 0)));
    }

    @Test
    public void testConvertToPresentation_TwoDigitYear_NoConversion() {
        LocalDateTime dateTime = LocalDateTime.of(10, 3, 21, 0, 0);
        assertThat((converter.convertToPresentation(dateTime, context)),
                   is(LocalDateTime.of(10, 3, 21, 0, 0)));
    }

}
