/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.linkki.core.binding.dispatcher.PropertyNamingConvention;

public class PropertyNamingConventionTest {

    private PropertyNamingConvention namingConvention = new PropertyNamingConvention();

    @Test
    public void testGetValueProperty() {
        assertEquals("test", namingConvention.getValueProperty("test"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetValueProperty_null() {
        namingConvention.getValueProperty(null);
    }

    @Test
    public void testGetEnabledProperty() {
        assertEquals("testEnabled", namingConvention.getEnabledProperty("test"));
    }

    @Test
    public void testGetEnabledProperty_empty() {
        assertEquals("enabled", namingConvention.getEnabledProperty(StringUtils.EMPTY));
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnabledProperty_null() {
        namingConvention.getEnabledProperty(null);
    }

    @Test
    public void testGetVisibleProperty() {
        assertEquals("testVisible", namingConvention.getVisibleProperty("test"));
    }

    @Test
    public void testGetVisibleProperty_empty() {
        assertEquals("visible", namingConvention.getVisibleProperty(StringUtils.EMPTY));
    }

    @Test(expected = NullPointerException.class)
    public void testGetVisibleProperty_null() {
        namingConvention.getVisibleProperty(null);
    }

    @Test
    public void testGetMessagesProperty() {
        assertEquals("testMessages", namingConvention.getMessagesProperty("test"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessagesProperty_null() {
        namingConvention.getMessagesProperty(null);
    }

    @Test
    public void testGetRequiredProperty() {
        assertEquals("testRequired", namingConvention.getRequiredProperty("test"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetRequiredProperty_null() {
        namingConvention.getRequiredProperty(null);
    }

    @Test
    public void testGetAvailableValuesProperty() {
        assertEquals("testAvailableValues", namingConvention.getAvailableValuesProperty("test"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetAvailableValuesProperty_null() {
        namingConvention.getAvailableValuesProperty(null);
    }

}
