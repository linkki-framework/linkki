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

package org.linkki.core.ui.aspects.types;

import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UILink;

/**
 * Defines the position of an icon.
 *
 * @see UILabel#iconPosition()
 * @see UILink#iconPosition()
 */
public enum IconPosition {

    /**
     * The icon is displayed on the left side.
     */
    LEFT,
    /**
     * The icon is displayed on the right side.
     */
    RIGHT
}
