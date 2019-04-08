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
package org.linkki.core.pmo;

import java.util.Optional;

import org.linkki.core.binding.BindingContext;

/**
 * Common interface for presentation model objects.
 */
public interface PresentationModelObject {

    /**
     * Returns the {@code ButtonPmo} for the button that edits the PMO if the PMO allows editing. The
     * default implementation returns {@code Optional.empty()} indicating that the PMO does not allow
     * editing.
     * 
     * @implSpec If you plan to {@link BindingContext#removeBindingsForPmo(Object) remove bindings from
     *           a binding context} later, make sure to not create a new instance on every call to this
     *           method.
     * 
     * @return the {@code ButtonPmo} for the button that edits the PMO if the PMO allows editing
     */
    default Optional<ButtonPmo> getEditButtonPmo() {
        return Optional.empty();
    }

}
