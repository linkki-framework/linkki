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
import org.linkki.core.binding.dispatcher.BehaviorDependentDispatcher;
import org.linkki.core.binding.dispatcher.BindingAnnotationDispatcher;
import org.linkki.core.binding.dispatcher.ExceptionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.ReflectionPropertyDispatcher;
import org.linkki.core.ui.section.annotations.BindingDescriptor;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.UIAnnotationReader;

/**
 * Creates Chains of {@link PropertyDispatcher PropertyDispatchers}.
 *
 * @author dschwering
 */
public class PropertyDispatcherFactory {

    public PropertyDispatcherFactory() {
    }

    @Nonnull
    public PropertyDispatcher createDispatcherChain(@Nonnull Object pmo,
            @Nonnull BindingDescriptor bindingDescriptor,
            @Nonnull PropertyBehaviorProvider behaviorProvider) {
        requireNonNull(pmo, "PresentationModelObject must not be null");
        requireNonNull(bindingDescriptor, "ElementDescriptor must not be null");
        requireNonNull(behaviorProvider, "PropertyBehaviorProvider must not be null");

        // @formatter:off
        String propertyName = bindingDescriptor.getPropertyName();
        String modelObjectName = bindingDescriptor.getModelObjectName();
        ExceptionPropertyDispatcher exceptionDispatcher = newExceptionDispatcher(pmo, modelObjectName, propertyName);
        ReflectionPropertyDispatcher reflectionDispatcher = newReflectionDispatcher(pmo, modelObjectName, propertyName, exceptionDispatcher);
        BindingAnnotationDispatcher bindingAnnotationDispatcher = new BindingAnnotationDispatcher(reflectionDispatcher, bindingDescriptor);
        PropertyDispatcher customDispatchers = createCustomDispatchers(pmo, bindingDescriptor, bindingAnnotationDispatcher);
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
     * @param bindingDescriptor the descriptor of the bound UI element
     * @param standardDispatchers the previously created dispatcher chain from
     *            {@link #createDispatcherChain(Object, BindingDescriptor, PropertyBehaviorProvider)}
     */
    @Nonnull
    protected PropertyDispatcher createCustomDispatchers(@Nonnull Object pmo,
            @Nonnull BindingDescriptor bindingDescriptor,
            @Nonnull PropertyDispatcher standardDispatchers) {
        return standardDispatchers;
    }

    @Nonnull
    public PropertyDispatcher createDispatcherChain(@Nonnull ButtonPmo buttonPmo,
            @Nonnull PropertyBehaviorProvider behaviorProvider) {
        requireNonNull(buttonPmo, "ButtonPmo must not be null");
        requireNonNull(behaviorProvider, "PropertyBehaviorProvider must not be null");

        // @formatter:off
        String modelObjectName = ModelObject.DEFAULT_NAME;
        ExceptionPropertyDispatcher exceptionDispatcher = newExceptionDispatcher(buttonPmo, modelObjectName, StringUtils.EMPTY);
        ReflectionPropertyDispatcher reflectionDispatcher = newReflectionDispatcher(buttonPmo, modelObjectName, StringUtils.EMPTY, exceptionDispatcher);
        @SuppressWarnings("deprecation")
        org.linkki.core.binding.dispatcher.ButtonPmoDispatcher buttonPmoDispatcher = new org.linkki.core.binding.dispatcher.ButtonPmoDispatcher(reflectionDispatcher);
        return new BehaviorDependentDispatcher(buttonPmoDispatcher, behaviorProvider);
        // @formatter:on
    }

    private ReflectionPropertyDispatcher newReflectionDispatcher(Object pmo,
            String modelObjectName,
            String property,
            PropertyDispatcher wrappedDispatcher) {
        if (UIAnnotationReader.hasModelObjectAnnotatedMethod(pmo, modelObjectName)) {
            ReflectionPropertyDispatcher modelObjectDispatcher = new ReflectionPropertyDispatcher(
                    UIAnnotationReader.getModelObjectSupplier(pmo, modelObjectName), property, wrappedDispatcher);
            return new ReflectionPropertyDispatcher(() -> pmo, property, modelObjectDispatcher);
        } else {
            return new ReflectionPropertyDispatcher(() -> pmo, property, wrappedDispatcher);
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
