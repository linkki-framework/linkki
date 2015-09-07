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
    public PropertyDispatcher defaultDispatcherChain(@Nonnull PresentationModelObject pmo,
            @Nonnull PropertyBehaviorProvider behaviorProvider) {
        Preconditions.checkNotNull(pmo, "PresentationModelObject must not be null");
        Preconditions.checkNotNull(behaviorProvider, "PropertyBehaviorProvider must not be null");

        // @formatter:off
        ExceptionPropertyDispatcher exceptionDispatcher = new ExceptionPropertyDispatcher(pmo.getModelObject(), pmo);
        ReflectionPropertyDispatcher modelObjectDispatcher = new ReflectionPropertyDispatcher(pmo::getModelObject, exceptionDispatcher);
        ReflectionPropertyDispatcher pmoDispatcher = new ReflectionPropertyDispatcher(() -> pmo, modelObjectDispatcher);
        BindingAnnotationDecorator bindingAnnotationDecorator = new BindingAnnotationDecorator(pmoDispatcher, pmo.getClass());
        return new BehaviourDependentDecorator(bindingAnnotationDecorator, behaviorProvider);
        // @formatter:on
    }

}
