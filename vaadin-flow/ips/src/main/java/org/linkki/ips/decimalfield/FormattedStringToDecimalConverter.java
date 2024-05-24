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

import java.io.Serial;
import java.text.DecimalFormat;

import org.faktorips.values.Decimal;
import org.faktorips.values.NullObject;
import org.linkki.core.ui.converters.FormattedStringToNumberConverter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Converts {@link Decimal} to {@link String} while taking a given format and the {@link NullObject}
 * pattern into account.
 * 
 * @see DecimalFormat
 */
public class FormattedStringToDecimalConverter
        extends FormattedStringToNumberConverter<Decimal> {

    public static final String DEFAULT_FORMAT = "#,##0.00##";

    @Serial
    private static final long serialVersionUID = 2694562525960273214L;

    public FormattedStringToDecimalConverter() {
        this(DEFAULT_FORMAT);
    }

    public FormattedStringToDecimalConverter(String format) {
        super(format);
    }

    @Override
    protected Result<Decimal> convertToModel(Number value) {
        if (value instanceof Decimal d) {
            return Result.ok(d);
        }
        return Result.ok(Decimal.valueOf(value.doubleValue()));
    }

    @Override
    @NonNull
    protected Decimal getNullValue() {
        return Decimal.NULL;
    }

    @Override
    public String convertToPresentation(@CheckForNull Decimal value, ValueContext context) {
        if (value == null || value.isNull()) {
            return super.convertToPresentation(null, context);
        }
        return super.convertToPresentation(value, context);
    }

}