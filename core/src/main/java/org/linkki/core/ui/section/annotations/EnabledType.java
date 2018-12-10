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
package org.linkki.core.ui.section.annotations;

/** The type how the enabled state of an UI component is bound. */
public enum EnabledType {

    /** The UI component is always enabled. */
    ENABLED,

    /** The UI component is always disabled. */
    DISABLED,

    /**
     * The enabled state is read from the PMO by invoking a method named is[PropertyName]Enabled().
     */
    DYNAMIC;

}
