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

import java.util.function.Consumer;

import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.util.handler.Handler;

/**
 * A convenient implementation for {@link LinkkiAspectDefinition}s that only updates upon model
 * changes.
 * <p>
 * Implementations need to specify the {@link Aspect} by implementing {@link #createAspect()} and
 * provide a consumer that is able to set the value to the component.
 */
public abstract class ModelToUiAspectDefinition<VALUE_TYPE> implements LinkkiAspectDefinition {

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        Consumer<VALUE_TYPE> setter = createComponentValueSetter(componentWrapper);
        Aspect<VALUE_TYPE> aspect = createAspect();
        return () -> {
            try {
                setter.accept(propertyDispatcher.pull(aspect));
                // CSOFF: IllegalCatch
            } catch (RuntimeException e) {
                Object boundObject = propertyDispatcher.getBoundObject();
                throw new RuntimeException(
                        e.getMessage() +
                                " while apply " +
                                (aspect.getName().isEmpty() ? "value" : "aspect " + aspect.getName()) +
                                " of " +
                                (boundObject != null ? boundObject.getClass() : "<no object>") +
                                "#" + propertyDispatcher.getProperty());
                // CSON: IllegalCatch
            }
        };
    }

    /**
     * Returns an {@link Aspect} for this {@link LinkkiAspectDefinition}.
     * <p>
     * This class assumes that the value of the {@link Aspect} does not depend on the type of model
     * value. If this is the case, use the {@link LinkkiAspectDefinition} interface directly.
     * 
     * @return a new {@link Aspect} of this definition
     */
    public abstract Aspect<VALUE_TYPE> createAspect();

    /**
     * Defines how the value of the value of the {@link ComponentWrapper} is set.
     * 
     * @param componentWrapper UI component of which the value has to be
     * @return setter for the value of the {@link ComponentWrapper}
     */
    public abstract Consumer<VALUE_TYPE> createComponentValueSetter(ComponentWrapper componentWrapper);
}
