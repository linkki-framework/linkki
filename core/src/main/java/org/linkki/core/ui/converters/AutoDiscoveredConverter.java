/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

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
