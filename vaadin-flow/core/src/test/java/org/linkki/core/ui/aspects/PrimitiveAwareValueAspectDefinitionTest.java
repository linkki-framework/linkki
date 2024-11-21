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

package org.linkki.core.ui.aspects;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.ui.converters.FormattedStringToIntegerConverter;
import org.linkki.core.ui.converters.LinkkiConverterRegistry;
import org.linkki.core.ui.test.KaribuUIExtension;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.server.VaadinSession;

import edu.umd.cs.findbugs.annotations.CheckForNull;

@ExtendWith(KaribuUIExtension.class)
class PrimitiveAwareValueAspectDefinitionTest {

    @BeforeEach
    void addCustomConverter() {
        var customRegistry = LinkkiConverterRegistry.DEFAULT
                .with(new Converter<String, Integer>() {
                    @Override
                    public Result<Integer> convertToModel(@CheckForNull String value, ValueContext context) {
                        return Optional.ofNullable(value)
                                .map(Integer::valueOf)
                                .map(i -> 100 + i)
                                .map(Result::ok)
                                .orElse(Result.ok(null));
                    }

                    @Override
                    public String convertToPresentation(Integer value, ValueContext context) {
                        return String.valueOf(value);
                    }
                });
        VaadinSession.getCurrent().setAttribute(LinkkiConverterRegistry.class, customRegistry);
    }

    @Test
    void testNonPrimitiveType_FixedConverter() {
        var aspectDefinition = new PrimitiveAwareValueAspectDefinition(new FormattedStringToIntegerConverter(""));

        @SuppressWarnings("unchecked")
        var converter = (Converter<String, Integer>)aspectDefinition.getConverter(String.class, Integer.class);

        assertThat(converter.convertToModel("1", new ValueContext(new Binder<>())))
                .as("Should ignore converters in the registry")
                .isNotEqualTo(Result.ok(101));
        assertThat(converter.convertToModel(null, new ValueContext(new Binder<>())))
                .isEqualTo(Result.ok(null));
    }

    @Test
    void testNonPrimitiveType_ConverterFromRegistry() {
        var aspectDefinition = new PrimitiveAwareValueAspectDefinition();

        @SuppressWarnings("unchecked")
        var converter = (Converter<String, Integer>)aspectDefinition.getConverter(String.class, Integer.class);

        assertThat(converter.convertToModel("1", new ValueContext(new Binder<>())))
                .as("Custom converter from the registry should be used")
                .isEqualTo(Result.ok(101));
        assertThat(converter.convertToModel(null, new ValueContext(new Binder<>())))
                .isEqualTo(Result.ok(null));
    }

    @Test
    void testPrimitiveType_FixedConverter() {
        var aspectDefinition = new PrimitiveAwareValueAspectDefinition(new FormattedStringToIntegerConverter(""));

        @SuppressWarnings("unchecked")
        var converter = (Converter<String, Integer>)aspectDefinition.getConverter(String.class, Integer.TYPE);

        assertThat(converter.convertToModel("1", new ValueContext(new Binder<>())))
                .as("Should ignore converters in the registry")
                .isNotEqualTo(Result.ok(101));
        assertThat(converter.convertToModel(null, new ValueContext(new Binder<>())))
                .isEqualTo(Result.error("null value not allowed"));
    }

    @Test
    void testPrimitiveType_ConverterFromRegistry() {
        var aspectDefinition = new PrimitiveAwareValueAspectDefinition();

        @SuppressWarnings("unchecked")
        var converter = (Converter<String, Integer>)aspectDefinition.getConverter(String.class, Integer.TYPE);

        assertThat(converter.convertToModel("1", new ValueContext(new Binder<>())))
                .as("Custom converter from the registry should be used")
                .isEqualTo(Result.ok(101));
        assertThat(converter.convertToModel(null, new ValueContext(new Binder<>())))
                .isEqualTo(Result.error("null value not allowed"));
    }
}
