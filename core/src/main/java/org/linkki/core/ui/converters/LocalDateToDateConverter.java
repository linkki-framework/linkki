/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.converters;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import com.vaadin.data.util.converter.Converter;

/**
 * Converter used for {@link org.linkki.core.ui.section.annotations.UIDateField}s for
 * {@link LocalDate}.
 */
public class LocalDateToDateConverter implements Converter<Date, LocalDate>, AutoDiscoveredConverter {


    private static final long serialVersionUID = -2921191891769430781L;

    @Override
    @CheckForNull
    public LocalDate convertToModel(@Nullable Date value,
            @Nullable Class<? extends LocalDate> targetType,
            @Nullable Locale locale)
            throws ConversionException {
        if (value == null) {
            return null;
        }

        return toLocalDateWithHack(value);
    }

    @Override
    @CheckForNull
    public Date convertToPresentation(@Nullable LocalDate value,
            @Nullable Class<? extends Date> targetType,
            @Nullable Locale locale)
            throws ConversionException {
        if (value == null) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.clear();
        // Cause LocalDate don't have time we set 0 for hours,minutes and seconds
        c.set(value.getYear(), value.getMonthValue() - 1, value.getDayOfMonth(), 0, 0, 0);
        return c.getTime();

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
        String pattern = "yyyy.MM.dd";
        String parsedDate = new SimpleDateFormat(pattern).format(value);
        if (parsedDate.startsWith("00")) {
            parsedDate = parsedDate.substring(2);
            pattern = pattern.substring(2);
        }


        return LocalDate.parse(parsedDate, DateTimeFormatter.ofPattern(pattern));


    }


}
