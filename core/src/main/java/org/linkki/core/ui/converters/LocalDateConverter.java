package org.linkki.core.ui.converters;

import java.util.Locale;

import javax.annotation.Nonnull;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.linkki.util.DateFormatRegistry;

import com.vaadin.data.util.converter.Converter;

public class LocalDateConverter implements Converter<String, LocalDate> {

    private static final long serialVersionUID = 1L;

    @Override
    public LocalDate convertToModel(String value, Class<? extends LocalDate> targetType, Locale locale)
            throws Converter.ConversionException {

        throw new UnsupportedOperationException();
    }

    @Override
    public String convertToPresentation(LocalDate value, Class<? extends String> targetType, Locale locale)
            throws Converter.ConversionException {
        if (value == null) {
            return "";
        }
        return getFormat(getNullsafeLocale(locale)).print(value);
    }

    public static DateTimeFormatter getFormat(Locale locale) {
        return DateTimeFormat.forPattern(new DateFormatRegistry().getPattern(getNullsafeLocale(locale)));
    }

    @SuppressWarnings("null")
    @Nonnull
    private static Locale getNullsafeLocale(Locale locale) {
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