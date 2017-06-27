/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.converters;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
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

        return toLocalWorkaround(value);
    }

    /**
     * We need this workaround because the 'real' way doesn't work:
     * 
     * <pre>
     * value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
     * </pre>
     * 
     * would "lose" 2 or 3 days (depending on the time zone) with 2 digit years and dates prior to
     * 1893-04-01
     */
    private static LocalDate toLocalWorkaround(Date value) {
        String formattedDate = new SimpleDateFormat("yyyyMMdd").format(value);
        if (formattedDate.startsWith("00")) {
            formattedDate = formattedDate.substring(2);
        }

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendValueReduced(ChronoField.YEAR, 2, 4, Year.now().getValue() - 80)
                .appendPattern("MMdd")
                .toFormatter();
        return LocalDate.parse(formattedDate, formatter);
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


}
