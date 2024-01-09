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
package org.linkki.core.ui.converters;

import java.io.Serial;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Converts {@link String} to {@link GregorianCalendar} and vice versa, taking locale-specific date
 * formats into account.
 * 
 * @see LocalDateToStringConverter
 */
public class StringToGregorianCalendarConverter implements Converter<String, GregorianCalendar> {

    @Serial
    private static final long serialVersionUID = 1L;
    private final LocalDateToStringConverter localDateToStringConverter = new LocalDateToStringConverter();

    @Override
    public Result<GregorianCalendar> convertToModel(@CheckForNull String value, ValueContext context) {
        if (StringUtils.isBlank(value)) {
            return Result.ok(null);
        }
        String trimmedValue = value.trim();

        return localDateToStringConverter.convertToModel(trimmedValue, context)
                .map(localDate -> GregorianCalendar.from(localDate.atStartOfDay(ZoneId.systemDefault())));
    }

    @CheckForNull
    @Override
    public String convertToPresentation(@CheckForNull GregorianCalendar value, ValueContext context) {
        if (value == null) {
            return null;
        }

        LocalDate localDateValue = value.toZonedDateTime().toLocalDate();
        return localDateToStringConverter.convertToPresentation(localDateValue, context);
    }

}
