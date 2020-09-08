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
package org.linkki.core.defaults.ui.aspects.types;

/**
 * Defines how the required state (whether a value must be entered/selected) of an UI component is
 * bound.
 */
public enum RequiredType {

    /** The UI component is always required. */
    REQUIRED,

    /** The UI component is required if the component is {@link EnabledType enabled}. */
    REQUIRED_IF_ENABLED,

    /** The UI component is optional. */
    NOT_REQUIRED,

    /**
     * The required state is read from the PMO by invoking a method named
     * {@code is[PropertyName]Required()}.
     */
    DYNAMIC,

}
