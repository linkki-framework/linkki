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
package org.linkki.core.defaults.uielement;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ItemCaptionProviderTest {

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testDefaultCaptionProvider_canHandleAnonymousClasses() {
        ItemCaptionProvider provider = new ItemCaptionProvider.DefaultCaptionProvider();
        assertThat(provider.getCaption(TestEnum.VALUE1), is("name1"));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testIdAndNameCaptionProvider_canHandleAnonymousClasses() {
        ItemCaptionProvider provider = new ItemCaptionProvider.IdAndNameCaptionProvider();
        assertThat(provider.getCaption(TestEnum.VALUE1), is("name1 [id1]"));
    }

    enum TestEnum {

        VALUE1("id1", "name1") {

            @Override
            public void doSomething() {
                // do foo
            }

        };

        private String id;
        private String name;

        private TestEnum(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public abstract void doSomething();
    }
}
