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

package org.linkki.core.vaadin.component;

/**
 * Styles that define the appearance of a badge independent of its {@link BadgeSeverity}. The
 * constants are the names of the theme variants that are applied to the badge.
 * <p>
 * The neutral {@code contrast} appearance is not part of this class, as it is defined by
 * {@link BadgeSeverity#NONE}.
 */
public final class BadgeStyleVariant {

    /**
     * Compact badge with reduced size.
     */
    public static final String SMALL = "small";
    /**
     * Badge with a filled background in the color of its {@link BadgeSeverity}.
     */
    public static final String FILLED = "filled";
    /**
     * Badge with slightly rounded corners instead of the pill shape.
     */
    public static final String SQUARE = "square";
    /**
     * Badge that is rendered as a small dot indicator. The content is visually hidden.
     */
    public static final String DOT = "dot";

    private BadgeStyleVariant() {
        // constant class
    }
}
