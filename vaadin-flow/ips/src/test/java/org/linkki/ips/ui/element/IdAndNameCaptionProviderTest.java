/*
 * Copyright Faktor Zehn GmbH.
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
package org.linkki.ips.ui.element;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;

class IdAndNameCaptionProviderTest {

    @Test
    void testIdAndNameCaptionProvider_AnonymousClass() {
        ItemCaptionProvider<Object> provider = new IdAndNameCaptionProvider();
        assertThat(provider.getCaption(TestEnum.VALUE1), is("name1 [id1]"));
    }

    @Test
    void testIdAndNameCaptionProvider_AllMethods() {
        ItemCaptionProvider<Object> provider = new IdAndNameCaptionProvider();
        assertThat(provider.getCaption(AllMethodsEnum.VALUE), is("getName(Locale) [id]"));
    }

    @Test
    void testIdAndNameCaptionProvider_MissingGetIdMethod() {
        ItemCaptionProvider<Object> provider = new IdAndNameCaptionProvider();
        Assertions.assertThrows(IllegalStateException.class, () -> {
            provider.getCaption(UnnamedEnum.VALUE);
        });
    }

    enum TestEnum {

        VALUE1("id1", "name1") {

            @Override
            public void doSomething() {
                // do foo
            }

        };

        private final String id;
        private final String name;

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

    enum UnnamedEnum {
        VALUE;
    }

    enum AllMethodsEnum {
        VALUE;

        @SuppressWarnings("unused")
        public String getName(Locale locale) {
            return "getName(Locale)";
        }

        public String getName() {
            return "getName";
        }

        @Override
        public String toString() {
            return "toString";
        }

        public String getId() {
            return "id";
        }
    }
}
