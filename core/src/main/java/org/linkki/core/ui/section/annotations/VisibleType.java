/*
 * Copyright Faktor Zehn AG.
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

/** Defines whether an UI component is visible. */
public enum VisibleType {

    /** The UI component is always visible. */
    VISIBLE,

    /** The UI component is never visible. */
    INVISIBLE,

    /**
     * The visible state is read from the PMO by invoking a method named is[PropertyName]Visible().
     */
    DYNAMIC;

}
