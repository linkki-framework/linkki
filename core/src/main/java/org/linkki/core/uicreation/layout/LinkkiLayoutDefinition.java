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

package org.linkki.core.uicreation.layout;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;

/**
 * Defines how to create child UI components, add them to their parent and bind them.
 */
@FunctionalInterface
public interface LinkkiLayoutDefinition {

    /**
     * Creates UI components defined by the given PMO, adds them to the parent component and binds
     * them using the {@link BindingContext}.
     * 
     * @implSpec Implementations may cast the parent component to the class created by an
     *           accompanying {@link LinkkiComponentDefinition}.
     */
    public void createChildren(Object parentComponent, Object pmo, BindingContext bindingContext);

}
