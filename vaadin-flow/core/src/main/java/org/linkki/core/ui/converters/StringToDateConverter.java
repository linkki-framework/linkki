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
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.converter.LocalDateToDateConverter;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Converts {@link Date}to {@link String} and vice versa, considering the locale-specific date
 * format and ensures full year representation.
 */
public class StringToDateConverter implements Converter<String, Date> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final LocalDateToDateConverter localDateToDateConverter = new LocalDateToDateConverter();
    private final LocalDateToStringConverter localDateToStringConverter = new LocalDateToStringConverter();

    @Override
    public Result<Date> convertToModel(@CheckForNull String value, ValueContext context) {
        if (StringUtils.isBlank(value)) {
            return Result.ok(null);
        }
        String trimmedValue = value.trim();

        return localDateToStringConverter.convertToModel(trimmedValue, context)
                .flatMap(localDate -> localDateToDateConverter.convertToModel(localDate, context));
    }

    @CheckForNull
    @Override
    public String convertToPresentation(@CheckForNull Date value, ValueContext context) {
        LocalDate localDateValue = localDateToDateConverter.convertToPresentation(value, context);
        return localDateToStringConverter.convertToPresentation(localDateValue, context);
    }

}
