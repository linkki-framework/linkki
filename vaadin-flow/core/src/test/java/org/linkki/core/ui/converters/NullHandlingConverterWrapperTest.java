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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;

public class NullHandlingConverterWrapperTest {

    @Test
    public void testConvertToModel_NullValue() {
        FormattedIntegerToStringConverter converter = new FormattedIntegerToStringConverter("#,##0");
        NullHandlingConverterWrapper<String, Integer> wrapper = new NullHandlingConverterWrapper<>(converter);
        ValueContext context = new ValueContext(Locale.GERMAN);

        Result<Integer> converterResult = converter.convertToModel("", context);
        Result<Integer> wrapperResult = wrapper.convertToModel("", context);

        // converter returns null -> wrapper returns error
        assertThat(converterResult.isError(), is(false));
        assertThat(getValue(converterResult), is(nullValue()));
        assertThat(wrapperResult.isError(), is(true));
    }

    @Test
    public void testConvertToModel_NonnullValue() {
        FormattedIntegerToStringConverter converter = new FormattedIntegerToStringConverter("#,##0");
        NullHandlingConverterWrapper<String, Integer> wrapper = new NullHandlingConverterWrapper<>(converter);
        ValueContext context = new ValueContext(Locale.GERMAN);

        Result<Integer> converterResult = converter.convertToModel("123", context);
        Result<Integer> wrapperResult = wrapper.convertToModel("123", context);

        // converter returns value -> wrapper returns same value
        assertThat(converterResult.isError(), is(false));
        assertThat(getValue(converterResult), is(123));
        assertThat(wrapperResult.isError(), is(false));
        assertThat(getValue(wrapperResult), is(123));
    }

    @Test
    public void testConvertToModel_Error() {
        FormattedIntegerToStringConverter converter = new FormattedIntegerToStringConverter("#,##0");
        NullHandlingConverterWrapper<String, Integer> wrapper = new NullHandlingConverterWrapper<>(converter);
        ValueContext context = new ValueContext(Locale.GERMAN);

        Result<Integer> converterResult = converter.convertToModel("xyz", context);
        Result<Integer> wrapperResult = wrapper.convertToModel("xyz", context);

        // converter returns error -> wrapper returns error
        assertThat(converterResult.isError(), is(true));
        assertThat(wrapperResult.isError(), is(true));
    }

    private <T> T getValue(Result<T> result) {
        return result.getOrThrow(IllegalStateException::new);
    }

}
