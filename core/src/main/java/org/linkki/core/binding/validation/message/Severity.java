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

package org.linkki.core.binding.validation.message;

/**
 * Enum indicating the kind of severity a {@link Message} can have.
 * <p>
 * Values are sorted by increasing severity, so that the {@link Enum#compareTo(Enum) Enum's compareTo}
 * function can be used.
 */
public enum Severity {

    /**
     * Indicates that the {@link Message} contains nothing alarming but is purely informative in nature.
     */
    INFO,

    /**
     * Indicates that the user should be warned about the content of the {@link Message}. A confirmation
     * may be needed before the procedure that produced the {@link Message} continues.
     */
    WARNING,

    /**
     * Indicates that something went wrong. A {@link Message} of this severity may suggest that the
     * procedure which produced the {@link Message} should not be continued.
     */
    ERROR
}
