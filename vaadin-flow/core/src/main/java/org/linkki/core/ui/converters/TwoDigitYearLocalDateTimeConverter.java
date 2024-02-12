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

import java.time.LocalDateTime;

import org.linkki.util.TwoDigitYearUtil;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Converts {@link LocalDateTime} to {@link LocalDateTime} while recalculating two digit years into
 * four digit years based on the -80 / +20 rule during the convertToModel conversion. E.g. 19/01/01
 * will be converted to 2019/01/01 and 90/01/01 to 1990/01/01.
 */
public class TwoDigitYearLocalDateTimeConverter implements Converter<LocalDateTime, LocalDateTime> {

    private static final long serialVersionUID = -7168406748935260872L;

    @CheckForNull
    @Override
    public Result<LocalDateTime> convertToModel(@CheckForNull LocalDateTime value, ValueContext context) {
        if (value == null) {
            return Result.ok(null);
        } else {
            return Result.ok(TwoDigitYearUtil.convert(value));
        }
    }

    @Override
    public LocalDateTime convertToPresentation(LocalDateTime value, ValueContext context) {
        return value;
    }
}
