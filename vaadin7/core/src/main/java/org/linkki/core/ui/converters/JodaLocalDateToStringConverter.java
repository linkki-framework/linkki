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

import java.util.Locale;

import org.eclipse.jdt.annotation.Nullable;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.linkki.util.DateFormats;

import com.vaadin.data.util.converter.Converter;

/**
 * Converter for converting {@link LocalDate} to {@link String}. <br>
 * This converter can be used for example in {@link com.vaadin.ui.Table Tables} or for
 * {@link com.vaadin.ui.Label Labels}
 * <p>
 * Do <strong>NOT</strong> use this converter for Fields - this converter is for representation only
 * only!!
 */
public class JodaLocalDateToStringConverter implements Converter<String, LocalDate> {

    private static final long serialVersionUID = 1L;

    @Nullable
    @Override
    public LocalDate convertToModel(@Nullable String value,
            @Nullable Class<? extends LocalDate> targetType,
            @Nullable Locale locale) throws Converter.ConversionException {
        throw new UnsupportedOperationException(getClass().getName() + " only supports convertToPresentation");
    }

    @Nullable
    @Override
    public String convertToPresentation(@Nullable LocalDate value,
            @Nullable Class<? extends String> targetType,
            @Nullable Locale locale) throws Converter.ConversionException {
        if (value == null) {
            return "";
        }
        return getFormat(getNullsafeLocale(locale)).print(value);
    }

    public static DateTimeFormatter getFormat(Locale locale) {
        return DateTimeFormat.forPattern(DateFormats.getPattern(getNullsafeLocale(locale)));
    }

    static Locale getNullsafeLocale(@Nullable Locale locale) {
        if (locale == null) {
            return Locale.getDefault();
        } else {
            return locale;
        }
    }

    @Override
    public Class<LocalDate> getModelType() {
        return LocalDate.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }

}
