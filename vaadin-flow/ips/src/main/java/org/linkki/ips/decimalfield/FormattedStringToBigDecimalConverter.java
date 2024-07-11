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
package org.linkki.ips.decimalfield;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.linkki.core.ui.converters.FormattedStringToNumberConverter;

import com.vaadin.flow.data.binder.Result;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Converts {@link BigDecimal} to {@link String} with a given format.
 *
 * @see DecimalFormat
 */
public class FormattedStringToBigDecimalConverter extends FormattedStringToNumberConverter<BigDecimal> {

    public static final String DEFAULT_FORMAT = "#,##0.00##";

    public FormattedStringToBigDecimalConverter() {
        this(DEFAULT_FORMAT);
    }

    public FormattedStringToBigDecimalConverter(String format) {
        super(format);
    }

    @Override
    @CheckForNull
    protected BigDecimal getNullValue() {
        return null;
    }

    @Override
    protected Result<BigDecimal> convertToModel(@NonNull Number value) {
        return value instanceof BigDecimal bigDecimal
                ? Result.ok(bigDecimal)
                : Result.ok(new BigDecimal(value.toString()));
    }
}
