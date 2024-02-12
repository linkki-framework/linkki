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

import java.time.LocalDateTime;
import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;

class LocalDateTimeToStringConverterTest {

    @Test
    void testConvertToModel() throws Exception {
        LocalDateTimeToStringConverter converter = new LocalDateTimeToStringConverter();

        Result<LocalDateTime> result = converter.convertToModel("01.02.3456 07:08",
                                                                new ValueContext(Locale.GERMANY));

        Assertions.assertThat(result.isError()).isFalse();
        Assertions.assertThat(result.getOrThrow(m -> new Exception(m)))
                .isEqualTo(LocalDateTime.of(3456, 2, 1, 7, 8));
    }

    @Test
    void testConvertToModel_IncompatibleLocale() {
        LocalDateTimeToStringConverter converter = new LocalDateTimeToStringConverter();

        Result<LocalDateTime> result = converter.convertToModel("01.02.3456 07:08", new ValueContext(Locale.US));

        Assertions.assertThat(result.isError()).isTrue();
    }

    @Test
    void testConvertToModel_Null() throws Exception {
        LocalDateTimeToStringConverter converter = new LocalDateTimeToStringConverter();

        Result<LocalDateTime> result = converter.convertToModel(null, new ValueContext(Locale.US));

        Assertions.assertThat(result.isError()).isFalse();
        Assertions.assertThat(result.getOrThrow(m -> new Exception(m))).isNull();
    }

    @Test
    void testConvertToPresentation() {
        LocalDateTimeToStringConverter converter = new LocalDateTimeToStringConverter();
        LocalDateTime dateToConvert = LocalDateTime.of(1234, 5, 6, 7, 8, 9);

        Assertions.assertThat(converter.convertToPresentation(dateToConvert, new ValueContext(Locale.GERMANY)))
                .isEqualTo("06.05.1234 07:08");
        Assertions.assertThat(converter.convertToPresentation(dateToConvert, new ValueContext(Locale.GERMAN)))
                .isEqualTo("06.05.1234 07:08");
        Assertions.assertThat(converter.convertToPresentation(dateToConvert, new ValueContext(Locale.ENGLISH)))
                .isEqualTo("05/06/1234 7:08 AM");
        Assertions.assertThat(converter.convertToPresentation(dateToConvert, new ValueContext(Locale.UK)))
                .isEqualTo("06/05/1234 07:08");
        Assertions.assertThat(converter.convertToPresentation(dateToConvert, new ValueContext(Locale.US)))
                .isEqualTo("05/06/1234 7:08 AM");
    }

    @Test
    void testConvertToPresentation_Null() {
        LocalDateTimeToStringConverter converter = new LocalDateTimeToStringConverter();

        String string = converter.convertToPresentation(null, new ValueContext(Locale.GERMANY));

        Assertions.assertThat(string).isNull();
    }
}