/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DoubleFieldTest {

    @Test
    public void testConstructor() {
        DoubleField field = new DoubleField();
        assertNotNull(field.getConverter());
        assertEquals(Double.class, field.getConverter().getModelType());
    }
}
