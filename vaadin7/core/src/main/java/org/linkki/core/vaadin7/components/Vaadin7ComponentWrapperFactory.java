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

package org.linkki.core.vaadin7.components;

import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.ComponentWrapperFactory;
import org.linkki.core.ui.components.LabelComponentWrapper;

import com.vaadin.ui.Component;

/**
 * {@link ComponentWrapperFactory} for Vaadin 7 {@link Component Components}.
 */
public enum Vaadin7ComponentWrapperFactory implements ComponentWrapperFactory {

    /**
     * The singleton instance of {@link Vaadin7ComponentWrapperFactory}.
     */
    INSTANCE;

    /**
     * {@inheritDoc}
     * <p>
     * 
     * @implNote Considers every implementation of {@link Component} a component.
     */
    @Override
    public boolean isUiComponent(Class<?> clazz) {
        return Component.class.isAssignableFrom(clazz);
    }

    @Override
    public ComponentWrapper createComponentWrapper(Object component) {
        return new LabelComponentWrapper((Component)component);
    }

}
