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
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.math.BigInteger;
import java.util.Currency;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.converter.StringToBigIntegerConverter;
import com.vaadin.flow.data.converter.StringToDoubleConverter;

class LinkkiConverterRegistryTest {

    @Test
    void testFindConverter_Default() {
        LinkkiConverterRegistry linkkiConverterRegistry = new LinkkiConverterRegistry();

        assertThat(linkkiConverterRegistry.findConverter(String.class, Date.class),
                   is(instanceOf(StringToDateConverter.class)));
    }

    @Test
    void testFindConverter_NotFound() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new LinkkiConverterRegistry().findConverter(String.class, Currency.class);
        });
    }

    @Test
    void testFindConverter_Subclass() {
        LinkkiConverterRegistry linkkiConverterRegistry = new LinkkiConverterRegistry(new MyStringToNumberConverter());

        assertThat(linkkiConverterRegistry.findConverter(String.class, CustomBigInteger.class),
                   is(instanceOf(StringToBigIntegerConverter.class)));
    }

    @Test
    void testFindConverter_MostSpecificUnordered() {
        LinkkiConverterRegistry linkkiConverterRegistry = new LinkkiConverterRegistry(
                new MyStringToBigIntegerConverter(), new MyStringToNumberConverter());

        assertThat(linkkiConverterRegistry.findConverter(String.class, CustomBigInteger.class),
                   is(instanceOf(MyStringToBigIntegerConverter.class)));
        assertThat(linkkiConverterRegistry.findConverter(String.class, BigInteger.class),
                   is(instanceOf(MyStringToBigIntegerConverter.class)));
        assertThat(linkkiConverterRegistry.findConverter(String.class, Double.class),
                   is(instanceOf(StringToDoubleConverter.class)));
    }

    @Test
    void testFindConverter_MostSpecificOrdered() {
        LinkkiConverterRegistry linkkiConverterRegistry = new LinkkiConverterRegistry(
                new MyStringToNumberConverter(), new MyStringToBigIntegerConverter());

        assertThat(linkkiConverterRegistry.findConverter(String.class, CustomBigInteger.class),
                   is(instanceOf(MyStringToBigIntegerConverter.class)));
        assertThat(linkkiConverterRegistry.findConverter(String.class, BigInteger.class),
                   is(instanceOf(MyStringToBigIntegerConverter.class)));
        assertThat(linkkiConverterRegistry.findConverter(String.class, Double.class),
                   is(instanceOf(StringToDoubleConverter.class)));
    }

    @Test
    void testFindConverter_MostSpecificEquals() {
        LinkkiConverterRegistry linkkiConverterRegistry = new LinkkiConverterRegistry(
                new MyStringToBigIntegerConverter(), new MyStringToBigIntegerConverter(),
                new MyStringToNumberConverter());

        assertThat(linkkiConverterRegistry.findConverter(String.class, BigInteger.class),
                   is(instanceOf(MyStringToBigIntegerConverter.class)));
    }

    @Test
    void testFindConverter_OverrideDefault() {
        LinkkiConverterRegistry linkkiConverterRegistry = new LinkkiConverterRegistry(new MyStringToDateConverter());

        assertThat(linkkiConverterRegistry.findConverter(String.class, Date.class),
                   is(instanceOf(MyStringToDateConverter.class)));
        assertThat(linkkiConverterRegistry.findConverter(String.class, Double.class),
                   is(instanceOf(StringToDoubleConverter.class)));
    }

    public static class MyStringToDateConverter implements Converter<String, Date> {

        private static final long serialVersionUID = 1L;

        @Override
        public Result<Date> convertToModel(String value, ValueContext context) {
            return null;
        }

        @Override
        public String convertToPresentation(Date value, ValueContext context) {
            return null;
        }

    }

    public static class MyStringToNumberConverter implements Converter<String, Number> {

        private static final long serialVersionUID = 1L;

        @Override
        public Result<Number> convertToModel(String value, ValueContext context) {
            return null;
        }

        @Override
        public String convertToPresentation(Number value, ValueContext context) {
            return null;
        }

    }

    public static class MyStringToBigIntegerConverter implements Converter<String, BigInteger> {

        private static final long serialVersionUID = 1L;

        @Override
        public String convertToPresentation(BigInteger value, ValueContext context) {
            return null;
        }

        @Override
        public Result<BigInteger> convertToModel(String value, ValueContext context) {
            return null;
        }

    }

    public static class MyStringToCustomBigIntegerConverter implements Converter<String, CustomBigInteger> {

        private static final long serialVersionUID = 1L;

        @Override
        public Result<CustomBigInteger> convertToModel(String value, ValueContext context) {
            return null;
        }

        @Override
        public String convertToPresentation(CustomBigInteger value, ValueContext context) {
            return null;
        }

    }

    public static class CustomBigInteger extends BigInteger {

        private static final long serialVersionUID = 1L;

        public CustomBigInteger(String val, int radix) {
            super(val, radix);
        }

    }

}
