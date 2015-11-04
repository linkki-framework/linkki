/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.components;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEmptyString.emptyString;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.faktorips.values.Decimal;
import org.junit.Test;
import org.linkki.core.ui.components.DecimalFieldConverter;

public class DecimalFieldConverterTest {

    @Test
    public void testConvertToPresentation_toDecimal() {
        // arrange
        DecimalFieldConverter converter = new DecimalFieldConverter(NumberFormat.getNumberInstance(Locale.GERMAN));

        // act
        String number1 = converter.convertToPresentation(Decimal.valueOf(123888383838888.0), String.class,
                                                         Locale.GERMAN);
        String number2 = converter.convertToPresentation(Decimal.valueOf(0), String.class, Locale.GERMAN);
        String number3 = converter.convertToPresentation(Decimal.valueOf(123.45), String.class, Locale.GERMAN);

        // assert
        assertThat(number1, is("123.888.383.838.888"));
        assertThat(number2, is("0"));
        assertThat(number3, is("123,45"));
    }

    @Test
    public void testConvertToPresentation_DecimalNULL() {
        // arrange
        DecimalFieldConverter converter = new DecimalFieldConverter(DecimalFormat.getNumberInstance(Locale.GERMAN));

        // act
        String number = converter.convertToPresentation(Decimal.NULL, String.class, Locale.GERMAN);

        // assert
        assertThat(number, is(emptyString()));
    }

    @Test
    public void testConvertToPresentation_null() {
        // arrange
        DecimalFieldConverter converter = new DecimalFieldConverter(DecimalFormat.getNumberInstance(Locale.GERMAN));

        // act
        String number = converter.convertToPresentation(null, String.class, null);

        // assert
        assertThat(number, is(emptyString()));
    }

    @Test
    public void testConvertToModel() {
        // arrange
        DecimalFieldConverter converter = new DecimalFieldConverter(DecimalFormat.getNumberInstance(Locale.GERMAN));

        // act
        Decimal modelAtr = converter.convertToModel("17385,89", Decimal.class, Locale.GERMAN);

        // assert
        assertThat(modelAtr, is(Decimal.valueOf(17385.89)));
    }
}
