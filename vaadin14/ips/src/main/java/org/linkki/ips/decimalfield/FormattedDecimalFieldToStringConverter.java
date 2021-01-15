/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.ips.decimalfield;

import java.text.DecimalFormat;

import org.faktorips.values.Decimal;
import org.faktorips.values.NullObject;
import org.linkki.core.ui.converters.FormattedNumberToStringConverter;

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
public class FormattedDecimalFieldToStringConverter
        extends FormattedNumberToStringConverter<Decimal> {

    private static final long serialVersionUID = 2694562525960273214L;

    public FormattedDecimalFieldToStringConverter(String format) {
        super(format);
    }

    @Override
    protected Decimal convertToModel(Number value) {
        return Decimal.valueOf(value.doubleValue());
    }

    @Override
    @NonNull
    protected Decimal getNullValue() {
        return Decimal.NULL;
    }

    @Override
    public Result<Decimal> convertToModel(String value, ValueContext context) {
        return super.convertToModel(value, context);
    }

    @Override
    public String convertToPresentation(@CheckForNull Decimal value, ValueContext context) {
        if (value == null || value.isNull()) {
            return "";
        } else {
            return super.convertToPresentation(value, context);
        }
    }
}