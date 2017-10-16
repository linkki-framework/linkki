/*
 * Copyright Faktor Zehn AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.core.ui.converters;

import java.util.Date;
import java.util.Locale;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.joda.time.LocalDate;

import com.vaadin.data.util.converter.Converter;

/**
 * Converter used for {@link org.linkki.core.ui.section.annotations.UIDateField}s for
 * {@link LocalDate}.
 */
public class JodaLocalDateToDateConverter implements Converter<Date, LocalDate>, AutoDiscoveredConverter {

    private static final long serialVersionUID = 1L;

    @Override
    @CheckForNull
    public LocalDate convertToModel(@Nullable Date value,
            @Nullable Class<? extends LocalDate> targetType,
            @Nullable Locale locale) throws ConversionException {
        if (value == null) {
            return null;
        }
        LocalDate localDate = LocalDate.fromDateFields(value);
        return convertYearDigits2to4(localDate);
    }

    /**
     * Converts a LocalDate with two year digits to a LocalDate with four year digits. Calculates
     * the pivot year as 80 before today, e.g. for 2016 the pivot year is 1936.
     * <ul>
     * <li>All year numbers between 36 and 99 will be converted to 1936 through 1999.</li>
     * <li>All year numbers between 0 and 35 will be converted to 2000 through 2035.</li>
     * </ul>
     * Leaves other dates unchanged.
     * 
     * @param localDate the date to be converted.
     * @return the converted date with four year digits.
     */
    private LocalDate convertYearDigits2to4(LocalDate localDate) {
        LocalDate resultDate = localDate;
        if (resultDate.getYear() <= 99) {
            LocalDate pivotLocalDate = new LocalDate().minusYears(80);
            int pivotYear = pivotLocalDate.getYearOfCentury();
            int pivotCentury = pivotLocalDate.minusYears(pivotYear).getYear();
            if (resultDate.getYear() < pivotYear) {
                resultDate = resultDate.plusYears(100);
            }
            resultDate = resultDate.plusYears(pivotCentury);
        }
        return resultDate;
    }

    @Override
    @CheckForNull
    public Date convertToPresentation(@Nullable LocalDate value,
            @Nullable Class<? extends Date> targetType,
            @Nullable Locale locale) throws ConversionException {
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