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
package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

import org.linkki.core.binding.descriptor.UIAnnotationReader;
import org.linkki.core.binding.dispatcher.BehaviorDependentDispatcher;
import org.linkki.core.binding.dispatcher.ExceptionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.ReflectionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.StaticValueDispatcher;
import org.linkki.core.binding.property.BoundProperty;

/**
 * Creates Chains of {@link PropertyDispatcher PropertyDispatchers}.
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
        StaticValueDispatcher bindingAnnotationDispatcher = new StaticValueDispatcher(reflectionDispatcher);
        PropertyDispatcher customDispatchers = createCustomDispatchers(pmo, boundProperty, bindingAnnotationDispatcher);
        return new BehaviorDependentDispatcher(customDispatchers, behaviorProvider);
        // @formatter:on
    }

    /**
     * Subclasses may override to add custom dispatchers to the chain. The dispatchers will be added
     * <em>after</em> the standard dispatchers and <em>before</em> the behavior dependent dispatchers.
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
        if (UIAnnotationReader.hasModelObjectAnnotation(pmo, modelObjectName)) {
            Supplier<?> modelObject = UIAnnotationReader.getModelObjectSupplier(pmo, modelObjectName);
            ReflectionPropertyDispatcher modelObjectDispatcher = new ReflectionPropertyDispatcher(modelObject,
                    modelObjectProperty, wrappedDispatcher);
            return new ReflectionPropertyDispatcher(() -> pmo, pmoPropertyName, modelObjectDispatcher);
        } else {
            return new ReflectionPropertyDispatcher(() -> pmo, pmoPropertyName, wrappedDispatcher);
        }
    }

    private ExceptionPropertyDispatcher newExceptionDispatcher(Object pmo, String modelObjectName, String property) {
        if (UIAnnotationReader.hasModelObjectAnnotation(pmo, modelObjectName)) {
            return new ExceptionPropertyDispatcher(property,
                    UIAnnotationReader.getModelObjectSupplier(pmo, modelObjectName).get(), pmo);
        } else {
            return new ExceptionPropertyDispatcher(property, pmo);
        }
    }
}
