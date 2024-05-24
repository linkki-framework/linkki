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

import com.vaadin.flow.data.binder.ValueContext;

@Deprecated(since = "2.6.0")
class FormattedDoubleToStringConverterTest {

    @Test
    void testConvertToPresentation() {
        FormattedDoubleToStringConverter converter = new FormattedDoubleToStringConverter("0.00");
        ValueContext context = new ValueContext(Locale.GERMAN);
        assertThat(converter.convertToPresentation(0.2, context), is("0,20"));
    }

    @Test
    void testConvertToPresentation_Null() {
        FormattedDoubleToStringConverter converter = new FormattedDoubleToStringConverter("0.00");
        ValueContext context = new ValueContext(Locale.GERMAN);
        assertThat(converter.convertToPresentation(null, context), is(""));
    }

    @Test
    void testConvertToModel() {
        FormattedDoubleToStringConverter converter = new FormattedDoubleToStringConverter("0.00");
        ValueContext context = new ValueContext(Locale.GERMAN);
        assertThat(converter.convertToModel("1,23", context).getOrThrow(AssertionError::new), is(1.23));
    }

    @Test
    void testConvertToModel_Null() {
        FormattedDoubleToStringConverter converter = new FormattedDoubleToStringConverter("0.00");
        ValueContext context = new ValueContext(Locale.GERMAN);
        assertThat(converter.convertToModel("", context).getOrThrow(AssertionError::new), is(nullValue()));
    }
}
