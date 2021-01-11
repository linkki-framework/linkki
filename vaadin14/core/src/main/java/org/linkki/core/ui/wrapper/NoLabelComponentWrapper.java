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

package org.linkki.core.ui.wrapper;

import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;

import com.vaadin.flow.component.Component;

/**
 * Implementation of the {@link ComponentWrapper} with a vaadin {@link Component} without label support.
 */
public class NoLabelComponentWrapper extends VaadinComponentWrapper {

    private static final long serialVersionUID = 1L;

    public NoLabelComponentWrapper(Component component, WrapperType type) {
        super(component, type);
    }

    @Override
    public void setLabel(String labelText) {
        // no label supported
    }

    @Override
    public String toString() {
        return "Component " + "("
                + getComponent().getClass().getSimpleName() + ")";
    }
}
