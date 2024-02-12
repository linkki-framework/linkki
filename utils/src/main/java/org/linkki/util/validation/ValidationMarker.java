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
package org.linkki.util.validation;

import edu.umd.cs.findbugs.annotations.NonNull;

/** Interface for validation message markers. */
@FunctionalInterface
public interface ValidationMarker {

    /**
     * Marks a mandatory field and returns {@code true} for {@code isRequiredInformationMissing()}.
     */
    static @NonNull ValidationMarker REQUIRED = () -> true;

    /** Returns {@code true} if the marker marks a validation for a mandatory field. */
    boolean isRequiredInformationMissing();

}
