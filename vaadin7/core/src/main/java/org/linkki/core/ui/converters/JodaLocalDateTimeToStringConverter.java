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

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import com.vaadin.data.util.converter.Converter;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Converter for converting {@link LocalDate} to {@link String}. <br>
 * This converter can be used for example in {@link com.vaadin.ui.Table Tables} or for
 * {@link com.vaadin.ui.Label Labels}
 * <p>
 * Do <strong>NOT</strong> use this converter for Fields - this converter is for representation only
 * only!!
 */
public class JodaLocalDateTimeToStringConverter implements Converter<String, LocalDateTime> {

    private static final long serialVersionUID = 1L;
    private JodaLocalDateToStringConverter localDateConverter = new JodaLocalDateToStringConverter();

    @CheckForNull
    @Override
    public LocalDateTime convertToModel(@CheckForNull String value,
            @CheckForNull Class<? extends LocalDateTime> targetType,
            @CheckForNull Locale locale) throws ConversionException {
        throw new UnsupportedOperationException(getClass().getName() + " only supports convertToPresentation");
    }

    @CheckForNull
    @Override
    public String convertToPresentation(@CheckForNull LocalDateTime value,
            @CheckForNull Class<? extends String> targetType,
            @CheckForNull Locale locale) throws ConversionException {
        if (value == null) {
            return "";
        }
        String dateString = localDateConverter.convertToPresentation(value.toLocalDate(), targetType, locale);
        String timeString = DateTimeFormat.shortTime()
                .withLocale(JodaLocalDateToStringConverter.getNullsafeLocale(locale))
                .print(value);
        return dateString + '\t' + timeString;
    }

    @Override
    public Class<LocalDateTime> getModelType() {
        return LocalDateTime.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }

}