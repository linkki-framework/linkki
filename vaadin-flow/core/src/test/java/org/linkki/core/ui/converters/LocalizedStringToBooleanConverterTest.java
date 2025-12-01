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

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;

class LocalizedStringToBooleanConverterTest {

    @Test
    void testConvertToModel() {
        var converter = new LocalizedStringToBooleanConverter();

        assertThat(converter.convertToModel("Yes", valueContext(Locale.ENGLISH)))
                .as("english \"Yes\" should convert to true")
                .isEqualTo(Result.ok(Boolean.TRUE));

        assertThat(converter.convertToModel("No", valueContext(Locale.ENGLISH)))
                .as("english \"No\" should convert to false")
                .isEqualTo(Result.ok(Boolean.FALSE));

        assertThat(converter.convertToModel("Ja", valueContext(Locale.GERMAN)))
                .as("german \"Ja\" should convert to true")
                .isEqualTo(Result.ok(Boolean.TRUE));

        assertThat(converter.convertToModel("Nein", valueContext(Locale.GERMAN)))
                .as("german \"Nein\" should convert to false")
                .isEqualTo(Result.ok(Boolean.FALSE));
    }

    @Test
    void testConvertToModel_Null() {
        var converter = new LocalizedStringToBooleanConverter();

        assertThat(converter.convertToModel(null, null))
                .as("null should convert to null")
                .isEqualTo(Result.ok(null));
    }

    @Test
    void testConvertToPresentation() {
        var converter = new LocalizedStringToBooleanConverter();

        assertThat(converter.convertToPresentation(Boolean.TRUE, valueContext(Locale.ENGLISH)))
                .as("Boolean true should convert to english \"Yes\"")
                .isEqualTo("Yes");
        assertThat(converter.convertToPresentation(Boolean.FALSE, valueContext(Locale.ENGLISH)))
                .as("Boolean false should convert to english \"Yes\"")
                .isEqualTo("No");
        assertThat(converter.convertToPresentation(Boolean.TRUE, valueContext(Locale.GERMAN)))
                .as("Boolean true should convert to german \"Ja\"")
                .isEqualTo("Ja");
        assertThat(converter.convertToPresentation(Boolean.FALSE, valueContext(Locale.GERMAN)))
                .as("Boolean false should convert to german \"Nein\"")
                .isEqualTo("Nein");
    }

    @Test
    void testConvertToPresentation_Null() {
        var converter = new LocalizedStringToBooleanConverter();

        assertThat(converter.convertToPresentation(null, valueContext(Locale.ENGLISH)))
                .as("null should convert to null")
                .isNull();
    }

    private static ValueContext valueContext(Locale locale) {
        return new ValueContext(null, null, null, locale);
    }

}