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
package org.linkki.core.defaults.formatters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Locale;

import org.linkki.util.DateFormats;

/**
 * Formatter for formatting {@link LocalDateTime} to {@link String}.
 * 
 * @deprecated as it is not used internally anymore
 */
@Deprecated(since = "1.5.0")
public class LocalDateTimeFormatter extends TemporalAccessorFormatter<LocalDateTime> {

    @Override
    protected DateTimeFormatter getFormatter(Locale locale) {
        String pattern = DateFormats.getPattern(locale);

        return new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern(pattern))
                .appendLiteral(' ')
                .append(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                .toFormatter(locale);
    }

}