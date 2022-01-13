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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;

/**
 * Utility class for common component adjustments
 */
public class LinkkiComponentUtil {

    private LinkkiComponentUtil() {
        // no instances
    }

    /**
     * Prevents {@link Key#ENTER} to trigger button shortcut in the given component. This is useful for
     * multi-line components in which enter should only add a new line.
     */
    public static final void preventEnterKeyPropagation(Component multiLineInput) {
        Shortcuts.addShortcutListener(multiLineInput, e -> {
            // Does nothing. This listener prevents event propagation so that enter does not
            // trigger button enter shortcut
        }, Key.ENTER).allowBrowserDefault().listenOn(multiLineInput);
    }

}
