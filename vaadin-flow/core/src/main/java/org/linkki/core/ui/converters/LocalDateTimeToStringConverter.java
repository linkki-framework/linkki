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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Locale;

import org.linkki.core.uiframework.UiFramework;
import org.linkki.util.DateFormats;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class LocalDateTimeToStringConverter implements Converter<String, LocalDateTime> {

    private static final long serialVersionUID = -643467350091621552L;

    @Override
    public Result<LocalDateTime> convertToModel(String value, ValueContext context) {
        if (value == null) {
            return Result.ok(null);
        }
        try {
            return Result
                    .ok(LocalDateTime.parse(value, getFormatter(context)));
        } catch (DateTimeParseException e) {
            return Result.error(e.getMessage());
        }
    }

    @Override
    public String convertToPresentation(LocalDateTime value, ValueContext context) {
        if (value == null) {
            return null;
        }
        return getFormatter(context).format(value);
    }

    private DateTimeFormatter getFormatter(ValueContext context) {
        Locale locale = context.getLocale().orElse(UiFramework.getLocale());
        return new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern(DateFormats.getPattern(locale)))
                .appendLiteral(' ')
                .append(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                .toFormatter(locale);
    }
}
