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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;

class StringToGregorianCalendarConverterTest {

    private final StringToGregorianCalendarConverter converter = new StringToGregorianCalendarConverter();
    private final ValueContext germanyContext = new ValueContext(Locale.GERMANY);

    @Test
    void testConvertToModel_Null() {
        assertThat(converter.convertToModel(null, germanyContext)).isEqualTo(Result.ok(null));
    }

    @Test
    void testConvertToModel_InvalidFormat() {
        assertThat(converter.convertToModel("asdf", germanyContext).isError()).isTrue();
    }

    @Test
    void testConvertToModel_de() {
        var result = converter.convertToModel("24.12.2023", germanyContext);

        assertThat(result.isError()).isFalse();
        var value = result.getOrThrow(IllegalStateException::new);
        assertThat(value.get(Calendar.YEAR)).isEqualTo(2023);
        assertThat(value.get(Calendar.MONTH)).isEqualTo(Calendar.DECEMBER);
        assertThat(value.get(Calendar.DAY_OF_MONTH)).isEqualTo(24);
    }

    @Test
    void testConvertToModel_en_UK() {
        ValueContext context = new ValueContext(Locale.UK);

        var result = converter.convertToModel("24/12/2023", context);

        assertThat(result.isError()).isFalse();
        var value = result.getOrThrow(IllegalStateException::new);
        assertThat(value.get(Calendar.YEAR)).isEqualTo(2023);
        assertThat(value.get(Calendar.MONTH)).isEqualTo(Calendar.DECEMBER);
        assertThat(value.get(Calendar.DAY_OF_MONTH)).isEqualTo(24);
    }

    @Test
    void testConvertToModel_en_US() {
        ValueContext context = new ValueContext(Locale.US);

        var result = converter.convertToModel("12/24/2023", context);

        assertThat(result.isError()).isFalse();
        var value = result.getOrThrow(IllegalStateException::new);
        assertThat(value.get(Calendar.YEAR)).isEqualTo(2023);
        assertThat(value.get(Calendar.MONTH)).isEqualTo(Calendar.DECEMBER);
        assertThat(value.get(Calendar.DAY_OF_MONTH)).isEqualTo(24);
    }

    @Test
    void testConvertToPresentation_Null() {
        assertThat(converter.convertToPresentation(null, null)).isNull();
    }

    @Test
    void testConvertToPresentation_de() {
        ValueContext context = new ValueContext(Locale.GERMANY);
        GregorianCalendar gregorianCalendar = new GregorianCalendar(2023, Calendar.DECEMBER, 24);

        assertThat(converter.convertToPresentation(gregorianCalendar, context)).isEqualTo("24.12.2023");
    }

    @Test
    void testConvertToPresentation_en_UK() {
        ValueContext context = new ValueContext(Locale.UK);
        GregorianCalendar gregorianCalendar = new GregorianCalendar(2023, Calendar.DECEMBER, 24);

        assertThat(converter.convertToPresentation(gregorianCalendar, context)).isEqualTo("24/12/2023");
    }

    @Test
    void testConvertToPresentation_en_US() {
        ValueContext context = new ValueContext(Locale.US);
        GregorianCalendar gregorianCalendar = new GregorianCalendar(2023, Calendar.DECEMBER, 24);

        assertThat(converter.convertToPresentation(gregorianCalendar, context)).isEqualTo("12/24/2023");
    }
}
