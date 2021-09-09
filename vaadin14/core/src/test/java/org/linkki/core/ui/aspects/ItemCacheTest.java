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
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.ToStringCaptionProvider;

public class ItemCacheTest {

    @Test
    public void testUpdate_Unchanged() {
        SampleObject o1 = new SampleObject("o1");
        SampleObject o2 = new SampleObject("o2");
        SampleObject o3 = new SampleObject("o3");
        ItemCache cache = new ItemCache(new ToStringCaptionProvider());
        cache.replaceContent(Arrays.asList(o1, o2, o3));

        boolean changed = cache.replaceContent(Arrays.asList(o1, o2, o3));

        assertThat(changed, is(false));
    }

    @Test
    public void testUpdate_InstancesChanged() {
        SampleObject o1 = new SampleObject("o1");
        SampleObject o2 = new SampleObject("o2");
        SampleObject o3 = new SampleObject("o3");
        ItemCache cache = new ItemCache(new ToStringCaptionProvider());
        cache.replaceContent(Arrays.asList(o1, o2, o3));

        o1 = new SampleObject("o1");
        o2 = new SampleObject("o2");
        o3 = new SampleObject("o3");
        boolean changed = cache.replaceContent(Arrays.asList(o1, o2, o3));

        assertThat(changed, is(true));
    }

    @Test
    public void testUpdate_CaptionChanged() {
        SampleObject o1 = new SampleObject("o1");
        SampleObject o2 = new SampleObject("o2");
        SampleObject o3 = new SampleObject("o3");
        ItemCache cache = new ItemCache(new ToStringCaptionProvider());
        cache.replaceContent(Arrays.asList(o1, o2, o3));

        o2.setCaption("x");
        boolean changed = cache.replaceContent(Arrays.asList(o1, o2, o3));

        assertThat(changed, is(true));
    }

    @Test
    public void testUpdate_ItemAdded() {
        SampleObject o1 = new SampleObject("o1");
        SampleObject o2 = new SampleObject("o2");
        SampleObject o3 = new SampleObject("o3");
        ItemCache cache = new ItemCache(new ToStringCaptionProvider());
        cache.replaceContent(Arrays.asList(o1, o2, o3));

        SampleObject o4 = new SampleObject("o4");
        boolean changed = cache.replaceContent(Arrays.asList(o1, o2, o3, o4));

        assertThat(changed, is(true));
    }


    @Test
    public void testUpdate_ItemRemoved() {
        SampleObject o1 = new SampleObject("o1");
        SampleObject o2 = new SampleObject("o2");
        SampleObject o3 = new SampleObject("o3");
        ItemCache cache = new ItemCache(new ToStringCaptionProvider());
        cache.replaceContent(Arrays.asList(o1, o2, o3));

        boolean changed = cache.replaceContent(Arrays.asList(o1, o2));

        assertThat(changed, is(true));
    }


    @Test
    public void testUpdate_OrderChanged() {
        SampleObject o1 = new SampleObject("o1");
        SampleObject o2 = new SampleObject("o2");
        SampleObject o3 = new SampleObject("o3");
        ItemCache cache = new ItemCache(new ToStringCaptionProvider());
        cache.replaceContent(Arrays.asList(o1, o2, o3));

        boolean changed = cache.replaceContent(Arrays.asList(o1, o3, o2));

        assertThat(changed, is(true));
    }

    public class SampleObject {

        private String caption;

        public SampleObject(String caption) {
            this.caption = caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        @Override
        public String toString() {
            return caption;
        }

    }

}
