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
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

/**
 * Wraps a converter and returns an {@link Result#error(String) error} if the wrapped converter
 * would return {@code null} as the model value.
 */
public class NullHandlingConverterWrapper<PRESENTATION, MODEL> implements Converter<PRESENTATION, MODEL> {

    private static final long serialVersionUID = 1L;

    private final Converter<PRESENTATION, MODEL> converter;

    public NullHandlingConverterWrapper(Converter<PRESENTATION, MODEL> converter) {
        this.converter = converter;
    }

    @Override
    public Result<MODEL> convertToModel(PRESENTATION value, ValueContext context) {
        Result<MODEL> result = converter.convertToModel(value, context);
        if (!result.isError()) {
            // getOrThrow should always return a value if the result is OK
            MODEL convertedValue = result.getOrThrow(IllegalStateException::new);
            if (convertedValue == null) {
                return Result.error("null value not allowed");
            }
        }

        return result;
    }

    @Override
    public PRESENTATION convertToPresentation(MODEL value, ValueContext context) {
        return converter.convertToPresentation(value, context);
    }

}
