/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.converters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.linkki.util.DateFormatRegistry;

/**
 * Converter for converting {@link LocalDate} to {@link String}. <br>
 * This converter can be used for example in {@link com.vaadin.ui.Table Tables} or for
 * {@link com.vaadin.ui.Label Labels}
 * <p>
 * Do <strong>NOT</strong> use this converter for Fields - this converter is for representation only
 * only!!
 */
public class LocalDateToStringConverter extends TemporalAccessorToStringConverter<LocalDate>
        implements AutoDiscoveredConverter {

    private static final long serialVersionUID = 3829844680979291395L;

    @Override
    public Class<LocalDate> getModelType() {
        return LocalDate.class;
    }

    @Override
    protected DateTimeFormatter getFormatter(Locale locale) {
        Locale localeForConversion = getLocale(locale);
        return DateTimeFormatter.ofPattern(new DateFormatRegistry().getPattern(localeForConversion),
                                           localeForConversion);
    }

}
