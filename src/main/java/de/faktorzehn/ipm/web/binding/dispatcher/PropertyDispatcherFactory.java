/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding.dispatcher;

import javax.annotation.Nonnull;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;

import de.faktorzehn.ipm.web.PresentationModelObject;

/**
 * Factory for creating a chain of property dispatchers to use for data binding.
 */
public class PropertyDispatcherFactory {

    public PropertyDispatcherFactory() {
        super();
    }

    /**
     * Returns a new chain of property dispatchers to use for binding the given presentation model
     * object.
     * 
     * @param pmo the presentation model object to bind
     * @param behaviorProvider the provider for behaviors to use in the chain of property
     *            dispatchers
     * @return a new chain of property dispatchers
     */
    public PropertyDispatcher defaultDispatcherChain(@Nonnull Object pmo,
            @Nonnull PropertyBehaviorProvider behaviorProvider) {
        Preconditions.checkNotNull(pmo, "PresentationModelObject must not be null");
        Preconditions.checkNotNull(behaviorProvider, "PropertyBehaviorProvider must not be null");

        // @formatter:off
        ExceptionPropertyDispatcher exceptionDispatcher = newExceptionDispatcher(pmo);
        ReflectionPropertyDispatcher reflectionDispatcher = newReflectionDispatcher(pmo, exceptionDispatcher);
        BindingAnnotationDecorator bindingAnnotationDecorator = new BindingAnnotationDecorator(reflectionDispatcher, pmo.getClass());
        return new BehaviourDependentDecorator(bindingAnnotationDecorator, behaviorProvider);
        // @formatter:on
    }

    private ReflectionPropertyDispatcher newReflectionDispatcher(Object pmo, PropertyDispatcher wrappedDispatcher) {
        if (pmo instanceof PresentationModelObject) {
            ReflectionPropertyDispatcher modelObjectDispatcher = new ReflectionPropertyDispatcher(
                    ((PresentationModelObject)pmo)::getModelObject, wrappedDispatcher);
            return new ReflectionPropertyDispatcher(() -> pmo, modelObjectDispatcher);
        } else {
            return new ReflectionPropertyDispatcher(() -> pmo, wrappedDispatcher);
        }
    }

    private ExceptionPropertyDispatcher newExceptionDispatcher(Object pmo) {
        if (pmo instanceof PresentationModelObject) {
            return new ExceptionPropertyDispatcher(((PresentationModelObject)pmo).getModelObject(), pmo);
        } else {
            return new ExceptionPropertyDispatcher(pmo);
        }
    }

}
