/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.components;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.util.converter.Converter;

abstract class AbstractNumberFieldConverter<T extends Number> implements Converter<String, T> {

    private static final long serialVersionUID = -872944068146887949L;

    private final NumberFormat format;

    public AbstractNumberFieldConverter(NumberFormat format) {
        if (format == null) {
            throw new IllegalArgumentException("NumberFormat required");
        }
        this.format = format;
    }

    @Override
    public final T convertToModel(String value, Class<? extends T> targetType, Locale locale)
            throws ConversionException {

        if (StringUtils.isEmpty(value)) {
            return null;
        }
        try {
            return convertToModel(format.parse(value));
        } catch (ParseException e) {
            throw new ConversionException("Can't parse '" + value + "' to format '" + format + "')");
        }
    }

    abstract protected T convertToModel(Number value);

    @Override
    public String convertToPresentation(T value, Class<? extends String> targetType, Locale locale)
            throws ConversionException {

        if (value == null) {
            return "";
        } else {
            return format.format(value);
        }
    }

    @Override
    public final Class<String> getPresentationType() {
        return String.class;
    }
}