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

package org.linkki.core.ui.converters;

import java.time.LocalDate;

import org.linkki.util.TwoDigitYearUtil;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Converts {@link LocalDate} to {@link LocalDate} while recalculating two digit years into four digit
 * years based on the -80 / +20 rule during the convertToModel conversion. E.g. 19/01/01 will be
 * converted to 2019/01/01 and 90/01/01 to 1990/01/01.
 */
public class TwoDigitYearLocalDateConverter implements Converter<LocalDate, LocalDate> {

    private static final long serialVersionUID = -7168406748935260873L;

    private static final String MSG_YEAR_TOO_LARGE = "The year must not have more than four digits";

    @CheckForNull
    @Override
    public Result<LocalDate> convertToModel(@CheckForNull LocalDate value, ValueContext context) {
        if (value == null) {
            return Result.ok(null);
        } else {
            LocalDate date = TwoDigitYearUtil.convert(value);

            if (date.getYear() <= 9999) {
                return Result.ok(date);
            } else {
                return Result.error(MSG_YEAR_TOO_LARGE);
            }
        }
    }

    @Override
    public LocalDate convertToPresentation(LocalDate value, ValueContext context) {
        return value;
    }

}
