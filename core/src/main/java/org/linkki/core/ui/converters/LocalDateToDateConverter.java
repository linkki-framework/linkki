package org.linkki.core.ui.converters;

import java.util.Date;
import java.util.Locale;

import org.joda.time.LocalDate;

import com.vaadin.data.util.converter.Converter;

public class LocalDateToDateConverter implements Converter<Date, LocalDate> {

    private static final long serialVersionUID = 1L;

    @Override
    public LocalDate convertToModel(Date value, Class<? extends LocalDate> targetType, Locale locale)
            throws ConversionException {
        if (value == null) {
            return null;
        }
        return LocalDate.fromDateFields(value);
    }

    @Override
    public Date convertToPresentation(LocalDate value, Class<? extends Date> targetType, Locale locale)
            throws ConversionException {
        if (value == null) {
            return null;
        }
        return value.toDate();
    }

    @Override
    public Class<LocalDate> getModelType() {
        return LocalDate.class;
    }

    @Override
    public Class<Date> getPresentationType() {
        return Date.class;
    }

}