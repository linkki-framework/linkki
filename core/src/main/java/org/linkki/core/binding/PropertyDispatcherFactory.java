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
package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ButtonPmo;
import org.linkki.core.binding.dispatcher.BehaviorDependentDispatcher;
import org.linkki.core.binding.dispatcher.ExceptionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.ReflectionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.StaticValueDispatcher;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.descriptor.BindingDescriptor;
import org.linkki.core.ui.section.descriptor.UIAnnotationReader;

/**
 * Creates Chains of {@link PropertyDispatcher PropertyDispatchers}.
 */
public class PropertyDispatcherFactory {

    public PropertyDispatcher createDispatcherChain(Object pmo,
            BindingDescriptor bindingDescriptor,
            PropertyBehaviorProvider behaviorProvider) {
        requireNonNull(pmo, "pmo must not be null");
        requireNonNull(bindingDescriptor, "bindingDescriptor must not be null");
        requireNonNull(behaviorProvider, "behaviorProvider must not be null");

        // @formatter:off
        String modelPropertyName = bindingDescriptor.getModelPropertyName();
        String modelObjectName = bindingDescriptor.getModelObjectName();
        String pmoPropertyName = bindingDescriptor.getPmoPropertyName();
        ExceptionPropertyDispatcher exceptionDispatcher = newExceptionDispatcher(pmo, modelObjectName, pmoPropertyName);
        ReflectionPropertyDispatcher reflectionDispatcher = newReflectionDispatcher(pmo, pmoPropertyName, modelObjectName, modelPropertyName, exceptionDispatcher);
        StaticValueDispatcher bindingAnnotationDispatcher = new StaticValueDispatcher(reflectionDispatcher);
        PropertyDispatcher customDispatchers = createCustomDispatchers(pmo, bindingDescriptor, bindingAnnotationDispatcher);
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
     * @param bindingDescriptor the descriptor of the bound UI element
     * @param standardDispatchers the previously created dispatcher chain from
     *            {@link #createDispatcherChain(Object, BindingDescriptor, PropertyBehaviorProvider)}
     */
    protected PropertyDispatcher createCustomDispatchers(Object pmo,
            BindingDescriptor bindingDescriptor,
            PropertyDispatcher standardDispatchers) {
        requireNonNull(standardDispatchers, "standardDispatchers must not be null");
        return standardDispatchers;
    }

    public PropertyDispatcher createDispatcherChain(ButtonPmo buttonPmo,
            PropertyBehaviorProvider behaviorProvider) {
        requireNonNull(buttonPmo, "buttonPmo must not be null");
        requireNonNull(behaviorProvider, "behaviorProvider must not be null");

        // @formatter:off
        String modelObjectName = ModelObject.DEFAULT_NAME;
        
        ExceptionPropertyDispatcher exceptionDispatcher = newExceptionDispatcher(buttonPmo, modelObjectName, StringUtils.EMPTY);
        ReflectionPropertyDispatcher reflectionDispatcher = newReflectionDispatcher(buttonPmo, StringUtils.EMPTY, modelObjectName, StringUtils.EMPTY, exceptionDispatcher);
        @SuppressWarnings("deprecation")
        org.linkki.core.binding.dispatcher.ButtonPmoDispatcher buttonPmoDispatcher = new org.linkki.core.binding.dispatcher.ButtonPmoDispatcher(reflectionDispatcher);
        return new BehaviorDependentDispatcher(buttonPmoDispatcher, behaviorProvider);
        // @formatter:on
    }

    private ReflectionPropertyDispatcher newReflectionDispatcher(Object pmo,
            String pmoPropertyName,
            String modelObjectName,
            String modelObjectProperty,
            PropertyDispatcher wrappedDispatcher) {
        if (UIAnnotationReader.hasModelObjectAnnotatedMethod(pmo, modelObjectName)) {
            Supplier<?> modelObject = UIAnnotationReader.getModelObjectSupplier(pmo, modelObjectName);
            ReflectionPropertyDispatcher modelObjectDispatcher = new ReflectionPropertyDispatcher(modelObject,
                    modelObjectProperty, wrappedDispatcher);
            return new ReflectionPropertyDispatcher(() -> pmo, pmoPropertyName, modelObjectDispatcher);
        } else {
            return new ReflectionPropertyDispatcher(() -> pmo, pmoPropertyName, wrappedDispatcher);
        }
    }

    private ExceptionPropertyDispatcher newExceptionDispatcher(Object pmo, String modelObjectName, String property) {
        if (UIAnnotationReader.hasModelObjectAnnotatedMethod(pmo, modelObjectName)) {
            return new ExceptionPropertyDispatcher(property,
                    UIAnnotationReader.getModelObjectSupplier(pmo, modelObjectName).get(), pmo);
        } else {
            return new ExceptionPropertyDispatcher(property, pmo);
        }
    }
}
