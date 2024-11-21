/*******************************************************************************
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 *
 * All Rights Reserved - Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.ips.decimalfield;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.faktorips.values.Decimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValueContext;

class FormattedStringToDecimalConverterTest {

    private final FormattedStringToDecimalConverter converter = new FormattedStringToDecimalConverter("#,###0.0000#");

    static Collection<Object[]> dataStringToDecimal() {
        return Arrays.asList(new Object[][] {
                { Decimal.valueOf(123888383838888.0), "123.8883.8383.8888,0000" },

                { Decimal.valueOf(0), "0,0000" },

                { Decimal.valueOf(123.45), "123,4500" },

                { Decimal.NULL, "" },

                { Decimal.valueOf(17385.89), "1.7385,8900" },

                // input outside the range of double should not lead to precision loss
                { Decimal.valueOf("12345678901234567890123456789012345678901234567890" +
                        "1234567890123456789012345678901234567890.12345678901234567890"),

                        "12345678901234567890123456789012345678901234567890" +
                                "1234567890123456789012345678901234567890,12345678901234567890" },

                { Decimal.valueOf("-12345678901234567890123456789012345678901234567890" +
                        "1234567890123456789012345678901234567890.12345678901234567890"),

                        "-12345678901234567890123456789012345678901234567890" +
                                "1234567890123456789012345678901234567890,12345678901234567890" },
        });
    }

    static Collection<Object[]> dataDecimalToString() {
        return Arrays.asList(new Object[][] {
                { Decimal.valueOf(123888383838888.0), "123.8883.8383.8888,0000" },

                { Decimal.valueOf(0), "0,0000" },

                { Decimal.valueOf(123.45), "123,4500" },

                { Decimal.NULL, "" },

                { Decimal.valueOf(17385.89), "1.7385,8900" },

                // input outside the range of double should not lead to precision loss
                { Decimal.valueOf("12345678901234567890123456789012345678901234567890" +
                        "1234567890123456789012345678901234567890.12345678901234567890"),

                        "12.3456.7890.1234.5678.9012.3456.7890.1234.5678.9012.3456.7890" +
                                ".1234.5678.9012.3456.7890.1234.5678.9012.3456.7890,12346" },

                { Decimal.valueOf("-12345678901234567890123456789012345678901234567890" +
                        "1234567890123456789012345678901234567890.12345678901234567890"),

                        "-12.3456.7890.1234.5678.9012.3456.7890.1234.5678.9012.3456.7890" +
                                ".1234.5678.9012.3456.7890.1234.5678.9012.3456.7890,12346" },
        });
    }

    @ParameterizedTest
    @MethodSource("dataDecimalToString")
    void testConvertToPresentation(Decimal decimalValue, String stringValue) {
        ValueContext context = new ValueContext(new Binder<>(), Locale.GERMAN);

        String numberString = converter.convertToPresentation(decimalValue, context);
        Assertions.assertThat(numberString).isEqualTo(stringValue);
    }

    @ParameterizedTest
    @MethodSource("dataStringToDecimal")
    void testConvertToModel(Decimal decimalValue, String stringValue) {
        ValueContext context = new ValueContext(new Binder<>(), Locale.GERMAN);
        assertThat(converter.convertToModel(stringValue, context).getOrThrow(AssertionError::new))
                .isEqualTo(decimalValue);
    }

    /**
     * Special case only for convertToModel
     */
    @Test
    void testConvertToModelWithoutSeparators() {
        assertThat(converter.convertToModel("17385,89", new ValueContext(new Binder<>(), Locale.GERMAN))
                .getOrThrow(AssertionError::new)).isEqualTo(Decimal.valueOf(17385.89));
    }

    /**
     * Special case only for convertToModel
     */
    @Test
    void testConvertToModelWithNull() {
        assertThat(converter.convertToModel(null, new ValueContext(mock(Binder.class)))
                .getOrThrow(AssertionError::new)).isEqualTo(Decimal.NULL);
    }

    /**
     * Special case only for convertToPresentation
     */
    @Test
    void testConvertToPresentationWithNull() {
        assertThat(converter.convertToPresentation(null, new ValueContext(mock(Binder.class)))).isBlank();
    }

    /**
     * Test for default converter
     */
    @Test
    void testConvertWithDefaultFormat() {
        FormattedStringToDecimalConverter defaultConverter = new FormattedStringToDecimalConverter();
        assertThat(defaultConverter.convertToPresentation(Decimal.valueOf(1234567890),
                                                          new ValueContext(new Binder<>(), Locale.GERMAN)))
                .isEqualTo("1.234.567.890,00");
        assertThat(defaultConverter.convertToModel("17.385,89", new ValueContext(new Binder<>(), Locale.GERMAN))
                .getOrThrow(AssertionError::new)).isEqualTo(Decimal.valueOf(17385.89));
    }

}
