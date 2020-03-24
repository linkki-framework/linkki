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

package org.linkki.core.vaadin.component;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

import java.util.List;

import org.junit.jupiter.api.Test;

public class MultiformatDateFieldTest {

    private static final String GERMAN_DATE_FORMAT = "dd.MM.yyyy";

    private static final String ISO_DATE_FORMAT = "yyyy-MM-dd";

    private static final String UK_DATE_FORMAT = "dd/MM/yyyy";

    private static final String US_DATE_FORMAT = "M/d/yy";

    @Test
    public void testSetDateFormat_NoSpecialDateFormatSet() {
        MultiformatDateField multiformatDateField = new MultiformatDateField();

        String dateFormat = multiformatDateField.getDateFormat();
        List<String> alternativeFormat = multiformatDateField.getAlternativeFormat();

        assertThat(dateFormat, is(nullValue()));
        assertThat(alternativeFormat, is(empty()));
    }

    @Test
    public void testSetDateFormat_GermanDateFormatSet() {
        MultiformatDateField multiformatDateField = new MultiformatDateField();
        multiformatDateField.setDateFormat(GERMAN_DATE_FORMAT);

        String dateFormat = multiformatDateField.getDateFormat();
        List<String> alternativeFormat = multiformatDateField.getAlternativeFormat();

        assertThat(dateFormat, is(GERMAN_DATE_FORMAT));
        assertThat(alternativeFormat, contains("ddMMyyyy", "ddMMyy", "dd.MM.yy"));
    }

    @Test
    public void testSetDateFormat_IsoDateFormatSet() {
        MultiformatDateField multiformatDateField = new MultiformatDateField();
        multiformatDateField.setDateFormat(ISO_DATE_FORMAT);

        String dateFormat = multiformatDateField.getDateFormat();
        List<String> alternativeFormat = multiformatDateField.getAlternativeFormat();

        assertThat(dateFormat, is(ISO_DATE_FORMAT));
        assertThat(alternativeFormat, contains("yyyyMMdd", "yyMMdd", "yy-MM-dd"));
    }

    @Test
    public void testSetDateFormat_UkDateFormatSet() {
        MultiformatDateField multiformatDateField = new MultiformatDateField();
        multiformatDateField.setDateFormat(UK_DATE_FORMAT);

        String dateFormat = multiformatDateField.getDateFormat();
        List<String> alternativeFormat = multiformatDateField.getAlternativeFormat();

        assertThat(dateFormat, is(UK_DATE_FORMAT));
        assertThat(alternativeFormat, contains("ddMMyyyy", "ddMMyy", "dd/MM/yy"));
    }

    @Test
    public void testSetDateFormat_UsDateFormatSet() {
        MultiformatDateField multiformatDateField = new MultiformatDateField();
        multiformatDateField.setDateFormat(US_DATE_FORMAT);

        String dateFormat = multiformatDateField.getDateFormat();
        List<String> alternativeFormat = multiformatDateField.getAlternativeFormat();

        assertThat(dateFormat, is(US_DATE_FORMAT));
        assertThat(alternativeFormat, contains("Mdyy", "MMddyy", "MMddyyyy"));
    }

}
