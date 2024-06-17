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

package org.linkki.core.binding.descriptor.aspect.base;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.util.handler.Handler;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * A convenient implementation for {@link LinkkiAspectDefinition LinkkiAspectDefinitions} which use
 * multiple {@link LinkkiAspectDefinition LinkkiAspectDefinitions}.
 * <p>
 * Implementations of this class provide their {@link LinkkiAspectDefinition
 * LinkkiAspectDefinitions} as a List. Methods in this class can then be used to address all
 * {@link LinkkiAspectDefinition AspectDefinitions} in the list.
 */
public class CompositeAspectDefinition implements LinkkiAspectDefinition {

    private final List<LinkkiAspectDefinition> aspectDefinitions;

    /**
     * Creates a new {@link CompositeAspectDefinition} consisting of given aspect definitions.
     */
    public CompositeAspectDefinition(@NonNull LinkkiAspectDefinition... aspectDefinitions) {
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
        return aspectDefinitions.stream()
                .filter(d -> d.supports(componentWrapper.getType()))
                .map(lad -> lad.createUiUpdater(propertyDispatcher, componentWrapper))
                .reduce(Handler.NOP_HANDLER, Handler::andThen);
    }

    @Override
    public void initModelUpdate(PropertyDispatcher propertyDispatcher,
            ComponentWrapper componentWrapper,
            Handler modelUpdated) {
        aspectDefinitions
                .stream()
                .filter(d -> d.supports(componentWrapper.getType()))
                .forEach(lad -> {
                    // CSOFF: IllegalCatch
                    try {
                        lad.initModelUpdate(propertyDispatcher, componentWrapper, modelUpdated);
                    } catch (RuntimeException e) {
                        throw new LinkkiBindingException(
                                e.getMessage() + " while init model update of " + lad.getClass().getSimpleName()
                                        + " for " + componentWrapper + " <=> " + propertyDispatcher,
                                e);
                    }
                    // CSON: IllegalCatch
                });
    }

    @Override
    public boolean supports(WrapperType type) {
        return aspectDefinitions.stream()
                .anyMatch(d -> d.supports(type));
    }

    public List<LinkkiAspectDefinition> getAspectDefinitions() {
        return Collections.unmodifiableList(aspectDefinitions);
    }
}
