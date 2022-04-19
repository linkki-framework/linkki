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

package org.linkki.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Recalculates two digit years into four digit years, based on the -80 / +19 rule, for
 * {@link LocalDate} and {@link LocalDateTime} during the convertToModel conversion. For example,
 * 0019-01-01 will be converted to 2019-01-01 and 0090-01-01 to 1990-01-01.
 */
public class TwoDigitYearUtil {

    static final TwoDigitYearUtil INSTANCE = new TwoDigitYearUtil();

    private TwoDigitYearUtil() {
        // prevent instantiation
    }

    /**
     * Converts the given date and updates the year if it has only two digits. Otherwise it simply
     * returns the input value.
     * 
     * @param value The date that should be converted
     * @return the date adjusted to a year of -80/+19 if the year was below year 100.
     */
    public static LocalDate convert(LocalDate value) {
        return INSTANCE.convertInternal(value);
    }

    /**
     * Converts the given datetime and updates the year if it has only two digits. Otherwise it simply
     * returns the input value. The time is not affected.
     * 
     * @param value The datetime that should be converted
     * @return the datetime with date adjusted to a year of -80/+19 if the year was below year 100.
     */
    public static LocalDateTime convert(LocalDateTime value) {
        return INSTANCE.convertInternal(value);

    }

    /**
     * Converts the given date and updates the year if it has only two digits. Otherwise it simply
     * returns the input value.
     * 
     * @param value The date that should be converted
     * @return the date adjusted to a year of -80/+19 if the year was below year 100.
     */
    LocalDate convertInternal(LocalDate value) {
        if (value.isAfter(LocalDate.of(-1, 1, 1)) && value.isBefore(LocalDate.of(100, 1, 1))) {
            LocalDate resultDate = value;
            LocalDate pivotLocalDate = LocalDate.now().withYear(getCurrentYear()).minusYears(80);
            int pivotYear = pivotLocalDate.getYear() % 100;
            int pivotCentury = pivotLocalDate.minusYears(pivotYear).getYear();
            if (resultDate.getYear() < pivotYear) {
                resultDate = resultDate.plusYears(100);
            }
            resultDate = resultDate.plusYears(pivotCentury);
            return resultDate;
        } else {
            return value;
        }
    }

    /**
     * Converts the given datetime and updates the year if it has only two digits. Otherwise it simply
     * returns the input value. The time is not affected.
     * 
     * @param value The datetime that should be converted
     * @return the datetime with date adjusted to a year of -80/+19 if the year was below year 100.
     */
    LocalDateTime convertInternal(LocalDateTime value) {
        LocalDate convertedDate = convertInternal(value.toLocalDate());
        return LocalDateTime.of(convertedDate, value.toLocalTime());
    }

    int getCurrentYear() {
        return LocalDate.now().getYear();
    }

}
