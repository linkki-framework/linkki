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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;

/**
 * Defines the behavior when setting a component to read only.
 * {@link ReadOnlyBehaviorType#DISABLED}, {@link ReadOnlyBehaviorType#INVISIBLE},
 * {@link ReadOnlyBehaviorType#INVISIBLE_IF_WRITABLE} are currently only supported for buttons.
 * {@link ReadOnlyBehaviorType#WRITABLE} is only supported by {@link Component components} that
 * implement {@link HasValue}.
 */
public enum ReadOnlyBehaviorType {

    /**
     * The component is not enabled in read-only mode. Visible attribute is unaffected.
     */
    DISABLED,

    /**
     * The component is not visible in read-only mode. Enabled attribute is unaffected.
     */
    INVISIBLE,

    /**
     * The component ignores the read-only status and remains active. Enabled and visible state are
     * still applied but the read only state is always false.
     * <p>
     * This type is useful for components that need to be active although the rest of the UI is in
     * browse mode. For example an input field for filtering the content of a table.
     * 
     * @apiNote This type is only applicable for components of type {@link HasValue}. If the
     *          component is not of type {@link HasValue}, a {@link ClassCastException} is thrown.
     */
    WRITABLE,

    /**
     * The component is always invisible in writable mode. In read-only mode, the visible aspect is
     * respected.
     */
    INVISIBLE_IF_WRITABLE

}