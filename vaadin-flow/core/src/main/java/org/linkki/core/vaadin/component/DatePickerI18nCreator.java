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

package org.linkki.core.vaadin.component;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.linkki.core.nls.NlsService;
import org.linkki.util.DateFormats;

import com.vaadin.flow.component.datepicker.DatePicker.DatePickerI18n;

class DatePickerI18nCreator {

    private static final String BUNDLE_NAME = "org/linkki/core/vaadin/component/messages";

    private DatePickerI18nCreator() {
        // prevent instantiation
    }

    static DatePickerI18n createI18n(Locale locale) {
        DatePickerI18n i18n = new DatePickerI18n();
        initTexts(i18n, locale);

        i18n.setFirstDayOfWeek(getFirstDayOfWeek(locale));

        String pattern = DateFormats.getPattern(locale);
        String patternWithoutSeparators = pattern.replaceAll("[^a-zA-Z]", "");
        String patternWithoutSeparatorsShort = patternWithoutSeparators.replace("yyyy", "yy");
        i18n.setDateFormats(pattern, patternWithoutSeparators, patternWithoutSeparatorsShort);

        return i18n;
    }

    private static int getFirstDayOfWeek(Locale locale) {
        if (!locale.getCountry().isEmpty()) {
            return WeekFields.of(locale).getFirstDayOfWeek().getValue() % 7;
        } else {
            return 1;
        }
    }

    private static void initTexts(DatePickerI18n i18n, Locale locale) {
        getText("cancel", locale).ifPresent(i18n::setCancel);
        getText("today", locale).ifPresent(i18n::setToday);

        List<String> weekdays = new ArrayList<>(7);
        List<String> weekdaysShort = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            // Vaadin uses 0 for Sunday, JDK uses 7
            DayOfWeek day = DayOfWeek.of(i != 0 ? i : 7);
            weekdays.add(day.getDisplayName(TextStyle.FULL_STANDALONE, locale));
            weekdaysShort.add(day.getDisplayName(TextStyle.SHORT_STANDALONE, locale));
        }
        i18n.setWeekdays(weekdays);
        i18n.setWeekdaysShort(weekdaysShort);

        List<String> months = new ArrayList<>(12);
        for (Month month : Month.values()) {
            months.add(month.getDisplayName(TextStyle.FULL_STANDALONE, locale));
        }
        i18n.setMonthNames(months);
    }

    private static Optional<String> getText(String key, Locale locale) {
        return NlsService.get().getString(BUNDLE_NAME, DatePickerI18nCreator.class.getSimpleName() + "." + key, locale);
    }

}
