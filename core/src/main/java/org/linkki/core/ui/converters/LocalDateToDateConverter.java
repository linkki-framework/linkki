/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

/**
 * Converter used for {@link org.linkki.core.ui.section.annotations.UIDateField}s for
 * {@link LocalDate}.
 */
public class LocalDateToDateConverter implements Converter<Date, LocalDate>, AutoDiscoveredConverter {


    private static final long serialVersionUID = -2921191891769430781L;

    @Override
    public LocalDate convertToModel(Date value, Class<? extends LocalDate> targetType, Locale locale)
            throws ConversionException {
        if (value == null) {
            return null;
        }

        return toLocalDateWithHack(value);
    }

    @Override
    public Date convertToPresentation(LocalDate value, Class<? extends Date> targetType, Locale locale)
            throws ConversionException {
        if (value == null) {
            return null;
        }

        return Date.from(value.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public Class<LocalDate> getModelType() {
        return LocalDate.class;
    }

    @Override
    public Class<Date> getPresentationType() {
        return Date.class;
    }


    /**
     * We need this hack because the 'real' way doesn't work:
     * 
     * <pre>
     * value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
     * </pre>
     * 
     * would "lose" 2 or 3 days (depending on the time zone) with 2 digit years
     */
    private static LocalDate toLocalDateWithHack(Date value) {
        String pattern = "yyyyMMdd";
        String parsedDate = new SimpleDateFormat(pattern).format(value);
        if (parsedDate.startsWith("00")) {
            parsedDate = parsedDate.substring(2);
            pattern = pattern.substring(2);
        }

        try {
            Date fixedDate = new SimpleDateFormat(pattern).parse(parsedDate);
            return LocalDate.from(fixedDate.toInstant().atZone(ZoneId.systemDefault()));
        } catch (ParseException e) {
            throw new ConversionException("unable to convert " + value, e);
        }
    }

}
