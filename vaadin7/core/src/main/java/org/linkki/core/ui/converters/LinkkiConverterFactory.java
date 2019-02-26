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

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.linkki.util.Sequence;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.ConverterFactory;
import com.vaadin.data.util.converter.DefaultConverterFactory;
import com.vaadin.server.VaadinSession;

/**
 * For this {@link ConverterFactory} to be used by Vaadin, it must be
 * {@link VaadinSession#setConverterFactory(ConverterFactory) set in the VaadinSession}.
 * 
 * @see JodaConverters
 */
public class LinkkiConverterFactory extends DefaultConverterFactory {

    public static final Sequence<Converter<?, ?>> DEFAULT_JAVA_8_DATE_CONVERTERS = Sequence.of(
                                                                                               new LocalDateTimeToStringConverter(),
                                                                                               new LocalDateToDateConverter(),
                                                                                               new LocalDateToStringConverter());


    private static final long serialVersionUID = 1L;

    private Supplier<@NonNull Sequence<@NonNull Converter<?, ?>>> converterFinder;

    public LinkkiConverterFactory() {
        this(() -> DEFAULT_JAVA_8_DATE_CONVERTERS);
    }

    public LinkkiConverterFactory(Supplier<Sequence<Converter<?, ?>>> converterFinder) {
        this.converterFinder = requireNonNull(converterFinder, "converterFinder must not be null");
    }

    @Override
    @Nullable
    protected <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL> findConverter(
            @Nullable Class<PRESENTATION> presentationType,
            @Nullable Class<MODEL> modelType) {
        @SuppressWarnings("unchecked")
        @Nullable
        Converter<PRESENTATION, MODEL> converter = converterFinder.get().stream()
                .filter(Converter.class::isInstance)
                .map(Converter.class::cast)
                .filter(c -> c.getPresentationType().isAssignableFrom(presentationType)
                        && c.getModelType().isAssignableFrom(modelType))
                .findFirst()
                .orElseGet(() -> super.findConverter(presentationType, modelType));
        return converter;
    }

}
