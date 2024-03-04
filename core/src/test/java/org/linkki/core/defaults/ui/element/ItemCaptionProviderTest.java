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
package org.linkki.core.defaults.ui.element;

import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.core.defaults.ui.element.ItemCaptionProvider.instantiate;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.DefaultCaptionProvider;
import org.linkki.core.uiframework.TestUiFramework;
import org.linkki.core.uiframework.UiFramework;

class ItemCaptionProviderTest {

    @Test
    void testDefaultCaptionProvider_AnonymousClass() {
        ItemCaptionProvider<Object> provider = new ItemCaptionProvider.DefaultCaptionProvider();

        assertThat(provider.getCaption(NamedEnum.VALUE1)).isEqualTo("name1");
        assertThat(provider.getCaption(NamedEnum.VALUE2)).isEqualTo("anonymous");
    }

    @Test
    void testDefaultCaptionProvider_Localization() {
        ItemCaptionProvider<Object> provider = new ItemCaptionProvider.DefaultCaptionProvider();
        Locale locale = UiFramework.getLocale();

        assertThat(provider.getCaption(LocalizedEnum.VALUE)).isEqualTo(locale.toString());
    }

    @Test
    void testDefaultCaptionProvider_Unnamed() {
        ItemCaptionProvider<Object> provider = new ItemCaptionProvider.DefaultCaptionProvider();

        assertThat(provider.getCaption(UnnamedEnum.VALUE)).isEqualTo(UnnamedEnum.VALUE.toString());
    }

    @Test
    void testDefaultCaptionProvider_AllMethods() {
        ItemCaptionProvider<Object> provider = new ItemCaptionProvider.DefaultCaptionProvider();

        assertThat(provider.getCaption(AllMethodsEnum.VALUE)).isEqualTo("getName(Locale)");
    }

    @Test
    void testDefaultCaptionProvider_getUnsafeCaption_null() {
        ItemCaptionProvider<Object> provider = new ItemCaptionProvider.DefaultCaptionProvider();

        assertThat(provider.getUnsafeCaption(null)).isEmpty();
    }

    @Test
    void testDefaultCaptionProvider_getUnsafeCaption_AllMethods() {
        ItemCaptionProvider<Object> provider = new ItemCaptionProvider.DefaultCaptionProvider();

        assertThat(provider.getUnsafeCaption(AllMethodsEnum.VALUE)).isEqualTo("getName(Locale)");
    }

    @Test
    void testInstantiate_DefaultCaptionProvider() {
        ItemCaptionProvider<?> itemCaptionProvider = instantiate(() -> DefaultCaptionProvider.class);

        assertThat(itemCaptionProvider).isInstanceOf(ItemCaptionProvider.DefaultCaptionProvider.class);
    }

    @Test
    void testInstantiate_ToStringCaptionProvider() {
        ItemCaptionProvider<?> itemCaptionProvider =
                instantiate(() -> ItemCaptionProvider.ToStringCaptionProvider.class);

        assertThat(itemCaptionProvider).isInstanceOf(ItemCaptionProvider.ToStringCaptionProvider.class);
    }

    @Test
    void testDefaultCaptionProvider_WithBoolean() {
        ItemCaptionProvider<Object> provider = new ItemCaptionProvider.DefaultCaptionProvider();

        assertThat(provider.getCaption(true)).isEqualTo("Ja");
        assertThat(provider.getCaption(false)).isEqualTo("Nein");

        TestUiFramework.get().setUiLocale(Locale.ENGLISH);

        assertThat(provider.getCaption(true)).isEqualTo("Yes");
        assertThat(provider.getCaption(false)).isEqualTo("No");
    }

    enum LocalizedEnum {
        VALUE;

        public String getName(Locale locale) {
            return locale.toString();
        }
    }

    enum UnnamedEnum {
        VALUE;
    }

    enum NamedEnum {
        VALUE1("name1") {
            @Override
            public void doSomething() {
                // do foo
            }
        },
        VALUE2("name2") {
            @Override
            public String getName() {
                return "anonymous";
            }

            @Override
            public void doSomething() {
                // do foo
            }
        };

        private final String name;

        NamedEnum(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public abstract void doSomething();
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
