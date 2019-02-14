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

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.StringToDateConverter;

public class LinkkiConverterRegistryTest {

    @Test
    public void testFindConverter_Default() {
        LinkkiConverterRegistry linkkiConverterRegistry = new LinkkiConverterRegistry();

        assertThat(linkkiConverterRegistry.findConverter(String.class, Date.class),
                   is(instanceOf(StringToDateConverter.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindConverter_NotFound() {
        new LinkkiConverterRegistry().findConverter(String.class, java.time.LocalDate.class);
    }

    @Test
    public void testFindConverter_overrideDefault() {
        LinkkiConverterRegistry linkkiConverterRegistry = new LinkkiConverterRegistry(new MyStringToDateConverter());

        assertThat(linkkiConverterRegistry.findConverter(String.class, Date.class),
                   is(instanceOf(MyStringToDateConverter.class)));
    }

    @SuppressWarnings("null")
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

}
