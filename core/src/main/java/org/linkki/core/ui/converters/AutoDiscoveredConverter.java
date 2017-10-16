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

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractField;

/**
 * Marker for a {@link Converter} that should be automatically discovered an used by the
 * {@link LinkkiConverterFactory}.
 * <p>
 * This interface does not extend {@link Converter}, because some CDI implementations have problems
 * with generic types, but all implementations must also implement {@link Converter} to be returned
 * from the {@link LinkkiConverterFactory}.
 * <p>
 * A {@link Converter} not implementing {@link AutoDiscoveredConverter} can still be set manually by
 * {@link AbstractField#setConverter(Converter)}.
 */
public interface AutoDiscoveredConverter {
    // only a marker
}
