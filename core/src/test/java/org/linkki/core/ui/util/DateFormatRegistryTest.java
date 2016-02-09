/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.util;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static java.util.Locale.GERMANY;
import static java.util.Locale.US;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

public class DateFormatRegistryTest {

    private static final Locale AUSTRIA = new Locale(Locale.GERMAN.getLanguage(), "AT");

    @Test
    public void testGetPattern() {
        DateFormatRegistry registry = new DateFormatRegistry();
        assertThat(registry.getPattern(GERMAN), is(DateFormatRegistry.PATTERN_DE));
        assertThat(registry.getPattern(GERMANY), is(DateFormatRegistry.PATTERN_DE));
        assertThat(registry.getPattern(AUSTRIA), is(DateFormatRegistry.PATTERN_DE));

        assertThat(registry.getPattern(ENGLISH), is(not(nullValue())));
        assertThat(registry.getPattern(ENGLISH), is(not(DateFormatRegistry.PATTERN_DE)));
        assertThat(registry.getPattern(ENGLISH), is(not(DateFormatRegistry.PATTERN_ISO)));

        assertThat(registry.getPattern(US), is(not(nullValue())));
        assertThat(registry.getPattern(US), is(not(DateFormatRegistry.PATTERN_DE)));
        assertThat(registry.getPattern(US), is(not(DateFormatRegistry.PATTERN_ISO)));

    }

}
