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

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Type;

import org.linkki.core.ui.converters.NullHandlingConverterWrapper;

import com.vaadin.flow.data.converter.Converter;

/**
 * {@link ValueAspectDefinition} that prevents {@code null} from being set if the model type is a
 * primitive class.
 */
public class PrimitiveAwareValueAspectDefinition extends ValueAspectDefinition {

    private final Converter<?, ?> primitiveConverter;

    public PrimitiveAwareValueAspectDefinition(Converter<?, ?> converter) {
        super(requireNonNull(converter));
        this.primitiveConverter = new NullHandlingConverterWrapper<>(converter);
    }

    @Override
    protected Converter<?, ?> getConverter(Type presentationType, Type modelType) {
        if (modelType instanceof Class<?> && ((Class<?>)modelType).isPrimitive()) {
            return primitiveConverter;
        } else {
            return super.getConverter(presentationType, modelType);
        }
    }

}
