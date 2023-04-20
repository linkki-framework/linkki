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

package org.linkki.core.binding.dispatcher.staticvalue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.uiframework.TestUiFramework;
import org.linkki.core.uiframework.UiFramework;

class StaticValueNlsServiceTest {

    private static final Class<StaticValueNlsServiceTest> TEST_CLASS = StaticValueNlsServiceTest.class;
    private static final String PROPERTY_TEST = "test";
    private static final String PROPERTY_NOT_PRESENT = "notPresent";

    private static final StaticValueNlsService NLS_SERVICE = StaticValueNlsService.getInstance();

    private Locale defaultLocale;

    @BeforeEach
    void setLocaleToEnglish() {
        defaultLocale = UiFramework.getLocale();
        setLocale(Locale.ENGLISH);
    }

    @AfterEach
    void resetLocale() {
        setLocale(defaultLocale);
        StaticValueNlsService.clearCache();
    }

    // @formatter:off
    /*
     *   3               Level 3                        |   Horizontal: Interface
     *   |                                              |   Vertical: Parent class/interface
     *   |    5          Level 2 Parent interface        ―――――――――――――――――――――――――――――――――――
     *   |    |
     *   2――――4          Level 2
     *   |
     *   |    7          Level 1 Parent interface
     *   |    |
     *   1――――6――――8     Level 1
     */
    // @formatter:on
    @Test
    void classesToLookAt() {
        List<Class<?>> classes = NLS_SERVICE.getClassesForPropertyKeyLookup(Level1.class);
        assertThat(classes).isNotEmpty();
        assertThat(classes.get(0)).isEqualTo(Level1.class);
        assertThat(classes.get(1)).isEqualTo(Level2.class);
        assertThat(classes.get(2)).isEqualTo(Level3.class);
        assertThat(classes.get(3)).isEqualTo(Interface4.class);
        assertThat(classes.get(4)).isEqualTo(Interface5.class);
        assertThat(classes.get(5)).isEqualTo(Interface6.class);
        assertThat(classes.get(6)).isEqualTo(Interface7.class);
        assertThat(classes.get(7)).isEqualTo(Interface8.class);
    }

    @Test
    void testGetString_SectionCaption() {
        assertThat(NLS_SERVICE.getString(TEST_CLASS, "", StaticValueNlsService.CAPTION_KEY, "bar"))
                .isEqualTo("TheCaption");
    }

    @Test
    void testGetString() {
        assertThat(NLS_SERVICE.getString(TEST_CLASS, PROPERTY_TEST, "", "foo"))
                .isEqualTo("no aspect");
        assertThat(NLS_SERVICE.getString(TEST_CLASS, PROPERTY_TEST, "bla", "fallback"))
                .isEqualTo("blubb");
    }

    @Test
    void testGetString_fallbackToSingleUnderscoredKey() {
        assertThat(NLS_SERVICE.getString(TEST_CLASS, PROPERTY_NOT_PRESENT, "", "foo"))
                .isEqualTo("foo");

        assertThat(NLS_SERVICE.getString(TEST_CLASS, PROPERTY_NOT_PRESENT, "bla", "fallback"))
                .isEqualTo("fallback");
    }

    @Test
    void testGetString_withLocaleGerman() {
        setLocale(Locale.GERMAN);

        assertThat(NLS_SERVICE.getString(TEST_CLASS, PROPERTY_TEST, "", "foo"))
                .isEqualTo("Kein Aspekt");

        assertThat(NLS_SERVICE.getString(TEST_CLASS, PROPERTY_TEST, "bla", "fallback"))
                .isEqualTo("BlaBla");

        assertThat(NLS_SERVICE.getString(TEST_CLASS, "only", "german", "Subsidiaritätsprinzip"))
                .isEqualTo("Streichholzschächtelchen");

        setLocale(Locale.ENGLISH);
        assertThat(NLS_SERVICE.getString(TEST_CLASS, "only", "german", "Subsidiaritätsprinzip"))
                .isEqualTo("Subsidiaritätsprinzip");
    }

    @Test
    void testGetString_caching_computing_executed_once() {
        setLocale(Locale.GERMAN);
        var nlsServiceMock = spy(StaticValueNlsService.getInstance());

        nlsServiceMock.getString(TEST_CLASS, PROPERTY_TEST, "", "foo");
        nlsServiceMock.getString(TEST_CLASS, PROPERTY_TEST, "", "foo");
        verify(nlsServiceMock, times(1)).computeLabel(any());
    }

    @Test
    void testGetString_caching_locale() {
        assertThat(NLS_SERVICE.getString(TEST_CLASS, PROPERTY_TEST, "", "foo"))
                .isEqualTo("no aspect");

        setLocale(Locale.GERMAN);
        assertThat(NLS_SERVICE.getString(TEST_CLASS, PROPERTY_TEST, "", "foo"))
                .isEqualTo("Kein Aspekt");
    }

    @Test
    void testGetString_caching_fallback() {
        assertThat(NLS_SERVICE.getString(TEST_CLASS, PROPERTY_NOT_PRESENT, "", "foo"))
                .isEqualTo("foo");
        assertThat(NLS_SERVICE.getString(TEST_CLASS, PROPERTY_NOT_PRESENT, "", "bar"))
                .isEqualTo("bar");
    }

    @Test
    void testGetString_LabelDefinedInSuperClass() {
        var pmo = StaticValueNlsServiceTest.TestPmo.class;
        assertThat(NLS_SERVICE.getString(pmo, "property", "", "not found"))
                .isEqualTo("found");
        assertThat(NLS_SERVICE.getString(pmo, "superProperty", "", "not found"))
                .isEqualTo("found in super");
        assertThat(NLS_SERVICE.getString(pmo, "interfaceProperty", "", "not found"))
                .isEqualTo("found in interface");
        assertThat(NLS_SERVICE.getString(pmo, "superInterfaceProperty", "", "not found"))
                .isEqualTo("found in super interface");
    }

    @Test
    void testGetString_LocalizedLabelDefinedInSuperClass() {
        setLocale(Locale.GERMAN);
        var pmo = StaticValueNlsServiceTest.TestPmo.class;
        assertThat(NLS_SERVICE.getString(pmo, "superProperty", "", "not found")).isEqualTo("gefunden in super");
        assertThat(NLS_SERVICE.getString(pmo, "interfaceProperty", "", "not found")).isEqualTo("gefunden in interface");
        assertThat(NLS_SERVICE.getString(pmo, "superInterfaceProperty", "", "not found")).isEqualTo("gefunden in super interface");
    }

    private void setLocale(Locale locale) {
        TestUiFramework.get().setUiLocale(locale);
    }

    private interface Interface6 extends Interface7 {
        // empty
    }

    private interface Interface7 {
        // empty
    }

    private interface Interface8 {
        // empty
    }

    private interface Interface4 extends Interface5 {
        // empty
    }

    private interface Interface5 {
        // empty
    }

    private interface InterfacePmo extends StaticValueNlsServiceTest.SuperInterfacePmo {
        default String getInterfaceProperty() {
            return "";
        }
    }

    private interface SuperInterfacePmo {
        default String getSuperInterfaceProperty() {
            return "";
        }
    }

    private static class Level1 extends Level2 implements Interface6, Interface8 {
        // empty
    }

    private static class Level2 extends Level3 implements Interface4 {
        // empty
    }

    private static class Level3 {
        // empty
    }

    private static class TestPmo extends SuperPmo
            implements InterfacePmo {
        public String getProperty() {
            return "";
        }
    }

    private static class SuperPmo {
        public String getSuperProperty() {
            return "";
        }
    }
}