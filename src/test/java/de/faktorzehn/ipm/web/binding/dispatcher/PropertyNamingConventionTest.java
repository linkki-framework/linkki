/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding.dispatcher;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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

    @Test(expected = NullPointerException.class)
    public void testGetEnabledProperty_null() {
        namingConvention.getEnabledProperty(null);
    }

    @Test
    public void testGetVisibleProperty() {
        assertEquals("testVisible", namingConvention.getVisibleProperty("test"));
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
