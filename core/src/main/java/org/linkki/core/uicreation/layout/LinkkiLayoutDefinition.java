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

package org.linkki.core.uicreation.layout;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.wrapper.ComponentWrapper;

/**
 * Defines how UI components are added to their parent, what other UI elements might need to be created
 * to create a layout and creates {@link ComponentWrapper ComponentWrappers} to allow binding of various
 * aspects of the UI components.
 */
public interface LinkkiLayoutDefinition {

    /**
     * Adds the UI component (and possibly others, like labels and layout elements) to the parent
     * component and returns a {@link ComponentWrapper} that can be used to
     * {@link BindingContext#bind(Object, org.linkki.core.binding.descriptor.BindingDescriptor, ComponentWrapper)
     * bind} the component.
     * 
     * @param parent the parent/container component
     * @param component the child component
     * @return a {@link ComponentWrapper} that returns the given child component from its
     *         {@link ComponentWrapper#getComponent() getComponent()} method.
     */
    public ComponentWrapper add(Object parent, Object component);

}
