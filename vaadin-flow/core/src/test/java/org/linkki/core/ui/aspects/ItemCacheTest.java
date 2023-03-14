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

package org.linkki.core.ui.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class ItemCacheTest {

    @Test
    void testUpdate_Unchanged() {
        Object o1 = new Object();
        Object o2 = new Object();
        Object o3 = new Object();
        ItemCache cache = new ItemCache();
        cache.replaceContent(Arrays.asList(o1, o2, o3));

        boolean changed = cache.replaceContent(Arrays.asList(o1, o2, o3));

        assertThat(changed, is(false));
    }

    @Test
    void testUpdate_InstancesChanged() {
        Object o1 = new Object();
        Object o2 = new Object();
        Object o3 = new Object();
        ItemCache cache = new ItemCache();
        cache.replaceContent(Arrays.asList(o1, o2, o3));

        o1 = new Object();
        o2 = new Object();
        o3 = new Object();
        boolean changed = cache.replaceContent(Arrays.asList(o1, o2, o3));

        assertThat(changed, is(true));
    }

    @Test
    void testUpdate_ItemAdded() {
        Object o1 = new Object();
        Object o2 = new Object();
        Object o3 = new Object();
        ItemCache cache = new ItemCache();
        cache.replaceContent(Arrays.asList(o1, o2, o3));

        Object o4 = new Object();
        boolean changed = cache.replaceContent(Arrays.asList(o1, o2, o3, o4));

        assertThat(changed, is(true));
    }

    @Test
    void testUpdate_ItemRemoved() {
        Object o1 = new Object();
        Object o2 = new Object();
        Object o3 = new Object();
        ItemCache cache = new ItemCache();
        cache.replaceContent(Arrays.asList(o1, o2, o3));

        boolean changed = cache.replaceContent(Arrays.asList(o1, o2));

        assertThat(changed, is(true));
    }

    @Test
    void testUpdate_OrderChanged() {
        Object o1 = new Object();
        Object o2 = new Object();
        Object o3 = new Object();
        ItemCache cache = new ItemCache();
        cache.replaceContent(Arrays.asList(o1, o2, o3));

        boolean changed = cache.replaceContent(Arrays.asList(o1, o3, o2));

        assertThat(changed, is(true));
    }
}
