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

import java.io.Serial;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.vaadin.flow.data.binder.Result;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Converts {@link Integer} to {@link String} while taking a given format into account.
 * 
 * @see DecimalFormat
 */
public class FormattedStringToIntegerConverter extends FormattedStringToNumberConverter<Integer> {

    @Serial
    private static final long serialVersionUID = 6756969882235490962L;

    /**
     * Creates a new converter with a given format
     *
     * @param format number format according to {@link DecimalFormat}
     */
    public FormattedStringToIntegerConverter(String format) {
        super(format);
    }

    @Override
    @CheckForNull
    protected Integer getNullValue() {
        return null;
    }

    @Override
    @CheckForNull
    protected Result<Integer> convertToModel(Number value) {
        var bigInt = BigDecimal.valueOf(value.doubleValue());
        if (bigInt.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0) {
            return Result.error("The value is too big");
        } else {
            return Result.ok(value.intValue());
        }
    }

}