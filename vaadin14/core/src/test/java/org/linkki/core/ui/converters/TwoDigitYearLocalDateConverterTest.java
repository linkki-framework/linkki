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

package org.linkki.core.ui.converters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDate;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.linkki.util.TwoDigitYearUtil;

import com.vaadin.flow.data.binder.ValueContext;

public class TwoDigitYearLocalDateConverterTest {

    private TwoDigitYearLocalDateConverter converter = new TwoDigitYearLocalDateConverter();
    private ValueContext context = new ValueContext(Locale.GERMAN);

    @Test
    public void testConvertToModel_FourDigitYear_NoConversion() {
        assertThat((converter.convertToModel(LocalDate.of(2039, 12, 31), context))
                .getOrThrow(s -> new AssertionError(s)), is(LocalDate.of(2039, 12, 31)));
    }

    @Test
    public void testConvertToModel_NullValue() {
        assertNull((converter.convertToModel(null, context))
                .getOrThrow(s -> new AssertionError(s)));
    }

    @Test
    public void testConvertToModel_MaxTwoDigitYear() {
        LocalDate date = LocalDate.of(39, 12, 31);
        assertThat((converter.convertToModel(date, context))
                .getOrThrow(s -> new AssertionError(s)), is(TwoDigitYearUtil.convert(date)));
    }

    @Test
    public void testConvertToPresentation_FourDigitYear_NoConversion() {
        assertThat((converter.convertToPresentation(LocalDate.of(2011, 9, 5), context)), is(LocalDate.of(2011, 9, 5)));
    }

    
    @Test
    public void testConvertToPresentation_NullValue() {
        assertNull(converter.convertToPresentation(null, context));
    }

    @Test
    public void testConvertToPresentation_ThreeDigitYear_NoConversion() {
        assertThat((converter.convertToPresentation(LocalDate.of(312, 5, 1), context)), is(LocalDate.of(312, 5, 1)));
    }

    @Test
    public void testConvertToPresentation_TwoDigitYear_NoConversion() {
        assertThat((converter.convertToPresentation(LocalDate.of(10, 3, 21), context)), is(LocalDate.of(10, 3, 21)));
    }

}
