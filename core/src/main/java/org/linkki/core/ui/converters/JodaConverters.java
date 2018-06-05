/*
 * Copyright Faktor Zehn AG.
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

import org.linkki.util.Sequence;

import com.vaadin.data.util.converter.Converter;

/**
 * {@link Converter Converters} for Joda date classes to be used with
 * {@link LinkkiConverterFactory}.
 */
public final class JodaConverters {

    public static final Sequence<Converter<?, ?>> DEFAULT_JODA_DATE_CONVERTERS = Sequence.of(
                                                                                             new JodaLocalDateTimeToStringConverter(),
                                                                                             new JodaLocalDateToDateConverter(),
                                                                                             new JodaLocalDateToStringConverter());

    private JodaConverters() {
        throw new AssertionError("This is just a utility class and therefor the constructor should not be called");
    }

}
