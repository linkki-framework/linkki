/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

/**
 * A common interface for all annotations describing bound UI elements.
 */
public interface BindingDefinition {

    /** If and how the enabled state of the UI element is bound to the PMO. */
    EnabledType enabled();

    /** If and how the visible state of the UI element is bound to the PMO. */
    VisibleType visible();

    /**
     * If and how the required state of the UI element is bound to the PMO. Ignored for UI elements
     * that cannot be required, e.g. buttons.
     */
    RequiredType required();

    /**
     * If and how the available values are bound to the PMO. Relevant only for UI elements that have
     * available values (e.g. combo boxes), ignored for all other elements.
     */
    default AvailableValuesType availableValues() {
        return AvailableValuesType.NO_VALUES;
    }
}
