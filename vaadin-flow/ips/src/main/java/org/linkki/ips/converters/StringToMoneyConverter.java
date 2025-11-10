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

package org.linkki.ips.converters;

import java.io.Serial;
import java.util.Currency;
import java.util.Locale;
import java.util.regex.Pattern;

import org.faktorips.values.Money;
import org.jsoup.internal.StringUtil;
import org.linkki.ips.decimalfield.FormattedStringToDecimalConverter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Converts {@link Money} to {@link String}. It leverages a
 * {@link FormattedStringToDecimalConverter} for converting decimal values to and from their string
 * representations.
 *
 * @see FormattedStringToDecimalConverter
 */
public class StringToMoneyConverter implements Converter<String, Money> {

    @Serial
    private static final long serialVersionUID = 1L;
    private final FormattedStringToDecimalConverter stringToDecimalConverter = new FormattedStringToDecimalConverter();

    @Override
    public Result<Money> convertToModel(@CheckForNull String value, ValueContext context) {
        if (StringUtil.isBlank(value)) {
            return Result.ok(Money.NULL);
        }

        var trimmedValue = value.trim();
        var regex = "(.+)(\\w{3})";
        var matcher = Pattern.compile(regex).matcher(trimmedValue);

        if (!matcher.matches()) {
            return Result.error("Can't convert " + trimmedValue + " to Money. " +
                    "The last 3 characters must be a supported ISO 4217 currency code and the input string must contain at least 1 number.");
        }

        String decimalValue = matcher.group(1);
        String currencyCode = matcher.group(2).toUpperCase(Locale.ROOT);

        Currency currency;
        try {
            currency = Currency.getInstance(currencyCode);
        } catch (IllegalArgumentException e) {
            return Result.error("Can't convert " + trimmedValue
                    + " to Money. The last 3 characters must be a supported ISO 4217 currency code.");
        }

        return stringToDecimalConverter.convertToModel(decimalValue, context)
                .map(v -> Money.valueOf(v, currency));
    }

    @CheckForNull
    @Override
    public String convertToPresentation(@CheckForNull Money value, ValueContext context) {
        if (Money.NULL.equals(value) || value == null) {
            return null;
        }
        String decimalString = stringToDecimalConverter.convertToPresentation(value.getAmount(), context);
        String currencyString = value.getCurrency().getCurrencyCode();
        return String.format("%s %s", decimalString, currencyString);
    }

}
