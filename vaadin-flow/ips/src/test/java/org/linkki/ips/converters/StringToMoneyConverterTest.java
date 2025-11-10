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

package org.linkki.ips.converters;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Currency;
import java.util.Locale;

import org.faktorips.values.Decimal;
import org.faktorips.values.Money;
import org.junit.jupiter.api.Test;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;

class StringToMoneyConverterTest {

    private final StringToMoneyConverter converter = new StringToMoneyConverter();
    private final ValueContext valueContext = new ValueContext(Locale.GERMANY);

    @Test
    void testConvertToModel() {
        var money = Money.euro(100, 11);
        assertThat(converter.convertToModel("100,11 EUR", valueContext)).isEqualTo(Result.ok(money));
    }

    @Test
    void testConvertToModel_CaseSensitive() {
        var money = Money.valueOf(Decimal.valueOf(100.11), Currency.getInstance("EUR"));
        assertThat(converter.convertToModel("100,11 EUr", valueContext)).isEqualTo(Result.ok(money));
    }

    @Test
    void testConvertToModel_Trim() {
        var money = Money.euro(100, 11);
        assertThat(converter.convertToModel("100,11 EUR    ", valueContext)).isEqualTo(Result.ok(money));
        assertThat(converter.convertToModel("    100,11 EUR    ", valueContext)).isEqualTo(Result.ok(money));
        assertThat(converter.convertToModel("    100,11 EUR", valueContext)).isEqualTo(Result.ok(money));
        assertThat(converter.convertToModel("100,11EUR", valueContext)).isEqualTo(Result.ok(money));
        assertThat(converter.convertToModel("100,11     EUR", valueContext)).isEqualTo(Result.ok(money));
    }

    @Test
    void testConvertToModel_InvalidCurrency() {
        var result = converter.convertToModel("100,11 BLA", valueContext);

        assertThat(result.isError()).isTrue();
    }

    @Test
    void testConvertToModel_IncorrectFormat() {
        assertThat(converter.convertToModel("EUR", valueContext).isError()).isTrue();
        assertThat(converter.convertToModel("abc EUR", valueContext).isError()).isTrue();
    }

    @Test
    void testConvertToModel_ShouldIgnorePoint() {
        var money = Money.euro(10011);
        assertThat(converter.convertToModel("100.11 EUR", valueContext)).isEqualTo(Result.ok(money));
    }

    @Test
    void testConvertToModel_ShouldReturnMoneyNullBecauseOfEmptyString() {
        assertThat(converter.convertToModel("", valueContext)).isEqualTo(Result.ok(Money.NULL));
    }

    @Test
    void testConvertToModel_ShouldReturnMoneyNullBecauseOfNull() {
        assertThat(converter.convertToModel(null, valueContext)).isEqualTo(Result.ok(Money.NULL));
    }

    @Test
    void testConvertToPresentation() {
        var money = Money.euro(100, 11);
        assertThat(converter.convertToPresentation(money, valueContext)).isEqualTo("100,11 EUR");
    }

    @Test
    void testConvertToPresentation_ShouldReturnMoneyNull() {
        assertThat(converter.convertToPresentation(Money.NULL, valueContext)).isNull();
        assertThat(converter.convertToPresentation(null, valueContext)).isNull();
    }

}
