/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.components;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.junit.Test;

public class DoubleFieldConverterTest {
    @Test
    public void testConvertToModel_toDouble() {
        DoubleConverter converter = new DoubleConverter();

        double dValue = converter.convert(double.class, "123888383838888");
        assertEquals(123888383838888.0, dValue, 1.0);
    }

    @Test
    public void testConvertToModel_toString() {
        DoubleConverter converter = new DoubleConverter();

        String numberString = converter.convert(String.class, 123888383838888.0);
        assertEquals("1.23888383838888E14", numberString);
    }

    @Test(expected = ConversionException.class)
    public void testConvertToModel_toDate() {
        DoubleConverter converter = new DoubleConverter();

        converter.convert(Date.class, 123888.0);
    }

}
