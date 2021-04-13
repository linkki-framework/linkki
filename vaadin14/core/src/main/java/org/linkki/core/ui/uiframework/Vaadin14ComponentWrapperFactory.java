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

package org.linkki.core.ui.uiframework;

import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.ComponentWrapperFactory;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;

import com.vaadin.flow.component.Component;

/**
 * {@link ComponentWrapperFactory} for Vaadin 8 {@link Component Components}.
 */
public enum Vaadin14ComponentWrapperFactory implements ComponentWrapperFactory {

    /**
     * The singleton instance of {@link Vaadin14ComponentWrapperFactory}.
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
        return new NoLabelComponentWrapper((Component)component, WrapperType.FIELD);
    }

}
