/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.linkki.core.ui.components.DoubleField;

public class DoubleFieldTest {

    @Test
    public void testConstructor() {
        DoubleField field = new DoubleField();
        assertNotNull(field.getConverter());
        assertEquals(Double.class, field.getConverter().getModelType());
    }
}
