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

package org.linkki.core.nls.pmo;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.linkki.core.ui.TestUiFramework;
import org.linkki.core.ui.UiFramework;

public class DefaultPmoNlsServiceTest {

    public static final String PROPERTY_TEST = "test";
    public static final String PROPERTY_NOT_PRESENT = "notPresent";
    private Locale defaultLocale;

    @Before
    public void setLocaleToEnglish() {
        defaultLocale = UiFramework.getLocale();
        TestUiFramework.get().setUiLocale(Locale.ENGLISH);
    }

    @After
    public void resetLocale() {
        TestUiFramework.get().setUiLocale(defaultLocale);
    }

    @Test
    public void testGetSectionCaption() {
        DefaultPmoNlsService defaultPmoNlsService = new DefaultPmoNlsService();

        assertThat(defaultPmoNlsService.getSectionCaption(DefaultPmoNlsServiceTest.class, "bar"), is("TheCaption"));
    }

    @Test
    public void testGetLabel() {
        DefaultPmoNlsService defaultPmoNlsService = new DefaultPmoNlsService();

        assertThat(defaultPmoNlsService.getLabel(DefaultPmoNlsServiceTest.class, PROPERTY_TEST, "", "foo"),
                   is("no aspect"));
        assertThat(defaultPmoNlsService.getLabel(DefaultPmoNlsServiceTest.class, PROPERTY_TEST, "bla", "fallback"),
                   is("blubb"));
    }

    @Test
    public void testGetLabel_Fallback() {
        DefaultPmoNlsService defaultPmoNlsService = new DefaultPmoNlsService();

        assertThat(defaultPmoNlsService.getLabel(DefaultPmoNlsServiceTest.class, PROPERTY_NOT_PRESENT, "", "foo"),
                   is("foo"));
        assertThat(defaultPmoNlsService.getLabel(DefaultPmoNlsServiceTest.class, PROPERTY_NOT_PRESENT, "bla",
                                                 "fallback"),
                   is("fallback"));
    }

    @Test
    public void testGetLabel_Locale() {
        TestUiFramework.get().setUiLocale(Locale.GERMAN);
        DefaultPmoNlsService defaultPmoNlsService = new DefaultPmoNlsService();

        assertThat(defaultPmoNlsService.getLabel(DefaultPmoNlsServiceTest.class, PROPERTY_TEST, "", "foo"),
                   is("Kein Aspekt"));
        assertThat(defaultPmoNlsService.getLabel(DefaultPmoNlsServiceTest.class, PROPERTY_TEST, "bla", "fallback"),
                   is("BlaBla"));
        assertThat(defaultPmoNlsService.getLabel(DefaultPmoNlsServiceTest.class, "only", "german",
                                                 "Subsidiaritätsprinzip"),
                   is("Streichholzschächtelchen"));

        TestUiFramework.get().setUiLocale(Locale.ENGLISH);
        assertThat(defaultPmoNlsService.getLabel(DefaultPmoNlsServiceTest.class, "only", "german",
                                                 "Subsidiaritätsprinzip"),
                   is("Subsidiaritätsprinzip"));
    }

    @Test
    public void testGetLabel_OtherPmoBundleNameGenerator() {
        DefaultPmoNlsService defaultPmoNlsService = new DefaultPmoNlsService(
                pmoClass -> "messages." + pmoClass.getName().replace('.', '_'));

        assertThat(defaultPmoNlsService.getLabel(DefaultPmoNlsServiceTest.class, PROPERTY_TEST, "", "foo"),
                   is("other no aspect"));
        assertThat(defaultPmoNlsService.getLabel(DefaultPmoNlsServiceTest.class, PROPERTY_TEST, "bla", "fallback"),
                   is("blubber"));
    }

}
