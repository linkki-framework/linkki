/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ButtonPmo;
import org.linkki.core.binding.dispatcher.BehaviourDependentDispatcher;
import org.linkki.core.binding.dispatcher.BindingAnnotationDispatcher;
import org.linkki.core.binding.dispatcher.ExceptionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.ReflectionPropertyDispatcher;
import org.linkki.core.ui.section.annotations.ElementDescriptor;
import org.linkki.core.ui.section.annotations.UIAnnotationReader;

/**
 * Creates Chains of {@link PropertyDispatcher PropertyDispatchers}.
 *
 * @author dschwering
 */
class PropertyDispatcherFactory {

    private PropertyDispatcherFactory() {
    }

    @Nonnull
    static PropertyDispatcher createDispatcherChain(@Nonnull Object pmo,
            @Nonnull ElementDescriptor elementDescriptor,
            @Nonnull PropertyBehaviorProvider behaviorProvider) {
        requireNonNull(pmo, "PresentationModelObject must not be null");
        requireNonNull(elementDescriptor, "ElementDescriptor must not be null");
        requireNonNull(behaviorProvider, "PropertyBehaviorProvider must not be null");

        // @formatter:off
        String propertyName = elementDescriptor.getPropertyName();
        ExceptionPropertyDispatcher exceptionDispatcher = newExceptionDispatcher(pmo, propertyName);
        ReflectionPropertyDispatcher reflectionDispatcher = newReflectionDispatcher(pmo, propertyName, exceptionDispatcher);
        BindingAnnotationDispatcher bindingAnnotationDispatcher = new BindingAnnotationDispatcher(reflectionDispatcher, elementDescriptor);
        return new BehaviourDependentDispatcher(bindingAnnotationDispatcher, behaviorProvider);
        // @formatter:on
    }

    @Nonnull
    static PropertyDispatcher createDispatcherChain(@Nonnull ButtonPmo buttonPmo,
            @Nonnull PropertyBehaviorProvider behaviorProvider) {
        requireNonNull(buttonPmo, "ButtonPmo must not be null");
        requireNonNull(behaviorProvider, "PropertyBehaviorProvider must not be null");

        // @formatter:off
        ExceptionPropertyDispatcher exceptionDispatcher = newExceptionDispatcher(buttonPmo, StringUtils.EMPTY);
        ReflectionPropertyDispatcher reflectionDispatcher = newReflectionDispatcher(buttonPmo, StringUtils.EMPTY, exceptionDispatcher);
        @SuppressWarnings("deprecation")
        org.linkki.core.binding.dispatcher.ButtonPmoDispatcher buttonPmoDispatcher = new org.linkki.core.binding.dispatcher.ButtonPmoDispatcher(reflectionDispatcher);
        return new BehaviourDependentDispatcher(buttonPmoDispatcher, behaviorProvider);
        // @formatter:on
    }

    private static ReflectionPropertyDispatcher newReflectionDispatcher(Object pmo,
            String property,
            PropertyDispatcher wrappedDispatcher) {
        if (UIAnnotationReader.hasModelObjectAnnotatedMethod(pmo)) {
            ReflectionPropertyDispatcher modelObjectDispatcher = new ReflectionPropertyDispatcher(
                    UIAnnotationReader.getModelObjectSupplier(pmo), property, wrappedDispatcher);
            return new ReflectionPropertyDispatcher(() -> pmo, property, modelObjectDispatcher);
        } else {
            return new ReflectionPropertyDispatcher(() -> pmo, property, wrappedDispatcher);
        }
    }

    private static ExceptionPropertyDispatcher newExceptionDispatcher(Object pmo, String property) {
        if (UIAnnotationReader.hasModelObjectAnnotatedMethod(pmo)) {
            return new ExceptionPropertyDispatcher(property, UIAnnotationReader.getModelObjectSupplier(pmo).get(), pmo);
        } else {
            return new ExceptionPropertyDispatcher(property, pmo);
        }
    }
}
