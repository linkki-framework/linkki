/*
 * Copyright Faktor Zehn AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.core.binding.dispatcher;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class PropertyNamingConventionTest {

    private PropertyNamingConvention namingConvention = new PropertyNamingConvention();

    @Test
    public void testGetValueProperty() {
        assertEquals("test", namingConvention.getValueProperty("test"));
    }

    @SuppressWarnings("null")
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

    @SuppressWarnings("null")
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

    @SuppressWarnings("null")
    @Test(expected = NullPointerException.class)
    public void testGetVisibleProperty_null() {
        namingConvention.getVisibleProperty(null);
    }

    @Test
    public void testGetMessagesProperty() {
        assertEquals("testMessages", namingConvention.getMessagesProperty("test"));
    }

    @SuppressWarnings("null")
    @Test(expected = NullPointerException.class)
    public void testGetMessagesProperty_null() {
        namingConvention.getMessagesProperty(null);
    }

    @Test
    public void testGetRequiredProperty() {
        assertEquals("testRequired", namingConvention.getRequiredProperty("test"));
    }

    @SuppressWarnings("null")
    @Test(expected = NullPointerException.class)
    public void testGetRequiredProperty_null() {
        namingConvention.getRequiredProperty(null);
    }

    @Test
    public void testGetAvailableValuesProperty() {
        assertEquals("testAvailableValues", namingConvention.getAvailableValuesProperty("test"));
    }

    @SuppressWarnings("null")
    @Test(expected = NullPointerException.class)
    public void testGetAvailableValuesProperty_null() {
        namingConvention.getAvailableValuesProperty(null);
    }

}
