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

import com.vaadin.flow.data.binder.Result;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Converts {@link Long} to {@link String} while taking a given format into account.
 *
 * @see DecimalFormat
 */
public class FormattedStringToLongConverter extends FormattedStringToNumberConverter<Long> {

    @Serial
    private static final long serialVersionUID = 6756969882235490962L;

    /**
     * Creates a new converter with a given format
     *
     * @param format number format according to {@link DecimalFormat}
     */
    public FormattedStringToLongConverter(String format) {
        super(format);
    }

    @Override
    @CheckForNull
    protected Long getNullValue() {
        return null;
    }

    @Override
    @CheckForNull
    protected Result<Long> convertToModel(Number value) {
        var bigLong = BigDecimal.valueOf(value.doubleValue());
        return bigLong.compareTo(
                BigDecimal.valueOf(Long.MAX_VALUE)) > 0
                ? Result.error("The value is too big")
                : Result.ok(value.longValue());
    }
}
