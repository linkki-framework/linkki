/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.components;

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
