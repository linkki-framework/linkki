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

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

import org.eclipse.jdt.annotation.Nullable;

import com.vaadin.data.util.converter.Converter;

/**
 * Base class for all converters which convert {@link TemporalAccessor}-subclasses to {@link String}
 *
 * @param <T> type of the subclass to convert
 */
public abstract class TemporalAccessorToStringConverter<T extends TemporalAccessor> implements Converter<String, T> {

    private static final long serialVersionUID = -1925862565257523273L;


    /**
     * @param locale the {@link Locale} used for the {@link DateTimeFormatter}
     *
     * @return {@link DateTimeFormatter} used to convert {@code T} to {@link String}
     */
    protected abstract DateTimeFormatter getFormatter(@Nullable Locale locale);


    @Override
    public T convertToModel(@Nullable String value, @Nullable Class<? extends T> targetType, @Nullable Locale locale)
            throws ConversionException {
        // do not use getSimple name because LocalDate is not enough information in the logs ;)
        throw new UnsupportedOperationException(
                "this converter shall only be used to convert " + getModelType().getName() + " for presentation");
    }

    @Nullable
    @Override
    public String convertToPresentation(@Nullable T value,
            @Nullable Class<? extends String> targetType,
            @Nullable Locale locale)
            throws ConversionException {

        if (value == null) {
            return "";
        }

        return getFormatter(locale).format(value);
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }


    protected static Locale getLocale(@Nullable Locale locale) {
        if (locale != null) {
            return locale;
        }

        return Locale.getDefault();
    }

}
