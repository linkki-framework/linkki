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
public abstract class FormattedNumberToStringConverter<T extends Number> implements Converter<String, T> {

    private static final long serialVersionUID = -872944068146887949L;

    private final String format;
    private final Map<Locale, NumberFormat> formats = new HashMap<>();

    public FormattedNumberToStringConverter(String format) {
        this.format = requireNonNull(format, "format must not be null");
    }

    @Override
    public Result<T> convertToModel(@CheckForNull String value, ValueContext context) {
        if (StringUtils.isBlank(value)) {
            return Result.ok(getNullValue());
        }
        try {
            return Result
                    .ok(convertToModel(getNumberFormat(getLocale(context)).parse(value)));
        } catch (ParseException e) {
            return Result.error("Cannot parse '" + value + "' to format '" + format + "')");
        }
    }

    @CheckForNull
    protected abstract T getNullValue();

    protected abstract T convertToModel(Number value);

    @Override
    public String convertToPresentation(@CheckForNull T value, ValueContext context) {
        if (value == null) {
            return getEmptyPresentation(context);
        } else {
            return getNumberFormat(getLocale(context)).format(value);
        }
    }

    protected String getEmptyPresentation(ValueContext context) {
        return context.getHasValue().map(HasValue::getEmptyValue).map(Object::toString).orElse("");
    }

    private NumberFormat getNumberFormat(Locale locale) {
        return formats.computeIfAbsent(locale, l -> {
            if (StringUtils.isEmpty(format)) {
                return NumberFormat.getIntegerInstance(l);
            } else {
                return new DecimalFormat(format, DecimalFormatSymbols.getInstance(l));
            }
        });
    }

    private Locale getLocale(ValueContext context) {
        return context.getLocale().orElse(UiFramework.getLocale());
    }
}