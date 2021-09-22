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

package org.linkki.core.ui;

import java.util.Optional;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;

/**
 * Utility class for common component adjustments
 */
public class LinkkiComponentUtil {

    private LinkkiComponentUtil() {
        // no instances
    }

    /**
     * Adds a click shortcut to the given {@link Button}
     * <p>
     * <b>The shortcut event is not triggered in case, a vaadin-text-area is currently focused</b>
     * 
     * @param button The {@link Button} to add the shortcut click listener
     * @param key The {@link Key shortcut key}
     * @param keyModifier Additional {@link KeyModifier}
     */
    public static final void addShortcutRegistration(Button button, Key key, KeyModifier... keyModifier) {
        Shortcuts.addShortcutListener(button, e -> {
            if (button.isVisible() && button.isEnabled()) {
                UI.getCurrent().getElement().executeJs("document.activeElement.blur()")
                        .then(v -> button.clickInClient());
            }
        }, key, Optional.ofNullable(keyModifier).orElse(new KeyModifier[] {})).allowEventPropagation();
    }

}
