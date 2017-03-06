/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.converters;

import java.util.Locale;

import javax.annotation.Nullable;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import com.vaadin.data.util.converter.Converter;

/**
 * Converter for converting {@link LocalDate} to {@link String}. <br>
 * This converter can be used for example in {@link com.vaadin.ui.Table Tables} or for
 * {@link com.vaadin.ui.Label Labels}
 * <p>
 * Do <strong>NOT</strong> use this converter for Fields - this converter is for representation only
 * only!!
 */
public class JodaLocalDateTimeToStringConverter implements Converter<String, LocalDateTime>, AutoDiscoveredConverter {

    private static final long serialVersionUID = 1L;
    private JodaLocalDateToStringConverter localDateConverter = new JodaLocalDateToStringConverter();

    @Override
    public LocalDateTime convertToModel(@Nullable String value,
            @Nullable Class<? extends LocalDateTime> targetType,
            @Nullable Locale locale) throws ConversionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String convertToPresentation(@Nullable LocalDateTime value,
            @Nullable Class<? extends String> targetType,
            @Nullable Locale locale) throws ConversionException {
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