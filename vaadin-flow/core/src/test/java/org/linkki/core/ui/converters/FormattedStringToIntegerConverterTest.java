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

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vaadin.flow.data.binder.ValueContext;

class FormattedStringToIntegerConverterTest {

    private FormattedStringToIntegerConverter converter;
    private ValueContext context;

    @BeforeEach
    void setup() {
        converter = new FormattedStringToIntegerConverter("#,##0");
        context = new ValueContext(Locale.GERMAN);
    }

    @Test
    void testConvertToPresentation() {
        assertThat(converter.convertToPresentation(12345, context)).isEqualTo("12.345");
    }

    @Test
    void testConvertToPresentation_Null() {
        assertThat(converter.convertToPresentation(null, context)).isEmpty();
    }

    @Test
    void testConvertToModel() {
        assertThat(converter.convertToModel("1.234", context).getOrThrow(AssertionError::new)).isEqualTo(1234);
    }

    @Test
    void testConvertToModel_Null() {
        assertThat(converter.convertToModel("", context).getOrThrow(AssertionError::new)).isNull();
    }

    @Test
    void testConvertToModel_Overflow() {
        assertThat(converter.convertToModel(String.valueOf((double)Integer.MAX_VALUE + 1), context).isError()).isTrue();
    }

    @Test
    void testConvertToModel_NumberFormattedExceptionWithVeryLargeNumber() {
        assertThat(converter.convertToModel(
                  "4325654138592074895617348926748392789" +
                        "4325654138592074895617348926748392789" +
                        "4325654138592074895617348926748392789" +
                        "4325654138592074895617348926748392789" +
                        "4325654138592074895617348926748392789" +
                        "4325654138592074895617348926748392789" +
                        "4325654138592074895617348926748392789" +
                        "4325654138592074895617348926748392789" +
                        "4325654138592074895617348926748392789",
                context)
                .isError())
                .isTrue();
    }

    @Test
    void testConvertToModel_ParseExceptionWithVeryLargeNumber() {
        assertThat(converter.convertToModel("no-number-input", context).isError()).isTrue();
    }

}
