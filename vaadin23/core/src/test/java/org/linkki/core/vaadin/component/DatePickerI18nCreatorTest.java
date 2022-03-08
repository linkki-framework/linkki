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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.vaadin.flow.component.datepicker.DatePicker.DatePickerI18n;

class DatePickerI18nCreatorTest {

    @Test
    void testCreateI18n_DateFormats() {
        DatePickerI18n i18n = DatePickerI18nCreator.createI18n(Locale.GERMAN);

        assertThat(i18n.getDateFormats()).containsExactly("dd.MM.yyyy", "ddMMyyyy", "ddMMyy");
    }

    @Test
    void testCreateI18n_NlsTexts() {
        DatePickerI18n i18n = DatePickerI18nCreator.createI18n(Locale.GERMAN);

        assertThat(i18n.getCancel()).isEqualTo("Abbrechen");
        assertThat(i18n.getToday()).isEqualTo("Heute");
        assertThat(i18n.getWeekdaysShort()).containsExactly("So", "Mo", "Di", "Mi", "Do", "Fr", "Sa");
        assertThat(i18n.getWeekdays()).containsExactly("Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag",
                                                       "Freitag", "Samstag");
        assertThat(i18n.getMonthNames()).containsExactly("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli",
                                                         "August", "September", "Oktober", "November", "Dezember");
    }

    @ParameterizedTest
    @MethodSource("firstDayOfWeeks")
    void testCreateI18n_FirstDayOfWeek(Locale locale, int expectedFirstDayOfWeek) {
        DatePickerI18n i18n = DatePickerI18nCreator.createI18n(locale);

        int actualFirstDayOfWeek = i18n.getFirstDayOfWeek();

        assertThat(actualFirstDayOfWeek).isEqualTo(expectedFirstDayOfWeek);
    }

    static Stream<Arguments> firstDayOfWeeks() {
        return Stream.of(
                         Arguments.of(Locale.GERMAN, 1),
                         Arguments.of(Locale.GERMANY, 1),
                         Arguments.of(Locale.ENGLISH, 1),
                         Arguments.of(Locale.US, 0));
    }

}
