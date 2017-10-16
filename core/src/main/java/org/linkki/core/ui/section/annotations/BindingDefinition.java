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
