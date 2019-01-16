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

package org.linkki.core.binding.aspect.definition;

import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.util.handler.Handler;

/**
 * Wraps a {@link LinkkiAspectDefinition} that will only be called if the {@link ComponentWrapper}
 * wraps a Component of type {@code<T>}.
 */
public class ApplicableTypeAspectDefinition implements LinkkiAspectDefinition {

    private final LinkkiAspectDefinition wrappedAspectDefinition;
    private final Class<?> applicableType;

    protected ApplicableTypeAspectDefinition(LinkkiAspectDefinition wrappedAspectDefinition, Class<?> applicableType) {
        this.wrappedAspectDefinition = wrappedAspectDefinition;
        this.applicableType = applicableType;
    }

    public static ApplicableTypeAspectDefinition ifApplicable(Class<?> applicableType,
            LinkkiAspectDefinition wrappedAspectDefinition) {
        return new ApplicableTypeAspectDefinition(wrappedAspectDefinition, applicableType);
    }

    @Override
    public void initModelUpdate(PropertyDispatcher propertyDispatcher,
            ComponentWrapper componentWrapper,
            Handler modelUpdated) {
        if (isApplicableFor(componentWrapper)) {
            wrappedAspectDefinition.initModelUpdate(propertyDispatcher, componentWrapper, modelUpdated);
        }
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        if (isApplicableFor(componentWrapper)) {
            return wrappedAspectDefinition.createUiUpdater(propertyDispatcher, componentWrapper);
        } else {
            return Handler.NOP_HANDLER;
        }
    }

    private boolean isApplicableFor(ComponentWrapper componentWrapper) {
        return applicableType.isInstance(componentWrapper.getComponent());
    }
}
