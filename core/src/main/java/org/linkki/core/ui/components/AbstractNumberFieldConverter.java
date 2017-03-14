/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.components;

import static java.util.Objects.requireNonNull;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.util.converter.Converter;

abstract class AbstractNumberFieldConverter<T extends Number> implements Converter<String, T> {

    private static final long serialVersionUID = -872944068146887949L;

    private final NumberFormat format;

    public AbstractNumberFieldConverter(NumberFormat format) {
        this.format = requireNonNull(format, "format must not be null");
    }

    @Override
    @CheckForNull
    public final T convertToModel(@Nullable String value,
            @Nullable Class<? extends T> targetType,
            @Nullable Locale locale)
            throws ConversionException {
        if (StringUtils.isEmpty(value)) {
            return getNullValue();
        }
        try {
            return convertToModel(format.parse(value));
        } catch (ParseException e) {
            throw new ConversionException("Can't parse '" + value + "' to format '" + format + "')");
        }
    }

    @CheckForNull
    protected abstract T getNullValue();

    protected abstract T convertToModel(Number value);

    @Override
    public String convertToPresentation(@Nullable T value,
            @Nullable Class<? extends String> targetType,
            @Nullable Locale locale)
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