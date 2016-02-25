/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.components;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.faktorips.values.Decimal;
import org.junit.Test;

public class DecimalFieldTest {

    @Test
    public void testConstructor() {
        DecimalField field = new DecimalField(Locale.GERMAN);
        assertThat(field.getConverter(), instanceOf(DecimalFieldConverter.class));
        assertThat(field.getConverter().getModelType(), is(Decimal.class));
    }
}
