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

import java.util.Locale;

import org.linkki.util.service.Services;

/**
 * Abstraction for UI framework specific code.
 * 
 * @implNote Delegates to the implementation of {@link UiFrameworkExtension} found via
 *           {@link Services#get(Class)}.
 */
public final class UiFramework {

    private UiFramework() {
        // static util
    }

    /**
     * Returns the locale defined for the UI session, may differ from the System locale.
     * 
     * @return the locale defined for the UI session
     */
    public static Locale getLocale() {
        return get().getLocale();
    }

    public static UiFrameworkExtension get() {
        return Services.get(UiFrameworkExtension.class);
    }
}
