/*******************************************************************************
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 *
 * All Rights Reserved - Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.ips.decimalfield;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import org.faktorips.values.Decimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.linkki.ips.decimalfield.FormattedDecimalFieldToStringConverter;

import com.vaadin.data.ValueContext;

@RunWith(value = Parameterized.class)
public class FormattedDecimalFieldToStringConverterTest {

    @Parameter(value = 0)
    public Decimal decimalValue;

    @Parameter(value = 1)
    public String stringValue;

    private FormattedDecimalFieldToStringConverter converter = new FormattedDecimalFieldToStringConverter("#,##0.00##");

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { Decimal.valueOf(123888383838888.0), "123.888.383.838.888,00" },
                { Decimal.valueOf(0), "0,00" },
                { Decimal.valueOf(123.45), "123,45" },
                { Decimal.NULL, "" },
                { Decimal.valueOf(17385.89), "17.385,89" }
        });
    }

    @Test
    public void testConvertToPresentation() {
        ValueContext context = new ValueContext(Locale.GERMAN);

        String numberString = converter.convertToPresentation(decimalValue, context);
        assertThat(numberString, is(stringValue));
    }

    @Test
    public void testConvertToModel() {
        ValueContext context = new ValueContext(Locale.GERMAN);
        assertThat(converter.convertToModel(stringValue, context).getOrThrow(s -> new AssertionError(s)),
                   is(decimalValue));
    }

    /**
     * Special case only for convertToModel
     */
    @Test
    public void testConvertToModelWithoutSeparators() {
        assertThat(converter.convertToModel("17385,89", new ValueContext(Locale.GERMAN))
                .getOrThrow(s -> new AssertionError(s)), is(Decimal.valueOf(17385.89)));
    }

    /**
     * Special case only for convertToModel
     */
    @Test
    public void testConvertToModelWithNull() {
        assertThat(converter.convertToModel(null, new ValueContext())
                .getOrThrow(s -> new AssertionError(s)), is(Decimal.NULL));
    }

    /**
     * Special case only for convertToPresentation
     */
    @Test
    public void testConvertToPresentationWithNull() {
        assertThat(converter.convertToPresentation(null, new ValueContext()), is(""));
    }

}
