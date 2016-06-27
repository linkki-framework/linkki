package org.linkki.core.ui.converters;

import java.util.Locale;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import com.vaadin.data.util.converter.Converter;

public class LocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private static final long serialVersionUID = 1L;
    private LocalDateConverter localDateConverter = new LocalDateConverter();

    @Override
    public LocalDateTime convertToModel(String value, Class<? extends LocalDateTime> targetType, Locale locale)
            throws ConversionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String convertToPresentation(LocalDateTime value, Class<? extends String> targetType, Locale locale)
            throws ConversionException {
        if (value == null) {
            return "";
        }
        String dateString = localDateConverter.convertToPresentation(value.toLocalDate(), targetType, locale);
        String timeString = DateTimeFormat.shortTime().withLocale(LocalDateConverter.getNullsafeLocale(locale))
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