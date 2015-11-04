/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.function.Function;

import org.junit.Test;

import org.linkki.util.LazyInitializingMap;

public class LazyInitializingMapTest {

    private Function<String, Object> initializer = (String key) -> key + "x";

    @Test
    public void testGetIfPresent() {
        LazyInitializingMap<String, Object> lazyInitializingMap = new LazyInitializingMap<String, Object>(initializer);

        Object value = lazyInitializingMap.getIfPresent("string");
        assertNull(value);
    }

    @Test(expected = NullPointerException.class)
    public void testGet_null() {
        LazyInitializingMap<String, Object> lazyInitializingMap = new LazyInitializingMap<String, Object>(initializer);

        lazyInitializingMap.get(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGet_initializerFunctionReturnsNull() {
        LazyInitializingMap<String, Object> lazyInitializingMap = new LazyInitializingMap<String, Object>(
                (String key) -> null);

        lazyInitializingMap.get("string");
    }

    @Test
    public void testGet() {
        LazyInitializingMap<String, Object> lazyInitializingMap = new LazyInitializingMap<String, Object>(initializer);

        assertNull(lazyInitializingMap.getIfPresent("string"));
        lazyInitializingMap.get("string");
        assertNotNull(lazyInitializingMap.getIfPresent("string"));
    }

    @Test
    public void testGet2() {
        LazyInitializingMap<String, Object> lazyInitializingMap = new LazyInitializingMap<String, Object>(initializer);

        Object value = lazyInitializingMap.get("string");
        assertEquals("stringx", value);
        Object value2 = lazyInitializingMap.get("string");
        assertSame(value, value2);
    }
}
