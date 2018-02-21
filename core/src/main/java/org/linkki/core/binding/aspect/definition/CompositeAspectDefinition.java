/*
 * Copyright Faktor Zehn AG.
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

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.util.handler.Handler;

/**
 * A convenient implementation for {@link LinkkiAspectDefinition LinkkiAspectDefinitions} which use
 * multiple {@link LinkkiAspectDefinition LinkkiAspectDefinitions}.
 * <p>
 * Implementations of this class provide their {@link LinkkiAspectDefinition
 * LinkkiAspectDefinitions} as a List. Methods in this class can then be used to address all
 * {@link LinkkiAspectDefinition AspectDefinitions} in the list.
 */
public class CompositeAspectDefinition implements LinkkiAspectDefinition {

    private List<LinkkiAspectDefinition> aspectDefinitions;

    /**
     * Creates a new {@link CompositeAspectDefinition} consisting of given aspect definitions.
     */
    public CompositeAspectDefinition(LinkkiAspectDefinition... aspectDefinitions) {
        this(Arrays.asList(aspectDefinitions));
    }

    /**
     * Creates a new {@link CompositeAspectDefinition} consisting of given aspect definitions.
     */
    public CompositeAspectDefinition(List<LinkkiAspectDefinition> aspectDefinitions) {
        this.aspectDefinitions = aspectDefinitions;
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        return aspectDefinitions.stream().map(a -> a.createUiUpdater(propertyDispatcher, componentWrapper))
                .reduce(Handler.NOP_HANDLER, Handler::andThen);
    }

    @Override
    public void initModelUpdate(PropertyDispatcher propertyDispatcher,
            ComponentWrapper componentWrapper,
            Handler modelUpdated) {
        aspectDefinitions.forEach(a -> a.initModelUpdate(propertyDispatcher, componentWrapper, modelUpdated));
    }

    @Override
    public void initialize(Annotation annotation) {
        aspectDefinitions.forEach(a -> a.initialize(annotation));
    }
}
