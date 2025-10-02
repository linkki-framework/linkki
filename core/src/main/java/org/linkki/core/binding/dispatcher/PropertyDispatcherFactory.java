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
package org.linkki.core.binding.dispatcher;

import static java.util.Objects.requireNonNull;

import org.linkki.core.binding.descriptor.modelobject.ModelObjects;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.behavior.BehaviorDependentDispatcher;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.fallback.ExceptionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.reflection.ReflectionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.staticvalue.StaticValueDispatcher;
import org.linkki.util.reflection.accessor.MemberAccessors;

/**
 * Creates the default Chains of {@link PropertyDispatcher PropertyDispatchers}.
 * <p>
 * The property dispatchers are in the following order:
 *
 * <ol>
 * <li>{@link BehaviorDependentDispatcher}</li>
 * <li>Custom dispatchers from
 * {@link #createCustomDispatchers(Object, BoundProperty, PropertyDispatcher)}</li>
 * <li>{@link StaticValueDispatcher}</li>
 * <li>{@link ReflectionPropertyDispatcher} for PMO</li>
 * <li>{@link ReflectionPropertyDispatcher} for model object if it exists</li>
 * <li>{@link ExceptionPropertyDispatcher}</li>
 * </ol>
 */
public class PropertyDispatcherFactory {

    public PropertyDispatcher createDispatcherChain(Object pmo,
            BoundProperty boundProperty,
            PropertyBehaviorProvider behaviorProvider) {
        requireNonNull(pmo, "pmo must not be null");
        requireNonNull(boundProperty, "boundProperty must not be null");
        requireNonNull(behaviorProvider, "behaviorProvider must not be null");

        // @formatter:off
        String modelPropertyName = boundProperty.getModelAttribute();
        String modelObjectName = boundProperty.getModelObject();
        String pmoPropertyName = boundProperty.getPmoProperty();
        ExceptionPropertyDispatcher exceptionDispatcher = newExceptionDispatcher(pmo, modelObjectName, pmoPropertyName);
        ReflectionPropertyDispatcher reflectionDispatcher = newReflectionDispatcher(pmo, pmoPropertyName, modelObjectName, modelPropertyName, exceptionDispatcher);
        StaticValueDispatcher staticValueDispatcher = new StaticValueDispatcher(reflectionDispatcher);
        PropertyDispatcher customDispatchers = createCustomDispatchers(pmo, boundProperty, staticValueDispatcher);
        return new BehaviorDependentDispatcher(customDispatchers, behaviorProvider);
        // @formatter:on
    }

    /**
     * Subclasses may override to add custom dispatchers to the chain. The dispatchers will be added
     * <em>after</em> the standard dispatchers and <em>before</em> the behavior dependent
     * dispatchers.
     * <p>
     * The default implementation adds no dispatchers.
     * <p>
     * Must return a {@link PropertyDispatcher}; if none are created, the given standardDispatchers
     * should be returned.
     *
     * @param pmo the PMO the dispatcher is responsible for
     * @param boundProperty the {@link BoundProperty} of the bound UI element
     * @param standardDispatchers the previously created dispatcher chain from
     *            {@link #createDispatcherChain(Object, BoundProperty, PropertyBehaviorProvider)}
     */
    protected PropertyDispatcher createCustomDispatchers(Object pmo,
            BoundProperty boundProperty,
            PropertyDispatcher standardDispatchers) {
        requireNonNull(standardDispatchers, "standardDispatchers must not be null");
        return standardDispatchers;
    }

    private ReflectionPropertyDispatcher newReflectionDispatcher(Object pmo,
            String pmoPropertyName,
            String modelObjectName,
            String modelObjectProperty,
            PropertyDispatcher wrappedDispatcher) {
        PropertyDispatcher fallbackDispatcher = ModelObjects.getModelObjectAccessMember(pmo, modelObjectName)
                .map(member -> new ReflectionPropertyDispatcher(
                        () -> MemberAccessors.getValue(pmo, member),
                        MemberAccessors.getType(member, pmo.getClass()),
                        modelObjectProperty,
                        wrappedDispatcher))
                .map(PropertyDispatcher.class::cast)
                .orElse(wrappedDispatcher);

        return new ReflectionPropertyDispatcher(() -> pmo, pmo.getClass(), pmoPropertyName, fallbackDispatcher);
    }

    private ExceptionPropertyDispatcher newExceptionDispatcher(Object pmo, String modelObjectName, String property) {
        if (ModelObjects.isAccessible(pmo, modelObjectName)) {
            return new ExceptionPropertyDispatcher(property,
                    ModelObjects.supplierFor(pmo, modelObjectName).get(), pmo);
        } else {
            return new ExceptionPropertyDispatcher(property, pmo);
        }
    }
}
