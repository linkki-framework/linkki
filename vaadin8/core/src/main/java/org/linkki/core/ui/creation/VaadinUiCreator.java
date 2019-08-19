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

package org.linkki.core.ui.creation;

import java.util.Optional;
import java.util.function.Function;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;

import com.vaadin.ui.Component;

/**
 * Wrapper class for {@link UiCreator}, performing casts to {@link Component}
 */
public class VaadinUiCreator {

    private VaadinUiCreator() {
        // prevents instantiation
    }

    /**
     * Calls {@link UiCreator#createComponent(Object, BindingContext, Function, Function)} and returns
     * the component of the returned wrapper.
     */
    public static Component createComponent(Object pmo, BindingContext bindingContext) {
        ComponentWrapper wrapper = UiCreator.createComponent(pmo, bindingContext);
        return (Component)wrapper.getComponent();
    }

    /**
     * Calls {@link UiCreator#createComponent(Object, BindingContext, Function, Function)} and returns
     * the component of the returned wrapper.
     */
    public static Component createComponent(Object pmo,
            BindingContext bindingContext,
            Function<Class<?>, Optional<LinkkiComponentDefinition>> componentDefinitionFinder,
            Function<Class<?>, Optional<LinkkiLayoutDefinition>> layoutDefinitionFinder) {

        ComponentWrapper wrapper = UiCreator.createComponent(pmo, bindingContext, componentDefinitionFinder,
                                                             layoutDefinitionFinder);
        return (Component)wrapper.getComponent();
    }
}
