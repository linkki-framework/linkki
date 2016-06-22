package org.linkki.core.ui.converters;

import java.util.Locale;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import com.vaadin.data.util.converter.Converter;

public class LocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    @Override
    public LocalDateTime convertToModel(String value, Class<? extends LocalDateTime> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {

        throw new UnsupportedOperationException();
    }

    @Override
    public String convertToPresentation(LocalDateTime value, Class<? extends String> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {

        if (value == null) {
            return "";
        }
        return DateTimeFormat.mediumDateTime().withLocale(locale).print(value);
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