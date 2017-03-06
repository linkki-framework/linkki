/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

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