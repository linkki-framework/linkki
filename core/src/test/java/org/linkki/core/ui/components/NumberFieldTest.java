/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.components;

import static org.junit.Assert.assertNotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Test;
import org.linkki.core.ui.components.NumberField;

public class NumberFieldTest {

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("unused")
    // warning suppressed as object is created to test the constructor, not to use it
    public void testConstructorException() {
        new TestNumberField(null);
    }

    @Test
    public void testConstructor() {
        TestNumberField field = new TestNumberField(DecimalFormat.getCurrencyInstance());
        assertNotNull(field.getFormat());
        assertNotNull(field.getConversionError());
    }

    private class TestNumberField extends NumberField {
        private static final long serialVersionUID = 1L;

        public TestNumberField(NumberFormat format) {
            super(format);
        }
    }

}
