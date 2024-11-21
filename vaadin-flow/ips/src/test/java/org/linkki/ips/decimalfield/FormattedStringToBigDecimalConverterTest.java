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
package org.linkki.ips.decimalfield;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import org.faktorips.values.Decimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValueContext;

public class FormattedStringToBigDecimalConverterTest {

    private final FormattedStringToBigDecimalConverter converter =
            new FormattedStringToBigDecimalConverter("#,#0.00");

    static Collection<Object[]> dataStringToBigDecimal() {
        return Arrays.asList(new Object[][] {

                { BigDecimal.ZERO, "0.00" },

                { BigDecimal.valueOf(1234.56), "1.234,56" },

                { BigDecimal.valueOf(123.45000).setScale(5), "000123,45000" },

                // input outside the range of double should not lead to precision loss
                { new BigDecimal(
                        "12345678901234567890123456789012345678901234567890" +
                                "1234567890123456789012345678901234567890.12345678901234567890"),

                        "12345678901234567890123456789012345678901234567890" +
                                "1234567890123456789012345678901234567890,12345678901234567890" },

                { new BigDecimal(
                        "-12345678901234567890123456789012345678901234567890" +
                                "1234567890123456789012345678901234567890.12345678901234567890"),

                        "-12345678901234567890123456789012345678901234567890" +
                                "1234567890123456789012345678901234567890,12345678901234567890" },
        });
    }

    static Collection<Object[]> dataBigDecimalToString() {
        return Arrays.asList(new Object[][] {

                { BigDecimal.valueOf(1.015), "1,02" },

                { BigDecimal.ZERO, "0,00" },

                { BigDecimal.valueOf(123.450), "1.23,45" },

                // input outside the range of double should not lead to precision loss
                { new BigDecimal(
                        "12345678901234567890123456789012345678901234567890" +
                                "1234567890123456789012345678901234567890.12345678901234567890"),

                        "12.34.56.78.90.12.34.56.78.90.12.34.56.78.90.12.34.56.78.90.12.34.56.78.90." +
                                "12.34.56.78.90.12.34.56.78.90.12.34.56.78.90.12.34.56.78.90,12" },

                { new BigDecimal(
                        "-12345678901234567890123456789012345678901234567890" +
                                "1234567890123456789012345678901234567890.12345678901234567890"),

                        "-12.34.56.78.90.12.34.56.78.90.12.34.56.78.90.12.34.56.78.90.12.34.56.78.90." +
                                "12.34.56.78.90.12.34.56.78.90.12.34.56.78.90.12.34.56.78.90,12" },
        });
    }

    @ParameterizedTest
    @MethodSource("dataBigDecimalToString")
    void testConvertToPresentation(BigDecimal bigDecimalValue, String stringValue) {
        assertThat(converter.convertToPresentation(bigDecimalValue, new ValueContext(new Binder<>(), Locale.GERMAN)))
                .isEqualTo(stringValue);
    }

    @ParameterizedTest
    @MethodSource("dataStringToBigDecimal")
    void testConvertToModel(BigDecimal bigDecimalValue, String stringValue) {
        assertThat(converter.convertToModel(stringValue, new ValueContext(new Binder<>(), Locale.GERMAN))
                .getOrThrow(AssertionError::new))
                        .isEqualTo(bigDecimalValue);
    }

    /**
     * Special case only for convertToModel
     */
    @Test
    void testConvertToModelWithNull() {
        assertThat(converter.convertToModel(null, new ValueContext(new Binder<>(), Locale.GERMAN))
                .getOrThrow(AssertionError::new)).isNull();
    }

    /**
     * Special case only for convertToPresentation
     */
    @Test
    void testConvertToPresentationWithNull() {
        assertThat(converter.convertToPresentation(null, new ValueContext(new Binder<>(), Locale.GERMAN))).isBlank();
    }

    /**
     * Special case only for convertToModel with no BigDecimal Type
     */
    @Test
    void testConvertToPresentationWithDecimalTypeAsInput() {
        var defaultConverter = new FormattedStringToBigDecimalConverter();
        assertThat(defaultConverter.convertToModel(Decimal.valueOf("123456789")).getOrThrow(AssertionError::new))
                .isEqualTo(BigDecimal.valueOf(123456789));
    }
}
