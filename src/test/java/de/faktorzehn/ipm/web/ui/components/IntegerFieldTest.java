/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class IntegerFieldTest {

    @Test
    public void testConstructor() {
        IntegerField field = new IntegerField();
        assertNotNull(field.getConverter());
        assertEquals(Integer.class, field.getConverter().getModelType());
    }
}
