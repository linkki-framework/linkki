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

package org.linkki.core.defaults.ui.aspects.types;

/**
 * Defines the type of the tooltip binding.
 */
public enum TooltipType {

    /**
     * The tooltip is static and given by the value attribute.
     */
    STATIC,

    /**
     * Tooltip is bound to the property using the method {@code get<PropertyName>Tooltip()}.
     */
    DYNAMIC,

    /**
     * Linkki decides whether the tooltip is {@link #DYNAMIC} or {@link #STATIC}. In case the value
     * is the empty string it calls a method named {@code get<PropertyName>Tooltip()}. Otherwise the
     * specified value is used as tooltip.
     */
    AUTO;
}