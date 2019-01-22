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

package org.linkki.core.binding.aspect;

import java.util.List;

import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.aspect.definition.CompositeAspectDefinition;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.util.handler.Handler;

/**
 * Updaters for {@link Aspect Aspects} that are responsible for the same {@link ComponentWrapper} and
 * the same property in the same bound object. Given a bound object with a property bound to a
 * {@link ComponentWrapper}, all aspects of this property that are bound to the same component (for
 * example visiblity, tooltip) are collected in the {@link AspectUpdaters} object corresponding to this
 * property.
 */
public class AspectUpdaters {

    private final Handler uiUpdater;

    public AspectUpdaters(List<LinkkiAspectDefinition> aspectDefinitions, PropertyDispatcher propertyDispatcher,
            ComponentWrapper componentWrapper, Handler modelChanged) {
        CompositeAspectDefinition aspectDefinition = new CompositeAspectDefinition(aspectDefinitions);
        aspectDefinition.initModelUpdate(propertyDispatcher, componentWrapper, modelChanged);
        this.uiUpdater = aspectDefinition.createUiUpdater(propertyDispatcher, componentWrapper);
    }

    /**
     * Prompt all aspects to update the UI component.
     */
    public void updateUI() {
        try {
            uiUpdater.apply();
            // CSOFF: IllegalCatch
        } catch (RuntimeException e) {
            throw new LinkkiBindingException(
                    e.getMessage() + " in " + e.getStackTrace()[0], e);
        }
        // CSON: IllegalCatch
    }

}
