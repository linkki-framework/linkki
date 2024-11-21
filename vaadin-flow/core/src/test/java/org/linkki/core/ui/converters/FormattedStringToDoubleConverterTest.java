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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValueContext;

class FormattedStringToDoubleConverterTest {

    @Test
    void testConvertToPresentation() {
        FormattedStringToDoubleConverter converter = new FormattedStringToDoubleConverter("0.00");
        ValueContext context = new ValueContext(new Binder<>(), Locale.GERMAN);
        assertThat(converter.convertToPresentation(0.2, context)).isEqualTo("0,20");
    }

    @Test
    void testConvertToPresentation_Null() {
        FormattedStringToDoubleConverter converter = new FormattedStringToDoubleConverter("0.00");
        ValueContext context = new ValueContext(new Binder<>(), Locale.GERMAN);
        assertThat(converter.convertToPresentation(null, context)).isEmpty();
    }

    @Test
    void testConvertToModel() {
        FormattedStringToDoubleConverter converter = new FormattedStringToDoubleConverter("0.00");
        ValueContext context = new ValueContext(new Binder<>(), Locale.GERMAN);
        assertThat(converter.convertToModel("1,23", context).getOrThrow(AssertionError::new)).isEqualTo(1.23);
    }

    @Test
    void testConvertToModel_Null() {
        FormattedStringToDoubleConverter converter = new FormattedStringToDoubleConverter("0.00");
        ValueContext context = new ValueContext(new Binder<>(), Locale.GERMAN);
        assertThat(converter.convertToModel("", context).getOrThrow(AssertionError::new)).isNull();
    }

    @Test
    void testConvertToModel_ExtraLargeNumber() {
        FormattedStringToDoubleConverter converter = new FormattedStringToDoubleConverter("0.00");
        ValueContext context = new ValueContext(new Binder<>(), Locale.GERMAN);
        assertThat(converter
                .convertToModel("12345678901234567890123456789012345678901234567890123456789012345678901234567890",
                                context)
                .getOrThrow(AssertionError::new))
                        .isEqualTo(12345678901234567890123456789012345678901234567890123456789012345678901234567890d);
    }

    @Test
    void testConvertToPresentation_ExtraLargeNumber() {
        FormattedStringToDoubleConverter converter = new FormattedStringToDoubleConverter("0.00");
        ValueContext context = new ValueContext(new Binder<>(), Locale.GERMAN);
        assertThat(converter
                .convertToPresentation(12345678901234567890123456789012345678901234567890123456789012345678901234567890d,
                                       context))
                                               .isEqualTo("12345678901234568000000000000000000000000000000000000000000000000000000000000000,00");
    }

}
