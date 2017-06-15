/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.components;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

import com.vaadin.data.util.converter.Converter;

public class DoubleFieldTest {

    @Test
    public void testConstructor1() {
        DoubleField field = new DoubleField(Locale.GERMAN);
        assertThat(field.getConverter(), instanceOf(DoubleFieldConverter.class));
        assertThat(field.getConverter().getModelType(), is(Double.class));
    }

    @Test
    public void testConstructor2() {
        DoubleField field = new DoubleField("0.00", Locale.GERMAN);
        Converter<String, Object> converter = field.getConverter();
        assertThat(converter.convertToPresentation(0.2, String.class, null), is("0,20"));
    }

}
