/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.function.Function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LazyInitializingMapTest {

    private Function<String, Object> initializer = (String key) -> key + "x";

    @Test
    public void testGetIfPresent() {
        LazyInitializingMap<String, Object> lazyInitializingMap = new LazyInitializingMap<>(initializer);

        Object value = lazyInitializingMap.getIfPresent("string");
        assertNull(value);
    }

    @Test
    public void testGet_null() {
        LazyInitializingMap<String, Object> lazyInitializingMap = new LazyInitializingMap<>(initializer);

        Assertions.assertThrows(NullPointerException.class, () -> {
            lazyInitializingMap.get(null);
        });
    }

    @Test
    public void testGet_initializerFunctionReturnsNull() {
        LazyInitializingMap<String, Object> lazyInitializingMap = new LazyInitializingMap<>(
                (String key) -> null);

        Assertions.assertThrows(NullPointerException.class, () -> {
            lazyInitializingMap.get("string");
        });
    }

    @Test
    public void testGet() {
        LazyInitializingMap<String, Object> lazyInitializingMap = new LazyInitializingMap<>(initializer);

        assertNull(lazyInitializingMap.getIfPresent("string"));
        lazyInitializingMap.get("string");
        assertNotNull(lazyInitializingMap.getIfPresent("string"));
    }

    @Test
    public void testGet2() {
        LazyInitializingMap<String, Object> lazyInitializingMap = new LazyInitializingMap<>(initializer);

        Object value = lazyInitializingMap.get("string");
        assertEquals("stringx", value);
        Object value2 = lazyInitializingMap.get("string");
        assertSame(value, value2);
    }
}
