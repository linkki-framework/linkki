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

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Severity levels for badge styling.
 */
public enum BadgeSeverity {

    /**
     * Informational badge with default styling.
     */
    INFO(null),
    /**
     * Success badge with positive styling.
     */
    SUCCESS("success"),
    /**
     * Warning badge for cautionary information.
     */
    WARNING("warning"),
    /**
     * Error badge for critical information.
     */
    ERROR("error"),
    /**
     * Neutral badge with contrast styling.
     */
    NONE("contrast");

    @CheckForNull
    private final String variantName;

    BadgeSeverity(@CheckForNull String variantName) {
        this.variantName = variantName;
    }

    @CheckForNull
    public String getVariantName() {
        return variantName;
    }
}
