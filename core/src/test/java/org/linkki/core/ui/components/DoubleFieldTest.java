/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Test;

public class DoubleFieldTest {

    @Test
    public void testConstructor() {
        DoubleField field = new DoubleField(Locale.GERMAN);
        assertNotNull(field.getConverter());
        assertEquals(Double.class, field.getConverter().getModelType());
    }
}
