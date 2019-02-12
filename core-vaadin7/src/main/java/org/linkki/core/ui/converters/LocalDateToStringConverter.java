/*
 * Copyright Faktor Zehn GmbH.
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.eclipse.jdt.annotation.Nullable;
import org.linkki.util.DateFormatRegistry;

/**
 * Converter for converting {@link LocalDate} to {@link String}. <br>
 * This converter can be used for example in {@link com.vaadin.ui.Table Tables} or for
 * {@link com.vaadin.ui.Label Labels}
 * <p>
 * Do <strong>NOT</strong> use this converter for Fields - this converter is for representation only
 * only, as it handles only one way conversion from model to label!!
 */
public class LocalDateToStringConverter extends TemporalAccessorToStringConverter<LocalDate> {

    private static final long serialVersionUID = 3829844680979291395L;

    @Override
    public Class<LocalDate> getModelType() {
        return LocalDate.class;
    }

    @Override
    protected DateTimeFormatter getFormatter(@Nullable Locale locale) {
        Locale localeForConversion = getLocale(locale);
        return DateTimeFormatter.ofPattern(new DateFormatRegistry().getPattern(localeForConversion),
                                           localeForConversion);
    }

}
