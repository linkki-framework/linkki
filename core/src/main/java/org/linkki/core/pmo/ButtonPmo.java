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
package org.linkki.core.pmo;

import java.util.List;

/** A presentation model object for a button which only displays an icon (no text). */
public interface ButtonPmo {

    /** Executes the button click action. */
    void onClick();

    /**
     * Returns the icon to display for the button.
     * <p>
     * The kind of object depends on the current UI framework. It needs to be accepted by the
     * corresponding button implementation.
     */
    Object getButtonIcon();

    /** Returns the style names for the button. */
    List<String> getStyleNames();

    /**
     * Returns <code>true</code> if the button is enabled, otherwise <code>false</code>.
     */
    default boolean isEnabled() {
        return true;
    }

    /**
     * Returns <code>true</code> if the button is visible, otherwise <code>false</code>.
     */
    default boolean isVisible() {
        return true;
    }

}
