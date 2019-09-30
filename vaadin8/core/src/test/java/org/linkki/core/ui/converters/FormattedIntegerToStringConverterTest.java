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

package org.linkki.core.ui.converters;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.vaadin.data.ValueContext;

public class FormattedIntegerToStringConverterTest {

    @Test
    public void testConvertToPresentation() {
        FormattedIntegerToStringConverter converter = new FormattedIntegerToStringConverter("#,##0");
        ValueContext context = new ValueContext(Locale.GERMAN);
        assertThat(converter.convertToPresentation(12345, context), is("12.345"));
    }

    @Test
    public void testConvertToPresentation_Null() {
        FormattedIntegerToStringConverter converter = new FormattedIntegerToStringConverter("#,##0");
        ValueContext context = new ValueContext(Locale.GERMAN);
        assertThat(converter.convertToPresentation(null, context), is(""));
    }

    @Test
    public void testConvertToModel() {
        FormattedIntegerToStringConverter converter = new FormattedIntegerToStringConverter("#,##0");
        ValueContext context = new ValueContext(Locale.GERMAN);
        assertThat(converter.convertToModel("1.234", context).getOrThrow(s -> new AssertionError(s)), is(1234));
    }

    @Test
    public void testConvertToModel_Null() {
        FormattedIntegerToStringConverter converter = new FormattedIntegerToStringConverter("#,##0");
        ValueContext context = new ValueContext(Locale.GERMAN);
        assertThat(converter.convertToModel("", context).getOrThrow(s -> new AssertionError(s)), is(nullValue()));
    }
}
