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

import static java.util.Objects.requireNonNull;

import java.io.Serial;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.uiframework.UiFramework;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Converter for {@link Number numbers} that takes a format into count while converting.
 * 
 * @see DecimalFormat
 */
public abstract class FormattedStringToNumberConverter<T extends Number> implements Converter<String, T> {

    @Serial
    private static final long serialVersionUID = -872944068146887949L;

    private final String format;
    private final Map<Locale, NumberFormat> formats = new HashMap<>();

    protected FormattedStringToNumberConverter(String format) {
        this.format = requireNonNull(format, "format must not be null");
    }

    @Override
    public Result<T> convertToModel(@CheckForNull String value, ValueContext context) {
        if (StringUtils.isBlank(value)) {
            return Result.ok(getNullValue());
        }
        try {
            return convertToModel(getNumberFormat(formats, format, getLocale(context)).parse(value));
        } catch (ParseException e) {
            return Result.error("Cannot parse '" + value + "' to format '" + format + "')");
        } catch (NumberFormatException numberFormatException) {
            return Result.error("Number is too large");
        }
    }

    @CheckForNull
    protected abstract T getNullValue();

    protected abstract Result<T> convertToModel(Number value);

    @Override
    public String convertToPresentation(@CheckForNull T value, ValueContext context) {
        if (value == null) {
            return getEmptyPresentation(context);
        } else {
            return getNumberFormat(formats, format, getLocale(context)).format(value);
        }
    }

    public static String getEmptyPresentation(ValueContext context) {
        return context.getHasValue().map(HasValue::getEmptyValue).map(Object::toString).orElse("");
    }

    public static NumberFormat getNumberFormat(Map<Locale, NumberFormat> formats, String format, Locale locale) {
        return formats.computeIfAbsent(locale, l -> {
            if (StringUtils.isEmpty(format)) {
                return NumberFormat.getIntegerInstance(l);
            } else {
                var df = new DecimalFormat(format, DecimalFormatSymbols.getInstance(l));
                df.setParseBigDecimal(true);

                return df;
            }
        });
    }

    public static Locale getLocale(ValueContext context) {
        return context.getLocale().orElse(UiFramework.getLocale());
    }
}