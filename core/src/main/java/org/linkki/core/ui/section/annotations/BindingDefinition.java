/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import java.util.Collections;
import java.util.List;

/**
 * A common interface for all annotations describing bound UI elements.
 */
public interface BindingDefinition {

    EnabledType enabled();

    VisibleType visible();

    RequiredType required();

    default AvailableValuesType availableValues() {
        return AvailableValuesType.NO_VALUES;
    }

    // TODO Name?
    default List<Object> getAvailableValues() {
        return Collections.emptyList();
    }
}
