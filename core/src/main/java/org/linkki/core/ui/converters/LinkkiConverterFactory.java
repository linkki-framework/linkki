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

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.function.Supplier;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.apache.deltaspike.core.api.provider.BeanProvider;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.ConverterFactory;
import com.vaadin.data.util.converter.DefaultConverterFactory;
import com.vaadin.server.VaadinSession;

/**
 * Finds all {@link AutoDiscoveredConverter AutoDiscoveredConverters} via CDI and returns the first
 * matching converter, falling back to Vaadin's {@link DefaultConverterFactory}.
 * <p>
 * For this {@link ConverterFactory} to be used by Vaadin, it must be
 * {@link VaadinSession#setConverterFactory(ConverterFactory) set in the VaadinSession}. The
 * {@code org.linkki.framework.ui.application.ApplicationFrame.init(UI)} does this by default.
 */
public class LinkkiConverterFactory extends DefaultConverterFactory {

    private static final long serialVersionUID = 1L;

    private Supplier<Collection<AutoDiscoveredConverter>> converterFinder;

    public LinkkiConverterFactory() {
        converterFinder = () -> BeanProvider.getContextualReferences(AutoDiscoveredConverter.class, true);
    }

    LinkkiConverterFactory(Supplier<Collection<AutoDiscoveredConverter>> converterFinder) {
        this.converterFinder = requireNonNull(converterFinder, "converterFinder must not be null");
    }

    @Override
    @CheckForNull
    protected <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL> findConverter(
            @Nullable Class<PRESENTATION> presentationType, @Nullable Class<MODEL> modelType) {
        @SuppressWarnings("unchecked")
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
