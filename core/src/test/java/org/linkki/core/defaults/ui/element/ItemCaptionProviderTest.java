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
package org.linkki.core.defaults.ui.element;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.core.uiframework.UiFramework;

public class ItemCaptionProviderTest {

    @Test
    public void testDefaultCaptionProvider_AnonymousClass() {
        ItemCaptionProvider<Object> provider = new ItemCaptionProvider.DefaultCaptionProvider();

        assertThat(provider.getCaption(NamedEnum.VALUE1), is("name1"));
        assertThat(provider.getCaption(NamedEnum.VALUE2), is("anonymous"));
    }

    @Test
    public void testDefaultCaptionProvider_Localization() {
        ItemCaptionProvider<Object> provider = new ItemCaptionProvider.DefaultCaptionProvider();
        Locale locale = UiFramework.getLocale();

        assertThat(provider.getCaption(LocalizedEnum.VALUE), is(locale.toString()));
    }

    @Test
    public void testDefaultCaptionProvider_Unnamed() {
        ItemCaptionProvider<Object> provider = new ItemCaptionProvider.DefaultCaptionProvider();

        assertThat(provider.getCaption(UnnamedEnum.VALUE), is(UnnamedEnum.VALUE.toString()));
    }

    @Test
    public void testDefaultCaptionProvider_AllMethods() {
        ItemCaptionProvider<Object> provider = new ItemCaptionProvider.DefaultCaptionProvider();

        assertThat(provider.getCaption(AllMethodsEnum.VALUE), is("getName(Locale)"));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testIdAndNameCaptionProvider_AllMethods() {
        ItemCaptionProvider<Object> provider = new ItemCaptionProvider.IdAndNameCaptionProvider();
        assertThat(provider.getCaption(AllMethodsEnum.VALUE), is("getName [id]"));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testIdAndNameCaptionProvider_MissingGetIdMethod() {
        ItemCaptionProvider<Object> provider = new ItemCaptionProvider.IdAndNameCaptionProvider();
        Assertions.assertThrows(IllegalStateException.class, () -> {
            provider.getCaption(UnnamedEnum.VALUE);
        });
    }

    @Test
    public void testInstantiate_DefaultCaptionProvider() {
        ItemCaptionProvider<Object> itemCaptionProvider = ItemCaptionProvider
                .instantiate(ItemCaptionProvider.DefaultCaptionProvider.class);

        assertThat(itemCaptionProvider, instanceOf(ItemCaptionProvider.DefaultCaptionProvider.class));
    }

    @Test
    public void testInstantiate_ToStringCaptionProvider() {
        ItemCaptionProvider<Object> itemCaptionProvider = ItemCaptionProvider
                .instantiate(ItemCaptionProvider.ToStringCaptionProvider.class);

        assertThat(itemCaptionProvider, instanceOf(ItemCaptionProvider.ToStringCaptionProvider.class));
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

        private String name;

        private NamedEnum(String name) {
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
