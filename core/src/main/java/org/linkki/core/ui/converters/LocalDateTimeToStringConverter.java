/*
 * Copyright Faktor Zehn AG.
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Locale;

import javax.annotation.Nullable;

import org.linkki.util.DateFormatRegistry;

/**
 * Converter for converting {@link LocalDateTime} to {@link String}. <br>
 * This converter can be used for example in {@link com.vaadin.ui.Table}s or for
 * {@link com.vaadin.ui.Label}s
 * <p>
 * Do <strong>NOT</strong> use this converter for Fields - this converter is for representation only
 * only!!
 */
public class LocalDateTimeToStringConverter extends TemporalAccessorToStringConverter<LocalDateTime>
        implements AutoDiscoveredConverter {

    private static final long serialVersionUID = -4156241047177368821L;

    private static final DateFormatRegistry FORMAT_REGISTRY = new DateFormatRegistry();

    @Override
    public Class<LocalDateTime> getModelType() {
        return LocalDateTime.class;
    }

    @Override
    protected DateTimeFormatter getFormatter(@Nullable Locale locale) {
        Locale localeForConversion = getLocale(locale);
        String pattern = FORMAT_REGISTRY.getPattern(localeForConversion);

        return new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern(pattern))
                .appendLiteral(' ')
                .append(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                .toFormatter(localeForConversion);
    }

}