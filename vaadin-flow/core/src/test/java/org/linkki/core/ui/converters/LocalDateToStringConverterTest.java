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

import java.time.LocalDate;
import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;

class LocalDateToStringConverterTest {

    @Test
    void testConvertToModel() throws Exception {
        LocalDateToStringConverter converter = new LocalDateToStringConverter();

        Result<LocalDate> result =
                converter.convertToModel("01.02.3456", new ValueContext(new Binder<>(), Locale.GERMANY));

        Assertions.assertThat(result.isError()).isFalse();
        Assertions.assertThat(result.getOrThrow(m -> new Exception(m))).isEqualTo(LocalDate.of(3456, 2, 1));
    }

    @Test
    void testConvertToModel_IncompatibleLocale() {
        LocalDateToStringConverter converter = new LocalDateToStringConverter();

        Result<LocalDate> result = converter.convertToModel("01.02.3456", new ValueContext(new Binder<>(), Locale.US));

        Assertions.assertThat(result.isError()).isTrue();
    }

    @Test
    void testConvertToModel_Null() throws Exception {
        LocalDateToStringConverter converter = new LocalDateToStringConverter();

        Result<LocalDate> result = converter.convertToModel(null, new ValueContext(new Binder<>(), Locale.US));

        Assertions.assertThat(result.isError()).isFalse();
        Assertions.assertThat(result.getOrThrow(m -> new Exception(m))).isNull();
    }

    @Test
    void testConvertToPresentation() {
        LocalDateToStringConverter converter = new LocalDateToStringConverter();
        LocalDate dateToConvert = LocalDate.of(1234, 5, 6);

        Assertions
                .assertThat(converter.convertToPresentation(dateToConvert,
                                                            new ValueContext(new Binder<>(), Locale.GERMANY)))
                .isEqualTo("06.05.1234");
        Assertions
                .assertThat(converter.convertToPresentation(dateToConvert,
                                                            new ValueContext(new Binder<>(), Locale.GERMAN)))
                .isEqualTo("06.05.1234");
        Assertions
                .assertThat(converter.convertToPresentation(dateToConvert,
                                                            new ValueContext(new Binder<>(), Locale.ENGLISH)))
                .isEqualTo("05/06/1234");
        Assertions
                .assertThat(converter.convertToPresentation(dateToConvert, new ValueContext(new Binder<>(), Locale.UK)))
                .isEqualTo("06/05/1234");
        Assertions
                .assertThat(converter.convertToPresentation(dateToConvert, new ValueContext(new Binder<>(), Locale.US)))
                .isEqualTo("05/06/1234");
    }

    @Test
    void testConvertToPresentation_Null() {
        LocalDateToStringConverter converter = new LocalDateToStringConverter();

        String string = converter.convertToPresentation(null, new ValueContext(new Binder<>(), Locale.GERMANY));

        Assertions.assertThat(string).isNull();
    }
}