/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.linkki.core.ui.components.IntegerField;

public class IntegerFieldTest {

    @Test
    public void testConstructor() {
        IntegerField field = new IntegerField();
        assertNotNull(field.getConverter());
        assertEquals(Integer.class, field.getConverter().getModelType());
    }
}
