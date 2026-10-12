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

package org.linkki.core.ui.element.annotation;

import org.linkki.core.vaadin.component.BadgeStyleVariant;

/**
 * Variants that define the style of a badge.
 *
 * @deprecated Use {@link BadgeStyleVariant} instead. The name was easily confused with Vaadin's
 *             {@code BadgeVariant}. {@link #PILL} has no replacement, as the pill shape is the default
 *             of a badge.
 */
@Deprecated(since = "2.11.0")
public final class BadgeVariant {

    /**
     * Rounded badge with pill-shaped styling.
     */
    public static final String PILL = "pill";
    /**
     * Compact badge with reduced size.
     */
    public static final String SMALL = "small";
    /**
     * Badge with primary theme color.
     */
    public static final String PRIMARY = "primary";

    private BadgeVariant() {
        // constant class
    }
}
