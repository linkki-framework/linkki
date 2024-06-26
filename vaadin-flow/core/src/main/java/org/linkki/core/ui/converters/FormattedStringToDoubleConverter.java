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
import java.text.DecimalFormat;

import com.vaadin.flow.data.binder.Result;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Converts {@link Double} to {@link String} while taking a given format into account.
 * 
 * @see DecimalFormat
 */
public class FormattedStringToDoubleConverter extends FormattedStringToNumberConverter<Double> {

    @Serial
    private static final long serialVersionUID = 6756969882235490962L;

    public FormattedStringToDoubleConverter(String format) {
        super(format);
    }

    @Override
    @CheckForNull
    protected Double getNullValue() {
        return null;
    }

    @Override
    protected Result<Double> convertToModel(Number value) {
        return Result.ok(value.doubleValue());
    }

}