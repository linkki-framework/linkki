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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;

class StringToDateConverterTest {

    private final StringToDateConverter converter = new StringToDateConverter();

    @Test
    void testConvertToModel_Null() {
        assertThat(converter.convertToModel(null, new ValueContext(new Binder<>()))).isEqualTo(Result.ok(null));
    }

    @Test
    void testConvertToModel_DE() {
        ValueContext context = new ValueContext(new Binder<>(), Locale.GERMANY);

        assertThat(converter.convertToModel("24.12.2023", context))
                .isEqualTo(Result.ok(dateOf(2023, 12, 24)));
    }

    @Test
    void testConvertToModel_EN_US() {
        ValueContext context = new ValueContext(new Binder<>(), Locale.US);

        assertThat(converter.convertToModel("12/24/2023", context))
                .isEqualTo(Result.ok(dateOf(2023, 12, 24)));
    }

    @Test
    void testConvertToModel_EN_UK() {
        ValueContext context = new ValueContext(new Binder<>(), Locale.UK);

        assertThat(converter.convertToModel("24/12/2023", context))
                .isEqualTo(Result.ok(dateOf(2023, 12, 24)));
    }

    @Test
    void testConvertToPresentation_Null() {
        ValueContext context = new ValueContext(new Binder<>(), Locale.GERMANY);

        assertThat(converter.convertToPresentation(null, context)).isNull();
    }

    @Test
    void testConvertToPresentation_DE() {
        ValueContext context = new ValueContext(new Binder<>(), Locale.GERMANY);

        assertThat(converter.convertToPresentation(dateOf(2023, 12, 24), context)).isEqualTo("24.12.2023");
        assertThat(converter.convertToPresentation(dateOf(23, 12, 24), context)).isEqualTo("24.12.0023");
    }

    @Test
    void testConvertToPresentation_EN_UK() {
        ValueContext context = new ValueContext(new Binder<>(), Locale.UK);

        assertThat(converter.convertToPresentation(dateOf(2023, 12, 24), context)).isEqualTo("24/12/2023");
        assertThat(converter.convertToPresentation(dateOf(23, 12, 24), context)).isEqualTo("24/12/0023");
    }

    @Test
    void testConvertToPresentation_EN_US() {
        ValueContext context = new ValueContext(new Binder<>(), Locale.US);

        assertThat(converter.convertToPresentation(dateOf(2023, 12, 24), context)).isEqualTo("12/24/2023");
        assertThat(converter.convertToPresentation(dateOf(23, 12, 24), context)).isEqualTo("12/24/0023");
    }

    private Date dateOf(int year, int month, int dayOfMonth) {
        LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
