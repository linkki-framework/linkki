/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.converters;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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


    @SuppressWarnings("null")
    @Nonnull
    protected static Locale getLocale(@Nullable Locale locale) {
        if (locale != null) {
            return locale;
        }

        return Locale.getDefault();
    }

}
